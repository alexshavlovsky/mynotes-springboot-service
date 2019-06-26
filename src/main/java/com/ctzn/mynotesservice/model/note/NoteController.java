package com.ctzn.mynotesservice.model.note;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.repositories.PersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping(NoteController.BASE_PATH)
public class NoteController {

    public static final String BASE_PATH = "/api/notes";

    private PersistenceService persistenceService;
    private DomainMapper domainMapper;

    public NoteController(PersistenceService persistenceService, DomainMapper domainMapper) {
        this.persistenceService = persistenceService;
        this.domainMapper = domainMapper;
    }

    @PostMapping() // create only
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse saveNote(@RequestBody NoteRequest noteRequest, Principal principal) throws ApiException {
        UserEntity user = persistenceService.getUser(principal);
        NotebookEntity notebook = persistenceService.getNotebook(noteRequest.getNotebookId(), user);
        NoteEntity note = domainMapper.map(noteRequest, NoteEntity.class);
        note.setNotebook(notebook);
        return domainMapper.map(persistenceService.saveNote(note), NoteResponse.class);
    }

    @PutMapping("{id}") // update only
    public NoteResponse updateNote(@RequestBody NoteRequest noteRequest,
                                   @PathVariable("id") long id, Principal principal) throws ApiException {
        UserEntity user = persistenceService.getUser(principal);
        NoteEntity note = persistenceService.getNote(id, user);
        domainMapper.map(noteRequest, note);
        note.setLastModifiedOn(TimeSource.now());
        // if the note must be moved to another notebook
        Long destinationNotebookId = noteRequest.getNotebookId();
        if (!note.getNotebook().getId().equals(destinationNotebookId)) {
            NotebookEntity destinationNotebook = persistenceService.getNotebook(destinationNotebookId, user);
            note.setNotebook(destinationNotebook);
        }
        return domainMapper.map(persistenceService.saveNote(note), NoteResponse.class);
    }

    @DeleteMapping(path = "{id}")
    public ApiMessage deleteNote(@PathVariable("id") long id, Principal principal) throws ApiException {
        UserEntity user = persistenceService.getUser(principal);
        NoteEntity note = persistenceService.getNote(id, user);
        persistenceService.deleteNote(note);
        return new ApiMessage("Note deleted");
    }

}
