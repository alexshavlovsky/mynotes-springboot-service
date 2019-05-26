package com.ctzn.mynotesservice.model.notebook;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotebookResponse {
    private Long id;
    private String name;
    private Integer size;
}
