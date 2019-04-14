package com.ctzn.springangularsandbox.repositories;

import com.ctzn.springangularsandbox.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
