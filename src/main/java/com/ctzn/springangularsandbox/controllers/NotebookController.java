package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NotebookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.ctzn.springangularsandbox.controllers.RequestValidator.*;

@RestController
@CrossOrigin
@RequestMapping(NotebookController.BASE_PATH)
public class NotebookController {

    static final String BASE_PATH = "/api/notebooks/";

    private static final String NOTEBOOK_OBJECT_NAME = Notebook.getLogObjectName();

    private NotebookRepository notebookRepository;

    public NotebookController(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    @GetMapping()
    public List<Notebook> getAllNotebooks() {
        return notebookRepository.findAll();
    }

    @GetMapping("{id}")
    public Notebook getNotebook(@PathVariable("id") long id) {
        return validateObjectExists(notebookRepository.findById(id), NOTEBOOK_OBJECT_NAME);
    }

    @PostMapping() // create only
    @ResponseStatus(HttpStatus.CREATED)
    public Notebook saveNotebook(@Valid @RequestBody Notebook notebook) {
        validateIdIsNull(notebook.getId(), NOTEBOOK_OBJECT_NAME);
        return notebookRepository.save(notebook);
    }

    @PutMapping("{id}") // update only
    public Notebook updateNotebook(@Valid @RequestBody Notebook notebook, @PathVariable Long id) {
        validateObjectExists(notebookRepository.existsById(id), NOTEBOOK_OBJECT_NAME);
        validateIdNotNullAndEqual(notebook.getId(), id, NOTEBOOK_OBJECT_NAME);
        notebook.setId(id);
        return notebookRepository.save(notebook);
    }

    @DeleteMapping(path = "{id}")
    public Object deleteNotebook(@PathVariable long id) {
        validateObjectExists(notebookRepository.existsById(id), NOTEBOOK_OBJECT_NAME);
        notebookRepository.deleteById(id);
        return customJsonMessage(NOTEBOOK_OBJECT_NAME + " deleted");
    }

}
