package com.rocksddservice.rocksddservice.service;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
public class ReadParseDocument {
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
}
