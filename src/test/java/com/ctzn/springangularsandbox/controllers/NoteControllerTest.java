package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.model.Note;
import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NoteRepository;
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
import java.util.Optional;

import static com.ctzn.springangularsandbox.controllers.RestTestUtil.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class NoteControllerTest {

    private static final String API_PATH = "/api/notes/";

    @Mock
    private NoteRepository noteRepository;

    private MockMvc mockMvc;

    private Notebook someNotebook;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new NoteController(noteRepository)).build();

        someNotebook = new Notebook("Some notebook");
        someNotebook.setId(255L);
    }

    @Test
    public void getAll() throws Exception {
        final Note note1 = new Note("Note 1", "Note 1 text", someNotebook);
        note1.setId(1L);

        final Note note2 = new Note("Note 2", "Note 2 text", someNotebook);
        note2.setId(2L);

        List<Note> noteList = Arrays.asList(note1, note2);

        // should return a list of entities
        reset(noteRepository);
        when(noteRepository.findAll()).thenReturn(noteList);

        mockGetRequest(mockMvc, API_PATH, null, status().isOk(), noteList);

        verify(noteRepository, times(1)).findAll();
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    public void getOne() throws Exception {
        final Long Id = 1L;

        final Note noteRepo = new Note("Note from repo", "This is a text", someNotebook);
        noteRepo.setId(Id);

        // should return entity
        reset(noteRepository);
        when(noteRepository.findById(Id)).thenReturn(java.util.Optional.of(noteRepo));

        mockGetRequest(mockMvc, API_PATH, Id, status().isOk(), noteRepo);

        verify(noteRepository, times(1)).findById(Id);
        verifyNoMoreInteractions(noteRepository);

        // should return "not found" if entity does not exist
        reset(noteRepository);
        when(noteRepository.findById(Id)).thenReturn(Optional.empty());

        mockGetRequest(mockMvc, API_PATH, Id, status().isNotFound(), null);

        verify(noteRepository, times(1)).findById(Id);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    public void create() throws Exception {
        final String noteTitle = "New note";
        final String noteText = "Some text";

        final Note noteRepo = new Note(noteTitle, noteText, someNotebook);
        noteRepo.setId(1L);

        final Note noteDtoNullId = new Note(noteTitle, noteText, someNotebook);

        final Note noteDtoNotNullId = new Note(noteTitle, noteText, someNotebook);
        noteDtoNotNullId.setId(2L);

        // should save entity
        reset(noteRepository);
        when(noteRepository.save(any())).thenReturn(noteRepo);

        mockPostRequest(mockMvc, API_PATH, noteDtoNullId, status().isOk(), noteRepo);

        ArgumentCaptor<Note> noteArgumentCaptor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository, times(1)).save(noteArgumentCaptor.capture());
        verifyNoMoreInteractions(noteRepository);
        Assert.assertNull(noteArgumentCaptor.getValue().getId());
        Assert.assertEquals(noteTitle, noteArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(noteText, noteArgumentCaptor.getValue().getText());

        // should return "bad request" if id is not null
        reset(noteRepository);

        mockPostRequest(mockMvc, API_PATH, noteDtoNotNullId, status().isBadRequest(), null);

        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    public void update() throws Exception {
        final Long noteRepoId = 1L;
        final Long noteNotExistId = 2L;
        final String updatedNoteTitle = "Updated note";
        final String updatedNoteText = "Some text";

        final Note updatedNote = new Note(updatedNoteTitle, updatedNoteText, someNotebook);
        updatedNote.setId(noteRepoId);

        final Note noteDtoNullId = new Note(updatedNoteTitle, updatedNoteText, someNotebook);

        final Note noteDtoNotNullId = new Note(updatedNoteTitle, updatedNoteText, someNotebook);
        noteDtoNotNullId.setId(noteNotExistId);

        // should update entity
        reset(noteRepository);
        when(noteRepository.existsById(noteRepoId)).thenReturn(true);
        when(noteRepository.save(any())).thenReturn(updatedNote);

        mockPutRequest(mockMvc, API_PATH, noteRepoId, noteDtoNullId, status().isOk(), updatedNote);

        ArgumentCaptor<Note> noteArgumentCaptor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository, times(1)).existsById(noteRepoId);
        verify(noteRepository, times(1)).save(noteArgumentCaptor.capture());
        verifyNoMoreInteractions(noteRepository);
        Assert.assertEquals(noteRepoId, noteArgumentCaptor.getValue().getId());
        Assert.assertEquals(updatedNoteTitle, noteArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(updatedNoteText, noteArgumentCaptor.getValue().getText());

        // should return "not fond" if entity does not exist
        reset(noteRepository);
        when(noteRepository.existsById(noteNotExistId)).thenReturn(false);

        mockPutRequest(mockMvc, API_PATH, noteNotExistId, noteDtoNullId, status().isNotFound(), null);

        verify(noteRepository, times(1)).existsById(noteNotExistId);
        verifyNoMoreInteractions(noteRepository);

        // should return "bad request" if dto.id != path.id
        reset(noteRepository);
        when(noteRepository.existsById(noteRepoId)).thenReturn(true);

        mockPutRequest(mockMvc, API_PATH, noteRepoId, noteDtoNotNullId, status().isBadRequest(), null);

        verify(noteRepository, times(1)).existsById(noteRepoId);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    public void delete() throws Exception {
        final Long Id = 1L;

        // should delete entity
        reset(noteRepository);
        when(noteRepository.existsById(Id)).thenReturn(true);

        mockDeleteRequest(mockMvc, API_PATH, Id, status().isOk());

        verify(noteRepository, times(1)).existsById(Id);
        verify(noteRepository, times(1)).deleteById(Id);
        verifyNoMoreInteractions(noteRepository);

        // should return "not fond" if entity does not exist
        reset(noteRepository);
        when(noteRepository.existsById(Id)).thenReturn(false);

        mockDeleteRequest(mockMvc, API_PATH, Id, status().isNotFound());

        verify(noteRepository, times(1)).existsById(Id);
        verifyNoMoreInteractions(noteRepository);
    }


}