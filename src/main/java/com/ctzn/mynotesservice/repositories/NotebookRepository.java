package com.ctzn.mynotesservice.repositories;

import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotebookRepository extends JpaRepository<NotebookEntity, Long> {
    List<NotebookEntity> findAllByUser(UserEntity user);
}
