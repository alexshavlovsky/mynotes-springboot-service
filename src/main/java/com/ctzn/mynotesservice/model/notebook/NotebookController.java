package com.ctzn.mynotesservice.model.notebook;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.ApiMessage;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.repositories.NotebookRepository;
import com.ctzn.mynotesservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(NotebookController.BASE_PATH)
public class NotebookController {

    public static final String BASE_PATH = "/api/notebooks/";

    private UserRepository userRepository;
    private NotebookRepository notebookRepository;

    private DomainMapper domainMapper;

    public NotebookController(UserRepository userRepository, NotebookRepository notebookRepository, DomainMapper domainMapper) {
        this.userRepository = userRepository;
        this.notebookRepository = notebookRepository;
        this.domainMapper = domainMapper;
    }

    @GetMapping()
    public List<NotebookResponse> getAllNotebooks(Principal principal) throws ApiException {
        UserEntity user = userRepository.findByUserId(principal.getName())
                .orElseThrow(ApiException::getCredentialsDeleted);
        return domainMapper.mapAll(notebookRepository.findAllByUser(user), NotebookResponse.class);
    }

    private NotebookEntity getNotebookByIdAndUserId(long id, String userId) throws ApiException {
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(ApiException::getCredentialsDeleted);
        NotebookEntity notebookEntity = notebookRepository.findById(id)
                .orElseThrow(() -> ApiException.getNotFoundById("Notebook", id));
        if (!notebookEntity.getUser().getId().equals(user.getId()))
            throw ApiException.getNotFoundById("Notebook", id);
        return notebookEntity;
    }

    @GetMapping("{id}")
    public NotebookResponse getNotebook(@PathVariable("id") long id, Principal principal) throws ApiException {
        NotebookEntity notebookEntity = getNotebookByIdAndUserId(id, principal.getName());
        return domainMapper.map(notebookEntity, NotebookResponse.class);
    }

    @PostMapping() // create only
    @ResponseStatus(HttpStatus.CREATED)
    public NotebookResponse saveNotebook(@RequestBody NotebookRequest notebookRequest, Principal principal) throws ApiException {
        UserEntity user = userRepository.findByUserId(principal.getName()).orElseThrow(ApiException::getCredentialsDeleted);
        NotebookEntity notebook = domainMapper.map(notebookRequest, NotebookEntity.class);
        notebook.setUser(user);
        return domainMapper.map(notebookRepository.save(notebook), NotebookResponse.class);
    }

    @PutMapping("{id}") // update only
    public NotebookResponse updateNotebook(@RequestBody NotebookRequest notebookRequest,
                                           @PathVariable("id") long id, Principal principal) throws ApiException {
        NotebookEntity notebookEntity = getNotebookByIdAndUserId(id, principal.getName());
        domainMapper.map(notebookRequest, notebookEntity);
        return domainMapper.map(notebookRepository.save(notebookEntity), NotebookResponse.class);
    }

    @DeleteMapping(path = "{id}")
    public ApiMessage deleteNotebook(@PathVariable("id") long id, Principal principal) throws ApiException {
        NotebookEntity notebookEntity = getNotebookByIdAndUserId(id, principal.getName());
        notebookRepository.delete(notebookEntity);
        return new ApiMessage("Notebook deleted");
    }

}
