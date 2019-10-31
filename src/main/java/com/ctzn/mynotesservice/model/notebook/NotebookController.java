package com.ctzn.mynotesservice.model.notebook;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.note.NoteResponse;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.model.user.UserRole;
import com.ctzn.mynotesservice.services.NotebookService;
import com.ctzn.mynotesservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public List<NotebookResponse> getAllNotebooks(Authentication auth) throws ApiException {
        UserEntity user = userService.getUserAssertRole(auth, UserRole.USER);
        return domainMapper.mapAll(notebookService.getAllNotebooks(user), NotebookResponse.class);
    }

    @GetMapping("{id}/notes")
    public List<NoteResponse> getAllNotesByNotebook(@PathVariable long id, Authentication auth) throws ApiException {
        UserEntity user = userService.getUserAssertRole(auth, UserRole.USER);
        NotebookEntity notebookEntity = notebookService.getNotebook(id, user);
        return domainMapper.mapAll(notebookService.getNotesFromNotebook(notebookEntity), NoteResponse.class);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public NotebookResponse saveNotebook(@RequestBody NotebookRequest notebookRequest, Authentication auth) throws ApiException {
        UserEntity user = userService.getUserAssertRole(auth, UserRole.USER);
        NotebookEntity notebook = domainMapper.map(notebookRequest, NotebookEntity.class);
        notebook.setUser(user);
        return domainMapper.map(notebookService.saveNotebook(notebook), NotebookResponse.class);
    }

    @PutMapping("{id}")
    public NotebookResponse updateNotebook(@RequestBody NotebookRequest notebookRequest,
                                           @PathVariable("id") long id, Authentication auth) throws ApiException {
        UserEntity user = userService.getUserAssertRole(auth, UserRole.USER);
        NotebookEntity notebookEntity = domainMapper.map(notebookRequest, notebookService.getNotebook(id, user));
        return domainMapper.map(notebookService.saveNotebook(notebookEntity), NotebookResponse.class);
    }

    @DeleteMapping(path = "{id}")
    public ApiMessage deleteNotebook(@PathVariable("id") long id, Authentication auth) throws ApiException {
        UserEntity user = userService.getUserAssertRole(auth, UserRole.USER);
        notebookService.deleteNotebook(notebookService.getNotebook(id, user));
        return new ApiMessage("Notebook deleted");
    }

}
