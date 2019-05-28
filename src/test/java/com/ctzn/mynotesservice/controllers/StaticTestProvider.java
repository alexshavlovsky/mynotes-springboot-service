package com.ctzn.mynotesservice.controllers;

import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;

import java.util.Arrays;
import java.util.List;

class StaticTestProvider {
    static List<NoteEntity> getNotesList(NotebookEntity notebook) {
        NoteEntity note1 = new NoteEntity("Note 1.1", "Some text 1", notebook);
        note1.setId(3L);

        NoteEntity note2 = new NoteEntity("Note 1.2", "Some text 2", notebook);
        note2.setId(4L);

        return Arrays.asList(note1, note2);
    }

    static List<NotebookEntity> getNotebookList() {
        NotebookEntity notebook1 = new NotebookEntity("Notebook 1");
        notebook1.setId(1L);

        NotebookEntity notebook2 = new NotebookEntity("Notebook 2");
        notebook2.setId(2L);

        notebook1.getNotes().addAll(getNotesList(notebook1));

        return Arrays.asList(notebook1, notebook2);
    }
}
