package com.ctzn.mynotesservice.model.notebook;

import com.ctzn.mynotesservice.model.note.NoteEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "notebooks")
public class NotebookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 50)
    @NonNull
    private String name;

    @OneToMany(mappedBy = "notebook", cascade = CascadeType.ALL)
    private List<NoteEntity> notes = new ArrayList<>();

    public Integer getSize() {
        return notes.size();
    }
}
