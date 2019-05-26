package com.ctzn.mynotesservice.model.note;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class NoteResponse {
    private Long id;
    private String title;
    private String text;
    private Long notebookId;
    private Date lastModifiedOn;
}
