package com.ctzn.mynotesservice.repositories;

import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class NotebookService {

    private UserRepository userRepository;
    private NotebookRepository notebookRepository;

    public NotebookService(UserRepository userRepository, NotebookRepository notebookRepository) {
        this.userRepository = userRepository;
        this.notebookRepository = notebookRepository;
    }

    public UserEntity authorize(Principal principal) throws ApiException {
        return userRepository.findByUserId(principal.getName()).orElseThrow(ApiException::getCredentialsNotExist);
    }

    public List<NotebookEntity> authorizeAndGetAll(Principal principal) throws ApiException {
        return notebookRepository.findAllByUser(authorize(principal));
    }

    public NotebookEntity authorizeAndGet(long nbId, Principal principal) throws ApiException {
        return notebookRepository.findByIdAndUser(nbId, authorize(principal))
                .orElseThrow(() -> ApiException.getNotFoundById("Notebook", nbId));
    }

}
