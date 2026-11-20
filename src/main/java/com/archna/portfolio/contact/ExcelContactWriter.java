package com.archna.portfolio.contact;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

@Component
public class ExcelContactWriter {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path excelFile;

    public ExcelContactWriter(
            @Value("${app.contacts.excel-file:./data/contacts.xlsx}") String filePath) {
        this.excelFile = Path.of(filePath).toAbsolutePath().normalize();
    }

    public synchronized void append(ContactMessage contactMessage) {
        Path parentDirectory = excelFile.getParent();
        Path temporaryFile = excelFile.resolveSibling(excelFile.getFileName() + ".tmp");

        try {
            Files.createDirectories(parentDirectory);

            try (Workbook workbook = openWorkbook()) {
                Sheet sheet = workbook.getNumberOfSheets() == 0
                        ? workbook.createSheet("Contacts")
                        : workbook.getSheetAt(0);

                if (sheet.getPhysicalNumberOfRows() == 0) {
                    createHeaderRow(sheet);
                }

                int nextRowNumber = Math.max(sheet.getLastRowNum() + 1, 1);
                Row row = sheet.createRow(nextRowNumber);
                row.createCell(0).setCellValue(contactMessage.getId());
                row.createCell(1).setCellValue(contactMessage.getName());
                row.createCell(2).setCellValue(contactMessage.getEmail());
                row.createCell(3).setCellValue(contactMessage.getMessage());
                row.createCell(4).setCellValue(contactMessage.getCreatedAt().format(DATE_FORMAT));

                try (OutputStream outputStream = Files.newOutputStream(
                        temporaryFile,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)) {
                    workbook.write(outputStream);
                }
            }

            try {
                Files.move(temporaryFile, excelFile,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, excelFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception exception) {
            try {
                Files.deleteIfExists(temporaryFile);
            } catch (Exception ignored) {
                // Preserve the original export exception.
            }
            throw new IllegalStateException(
                    "Contact was saved to SQLite, but the Excel export failed.", exception);
        }
    }

    private Workbook openWorkbook() throws Exception {
        if (Files.exists(excelFile)) {
            try (InputStream inputStream = Files.newInputStream(excelFile)) {
                return WorkbookFactory.create(inputStream);
            }
        }
        return new XSSFWorkbook();
    }

    private void createHeaderRow(Sheet sheet) {
        Row header = sheet.createRow(0);
        String[] headings = {"ID", "Name", "Email", "Message", "Received At"};
        for (int index = 0; index < headings.length; index++) {
            header.createCell(index).setCellValue(headings[index]);
        }
    }
}
