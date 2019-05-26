package com.ctzn.mynotesservice.model.note;

import lombok.Data;

import java.util.Date;

@Data
public class Note {
    private Long id;
    private String title;
    private String text;
    private Long notebookId;
    private Date lastModifiedOn;
}
