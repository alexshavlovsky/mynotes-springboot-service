package com.ctzn.mynotesservice.model.notebook;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.note.NoteResponse;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.repositories.PersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(NotebookController.BASE_PATH)
public class NotebookController {

    public static final String BASE_PATH = "/api/notebooks/";

    private PersistenceService persistenceService;
    private DomainMapper domainMapper;

    public NotebookController(PersistenceService persistenceService, DomainMapper domainMapper) {
        this.persistenceService = persistenceService;
        this.domainMapper = domainMapper;
    }

    @GetMapping()
    public List<NotebookResponse> getAllNotebooks(Principal principal) throws ApiException {
        UserEntity user = persistenceService.getUser(principal);
        return domainMapper.mapAll(persistenceService.getAllNotebooks(user), NotebookResponse.class);
    }

    @GetMapping("{id}/notes")
    public List<NoteResponse> getAllNotesByNotebook(@PathVariable long id, Principal principal) throws ApiException {
        UserEntity user = persistenceService.getUser(principal);
        NotebookEntity notebookEntity = persistenceService.getNotebook(id, user);
        return domainMapper.mapAll(persistenceService.getNotesFromNotebook(notebookEntity), NoteResponse.class);
    }

    @GetMapping("{id}")
    public NotebookResponse getNotebook(@PathVariable("id") long id, Principal principal) throws ApiException {
        UserEntity user = persistenceService.getUser(principal);
        return domainMapper.map(persistenceService.getNotebook(id, user), NotebookResponse.class);
    }

    @PostMapping() // create only
    @ResponseStatus(HttpStatus.CREATED)
    public NotebookResponse saveNotebook(@RequestBody NotebookRequest notebookRequest, Principal principal) throws ApiException {
        UserEntity user = persistenceService.getUser(principal);
        NotebookEntity notebook = domainMapper.map(notebookRequest, NotebookEntity.class);
        notebook.setUser(user);
        return domainMapper.map(persistenceService.saveNotebook(notebook), NotebookResponse.class);
    }

    @PutMapping("{id}") // update only
    public NotebookResponse updateNotebook(@RequestBody NotebookRequest notebookRequest,
                                           @PathVariable("id") long id, Principal principal) throws ApiException {
        UserEntity user = persistenceService.getUser(principal);
        NotebookEntity notebookEntity = domainMapper.map(notebookRequest, persistenceService.getNotebook(id, user));
        return domainMapper.map(persistenceService.saveNotebook(notebookEntity), NotebookResponse.class);
    }

    @DeleteMapping(path = "{id}")
    public ApiMessage deleteNotebook(@PathVariable("id") long id, Principal principal) throws ApiException {
        UserEntity user = persistenceService.getUser(principal);
        persistenceService.deleteNotebook(persistenceService.getNotebook(id, user));
        return new ApiMessage("Notebook deleted");
    }

}
