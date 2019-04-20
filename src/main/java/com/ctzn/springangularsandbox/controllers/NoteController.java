package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.model.Note;
import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NoteRepository;
import com.ctzn.springangularsandbox.repositories.NotebookRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.ctzn.springangularsandbox.controllers.RequestValidator.*;

@RestController
@CrossOrigin
@RequestMapping(NoteController.BASE_PATH)
public class NoteController {

    static final String BASE_PATH = "/api/notes/";

    private static final String NOTEBOOK_OBJECT_NAME = Notebook.getLogObjectName();
    private static final String NOTE_OBJECT_NAME = Note.getLogObjectName();

    private NoteRepository noteRepository;
    private NotebookRepository notebookRepository;

    public NoteController(NoteRepository noteRepository, NotebookRepository notebookRepository) {
        this.noteRepository = noteRepository;
        this.notebookRepository = notebookRepository;
    }

    @GetMapping()
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @GetMapping("{id}")
    public Note getNote(@PathVariable("id") long id) {
        return validateObjectExists(noteRepository.findById(id), NOTE_OBJECT_NAME);
    }

    @PostMapping() // create only
    public Note saveNote(@Valid @RequestBody Note note) {
        validateIdIsNull(note.getId(), NOTE_OBJECT_NAME);
        validateIdNotNull(note.getNotebook().getId(), NOTEBOOK_OBJECT_NAME);
        validateObjectExists(notebookRepository.existsById(note.getNotebook().getId()), NOTEBOOK_OBJECT_NAME);
        return noteRepository.save(note);
    }

    @PutMapping("{id}") // update only
    public Note updateNote(@Valid @RequestBody Note note, @PathVariable Long id) {
        validateObjectExists(noteRepository.existsById(id), NOTE_OBJECT_NAME);
        validateIdNotNullAndEqual(note.getId(), id, NOTE_OBJECT_NAME);
        validateIdNotNull(note.getNotebook().getId(), NOTEBOOK_OBJECT_NAME);
        validateObjectExists(notebookRepository.existsById(note.getNotebook().getId()), NOTEBOOK_OBJECT_NAME);
        return noteRepository.save(note);
    }

    @DeleteMapping("{id}")
    public void deleteNote(@PathVariable long id) {
        validateObjectExists(noteRepository.existsById(id), NOTE_OBJECT_NAME);
        noteRepository.deleteById(id);
    }

}
