package com.ctzn.springangularsandbox.repositories;

import com.ctzn.springangularsandbox.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotebookRepository extends JpaRepository<Notebook, Long> {
}
