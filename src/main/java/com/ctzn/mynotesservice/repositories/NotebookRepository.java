package com.ctzn.mynotesservice.repositories;

import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotebookRepository extends JpaRepository<NotebookEntity, Long> {
}
