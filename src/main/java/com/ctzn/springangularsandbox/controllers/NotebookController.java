package com.ctzn.springangularsandbox.controllers;

import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NotebookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NotFound extends RuntimeException {
    NotFound() {
        super("Notebook not found");
    }
}

@RestController
@CrossOrigin
@RequestMapping("api/notebooks")
public class NotebookController {

    private NotebookRepository notebookRepository;

    public NotebookController(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    @GetMapping("all")
    public List<Notebook> getAll() {
        return notebookRepository.findAll();
    }

    @GetMapping("{id}")
    public Notebook getOne(@PathVariable("id") long id) {
        return notebookRepository.findById(id).orElseThrow(NotFound::new);
    }

    @PostMapping
    public Notebook create(@RequestBody Notebook notebook) {
        return notebookRepository.save(notebook);
    }

    @PutMapping("{id}")
    public Notebook updateStudent(@RequestBody Notebook notebook, @PathVariable long id) {
        if (!notebookRepository.existsById(id)) throw new NotFound();
        notebook.setId(id);
        notebookRepository.save(notebook);
        return notebook;
    }

    @DeleteMapping("{id}")
    public void deleteStudent(@PathVariable long id) {
        if (!notebookRepository.existsById(id)) throw new NotFound();
        notebookRepository.deleteById(id);
    }

}
