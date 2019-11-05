package com.ctzn.mynotesservice.model.note.excel;

import com.ctzn.mynotesservice.model.note.NoteEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class ExcelXlsResourceFactory implements ExcelResourceFactory {

    private final static String[] HEADERS = {"Nb Id", "Nb Name", "Id", "Title", "Text", "Last Modified"};
    private final static String SHEET_NAME = "Notes";

    private static CellStyle getHeaderCellStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLUE.getIndex());
        CellStyle cs = workbook.createCellStyle();
        cs.setFont(font);
        return cs;
    }

    private static CellStyle getDateCellStyle(Workbook workbook) {
        short dateFormat = workbook.createDataFormat().getFormat("dd/mm/yy hh:mm:ss");
        CellStyle cs = workbook.createCellStyle();
        cs.setDataFormat(dateFormat);
        return cs;
    }

    @Override
    public InputStreamResource fromNotes(List<NoteEntity> notes) throws IOException {
        try (Workbook workbook = new HSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(SHEET_NAME);
            CellStyle headerCellStyle = getHeaderCellStyle(workbook);
            CellStyle dateCallStyle = getDateCellStyle(workbook);

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (NoteEntity note : notes) {
                Row row = sheet.createRow(rowIdx++);
                int i = 0;
                row.createCell(i++).setCellValue(note.getNotebook().getId());
                row.createCell(i++).setCellValue(note.getNotebook().getName());
                row.createCell(i++).setCellValue(note.getId());
                row.createCell(i++).setCellValue(note.getTitle());
                row.createCell(i++).setCellValue(note.getText());
                Cell cell = row.createCell(i);
                cell.setCellValue(note.getLastModifiedOn());
                cell.setCellStyle(dateCallStyle);
            }

            try {
                for (int i = 0; i < 6; i++) sheet.autoSizeColumn(i);
            } catch (NullPointerException e) {
                log.warn("Column auto-size: standard fonts are not installed on the host os");
                // the autoSizeColumn method will throw a null pointer exception
                // if host os has not got standard fonts installed
                // for example, if app is running in a container
                // so we catch the exception silently
                // columns in a resulting Excel file will have a default width
            }

            workbook.write(out);

            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        }
    }

}
