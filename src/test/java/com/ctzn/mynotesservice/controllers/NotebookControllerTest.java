package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiExceptionHandler;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimestampSource;
import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookController;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookRequest;
import com.ctzn.mynotesservice.model.notebook.NotebookResponse;
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
import java.util.Optional;

import static com.ctzn.mynotesservice.controllers.RestTestUtil.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class NotebookControllerTest {

    private static final String BASE_PATH = NotebookController.BASE_PATH;

    @Mock
    private NotebookRepository notebookRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new NotebookController(notebookRepository, new DomainMapper()))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        TimestampSource.setFixed(true);
    }

    private List<NotebookEntity> getNotebookList() {
        NotebookEntity notebook1 = new NotebookEntity("Notebook 1");
        notebook1.setId(1L);

        NotebookEntity notebook2 = new NotebookEntity("Notebook 2");
        notebook2.setId(2L);

        NoteEntity note1 = new NoteEntity("Note 1.1", "Some text 1", notebook1);
        note1.setId(3L);
        notebook1.getNotes().add(note1);

        NoteEntity note2 = new NoteEntity("Note 1.2", "Some text 2", notebook1);
        note2.setId(4L);
        notebook1.getNotes().add(note2);

        return Arrays.asList(notebook1, notebook2);
    }

    @Test
    public void getAll() throws Exception {
        // should return a list of entities
        reset(notebookRepository);
        when(notebookRepository.findAll()).thenReturn(getNotebookList());

        mockGetRequest(mockMvc, BASE_PATH, null, status().isOk(),
                Arrays.asList(
                        new NotebookResponse(1L, "Notebook 1", 2),
                        new NotebookResponse(2L, "Notebook 2", 0)
                )
        );

        verify(notebookRepository, times(1)).findAll();
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void getOne() throws Exception {
        NotebookEntity notebook = getNotebookList().get(0);
        long id = notebook.getId();

        // should return entity
        reset(notebookRepository);
        when(notebookRepository.findById(id)).thenReturn(java.util.Optional.of(notebook));

        mockGetRequest(mockMvc, BASE_PATH, id, status().isOk(), new NotebookResponse(1L, "Notebook 1", 2));

        verify(notebookRepository, times(1)).findById(id);
        verifyNoMoreInteractions(notebookRepository);

        // should return "not found" if entity does not exist
        reset(notebookRepository);
        when(notebookRepository.findById(id)).thenReturn(Optional.empty());

        ApiMessage expected = new ApiMessage("Notebook with id=" + id + " not found");

        mockGetRequest(mockMvc, BASE_PATH, id, status().isNotFound(), expected);

        verify(notebookRepository, times(1)).findById(id);
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void create() throws Exception {
        NotebookEntity notebook = getNotebookList().get(1);
        long id = notebook.getId();
        String name = notebook.getName();
        int size = notebook.getSize();

        NotebookRequest notebookRequest = new NotebookRequest(name);
        final NotebookResponse notebookResponse = new NotebookResponse(id, name, size);

        // should save entity
        reset(notebookRepository);
        when(notebookRepository.save(any())).thenReturn(notebook);

        mockPostRequest(mockMvc, BASE_PATH, notebookRequest, status().isCreated(), notebookResponse);

        ArgumentCaptor<NotebookEntity> notebookArgumentCaptor = ArgumentCaptor.forClass(NotebookEntity.class);
        verify(notebookRepository, times(1)).save(notebookArgumentCaptor.capture());
        verifyNoMoreInteractions(notebookRepository);
        Assert.assertNull(notebookArgumentCaptor.getValue().getId());
        Assert.assertEquals(name, notebookArgumentCaptor.getValue().getName());
    }

    @Test
    public void update() throws Exception {
        NotebookEntity notebook = getNotebookList().get(1);
        Long id = notebook.getId();
        int size = notebook.getSize();

        String newName = "New name";

        NotebookEntity newNotebook = new NotebookEntity(newName);
        newNotebook.setId(id);

        NotebookRequest notebookRequest = new NotebookRequest(newName);
        final NotebookResponse notebookResponse = new NotebookResponse(id, newName, size);

        // should update entity
        reset(notebookRepository);
        when(notebookRepository.findById(id)).thenReturn(Optional.of(notebook));
        when(notebookRepository.save(any())).thenReturn(newNotebook);

        mockPutRequest(mockMvc, BASE_PATH, id, notebookRequest, status().isOk(), notebookResponse);

        ArgumentCaptor<NotebookEntity> notebookArgumentCaptor = ArgumentCaptor.forClass(NotebookEntity.class);
        verify(notebookRepository, times(1)).findById(id);
        verify(notebookRepository, times(1)).save(notebookArgumentCaptor.capture());
        verifyNoMoreInteractions(notebookRepository);
        Assert.assertEquals(id, notebookArgumentCaptor.getValue().getId());
        Assert.assertEquals(newName, notebookArgumentCaptor.getValue().getName());

        // should return "not fond" if entity does not exist
        reset(notebookRepository);
        when(notebookRepository.findById(id)).thenReturn(Optional.empty());

        mockPutRequest(mockMvc, BASE_PATH, id, notebookRequest, status().isNotFound(), null);

        verify(notebookRepository, times(1)).findById(id);
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void delete() throws Exception {
        final Long id = 1L;

        // should delete entity
        reset(notebookRepository);
        when(notebookRepository.existsById(id)).thenReturn(true);

        mockDeleteRequest(mockMvc, BASE_PATH, id, status().isOk(), new ApiMessage("Notebook deleted"));

        verify(notebookRepository, times(1)).existsById(id);
        verify(notebookRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(notebookRepository);

        // should return "not fond" if entity does not exist
        reset(notebookRepository);
        when(notebookRepository.existsById(id)).thenReturn(false);

        mockDeleteRequest(mockMvc, BASE_PATH, id, status().isNotFound(), null);

        verify(notebookRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(notebookRepository);
    }

}
