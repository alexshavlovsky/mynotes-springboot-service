package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NotebookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ctzn.springangularsandbox.controllers.HttpExceptions.*;

@RestController
@CrossOrigin
@RequestMapping("api/notebooks")
public class NotebookController {

    private NotebookRepository notebookRepository;

    public NotebookController(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    @GetMapping()
    public List<Notebook> getAll() {
        return notebookRepository.findAll();
    }

    @GetMapping("{id}")
    public Notebook getOne(@PathVariable("id") long id) {
        return notebookRepository.findById(id).orElseThrow(() -> ENTITY_NOT_FOUND);
    }

    @PostMapping() // create only
    public Notebook create(@RequestBody Notebook notebook) {
        if (notebook.getId() != null) throw ENTITY_ID_SHOULD_BE_NULL;
        return notebookRepository.save(notebook);
    }

    @PutMapping("{id}") // update only
    public Notebook update(@RequestBody Notebook notebook, @PathVariable long id) {
        if (!notebookRepository.existsById(id)) throw ENTITY_NOT_FOUND;
        if (notebook.getId() != null && notebook.getId() != id) throw ENTITY_ID_IS_WRONG;
        notebook.setId(id);
        notebookRepository.save(notebook);
        return notebook;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        if (!notebookRepository.existsById(id)) throw ENTITY_NOT_FOUND;
        notebookRepository.deleteById(id);
    }

}
