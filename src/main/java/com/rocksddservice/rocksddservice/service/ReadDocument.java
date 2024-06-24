package com.rocksddservice.rocksddservice.service;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReadDocument {
    public String readWordDocument(String fileNameWithPath) {
        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(fileNameWithPath)))) {

            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            String docText = xwpfWordExtractor.getText();

            // find number of words in the document
            long count = Arrays.stream(docText.split("\\s+")).count();

            StringBuilder docTextCount = new StringBuilder(docText).append("Count:\t").append(count);
            return docTextCount.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> extractData(String filePath) throws IOException {
        Map<String, Object> data = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph) {
                    XWPFParagraph paragraph = (XWPFParagraph) element;
                    String text = paragraph.getText();
                    // Example logic to extract key-value pairs
                    if (text.contains(":")) {
                        String[] parts = text.split(":", 2);
                        data.put(parts[0].trim(), parts[1].trim());
                    }
                } else if (element instanceof XWPFTable) {
                    XWPFTable table = (XWPFTable) element;
                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            // Example logic to extract table data
                            data.put(cell.getText(), cell.getText());
                        }
                    }
                }
            }
        }
        return data;
    }
}
