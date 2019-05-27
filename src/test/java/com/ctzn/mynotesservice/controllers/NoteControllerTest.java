package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiExceptionHandler;
import com.ctzn.mynotesservice.model.note.NoteController;
import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.repositories.NoteRepository;
import com.ctzn.mynotesservice.repositories.NotebookRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ctzn.mynotesservice.Util.mapOf;
import static com.ctzn.mynotesservice.controllers.RestTestUtil.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
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
        someNotebookId = 255L;
        someNotebook = new NotebookEntity("Some notebook");
        someNotebook.setId(someNotebookId);
    }

    @Test
    public void getAll() throws Exception {
        final NoteEntity note1 = new NoteEntity("NoteEntity 1", "NoteEntity 1 text", someNotebook);
        note1.setId(1L);

        final NoteEntity note2 = new NoteEntity("NoteEntity 2", "NoteEntity 2 text", someNotebook);
        note2.setId(2L);

        List<NoteEntity> noteList = Arrays.asList(note1, note2);

        // should return a list of entities
        reset(noteRepository);
        when(noteRepository.findAll()).thenReturn(noteList);

        mockGetRequest(mockMvc, BASE_PATH, null, status().isOk(), noteList);

        verify(noteRepository, times(1)).findAll();
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    public void getOne() throws Exception {
        final Long Id = 1L;

        final NoteEntity noteRepo = new NoteEntity("NoteEntity from repo", "This is a text", someNotebook);
        noteRepo.setId(Id);

        // should return entity
        reset(noteRepository);
        when(noteRepository.findById(Id)).thenReturn(java.util.Optional.of(noteRepo));

        mockGetRequest(mockMvc, BASE_PATH, Id, status().isOk(), noteRepo);

        verify(noteRepository, times(1)).findById(Id);
        verifyNoMoreInteractions(noteRepository);

        // should return "not found" if entity does not exist
        reset(noteRepository);
        when(noteRepository.findById(Id)).thenReturn(Optional.empty());

        mockGetRequest(mockMvc, BASE_PATH, Id, status().isNotFound(), null);

        verify(noteRepository, times(1)).findById(Id);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    public void create() throws Exception {
        Long noteRepoId = 1L;
        Long someId = 101L;

        final String noteTitle = "New note";
        final String noteText = "Some text";

        final NoteEntity noteRepo = new NoteEntity(noteTitle, noteText, someNotebook);
        noteRepo.setId(noteRepoId);

        Map noteDTO = mapOf(
                "title", noteTitle,
                "text", noteText,
                "notebook", mapOf("id", someNotebookId)
        );

        // should save entity
        reset(noteRepository, notebookRepository);
        when(noteRepository.save(any())).thenReturn(noteRepo);
        when(notebookRepository.existsById(someNotebookId)).thenReturn(true);

        mockPostRequest(mockMvc, BASE_PATH, noteDTO, status().isCreated(), noteRepo);

        ArgumentCaptor<NoteEntity> noteArgumentCaptor = ArgumentCaptor.forClass(NoteEntity.class);
        verify(noteRepository, times(1)).save(noteArgumentCaptor.capture());
        Assert.assertNull(noteArgumentCaptor.getValue().getId());
        Assert.assertEquals(noteTitle, noteArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(noteText, noteArgumentCaptor.getValue().getText());
        Assert.assertEquals(someNotebookId, noteArgumentCaptor.getValue().getNotebook().getId());
        verify(notebookRepository, times(1)).existsById(someNotebookId);
        verifyNoMoreInteractions(noteRepository, notebookRepository);

        // should return "bad request" if note id is not null
        reset(noteRepository, notebookRepository);

        mockPostRequest(mockMvc, BASE_PATH, mapOf(
                "id", someId,
                "title", noteTitle,
                "text", noteText,
                "notebook", mapOf("id", someNotebookId)
        ), status().isBadRequest(), null);

        verifyNoMoreInteractions(noteRepository, notebookRepository);

        // should return "bad request" if notebook id is null
        reset(noteRepository, notebookRepository);

        mockPostRequest(mockMvc, BASE_PATH, mapOf(
                "title", noteTitle,
                "text", noteText,
                "notebook", mapOf("id", "")
        ), status().isBadRequest(), null);

        verifyNoMoreInteractions(noteRepository, notebookRepository);

        // should return "not found" if notebook does not exist
        reset(noteRepository, notebookRepository);
        when(notebookRepository.existsById(someNotebookId)).thenReturn(false);

        mockPostRequest(mockMvc, BASE_PATH, noteDTO, status().isNotFound(), null);

        verify(notebookRepository, times(1)).existsById(someNotebookId);
        verifyNoMoreInteractions(noteRepository, notebookRepository);
    }

    @Test
    public void update() throws Exception {
        final Long noteRepoId = 1L;
        final Long someId = 101L;
        final String noteTitle = "Updated note";
        final String noteText = "Some text";

        final NoteEntity noteRepo = new NoteEntity(noteTitle, noteText, someNotebook);
        noteRepo.setId(noteRepoId);

        Map noteDTO = mapOf(
                "id", noteRepoId,
                "title", noteTitle,
                "text", noteText,
                "notebook", mapOf("id", someNotebookId)
        );

        // should update entity
        reset(noteRepository, notebookRepository);
        when(noteRepository.existsById(noteRepoId)).thenReturn(true);
        when(noteRepository.save(any())).thenReturn(noteRepo);
        when(notebookRepository.existsById(someNotebookId)).thenReturn(true);

        mockPutRequest(mockMvc, BASE_PATH, noteRepoId, noteDTO, status().isOk(), noteRepo);

        ArgumentCaptor<NoteEntity> noteArgumentCaptor = ArgumentCaptor.forClass(NoteEntity.class);
        verify(noteRepository, times(1)).existsById(noteRepoId);
        verify(noteRepository, times(1)).save(noteArgumentCaptor.capture());
        Assert.assertEquals(noteRepoId, noteArgumentCaptor.getValue().getId());
        Assert.assertEquals(noteTitle, noteArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(noteText, noteArgumentCaptor.getValue().getText());
        Assert.assertEquals(someNotebookId, noteArgumentCaptor.getValue().getNotebook().getId());
        verify(notebookRepository, times(1)).existsById(someNotebookId);
        verifyNoMoreInteractions(noteRepository, notebookRepository);

        // should return "not fount" if note id is not exist
        reset(noteRepository, notebookRepository);
        when(noteRepository.existsById(noteRepoId)).thenReturn(false);

        mockPutRequest(mockMvc, BASE_PATH, noteRepoId, noteDTO, status().isNotFound(), null);

        verify(noteRepository, times(1)).existsById(noteRepoId);
        verifyNoMoreInteractions(noteRepository, notebookRepository);

        // should return "bad request" if note id does not match path id
        reset(noteRepository, notebookRepository);
        when(noteRepository.existsById(someId)).thenReturn(true);

        mockPutRequest(mockMvc, BASE_PATH, someId, noteDTO, status().isBadRequest(), null);

        verify(noteRepository, times(1)).existsById(someId);
        verifyNoMoreInteractions(noteRepository, notebookRepository);

        // should return "bad request" if notebook id is null
        reset(noteRepository, notebookRepository);
        when(noteRepository.existsById(noteRepoId)).thenReturn(true);

        mockPutRequest(mockMvc, BASE_PATH, noteRepoId, mapOf(
                "id", noteRepoId,
                "title", noteTitle,
                "text", noteText,
                "notebook", mapOf()
        ), status().isBadRequest(), null);

        verify(noteRepository, times(1)).existsById(noteRepoId);
        verifyNoMoreInteractions(noteRepository, notebookRepository);

        // should return "not found" if notebook does not exist
        reset(noteRepository, notebookRepository);
        when(noteRepository.existsById(noteRepoId)).thenReturn(true);
        when(notebookRepository.existsById(someNotebookId)).thenReturn(false);

        mockPutRequest(mockMvc, BASE_PATH, noteRepoId, noteDTO, status().isNotFound(), null);

        verify(noteRepository, times(1)).existsById(noteRepoId);
        verify(notebookRepository, times(1)).existsById(someNotebookId);
        verifyNoMoreInteractions(noteRepository, notebookRepository);
    }

    @Test
    public void delete() throws Exception {
        final Long Id = 1L;

        // should delete entity
        reset(noteRepository);
        when(noteRepository.existsById(Id)).thenReturn(true);

        mockDeleteRequest(mockMvc, BASE_PATH, Id, status().isOk());

        verify(noteRepository, times(1)).existsById(Id);
        verify(noteRepository, times(1)).deleteById(Id);
        verifyNoMoreInteractions(noteRepository);

        // should return "not fond" if entity does not exist
        reset(noteRepository);
        when(noteRepository.existsById(Id)).thenReturn(false);

        mockDeleteRequest(mockMvc, BASE_PATH, Id, status().isNotFound());

        verify(noteRepository, times(1)).existsById(Id);
        verifyNoMoreInteractions(noteRepository);
    }


}
