package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiExceptionHandler;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.note.NoteResponse;
import com.ctzn.mynotesservice.model.notebook.NotebookController;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookRequest;
import com.ctzn.mynotesservice.model.notebook.NotebookResponse;
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

import java.util.Arrays;
import java.util.List;

import static com.ctzn.mynotesservice.controllers.RestTestUtil.*;
import static com.ctzn.mynotesservice.controllers.StaticTestProvider.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class NotebookControllerTest {

    private static final String BASE_PATH = "/api-test287/notebooks-test873";
    private static final String NOTES_FRAGMENT = "notes-test094";

    @Mock
    private UserService userService;

    @Mock
    private NotebookService notebookService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new NotebookController(userService, notebookService, new DomainMapper()))
                .addPlaceholderValue("app.api.url.notebooks", BASE_PATH)
                .addPlaceholderValue("app.api.url.fragment.notes", NOTES_FRAGMENT)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        TimeSource.setFixed(true);
    }

    @After
    public void checkRepository() {
        verifyNoMoreInteractions(userService, notebookService);
    }

    @Test
    public void shouldReturnAllNotebooks() throws Exception {
        UserEntity user = getFixedIdUser();
        List<NotebookEntity> notebooks = getTwoNotebooksList();

        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(user);
        when(notebookService.getAllNotebooks(user)).thenReturn(notebooks);

        mockGetRequest(mockMvc, BASE_PATH, status().isOk(),
                Arrays.asList(
                        new NotebookResponse(1L, "Notebook 1", 2),
                        new NotebookResponse(2L, "Notebook 2", 0)
                )
        );

        verify(userService, times(1)).getUserAssertRole(any(), eq(UserRole.USER));
        verify(notebookService, times(1)).getAllNotebooks(user);
    }

    @Test
    public void shouldReturnNotesByNotebook() throws Exception {
        UserEntity user = getFixedIdUser();
        long nbId = 44L;
        NotebookEntity notebook = getEmptyNotebook(nbId, "Notebook");
        List<NoteEntity> notes = StaticTestProvider.getTwoNotesList(notebook);

        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(user);
        when(notebookService.getNotebook(nbId, user)).thenReturn(notebook);
        when(notebookService.getNotesFromNotebook(notebook)).thenReturn(notes);

        mockGetRequest(mockMvc, BASE_PATH + '/' + nbId + "/" + NOTES_FRAGMENT, status().isOk(),
                Arrays.asList(
                        new NoteResponse(3L, "Note 1.1", "Some text 1", nbId, TimeSource.now()),
                        new NoteResponse(4L, "Note 1.2", "Some text 2", nbId, TimeSource.now())
                )
        );

        verify(userService, times(1)).getUserAssertRole(any(), eq(UserRole.USER));
        verify(notebookService, times(1)).getNotebook(nbId, user);
        verify(notebookService, times(1)).getNotesFromNotebook(notebook);
    }

    @Test
    public void shouldSaveNotebook() throws Exception {
        UserEntity user = getFixedIdUser();
        long id = 54L;
        String name = "New notebook";
        NotebookRequest notebookRequest = new NotebookRequest(name);
        NotebookEntity notebook = getEmptyNotebook(id, name);
        NotebookResponse notebookResponse = new NotebookResponse(id, name, 0);

        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(user);
        when(notebookService.saveNotebook(any())).thenReturn(notebook);

        mockPostRequest(mockMvc, BASE_PATH, notebookRequest, status().isCreated(), notebookResponse);

        verify(userService, times(1)).getUserAssertRole(any(), eq(UserRole.USER));
        ArgumentCaptor<NotebookEntity> notebookArgumentCaptor = ArgumentCaptor.forClass(NotebookEntity.class);
        verify(notebookService, times(1)).saveNotebook(notebookArgumentCaptor.capture());
        Assert.assertNull(notebookArgumentCaptor.getValue().getId());
        Assert.assertEquals(name, notebookArgumentCaptor.getValue().getName());
        Assert.assertEquals(user, notebookArgumentCaptor.getValue().getUser());
    }

    @Test
    public void shouldUpdateNotebookById() throws Exception {
        UserEntity user = getFixedIdUser();
        long id = 64L;
        NotebookEntity oldNotebook = getEmptyNotebook(id, "Old name");
        String newName = "New name";
        NotebookEntity newNotebook = getEmptyNotebook(id, newName);
        NotebookRequest notebookRequest = new NotebookRequest(newName);
        NotebookResponse notebookResponse = new NotebookResponse(id, newName, 0);

        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(user);
        when(notebookService.getNotebook(id, user)).thenReturn(oldNotebook);
        when(notebookService.saveNotebook(any())).thenReturn(newNotebook);

        mockPutRequest(mockMvc, BASE_PATH + '/' + id, notebookRequest, status().isOk(), notebookResponse);

        verify(userService, times(1)).getUserAssertRole(any(), eq(UserRole.USER));
        ArgumentCaptor<NotebookEntity> captor = ArgumentCaptor.forClass(NotebookEntity.class);
        verify(notebookService, times(1)).getNotebook(id, user);
        verify(notebookService, times(1)).saveNotebook(captor.capture());
        Assert.assertEquals(Long.valueOf(id), captor.getValue().getId());
        Assert.assertEquals(newName, captor.getValue().getName());
        Assert.assertEquals(Integer.valueOf(0), captor.getValue().getSize());
        Assert.assertEquals(user, captor.getValue().getUser());
    }

    @Test
    public void shouldDeleteNotebookById() throws Exception {
        UserEntity user = getFixedIdUser();
        long id = 24L;
        NotebookEntity notebook = getEmptyNotebook(id, "Notebook to delete");

        when(userService.getUserAssertRole(any(), eq(UserRole.USER))).thenReturn(user);
        when(notebookService.getNotebook(id, user)).thenReturn(notebook);

        mockDeleteRequest(mockMvc, BASE_PATH + '/' + id, status().isOk(), new ApiMessage("Notebook deleted"));

        verify(userService, times(1)).getUserAssertRole(any(), eq(UserRole.USER));
        verify(notebookService, times(1)).getNotebook(id, user);
        verify(notebookService, times(1)).deleteNotebook(notebook);
    }

}
