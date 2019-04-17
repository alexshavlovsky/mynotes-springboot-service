package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NotebookRepository;
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
public class NotebookControllerTest {

    private static final String API_PATH = "/api/notebooks/";

    @Mock
    private NotebookRepository notebookRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new NotebookController(notebookRepository)).build();
    }

    @Test
    public void getAll() throws Exception {
        final Notebook notebook1 = new Notebook("Notebook 1");
        notebook1.setId(1L);

        final Notebook notebook2 = new Notebook("Notebook 2");
        notebook2.setId(2L);

        List<Notebook> notebookList = Arrays.asList(notebook1, notebook2);

        // should return a list of entities
        reset(notebookRepository);
        when(notebookRepository.findAll()).thenReturn(notebookList);

        mockGetRequest(mockMvc, API_PATH, null, status().isOk(), notebookList);

        verify(notebookRepository, times(1)).findAll();
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void getOne() throws Exception {
        final Long Id = 1L;

        final Notebook notebookRepo = new Notebook("Notebook from repo");
        notebookRepo.setId(Id);

        // should return entity
        reset(notebookRepository);
        when(notebookRepository.findById(Id)).thenReturn(java.util.Optional.of(notebookRepo));

        mockGetRequest(mockMvc, API_PATH, Id, status().isOk(), notebookRepo);

        verify(notebookRepository, times(1)).findById(Id);
        verifyNoMoreInteractions(notebookRepository);

        // should return "not found" if entity does not exist
        reset(notebookRepository);
        when(notebookRepository.findById(Id)).thenReturn(Optional.empty());

        mockGetRequest(mockMvc, API_PATH, Id, status().isNotFound(), null);

        verify(notebookRepository, times(1)).findById(Id);
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void create() throws Exception {
        final String notebookName = "New notebook";

        final Notebook notebookRepo = new Notebook(notebookName);
        notebookRepo.setId(1L);

        final Notebook notebookDtoNullId = new Notebook(notebookName);

        final Notebook notebookDtoNotNullId = new Notebook(notebookName);
        notebookDtoNotNullId.setId(2L);

        // should save entity
        reset(notebookRepository);
        when(notebookRepository.save(any())).thenReturn(notebookRepo);

        mockPostRequest(mockMvc, API_PATH, notebookDtoNullId, status().isOk(), notebookRepo);

        ArgumentCaptor<Notebook> notebookArgumentCaptor = ArgumentCaptor.forClass(Notebook.class);
        verify(notebookRepository, times(1)).save(notebookArgumentCaptor.capture());
        verifyNoMoreInteractions(notebookRepository);
        Assert.assertNull(notebookArgumentCaptor.getValue().getId());
        Assert.assertEquals(notebookName, notebookArgumentCaptor.getValue().getName());

        // should return "bad request" if id is not null
        reset(notebookRepository);

        mockPostRequest(mockMvc, API_PATH, notebookDtoNotNullId, status().isBadRequest(), null);

        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void update() throws Exception {
        final Long notebookRepoId = 1L;
        final Long notebookNotExistId = 2L;
        final String updatedNotebookName = "Updated notebook";

        final Notebook updatedNotebook = new Notebook(updatedNotebookName);
        updatedNotebook.setId(notebookRepoId);

        final Notebook notebookDtoNullId = new Notebook(updatedNotebookName);

        final Notebook notebookDtoNotNullId = new Notebook(updatedNotebookName);
        notebookDtoNotNullId.setId(notebookNotExistId);

        // should update entity
        reset(notebookRepository);
        when(notebookRepository.existsById(notebookRepoId)).thenReturn(true);
        when(notebookRepository.save(any())).thenReturn(updatedNotebook);

        mockPutRequest(mockMvc, API_PATH, notebookRepoId, notebookDtoNullId, status().isOk(), updatedNotebook);

        ArgumentCaptor<Notebook> notebookArgumentCaptor = ArgumentCaptor.forClass(Notebook.class);
        verify(notebookRepository, times(1)).existsById(notebookRepoId);
        verify(notebookRepository, times(1)).save(notebookArgumentCaptor.capture());
        verifyNoMoreInteractions(notebookRepository);
        Assert.assertEquals(notebookRepoId, notebookArgumentCaptor.getValue().getId());
        Assert.assertEquals(updatedNotebookName, notebookArgumentCaptor.getValue().getName());

        // should return "not fond" if entity does not exist
        reset(notebookRepository);
        when(notebookRepository.existsById(notebookNotExistId)).thenReturn(false);

        mockPutRequest(mockMvc, API_PATH, notebookNotExistId, notebookDtoNullId, status().isNotFound(), null);

        verify(notebookRepository, times(1)).existsById(notebookNotExistId);
        verifyNoMoreInteractions(notebookRepository);

        // should return "bad request" if dto.id != path.id
        reset(notebookRepository);
        when(notebookRepository.existsById(notebookRepoId)).thenReturn(true);

        mockPutRequest(mockMvc, API_PATH, notebookRepoId, notebookDtoNotNullId, status().isBadRequest(), null);

        verify(notebookRepository, times(1)).existsById(notebookRepoId);
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void delete() throws Exception {
        final Long Id = 1L;

        // should delete entity
        reset(notebookRepository);
        when(notebookRepository.existsById(Id)).thenReturn(true);

        mockDeleteRequest(mockMvc, API_PATH, Id, status().isOk());

        verify(notebookRepository, times(1)).existsById(Id);
        verify(notebookRepository, times(1)).deleteById(Id);
        verifyNoMoreInteractions(notebookRepository);

        // should return "not fond" if entity does not exist
        reset(notebookRepository);
        when(notebookRepository.existsById(Id)).thenReturn(false);

        mockDeleteRequest(mockMvc, API_PATH, Id, status().isNotFound());

        verify(notebookRepository, times(1)).existsById(Id);
        verifyNoMoreInteractions(notebookRepository);
    }

}
