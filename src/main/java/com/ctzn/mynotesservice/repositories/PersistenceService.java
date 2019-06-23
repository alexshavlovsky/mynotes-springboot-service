package com.ctzn.mynotesservice.repositories;

import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class PersistenceService {

    private UserRepository userRepository;
    private NotebookRepository notebookRepository;
    private NoteRepository noteRepository;

    public PersistenceService(UserRepository userRepository, NotebookRepository notebookRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.notebookRepository = notebookRepository;
        this.noteRepository = noteRepository;
    }

    public UserEntity getUser(Principal principal) throws ApiException {
        return userRepository.findByUserId(principal.getName())
                .orElseThrow(ApiException::getCredentialsNotExist);
    }

    public List<NotebookEntity> getAllNotebooks(UserEntity user) {
        return notebookRepository.findAllByUser(user);
    }

    public NotebookEntity getNotebook(long id, UserEntity user) throws ApiException {
        return notebookRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> ApiException.getNotFoundById("Notebook", id));
    }

    public NoteEntity getNote(long id, UserEntity user) throws ApiException {
        NoteEntity note = noteRepository.findById(id)
                .orElseThrow(() -> ApiException.getNotFoundById("Note", id));
        getNotebook(note.getNotebookId(), user); // check if user owns the notebook
        return note;
    }

    public List<NoteEntity> getNotesFromNotebook(NotebookEntity notebook) {
        return noteRepository.findAllByNotebook(notebook);
    }

    public NotebookEntity saveNotebook(NotebookEntity notebook) {
        return notebookRepository.save(notebook);
    }

    public void deleteNotebook(NotebookEntity notebook) {
        notebookRepository.delete(notebook);
    }

    public NoteEntity saveNote(NoteEntity note) {
        return noteRepository.save(note);
    }

    public void deleteNote(NoteEntity note) {
        noteRepository.delete(note);
    }

}
