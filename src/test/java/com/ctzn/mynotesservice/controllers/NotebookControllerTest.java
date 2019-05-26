package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.notebook.NotebookController;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
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
        mockMvc = MockMvcBuilders.standaloneSetup(new NotebookController(notebookRepository, new DomainMapper())).build();
    }

    @Test
    public void getAll() throws Exception {
        final NotebookEntity notebook1 = new NotebookEntity("NotebookEntity 1");
        notebook1.setId(1L);

        final NotebookEntity notebook2 = new NotebookEntity("NotebookEntity 2");
        notebook2.setId(2L);

        List<NotebookEntity> notebookList = Arrays.asList(notebook1, notebook2);

        // should return a list of entities
        reset(notebookRepository);
        when(notebookRepository.findAll()).thenReturn(notebookList);

        mockGetRequest(mockMvc, BASE_PATH, null, status().isOk(), notebookList);

        verify(notebookRepository, times(1)).findAll();
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void getOne() throws Exception {
        final Long Id = 1L;

        final NotebookEntity notebookRepo = new NotebookEntity("NotebookEntity from repo");
        notebookRepo.setId(Id);

        // should return entity
        reset(notebookRepository);
        when(notebookRepository.findById(Id)).thenReturn(java.util.Optional.of(notebookRepo));

        mockGetRequest(mockMvc, BASE_PATH, Id, status().isOk(), notebookRepo);

        verify(notebookRepository, times(1)).findById(Id);
        verifyNoMoreInteractions(notebookRepository);

        // should return "not found" if entity does not exist
        reset(notebookRepository);
        when(notebookRepository.findById(Id)).thenReturn(Optional.empty());

        mockGetRequest(mockMvc, BASE_PATH, Id, status().isNotFound(), null);

        verify(notebookRepository, times(1)).findById(Id);
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void create() throws Exception {
        final String notebookName = "New notebook";

        final NotebookEntity notebookRepo = new NotebookEntity(notebookName);
        notebookRepo.setId(1L);

        final NotebookEntity notebookDtoNullId = new NotebookEntity(notebookName);

        final NotebookEntity notebookDtoNotNullId = new NotebookEntity(notebookName);
        notebookDtoNotNullId.setId(2L);

        // should save entity
        reset(notebookRepository);
        when(notebookRepository.save(any())).thenReturn(notebookRepo);

        mockPostRequest(mockMvc, BASE_PATH, notebookDtoNullId, status().isCreated(), notebookRepo);

        ArgumentCaptor<NotebookEntity> notebookArgumentCaptor = ArgumentCaptor.forClass(NotebookEntity.class);
        verify(notebookRepository, times(1)).save(notebookArgumentCaptor.capture());
        verifyNoMoreInteractions(notebookRepository);
        Assert.assertNull(notebookArgumentCaptor.getValue().getId());
        Assert.assertEquals(notebookName, notebookArgumentCaptor.getValue().getName());

        // should return "bad request" if id is not null
        reset(notebookRepository);

        mockPostRequest(mockMvc, BASE_PATH, notebookDtoNotNullId, status().isBadRequest(), null);

        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void update() throws Exception {
        final Long repoId = 1L;
        final String updatedNotebookName = "Updated notebook";

        final NotebookEntity updatedNotebook = new NotebookEntity(updatedNotebookName);
        updatedNotebook.setId(repoId);

        final NotebookEntity notebookDtoNullId = new NotebookEntity(updatedNotebookName);

        final NotebookEntity notebookDto = new NotebookEntity(updatedNotebookName);
        notebookDto.setId(repoId);

        final NotebookEntity notebookDtoIdNotMatch = new NotebookEntity(updatedNotebookName);
        notebookDtoIdNotMatch.setId(2L);

        // should update entity
        reset(notebookRepository);
        when(notebookRepository.existsById(repoId)).thenReturn(true);
        when(notebookRepository.save(any())).thenReturn(updatedNotebook);

        mockPutRequest(mockMvc, BASE_PATH, repoId, notebookDto, status().isOk(), updatedNotebook);

        ArgumentCaptor<NotebookEntity> notebookArgumentCaptor = ArgumentCaptor.forClass(NotebookEntity.class);
        verify(notebookRepository, times(1)).existsById(repoId);
        verify(notebookRepository, times(1)).save(notebookArgumentCaptor.capture());
        verifyNoMoreInteractions(notebookRepository);
        Assert.assertEquals(repoId, notebookArgumentCaptor.getValue().getId());
        Assert.assertEquals(updatedNotebookName, notebookArgumentCaptor.getValue().getName());

        // should return "not fond" if entity does not exist
        reset(notebookRepository);
        when(notebookRepository.existsById(repoId)).thenReturn(false);

        mockPutRequest(mockMvc, BASE_PATH, repoId, notebookDto, status().isNotFound(), null);

        verify(notebookRepository, times(1)).existsById(repoId);
        verifyNoMoreInteractions(notebookRepository);

        // should return "bad request" if id is null
        reset(notebookRepository);
        when(notebookRepository.existsById(repoId)).thenReturn(true);

        mockPutRequest(mockMvc, BASE_PATH, repoId, notebookDtoNullId, status().isBadRequest(), null);

        verify(notebookRepository, times(1)).existsById(repoId);
        verifyNoMoreInteractions(notebookRepository);

        // should return "bad request" if object id does not match path id
        reset(notebookRepository);
        when(notebookRepository.existsById(repoId)).thenReturn(true);

        mockPutRequest(mockMvc, BASE_PATH, repoId, notebookDtoIdNotMatch, status().isBadRequest(), null);

        verify(notebookRepository, times(1)).existsById(repoId);
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void delete() throws Exception {
        final Long id = 1L;

        // should delete entity
        reset(notebookRepository);
        when(notebookRepository.existsById(id)).thenReturn(true);

        mockDeleteRequest(mockMvc, BASE_PATH, id, status().isOk());

        verify(notebookRepository, times(1)).existsById(id);
        verify(notebookRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(notebookRepository);

        // should return "not fond" if entity does not exist
        reset(notebookRepository);
        when(notebookRepository.existsById(id)).thenReturn(false);

        mockDeleteRequest(mockMvc, BASE_PATH, id, status().isNotFound());

        verify(notebookRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(notebookRepository);
    }

}
