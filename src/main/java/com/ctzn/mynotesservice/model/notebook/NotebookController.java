package com.ctzn.mynotesservice.model.notebook;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.note.NoteResponse;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.services.NotebookService;
import com.ctzn.mynotesservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(NotebookController.BASE_PATH)
public class NotebookController {

    public static final String BASE_PATH = "/api/notebooks";

    private UserService userService;
    private NotebookService notebookService;
    private DomainMapper domainMapper;

    public NotebookController(UserService userService, NotebookService notebookService, DomainMapper domainMapper) {
        this.userService = userService;
        this.notebookService = notebookService;
        this.domainMapper = domainMapper;
    }

    @GetMapping()
    public List<NotebookResponse> getAllNotebooks(Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        return domainMapper.mapAll(notebookService.getAllNotebooks(user), NotebookResponse.class);
    }

    @GetMapping("{id}/notes")
    public List<NoteResponse> getAllNotesByNotebook(@PathVariable long id, Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        NotebookEntity notebookEntity = notebookService.getNotebook(id, user);
        return domainMapper.mapAll(notebookService.getNotesFromNotebook(notebookEntity), NoteResponse.class);
    }

    @PostMapping() // create only
    @ResponseStatus(HttpStatus.CREATED)
    public NotebookResponse saveNotebook(@RequestBody NotebookRequest notebookRequest, Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        NotebookEntity notebook = domainMapper.map(notebookRequest, NotebookEntity.class);
        notebook.setUser(user);
        return domainMapper.map(notebookService.saveNotebook(notebook), NotebookResponse.class);
    }

    @PutMapping("{id}") // update only
    public NotebookResponse updateNotebook(@RequestBody NotebookRequest notebookRequest,
                                           @PathVariable("id") long id, Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        NotebookEntity notebookEntity = domainMapper.map(notebookRequest, notebookService.getNotebook(id, user));
        return domainMapper.map(notebookService.saveNotebook(notebookEntity), NotebookResponse.class);
    }

    @DeleteMapping(path = "{id}")
    public ApiMessage deleteNotebook(@PathVariable("id") long id, Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        notebookService.deleteNotebook(notebookService.getNotebook(id, user));
        return new ApiMessage("Notebook deleted");
    }

}
