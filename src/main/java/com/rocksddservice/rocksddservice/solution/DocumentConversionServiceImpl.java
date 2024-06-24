package com.rocksddservice.rocksddservice.solution;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class DocumentConversionServiceImpl implements DocumentConversionService {

    /*@Override
    public void convertToPDF1(String docPath, String pdfPath) throws Exception {
        File inputWord = new File(docPath);
        File outputFile = new File(pdfPath);
        try (InputStream docxInputStream = new FileInputStream(inputWord);
             OutputStream outputStream = new FileOutputStream(outputFile)) {
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
        } catch (Exception e) {
            throw new Exception("Error converting document to PDF: " + e.getMessage(), e);
        }
    }*/

    @Override
    public void convertToPDF(String sourcePath, String targetPath) throws Exception {
        File inputFile = new File(sourcePath);
        File outputFile = new File(targetPath);
        String fileExtension = getFileExtension(inputFile.getName());

        try (InputStream inputStream = new FileInputStream(inputFile);
             OutputStream outputStream = new FileOutputStream(outputFile)) {
            IConverter converter = LocalConverter.builder().build();

            switch (fileExtension) {
                case "doc":
                    converter.convert(inputStream).as(DocumentType.DOC).to(outputStream).as(DocumentType.PDF).execute();
                    break;
                case "docx":
                    converter.convert(inputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
                    break;
                case "xls":
                    converter.convert(inputStream).as(DocumentType.XLS).to(outputStream).as(DocumentType.PDF).execute();
                    break;
                case "xlsx":
                    converter.convert(inputStream).as(DocumentType.XLSX).to(outputStream).as(DocumentType.PDF).execute();
                    break;
                case "txt":
                    converter.convert(inputStream).as(DocumentType.TEXT).to(outputStream).as(DocumentType.PDF).execute();
                    break;
                default:
                    throw new Exception("Unsupported file type: " + fileExtension);
            }
        } catch (Exception e) {
            throw new Exception("Error converting document to PDF: " + e.getMessage(), e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}

