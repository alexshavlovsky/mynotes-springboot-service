package com.ctzn.mynotesservice.model.note.excel;

import com.ctzn.mynotesservice.model.note.NoteEntity;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.util.List;

public interface ExcelResourceFactory {
    InputStreamResource fromNotes(List<NoteEntity> notes) throws IOException;
}
