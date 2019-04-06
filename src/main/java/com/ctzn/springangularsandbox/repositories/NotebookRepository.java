package com.ctzn.springangularsandbox.repositories;

import com.ctzn.springangularsandbox.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotebookRepository extends JpaRepository<Notebook, UUID> {
}
