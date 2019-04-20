package com.ctzn.springangularsandbox.components;

import com.ctzn.springangularsandbox.model.Note;
import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NoteRepository;
import com.ctzn.springangularsandbox.repositories.NotebookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.jpa.hibernate.ddl-auto", havingValue = "create-drop")
public class Bootstrap implements CommandLineRunner {

    private NotebookRepository notebookRepository;
    private NoteRepository noteRepository;

    public Bootstrap(NotebookRepository notebookRepository, NoteRepository noteRepository) {
        this.notebookRepository = notebookRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(String... args) {
        Notebook nb1 = new Notebook("Default");
        Notebook nb2 = new Notebook("Quotes");
        notebookRepository.save(nb1);
        notebookRepository.save(nb2);
        noteRepository.save(new Note("Note 1", "Hello World", nb1));
        noteRepository.save(new Note("Hamlet", "To be or not to be", nb2));
        noteRepository.save(new Note("Terminator", "I'll be back", nb2));
    }
}
