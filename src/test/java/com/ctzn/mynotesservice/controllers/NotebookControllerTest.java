package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiExceptionHandler;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.notebook.NotebookController;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookRequest;
import com.ctzn.mynotesservice.model.notebook.NotebookResponse;
import com.ctzn.mynotesservice.repositories.NotebookRepository;
import com.ctzn.mynotesservice.repositories.UserRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static com.ctzn.mynotesservice.controllers.RestTestUtil.*;
import static com.ctzn.mynotesservice.controllers.StaticTestProvider.getEmptyNotebook;
import static com.ctzn.mynotesservice.controllers.StaticTestProvider.getTwoNotebooksList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// TODO: repair these tests
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class NotebookControllerTest {

    private static final String BASE_PATH = NotebookController.BASE_PATH;

    @Mock
    private NotebookRepository notebookRepository;

    // TODO: test interactions with userRepository
    @Mock
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new NotebookController(userRepository, notebookRepository, new DomainMapper()))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        TimeSource.setFixed(true);
    }

    @After
    public void checkRepository() {
        verifyNoMoreInteractions(notebookRepository);
    }

    @Test
    public void shouldReturnAllNotebooks() throws Exception {
        when(notebookRepository.findAll()).thenReturn(getTwoNotebooksList());

        mockGetRequest(mockMvc, BASE_PATH, null, status().isOk(),
                Arrays.asList(
                        new NotebookResponse(1L, "Notebook 1", 2),
                        new NotebookResponse(2L, "Notebook 2", 0)
                )
        );

        verify(notebookRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnNotebookById() throws Exception {

        NotebookEntity notebook = getTwoNotebooksList().get(0);
        long id = notebook.getId();

        when(notebookRepository.findById(id)).thenReturn(Optional.of(notebook));

        mockGetRequest(mockMvc, BASE_PATH, id, status().isOk(),
                new NotebookResponse(notebook.getId(), notebook.getName(), notebook.getSize())
        );

        verify(notebookRepository, times(1)).findById(id);
    }

    @Test
    public void shouldReturnNotFoundWhileFindNotebookById() throws Exception {
        long id = 76L;

        when(notebookRepository.findById(id)).thenReturn(Optional.empty());

        mockGetRequest(mockMvc, BASE_PATH, id, status().isNotFound(),
                new ApiMessage("Notebook with id=" + id + " not found")
        );

        verify(notebookRepository, times(1)).findById(id);
    }

    @Test
    public void shouldSaveNotebook() throws Exception {
        long id = 5L;
        String name = "New notebook";
        NotebookRequest notebookRequest = new NotebookRequest(name);
        NotebookEntity notebook = getEmptyNotebook(id, name);
        NotebookResponse notebookResponse = new NotebookResponse(id, name, 0);

        when(notebookRepository.save(any())).thenReturn(notebook);

        mockPostRequest(mockMvc, BASE_PATH, notebookRequest, status().isCreated(), notebookResponse);

        ArgumentCaptor<NotebookEntity> notebookArgumentCaptor = ArgumentCaptor.forClass(NotebookEntity.class);
        verify(notebookRepository, times(1)).save(notebookArgumentCaptor.capture());
        Assert.assertNull(notebookArgumentCaptor.getValue().getId());
        Assert.assertEquals(name, notebookArgumentCaptor.getValue().getName());
    }

    @Test
    public void shouldUpdateNotebookById() throws Exception {
        Long id = 763L;
        NotebookEntity oldNotebook = getEmptyNotebook(id, "Old name");
        String newName = "New name";
        NotebookEntity newNotebook = getEmptyNotebook(id, newName);
        NotebookRequest notebookRequest = new NotebookRequest(newName);
        NotebookResponse notebookResponse = new NotebookResponse(id, newName, 0);

        when(notebookRepository.findById(id)).thenReturn(Optional.of(oldNotebook));
        when(notebookRepository.save(any())).thenReturn(newNotebook);

        mockPutRequest(mockMvc, BASE_PATH, id, notebookRequest, status().isOk(), notebookResponse);

        ArgumentCaptor<NotebookEntity> captor = ArgumentCaptor.forClass(NotebookEntity.class);
        verify(notebookRepository, times(1)).findById(id);
        verify(notebookRepository, times(1)).save(captor.capture());
        Assert.assertEquals(id, captor.getValue().getId());
        Assert.assertEquals(newName, captor.getValue().getName());
        Assert.assertEquals(Integer.valueOf(0), captor.getValue().getSize());
    }

    @Test
    public void shouldReturnNotFoundWhileUpdateNotebookById() throws Exception {
        Long id = 345L;
        NotebookRequest notebookRequest = new NotebookRequest("New name");

        when(notebookRepository.findById(id)).thenReturn(Optional.empty());

        mockPutRequest(mockMvc, BASE_PATH, id, notebookRequest, status().isNotFound(),
                new ApiMessage("Notebook with id=" + id + " not found")
        );

        verify(notebookRepository, times(1)).findById(id);
    }

    @Test
    public void shouldDeleteNotebookById() throws Exception {
        final Long id = 1L;

        when(notebookRepository.existsById(id)).thenReturn(true);

        mockDeleteRequest(mockMvc, BASE_PATH, id, status().isOk(),
                new ApiMessage("Notebook deleted")
        );

        verify(notebookRepository, times(1)).existsById(id);
        verify(notebookRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldReturnNotFoundWhileDeleteNotebookById() throws Exception {
        final Long id = 1L;

        when(notebookRepository.existsById(id)).thenReturn(false);

        mockDeleteRequest(mockMvc, BASE_PATH, id, status().isNotFound(),
                new ApiMessage("Notebook with id=" + id + " not found")
        );

        verify(notebookRepository, times(1)).existsById(id);
    }

}
