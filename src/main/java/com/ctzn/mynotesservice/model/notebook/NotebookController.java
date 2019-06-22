package com.ctzn.mynotesservice.model.notebook;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.note.NoteResponse;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.repositories.NoteRepository;
import com.ctzn.mynotesservice.repositories.NotebookRepository;
import com.ctzn.mynotesservice.repositories.NotebookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(NotebookController.BASE_PATH)
public class NotebookController {

    public static final String BASE_PATH = "/api/notebooks/";

    private NotebookRepository notebookRepository;
    private NotebookService notebookService;
    private NoteRepository noteRepository;

    private DomainMapper domainMapper;

    public NotebookController(NotebookRepository notebookRepository, NotebookService notebookService, NoteRepository noteRepository, DomainMapper domainMapper) {
        this.notebookRepository = notebookRepository;
        this.notebookService = notebookService;
        this.noteRepository = noteRepository;
        this.domainMapper = domainMapper;
    }

    @GetMapping()
    public List<NotebookResponse> getAllNotebooks(Principal principal) throws ApiException {
        return domainMapper.mapAll(notebookService.authorizeAndGetAll(principal), NotebookResponse.class);
    }

    @GetMapping("{id}/notes")
    public List<NoteResponse> getAllNotesByNotebook(@PathVariable long id, Principal principal) throws ApiException {
        NotebookEntity notebookEntity = notebookService.authorizeAndGet(id, principal);
        return domainMapper.mapAll(noteRepository.findAllByNotebook(notebookEntity), NoteResponse.class);
    }

    @GetMapping("{id}")
    public NotebookResponse getNotebook(@PathVariable("id") long id, Principal principal) throws ApiException {
        return domainMapper.map(notebookService.authorizeAndGet(id, principal), NotebookResponse.class);
    }

    //authorize
    @PostMapping() // create only
    @ResponseStatus(HttpStatus.CREATED)
    public NotebookResponse saveNotebook(@RequestBody NotebookRequest notebookRequest, Principal principal) throws ApiException {
        UserEntity user = notebookService.authorize(principal);
        NotebookEntity notebook = domainMapper.map(notebookRequest, NotebookEntity.class);
        notebook.setUser(user);
        return domainMapper.map(notebookRepository.save(notebook), NotebookResponse.class);
    }

    @PutMapping("{id}") // update only
    public NotebookResponse updateNotebook(@RequestBody NotebookRequest notebookRequest,
                                           @PathVariable("id") long id, Principal principal) throws ApiException {
        NotebookEntity notebookEntity = domainMapper.map(notebookRequest, notebookService.authorizeAndGet(id, principal));
        return domainMapper.map(notebookRepository.save(notebookEntity), NotebookResponse.class);
    }

    @DeleteMapping(path = "{id}")
    public ApiMessage deleteNotebook(@PathVariable("id") long id, Principal principal) throws ApiException {
        notebookRepository.delete(notebookService.authorizeAndGet(id, principal));
        return new ApiMessage("Notebook deleted");
    }

}
