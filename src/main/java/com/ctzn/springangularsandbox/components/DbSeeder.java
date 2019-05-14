package com.ctzn.springangularsandbox.components;

import com.ctzn.springangularsandbox.model.Note;
import com.ctzn.springangularsandbox.model.Notebook;
import com.ctzn.springangularsandbox.repositories.NoteRepository;
import com.ctzn.springangularsandbox.repositories.NotebookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Random;

@Component
public class DbSeeder implements CommandLineRunner {

    private NotebookRepository notebookRepository;
    private NoteRepository noteRepository;

    public DbSeeder(NotebookRepository notebookRepository, NoteRepository noteRepository) {
        this.notebookRepository = notebookRepository;
        this.noteRepository = noteRepository;
    }

    private void generateRandomDb(int nbNum, int notesPerNbMin, int notesPerNbMax) {
        if (notebookRepository.count() > 0) return; // put random data to db if db is empty
        noteRepository.deleteAll();
        notebookRepository.deleteAll();
        Random rnd = new Random();
        long now = new Date().getTime();
        for (int i = 0; i < nbNum; i++) {
            Notebook nb = new Notebook("Notebook " + (i + 1));
            notebookRepository.save(nb);
            int notesNum = rnd.nextInt(notesPerNbMax - notesPerNbMin + 1) + notesPerNbMin;
            for (int n = 0; n < notesNum; n++) {
                Note note = new Note("Note " + (i + 1) + "." + (n + 1),
                        NonsenseGenerator.getInstance().makeText(1), nb);
                // set random creation date within last year
                int period = (int) Duration.ofDays(365L).getSeconds();
                note.setLastModifiedOn(new Date(now - 1000L * rnd.nextInt(period)));
                noteRepository.save(note);
            }
        }
    }

    @Override
    public void run(String... args) {
        generateRandomDb(12, 3, 40);
    }
}
