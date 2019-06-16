package com.ctzn.mynotesservice.model.notebook;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.repositories.NotebookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(NotebookController.BASE_PATH)
public class NotebookController {

    public static final String BASE_PATH = "/api/notebooks/";

    private NotebookRepository notebookRepository;

    private DomainMapper domainMapper;

    public NotebookController(NotebookRepository notebookRepository, DomainMapper domainMapper) {
        this.notebookRepository = notebookRepository;
        this.domainMapper = domainMapper;
    }

    @GetMapping()
    public List<NotebookResponse> getAllNotebooks() {
        return domainMapper.mapAll(notebookRepository.findAll(), NotebookResponse.class);
    }

    @GetMapping("{id}")
    public NotebookResponse getNotebook(@PathVariable("id") long id) throws ApiException {
        NotebookEntity notebookEntity = notebookRepository.findById(id)
                .orElseThrow(() -> ApiException.getNotFoundById("Notebook", id));
        return domainMapper.map(notebookEntity, NotebookResponse.class);
    }

    @PostMapping() // create only
    @ResponseStatus(HttpStatus.CREATED)
    public NotebookResponse saveNotebook(@RequestBody NotebookRequest notebookRequest) {
        return domainMapper.map(
                notebookRepository.save(domainMapper.map(notebookRequest, NotebookEntity.class)),
                NotebookResponse.class);
    }

    @PutMapping("{id}") // update only
    public NotebookResponse updateNotebook(
            @RequestBody NotebookRequest notebookRequest,
            @PathVariable("id") long id) throws ApiException {
        NotebookEntity notebookEntity = notebookRepository.findById(id)
                .orElseThrow(() -> ApiException.getNotFoundById("Notebook", id));
        domainMapper.map(notebookRequest, notebookEntity);
        return domainMapper.map(notebookRepository.save(notebookEntity), NotebookResponse.class);
    }

    @DeleteMapping(path = "{id}")
    public ApiMessage deleteNotebook(
            @PathVariable("id") long id) throws ApiException {
        if (!notebookRepository.existsById(id)) throw ApiException.getNotFoundById("Notebook", id);
        notebookRepository.deleteById(id);
        return new ApiMessage("Notebook deleted");
    }

}
