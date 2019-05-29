package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;

import java.util.Arrays;
import java.util.List;

class StaticTestProvider {
    static NoteEntity getNote(long id, String title, String text, NotebookEntity notebook) {
        NoteEntity note = new NoteEntity(title, text, notebook);
        note.setId(id);
        return note;
    }

    static NotebookEntity getEmptyNotebook(long id, String name) {
        NotebookEntity notebook = new NotebookEntity(name);
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
