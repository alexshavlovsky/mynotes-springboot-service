package com.ctzn.mynotesservice.services;

import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.repositories.NoteRepository;
import com.ctzn.mynotesservice.repositories.NotebookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotebookService {

    private NotebookRepository notebookRepository;
    private NoteRepository noteRepository;

    public NotebookService(NotebookRepository notebookRepository, NoteRepository noteRepository) {
        this.notebookRepository = notebookRepository;
        this.noteRepository = noteRepository;
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
