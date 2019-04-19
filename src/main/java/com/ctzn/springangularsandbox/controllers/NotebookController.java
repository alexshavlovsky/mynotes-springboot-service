package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NotebookRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.ctzn.springangularsandbox.controllers.RequestValidator.*;

@RestController
@CrossOrigin
@RequestMapping("api/notebooks")
public class NotebookController {

    private static final String NOTEBOOK_OBJECT_NAME = Notebook.getLogObjectName();

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
        return validateObjectExists(notebookRepository.findById(id), NOTEBOOK_OBJECT_NAME);
    }

    @PostMapping() // create only
    public Notebook create(@Valid @RequestBody Notebook notebook) {
        validateIdIsNull(notebook.getId(), NOTEBOOK_OBJECT_NAME);
        return notebookRepository.save(notebook);
    }

    @PutMapping("{id}") // update only
    public Notebook update(@Valid @RequestBody Notebook notebook, @PathVariable Long id) {
        validateObjectExists(notebookRepository.existsById(id), NOTEBOOK_OBJECT_NAME);
        validateIdNotNullAndEqual(notebook.getId(), id, NOTEBOOK_OBJECT_NAME);
        notebook.setId(id);
        return notebookRepository.save(notebook);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        validateObjectExists(notebookRepository.existsById(id), NOTEBOOK_OBJECT_NAME);
        notebookRepository.deleteById(id);
    }

}
