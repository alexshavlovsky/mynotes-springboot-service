package com.ctzn.mynotesservice.model.note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    private Long id;
    private String title;
    private String text;
    private Long notebookId;
    private Date lastModifiedOn;
}
