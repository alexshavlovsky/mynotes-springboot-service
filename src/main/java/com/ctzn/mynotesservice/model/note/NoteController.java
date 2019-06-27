package com.ctzn.mynotesservice.model.note;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.services.NotebookService;
import com.ctzn.mynotesservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping(NoteController.BASE_PATH)
public class NoteController {

    public static final String BASE_PATH = "/api/notes";

    private UserService userService;
    private NotebookService notebookService;
    private DomainMapper domainMapper;

    public NoteController(UserService userService, NotebookService notebookService, DomainMapper domainMapper) {
        this.userService = userService;
        this.notebookService = notebookService;
        this.domainMapper = domainMapper;
    }

    @PostMapping() // create only
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse saveNote(@RequestBody NoteRequest noteRequest, Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        NotebookEntity notebook = notebookService.getNotebook(noteRequest.getNotebookId(), user);
        NoteEntity note = domainMapper.map(noteRequest, NoteEntity.class);
        note.setNotebook(notebook);
        return domainMapper.map(notebookService.saveNote(note), NoteResponse.class);
    }

    @PutMapping("{id}") // update only
    public NoteResponse updateNote(@RequestBody NoteRequest noteRequest,
                                   @PathVariable("id") long id, Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        NoteEntity note = notebookService.getNote(id, user);
        domainMapper.map(noteRequest, note);
        note.setLastModifiedOn(TimeSource.now());
        // if the note must be moved to another notebook
        Long destinationNotebookId = noteRequest.getNotebookId();
        if (!note.getNotebook().getId().equals(destinationNotebookId)) {
            NotebookEntity destinationNotebook = notebookService.getNotebook(destinationNotebookId, user);
            note.setNotebook(destinationNotebook);
        }
        return domainMapper.map(notebookService.saveNote(note), NoteResponse.class);
    }

    @DeleteMapping(path = "{id}")
    public ApiMessage deleteNote(@PathVariable("id") long id, Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        NoteEntity note = notebookService.getNote(id, user);
        notebookService.deleteNote(note);
        return new ApiMessage("Note deleted");
    }

}
