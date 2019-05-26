package com.ctzn.mynotesservice.model.note;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoteRequest {
    private String title;
    private String text;
    private Long notebookId;
}
