package com.ctzn.springangularsandbox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity

public class Notebook {
    @Id
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "notebook", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Note> notes;

    public Notebook() {
        id = UUID.randomUUID();
        notes = new ArrayList<>();
    }

    public Notebook(String name) {
        this();
        this.name = name;
    }

    public Notebook(String id, String name) {
        this(name);
        if (id != null) this.id = UUID.fromString(id);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Note> getNotes() {
        return notes;
    }
}
