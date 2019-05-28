package com.ctzn.mynotesservice.model.note;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {
    private String title;
    private String text;
    private Long notebookId;
}
