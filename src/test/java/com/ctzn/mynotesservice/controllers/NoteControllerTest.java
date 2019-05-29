package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiExceptionHandler;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.note.NoteController;
import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.note.NoteRequest;
import com.ctzn.mynotesservice.model.note.NoteResponse;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.repositories.NoteRepository;
import com.ctzn.mynotesservice.repositories.NotebookRepository;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.ctzn.mynotesservice.controllers.RestTestUtil.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class NoteControllerTest {

    private static final String BASE_PATH = NoteController.BASE_PATH;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NotebookRepository notebookRepository;

    private MockMvc mockMvc;

    private Long someNotebookId;
    private NotebookEntity someNotebook;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new NoteController(noteRepository, notebookRepository, new DomainMapper()))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        TimeSource.setFixed(true);
        someNotebookId = 255L;
        someNotebook = new NotebookEntity("Some notebook");
        someNotebook.setId(someNotebookId);
    }

    @After
    public void checkRepositories() {
        verifyNoMoreInteractions(noteRepository, notebookRepository);
    }

    @Test
    public void shouldReturnAllNotes() throws Exception {
        List<NoteEntity> notes = StaticTestProvider.getTwoNotesList(someNotebook);

        when(noteRepository.findAll()).thenReturn(notes);

        mockGetRequest(mockMvc, BASE_PATH, null, status().isOk(),
                Arrays.asList(
                        new NoteResponse(3L, "Note 1.1", "Some text 1", someNotebookId, TimeSource.now()),
                        new NoteResponse(4L, "Note 1.2", "Some text 2", someNotebookId, TimeSource.now())
                )
        );

        verify(noteRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnNotesByNotebookId() throws Exception {
        List<NotebookEntity> notebooks = StaticTestProvider.getTwoNotebooksList();
        NotebookEntity notebook = notebooks.get(0);
        Long id = notebook.getId();

        when(notebookRepository.findById(id)).thenReturn(Optional.of(notebook));
        when(noteRepository.findAllByNotebook(notebook)).thenReturn(notebook.getNotes());

        mockGetParamRequest(mockMvc, BASE_PATH, null,
                "nbId", id.toString(), status().isOk(),
                Arrays.asList(
                        new NoteResponse(3L, "Note 1.1", "Some text 1", id, TimeSource.now()),
                        new NoteResponse(4L, "Note 1.2", "Some text 2", id, TimeSource.now())
                )
        );

        verify(notebookRepository, times(1)).findById(id);
        verify(noteRepository, times(1)).findAllByNotebook(notebook);
    }

    @Test
    public void shouldReturnNotFoundWhileFindNotesByNotebookId() throws Exception {
        Long id = 16L;
        when(notebookRepository.findById(id)).thenReturn(Optional.empty());

        mockGetParamRequest(mockMvc, BASE_PATH, null, "nbId", id.toString(), status().isNotFound(),
                new ApiMessage("Notebook with id=" + id + " not found")
        );

        verify(notebookRepository, times(1)).findById(id);
    }

    @Test
    public void shouldReturnNoteById() throws Exception {
        NoteEntity note = StaticTestProvider.getTwoNotesList(someNotebook).get(0);
        Long id = note.getId();

        when(noteRepository.findById(id)).thenReturn(java.util.Optional.of(note));

        mockGetRequest(mockMvc, BASE_PATH, id, status().isOk(),
                new NoteResponse(3L, "Note 1.1", "Some text 1", someNotebookId, TimeSource.now())
        );

        verify(noteRepository, times(1)).findById(id);
    }

    @Test
    public void shouldReturnNotFoundWhileFindNoteById() throws Exception {
        final Long id = 32L;

        when(noteRepository.findById(id)).thenReturn(Optional.empty());

        mockGetRequest(mockMvc, BASE_PATH, id, status().isNotFound(),
                new ApiMessage("Note with id=" + id + " not found")
        );

        verify(noteRepository, times(1)).findById(id);
    }

    @Test
    public void shouldSaveNote() throws Exception {
        Long noteRepoId = 1L;
        String noteTitle = "New note";
        String noteText = "Some text";
        NoteEntity noteRepo = new NoteEntity(noteTitle, noteText, someNotebook);
        noteRepo.setId(noteRepoId);
        NoteRequest noteRequest = new NoteRequest(noteTitle, noteText, someNotebookId);

        when(notebookRepository.findById(someNotebookId)).thenReturn(Optional.of(someNotebook));
        when(noteRepository.save(any())).thenReturn(noteRepo);

        mockPostRequest(mockMvc, BASE_PATH, noteRequest, status().isCreated(),
                new NoteResponse(noteRepoId, noteTitle, noteText, someNotebookId, TimeSource.now())
        );

        ArgumentCaptor<NoteEntity> noteArgumentCaptor = ArgumentCaptor.forClass(NoteEntity.class);
        verify(noteRepository, times(1)).save(noteArgumentCaptor.capture());
        Assert.assertNull(noteArgumentCaptor.getValue().getId());
        Assert.assertEquals(noteTitle, noteArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(noteText, noteArgumentCaptor.getValue().getText());
        Assert.assertEquals(someNotebookId, noteArgumentCaptor.getValue().getNotebook().getId());
        verify(notebookRepository, times(1)).findById(someNotebookId);
    }

    @Test
    public void shouldReturnNotebookNotFoundWhileSaveNote() throws Exception {
        Long noteRepoId = 17L;
        String noteTitle = "New note";
        String noteText = "Some text";
        NoteEntity noteRepo = new NoteEntity(noteTitle, noteText, someNotebook);
        noteRepo.setId(noteRepoId);
        NoteRequest noteRequest = new NoteRequest(noteTitle, noteText, someNotebookId);

        when(notebookRepository.findById(someNotebookId)).thenReturn(Optional.empty());

        mockPostRequest(mockMvc, BASE_PATH, noteRequest, status().isNotFound(),
                new ApiMessage("Notebook with id=" + someNotebookId + " not found")
        );

        verify(notebookRepository, times(1)).findById(someNotebookId);
    }

    @Test
    public void shouldUpdateNote() throws Exception {
        Long oldNotebookId = 75L;
        NotebookEntity oldNotebook = new NotebookEntity("Old notebook");
        oldNotebook.setId(oldNotebookId);
        Long id = 10L;
        String repoTitle = "Old note";
        String repoText = "Old text";
        NoteEntity repoNote = new NoteEntity(repoTitle, repoText, oldNotebook);
        repoNote.setId(id);
        // set lastModifiedOn to 1 hour before now
        repoNote.setLastModifiedOn(new Date(TimeSource.now().getTime() - 3600000));
        String newTitle = "New note";
        String newText = "New text";
        NoteEntity updatedNote = new NoteEntity(newTitle, newText, someNotebook);
        updatedNote.setId(id);
        NoteRequest noteRequest = new NoteRequest(newTitle, newText, someNotebookId);

        when(noteRepository.findById(id)).thenReturn(Optional.of(repoNote));
        when(notebookRepository.findById(someNotebookId)).thenReturn(Optional.of(someNotebook));
        when(noteRepository.save(any())).thenReturn(repoNote);

        mockPutRequest(mockMvc, BASE_PATH, id, noteRequest, status().isOk(),
                new NoteResponse(id, newTitle, newText, someNotebookId, TimeSource.now())
        );

        verify(noteRepository, times(1)).findById(id);
        ArgumentCaptor<NoteEntity> noteArgumentCaptor = ArgumentCaptor.forClass(NoteEntity.class);
        verify(noteRepository, times(1)).save(noteArgumentCaptor.capture());
        Assert.assertEquals(id, noteArgumentCaptor.getValue().getId());
        Assert.assertEquals(newTitle, noteArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(newText, noteArgumentCaptor.getValue().getText());
        Assert.assertEquals(someNotebookId, noteArgumentCaptor.getValue().getNotebook().getId());
        verify(notebookRepository, times(1)).findById(someNotebookId);
    }

    @Test
    public void shouldReturnNotFoundWhileUpdateNote() throws Exception {
        Long id = 34L;
        String newTitle = "New note";
        String newText = "New text";
        NoteRequest noteRequest = new NoteRequest(newTitle, newText, someNotebookId);

        when(noteRepository.findById(id)).thenReturn(Optional.empty());

        mockPutRequest(mockMvc, BASE_PATH, id, noteRequest, status().isNotFound(),
                new ApiMessage("Note with id=" + id + " not found")
        );

        verify(noteRepository, times(1)).findById(id);
    }

    @Test
    public void shouldReturnNotebookNotFoundWhileUpdateNote() throws Exception {
        Long id = 10L;
        String repoTitle = "Old note";
        String repoText = "Old text";
        NoteEntity repoNote = new NoteEntity(repoTitle, repoText, someNotebook);
        repoNote.setId(id);
        String newTitle = "New note";
        String newText = "New text";
        NoteRequest noteRequest = new NoteRequest(newTitle, newText, someNotebookId);

        when(noteRepository.findById(id)).thenReturn(Optional.of(repoNote));
        when(notebookRepository.findById(someNotebookId)).thenReturn(Optional.empty());

        mockPutRequest(mockMvc, BASE_PATH, id, noteRequest, status().isNotFound(),
                new ApiMessage("Notebook with id=" + someNotebookId + " not found")
        );

        verify(noteRepository, times(1)).findById(id);
        verify(notebookRepository, times(1)).findById(someNotebookId);
    }


    @Test
    public void shouldDeleteNoteById() throws Exception {
        final Long id = 12L;

        when(noteRepository.existsById(id)).thenReturn(true);

        mockDeleteRequest(mockMvc, BASE_PATH, id, status().isOk(),
                new ApiMessage("Note deleted")
        );

        verify(noteRepository, times(1)).existsById(id);
        verify(noteRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldReturnNotFoundWhileDeleteNoteById() throws Exception {
        final Long id = 43L;

        when(noteRepository.existsById(id)).thenReturn(false);

        mockDeleteRequest(mockMvc, BASE_PATH, id, status().isNotFound(),
                new ApiMessage("Note with id=" + id + " not found")
        );

        verify(noteRepository, times(1)).existsById(id);
    }


}
