package com.ctzn.mynotesservice.model.note;

import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.notebook.NotebookEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "notes")
public class NoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 50)
    @NonNull
    private String title;

    @Column(nullable = false)
    @Lob
    @NonNull
    private String text;

    @Column(nullable = false)
    @NonNull
    private Date lastModifiedOn = TimeSource.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notebook_id")
    @NonNull
    private NotebookEntity notebook;

    public Long getNotebookId() {
        return notebook.getId();
    }
}
