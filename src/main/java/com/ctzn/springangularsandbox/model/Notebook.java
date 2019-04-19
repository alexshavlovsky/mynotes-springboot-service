package com.ctzn.springangularsandbox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Notebook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "notebook", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Note> notes = new ArrayList<>();

    public Notebook() {
    }

    public Notebook(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notebook notebook = (Notebook) o;
        return Objects.equals(id, notebook.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    static public String getLogObjectName() {
        return "Notebook";
    }
}
