package com.ctzn.mynotesservice.repositories;

import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import com.ctzn.mynotesservice.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotebookRepository extends JpaRepository<NotebookEntity, Long> {
    List<NotebookEntity> findAllByUser(UserEntity user);

    Optional<NotebookEntity> findByIdAndUser(long id, UserEntity user);
}
