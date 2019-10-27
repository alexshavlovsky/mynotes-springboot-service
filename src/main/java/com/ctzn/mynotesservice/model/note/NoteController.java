package com.ctzn.mynotesservice.model.note;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.note.excel.ExcelResourceFactory;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.services.NotebookService;
import com.ctzn.mynotesservice.services.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(NoteController.BASE_PATH)
public class NoteController {

    public static final String BASE_PATH = "/api/notes";

    private UserService userService;
    private NotebookService notebookService;
    private DomainMapper domainMapper;
    private ExcelResourceFactory excelResourceFactory;

    public NoteController(UserService userService, NotebookService notebookService, DomainMapper domainMapper, ExcelResourceFactory excelResourceFactory) {
        this.userService = userService;
        this.notebookService = notebookService;
        this.domainMapper = domainMapper;
        this.excelResourceFactory = excelResourceFactory;
    }

    // TODO: test this method
    @GetMapping()
    public List<NoteResponse> getAllNotes(Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        return domainMapper.mapAll(notebookService.getAllNotes(user), NoteResponse.class);
    }

    // TODO: test this method
    @GetMapping(path = "export/xls", produces = {"application/vnd.ms-excel", "application/json"})
    public ResponseEntity getAllNotesExcel(Principal principal) throws ApiException, IOException {
        UserEntity user = userService.getUser(principal);
        InputStreamResource resource = excelResourceFactory.fromNotes(notebookService.getAllNotes(user));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        headers.add("Content-Disposition", String.format("attachment; filename=mynotes-%s.xls",
                new SimpleDateFormat("yyMMdd-HHmmss").format(TimeSource.now())));

        return ResponseEntity.ok().headers(headers).body(resource);
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
