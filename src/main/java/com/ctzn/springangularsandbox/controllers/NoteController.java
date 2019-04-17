package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.model.Note;
import com.ctzn.springangularsandbox.repositories.NoteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ctzn.springangularsandbox.controllers.HttpExceptions.*;

@RestController
@CrossOrigin
@RequestMapping("api/notes")
public class NoteController {

    private NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping()
    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    @GetMapping("{id}")
    public Note getOne(@PathVariable("id") long id) {
        return noteRepository.findById(id).orElseThrow(() -> ENTITY_NOT_FOUND);
    }

    @PostMapping() // create only
    public Note create(@RequestBody Note note) {
        if (note.getId() != null) throw ENTITY_ID_SHOULD_BE_NULL;
        return noteRepository.save(note);
    }

    @PutMapping("{id}") // update only
    public Note update(@RequestBody Note note, @PathVariable long id) {
        if (!noteRepository.existsById(id)) throw ENTITY_NOT_FOUND;
        if (note.getId() != null && note.getId() != id) throw ENTITY_ID_IS_WRONG;
        note.setId(id);
        return noteRepository.save(note);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        if (!noteRepository.existsById(id)) throw ENTITY_NOT_FOUND;
        noteRepository.deleteById(id);
    }

}
