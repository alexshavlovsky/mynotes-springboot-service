package com.ctzn.mynotesservice.model.note;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.repositories.NoteRepository;
import com.ctzn.mynotesservice.repositories.NotebookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(NoteController.BASE_PATH)
public class NoteController {

    public static final String BASE_PATH = "/api/notes";

    private NoteRepository noteRepository;
    private NotebookRepository notebookRepository;

    private DomainMapper domainMapper;

    public NoteController(NoteRepository noteRepository, NotebookRepository notebookRepository, DomainMapper domainMapper) {
        this.noteRepository = noteRepository;
        this.notebookRepository = notebookRepository;
        this.domainMapper = domainMapper;
    }

    @GetMapping()
    public List<NoteResponse> getAllNotesByNotebook(@RequestParam(required = false) Long nbId) throws ApiException {
        if (nbId == null) {
            return domainMapper.mapAll(noteRepository.findAll(), NoteResponse.class);
        } else {
            // TODO: create tests for this case
            NotebookEntity notebookEntity = notebookRepository.findById(nbId).orElseThrow(() -> ApiException.getNotFoundById("Notebook", nbId));
            return domainMapper.mapAll(noteRepository.findAllByNotebook(notebookEntity), NoteResponse.class);
        }
    }

    @GetMapping("{id}")
    public NoteResponse getNote(
            @PathVariable("id") long id) throws ApiException {
        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> ApiException.getNotFoundById("Note", id));
        return domainMapper.map(noteEntity, NoteResponse.class);
    }

    @PostMapping() // create only
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse saveNote(@RequestBody NoteRequest noteRequest) throws ApiException {
        long nbId = noteRequest.getNotebookId();
        NotebookEntity notebookEntity = notebookRepository.findById(nbId).orElseThrow(() -> ApiException.getNotFoundById("Notebook", nbId));
        NoteEntity noteEntity = domainMapper.map(noteRequest, NoteEntity.class);
        noteEntity.setNotebook(notebookEntity);
        return domainMapper.map(noteRepository.save(noteEntity), NoteResponse.class);
    }

    @PutMapping("{id}") // update only
    public NoteResponse updateNote(
            @RequestBody NoteRequest noteRequest,
            @PathVariable("id") long id,
            @PathVariable("id") NoteEntity noteEntity) throws ApiException {
        if (!noteRepository.existsById(id)) throw ApiException.getNotFoundById("Note", id);
        long nbId = noteRequest.getNotebookId();
        NotebookEntity requestNotebookEntity = notebookRepository.findById(nbId).orElseThrow(() -> ApiException.getNotFoundById("Notebook", nbId));
        domainMapper.map(noteRequest, noteEntity);
        noteEntity.setNotebook(requestNotebookEntity);
        return domainMapper.map(noteRepository.save(noteEntity), NoteResponse.class);
    }

    @DeleteMapping(path = "{id}")
    public ApiMessage deleteNote(
            @PathVariable("id") long id) throws ApiException {
        if (!noteRepository.existsById(id)) throw ApiException.getNotFoundById("Note", id);
        noteRepository.deleteById(id);
        return new ApiMessage("Note deleted");
    }

}
