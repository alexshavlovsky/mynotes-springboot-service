package com.ctzn.mynotesservice.repositories;

import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.model.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

@Component
public class DbSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DbSeeder.class);

    private NotebookRepository notebookRepository;
    private NoteRepository noteRepository;
    private UserRepository userRepository;

    public DbSeeder(NotebookRepository notebookRepository, NoteRepository noteRepository, UserRepository userRepository) {
        this.notebookRepository = notebookRepository;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    private void generateRandomDb(int nbNum, int notesPerNbMin, int notesPerNbMax) {
        notebookRepository.deleteAll();
        Random rnd = new Random();
        long now = new Date().getTime();
        for (int i = 0; i < nbNum; i++) {
            NotebookEntity nb = new NotebookEntity("Notebook " + (i + 1));
            notebookRepository.save(nb);
            int notesNum = rnd.nextInt(notesPerNbMax - notesPerNbMin + 1) + notesPerNbMin;
            for (int n = 0; n < notesNum; n++) {
                NoteEntity note = new NoteEntity(
                        "Note " + (i + 1) + "." + (n + 1),
                        NonsenseGenerator.getInstance().makeText(1),
                        nb);
                // set random creation date within last year
                int period = (int) Duration.ofDays(365L).getSeconds();
                note.setLastModifiedOn(new Date(now - 1000L * rnd.nextInt(period)));
                noteRepository.save(note);
            }
        }
        if (userRepository.count() == 0) {
            UserEntity user = new UserEntity("User", "", "user@example.com");
            user.setPassword("12345");
            user.setRolesMask(Collections.singletonList(UserRole.USER));
            // set the fixed public user id instead of randomly generated
            // for easier debugging
            user.setUserId("36c773c8-00b0-4f94-ada1-da5b74b49de4");
            userRepository.save(user);
            UserEntity admin = new UserEntity("Admin", "", "admin@example.com");
            admin.setPassword("12345");
            // set the fixed public user id instead of randomly generated
            // for easier debugging
            admin.setUserId("82e91d4a-5ba5-4854-b3d4-6977d58283b9");
            admin.setRolesMask(Collections.singletonList(UserRole.ADMIN));
            userRepository.save(admin);
            logger.debug("Default users created");
        }
    }

    @Override
    public void run(String... args) {
        // put random data to db if db is empty
        if (notebookRepository.count() == 0 || (args.length == 1 && "force".equals(args[0]))) {
            generateRandomDb(12, 3, 40);
            logger.debug("Random database generated");
        }
    }
}
