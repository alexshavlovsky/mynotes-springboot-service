package com.ctzn.springangularsandbox.repositories;

import com.ctzn.springangularsandbox.model.Note;
import com.ctzn.springangularsandbox.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByNotebook(Notebook notebook);
}
