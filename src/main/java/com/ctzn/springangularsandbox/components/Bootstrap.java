package com.ctzn.springangularsandbox.components;

import com.ctzn.springangularsandbox.model.Note;
import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NoteRepository;
import com.ctzn.springangularsandbox.repositories.NotebookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@ConditionalOnProperty(name = "spring.jpa.hibernate.ddl-auto", havingValue = "create-drop")
public class Bootstrap implements CommandLineRunner {

    private NotebookRepository notebookRepository;
    private NoteRepository noteRepository;

    public Bootstrap(NotebookRepository notebookRepository, NoteRepository noteRepository) {
        this.notebookRepository = notebookRepository;
        this.noteRepository = noteRepository;
    }

    private void generateRandomDb(int nbNum, int notesPerNbMin, int notesPerNbMax) {
        Random rnd = new Random();
        for (int i = 0; i < nbNum; i++) {
            Notebook nb = new Notebook("Notebook " + (i + 1));
            notebookRepository.save(nb);
            int notesNum = rnd.nextInt(notesPerNbMax - notesPerNbMin + 1) + notesPerNbMin;
            for (int n = 0; n < notesNum; n++) {
                noteRepository.save(new Note("Note " + (i + 1) + "." + (n + 1),
                        NonsenseGenerator.getInstance().makeText(1), nb));
            }
        }
    }

    private void generateStaticDb() {
        Notebook nb1 = new Notebook("Default");
        Notebook nb2 = new Notebook("Quotes");
        notebookRepository.save(nb1);
        notebookRepository.save(nb2);
        noteRepository.save(new Note("Note 1", "Hello World", nb1));
        noteRepository.save(new Note("Hamlet", "To be or not to be", nb2));
        noteRepository.save(new Note("Terminator", "I'll be back", nb2));
    }

    @Override
    public void run(String... args) {
        generateRandomDb(12, 3, 40);
        //generateStaticDb();
    }
}
