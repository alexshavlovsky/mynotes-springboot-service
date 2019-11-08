package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiExceptionHandler;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.note.NoteController;
import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.note.NoteRequest;
import com.ctzn.mynotesservice.model.note.NoteResponse;
import com.ctzn.mynotesservice.model.note.excel.ExcelXlsResourceFactory;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.model.user.UserRole;
import com.ctzn.mynotesservice.services.NotebookService;
import com.ctzn.mynotesservice.services.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static com.ctzn.mynotesservice.controllers.RestTestUtil.*;
import static com.ctzn.mynotesservice.controllers.StaticTestProvider.getFixedIdUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class NoteControllerTest {

    private static final String BASE_PATH = "/api-test233/notes-test479";
    private static final String EXPORT_XLS_FRAGMENT = "export-test235/xls-test989";

    @Mock
    private UserService userService;

    @Mock
    private NotebookService notebookService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new NoteController(userService, notebookService, new DomainMapper(), new ExcelXlsResourceFactory()))
                .addPlaceholderValue("app.api.url.notes", BASE_PATH)
                .addPlaceholderValue("app.api.url.fragment.export.xls", EXPORT_XLS_FRAGMENT)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        TimeSource.setFixed(true);
    }

    @After
    public void checkRepositories() {
        verifyNoMoreInteractions(userService, notebookService);
    }

    @Test
    public void shouldSaveNote() throws Exception {
        UserEntity user = getFixedIdUser();
        long nbId = 22L;
        NotebookEntity notebook = StaticTestProvider.getEmptyNotebook(nbId, "Some notebook");
        Long id = 11L;
        String noteTitle = "New note";
        String noteText = "Some text";
        NoteEntity note = StaticTestProvider.getNote(id, noteTitle, noteText, notebook);
        NoteRequest noteRequest = new NoteRequest(noteTitle, noteText, nbId);

        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(user);
        when(notebookService.getNotebook(nbId, user)).thenReturn(notebook);
        when(notebookService.saveNote(any())).thenReturn(note);

        mockPostRequest(mockMvc, BASE_PATH, noteRequest, status().isCreated(),
                new NoteResponse(id, noteTitle, noteText, nbId, TimeSource.now())
        );

        verify(userService, times(1)).getUserAssertRole(any(), eq(UserRole.USER));
        verify(notebookService, times(1)).getNotebook(nbId, user);
        ArgumentCaptor<NoteEntity> noteArgumentCaptor = ArgumentCaptor.forClass(NoteEntity.class);
        verify(notebookService, times(1)).saveNote(noteArgumentCaptor.capture());
        Assert.assertNull(noteArgumentCaptor.getValue().getId());
        Assert.assertEquals(noteTitle, noteArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(noteText, noteArgumentCaptor.getValue().getText());
        Assert.assertEquals(Long.valueOf(nbId), noteArgumentCaptor.getValue().getNotebook().getId());
    }

    @Test
    public void shouldUpdateNote() throws Exception {
        UserEntity user = getFixedIdUser();
        long nbId = 75L;
        NotebookEntity notebook = StaticTestProvider.getEmptyNotebook(nbId, "Some notebook");
        long id = 10L;
        String repoTitle = "Repo title";
        String repoText = "Repo text";
        NoteEntity repoNote = StaticTestProvider.getNote(id, repoTitle, repoText, notebook);
        // set lastModifiedOn to 1 hour before now
        repoNote.setLastModifiedOn(new Date(TimeSource.now().getTime() - 3600000));
        String newTitle = "New title";
        String newText = "New text";
        NoteEntity updatedNote = StaticTestProvider.getNote(id, newTitle, newText, notebook);
        NoteRequest noteRequest = new NoteRequest(newTitle, newText, nbId);

        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(user);
        when(notebookService.getNote(id, user)).thenReturn(repoNote);
        when(notebookService.saveNote(any())).thenReturn(updatedNote);

        mockPutRequest(mockMvc, BASE_PATH + '/' + id, noteRequest, status().isOk(),
                new NoteResponse(id, newTitle, newText, nbId, TimeSource.now())
        );

        verify(userService, times(1)).getUserAssertRole(any(), eq(UserRole.USER));
        verify(notebookService, times(1)).getNote(id, user);
        ArgumentCaptor<NoteEntity> noteArgumentCaptor = ArgumentCaptor.forClass(NoteEntity.class);
        verify(notebookService, times(1)).saveNote(noteArgumentCaptor.capture());
        Assert.assertEquals(Long.valueOf(id), noteArgumentCaptor.getValue().getId());
        Assert.assertEquals(newTitle, noteArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(newText, noteArgumentCaptor.getValue().getText());
        Assert.assertEquals(Long.valueOf(nbId), noteArgumentCaptor.getValue().getNotebook().getId());
    }

    @Test
    public void shouldUpdateNoteAndMoveToAnotherNotebook() throws Exception {
        UserEntity user = getFixedIdUser();
        Long sourceNbId = 33L;
        NotebookEntity sourceNotebook = StaticTestProvider.getEmptyNotebook(sourceNbId, "Source notebook");
        long id = 11L;
        String repoTitle = "Repo title";
        String repoText = "Repo text";
        NoteEntity repoNote = StaticTestProvider.getNote(id, repoTitle, repoText, sourceNotebook);
        // set lastModifiedOn to 1 hour before now
        repoNote.setLastModifiedOn(new Date(TimeSource.now().getTime() - 3600000));
        long destNbId = 22L;
        NotebookEntity destNotebook = StaticTestProvider.getEmptyNotebook(destNbId, "Destination notebook");
        String newTitle = "New title";
        String newText = "New text";
        NoteEntity updatedNote = StaticTestProvider.getNote(id, newTitle, newText, destNotebook);
        NoteRequest noteRequest = new NoteRequest(newTitle, newText, destNbId);

        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(user);
        when(notebookService.getNote(id, user)).thenReturn(repoNote);
        when(notebookService.getNotebook(destNbId, user)).thenReturn(destNotebook);
        when(notebookService.saveNote(any())).thenReturn(updatedNote);

        mockPutRequest(mockMvc, BASE_PATH + '/' + id, noteRequest, status().isOk(),
                new NoteResponse(id, newTitle, newText, destNbId, TimeSource.now())
        );

        verify(userService, times(1)).getUserAssertRole(any(), eq(UserRole.USER));
        verify(notebookService, times(1)).getNote(id, user);
        verify(notebookService, times(1)).getNotebook(destNbId, user);
        ArgumentCaptor<NoteEntity> noteArgumentCaptor = ArgumentCaptor.forClass(NoteEntity.class);
        verify(notebookService, times(1)).saveNote(noteArgumentCaptor.capture());
        Assert.assertEquals(Long.valueOf(id), noteArgumentCaptor.getValue().getId());
        Assert.assertEquals(newTitle, noteArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(newText, noteArgumentCaptor.getValue().getText());
        Assert.assertEquals(Long.valueOf(destNbId), noteArgumentCaptor.getValue().getNotebook().getId());
    }

    @Test
    public void shouldDeleteNoteById() throws Exception {
        UserEntity user = getFixedIdUser();
        long id = 33L;
        NoteEntity note = StaticTestProvider.getNote(id, "Some note", "Some text", StaticTestProvider.getEmptyNotebook(77L, "Some notebook"));

        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(user);
        when(notebookService.getNote(id, user)).thenReturn(note);

        mockDeleteRequest(mockMvc, BASE_PATH + '/' + id, status().isOk(), new ApiMessage("Note deleted"));

        verify(userService, times(1)).getUserAssertRole(any(), eq(UserRole.USER));
        verify(notebookService, times(1)).getNote(id, user);
        verify(notebookService, times(1)).deleteNote(note);
    }

}
