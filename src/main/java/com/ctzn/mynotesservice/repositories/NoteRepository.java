package com.ctzn.mynotesservice.repositories;

import com.ctzn.mynotesservice.model.note.NoteEntity;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    List<NoteEntity> findAllByNotebook(NotebookEntity notebook);

    // TODO: test this method
    @Query(value = "SELECT * FROM NOTES AS N WHERE N.NOTEBOOK_ID IN " +
            "(SELECT ID FROM NOTEBOOKS AS NB WHERE NB.USER_ID=?1)",
            nativeQuery = true)
    List<NoteEntity> findAllByUserId(Long userId);

}
