package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.model.user.UserRole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class StaticTestProvider {

    private static final String fixedUserPublicId = "74751a5a-951c-11e9-bc42-526af7764f64";

    static UserEntity getFixedIdUser() {
        UserEntity user = new UserEntity("Fixed Id User", "Last Name", "fixed_id_user@example.com");
        user.setPassword("12345");
        user.setRolesMask(Collections.singletonList(UserRole.USER));
        user.setUserId(fixedUserPublicId);
        return user;
    }

    static NoteEntity getNote(long id, String title, String text, NotebookEntity notebook) {
        NoteEntity note = new NoteEntity(title, text, notebook);
        note.setId(id);
        return note;
    }

    static NotebookEntity getEmptyNotebook(long id, String name) {
        NotebookEntity notebook = new NotebookEntity(name, getFixedIdUser());
        notebook.setId(id);
        return notebook;
    }

    static List<NoteEntity> getTwoNotesList(NotebookEntity notebook) {
        NoteEntity note1 = getNote(3L, "Note 1.1", "Some text 1", notebook);
        NoteEntity note2 = getNote(4L, "Note 1.2", "Some text 2", notebook);
        return Arrays.asList(note1, note2);
    }

    static List<NotebookEntity> getTwoNotebooksList() {
        NotebookEntity notebook1 = getEmptyNotebook(1L, "Notebook 1");
        NotebookEntity notebook2 = getEmptyNotebook(2L, "Notebook 2");
        notebook1.getNotes().addAll(getTwoNotesList(notebook1));
        return Arrays.asList(notebook1, notebook2);
    }
}
