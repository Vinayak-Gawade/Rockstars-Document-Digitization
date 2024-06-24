package com.rocksddservice.rocksddservice.controller;

import com.lowagie.text.DocumentException;
import com.rocksddservice.rocksddservice.service.*;
import com.rocksddservice.rocksddservice.solution.DocumentConversionService;
import com.rocksddservice.rocksddservice.solution.DocumentConversionServiceFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@RestController
//@RequestMapping("")
public class DigitizationController {

    @Autowired
    private CreateDocument createDocument;

    @Autowired
    private ReadDocument readDocument;

    @Autowired
    private ConvertDocument convertDocument;

    @Autowired
    private DocumentConversionService documentConversionService;

    @Autowired
    private DocumentConversionServiceFolder documentConversionServiceFolder;

    @GetMapping("/create-word-doc")
    public String createWordDocument(@RequestParam String fileNameWithPath) {
        createDocument.createWordDocument(fileNameWithPath);
        return fileNameWithPath;
    }

    @GetMapping("/read-word-doc")
    public String readWordDocument(@RequestParam String fileNameWithPath) {
        return readDocument.readWordDocument(fileNameWithPath);
    }

    @GetMapping("/extract-data")
    public Map<String, Object> extractData(@RequestParam String filePath) throws IOException {
        return readDocument.extractData(filePath);
    }

    @PostMapping("/WordToPdf")
    public String convertWordToPdf(@RequestParam("filePath") String filePath, @RequestParam("pdfPath") String pdfPath) {
        try {
            convertDocument.convertWordToPdf1(filePath, pdfPath);
            return "PDF generated successfully: " + pdfPath;
        } catch (FileNotFoundException | DocumentException e) {
            return "Error converting file: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/WordToPDFService")
    public String convertWordToPdf1(@RequestParam("filePath") String filePath, @RequestParam("pdfPath") String pdfPath) {
        try {
            convertDocument.convertWordToPdf2(filePath, pdfPath);
            return "PDF generated successfully: " + pdfPath;
        } catch (FileNotFoundException | DocumentException e) {
            return "Error converting file: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/DocToPdfConverter")
    public String convertDocToPDF(@RequestParam("filePath") String filePath, @RequestParam("pdfPath") String pdfPath) {
        try {
            convertDocument.convertDocToPDF(filePath, pdfPath);
            return "PDF generated successfully: " + pdfPath;
        } catch (FileNotFoundException | DocumentException e) {
            return "Error converting file: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/wordConverterService")
    public ResponseEntity<String> convertWordToPDF(@RequestParam("docPath") String docPath,
                                                   @RequestParam("pdfPath") String pdfPath) {
        try {
            convertDocument.convertToPDF2(docPath, pdfPath);
            return ResponseEntity.ok("Conversion successful");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Conversion failed: " + e.getMessage());
        }
    }

    @PostMapping("/convertDocToPdf")
    public String convertDocToPdf(@RequestParam("docPath") String docPath,
                                  @RequestParam("pdfPath") String pdfPath) {
        convertDocument.docToPdf(docPath, pdfPath);
        return "Conversion request submitted";
    }

    @GetMapping("/convertDocToPdfiText")
    public String convertDocumentT(@RequestParam String docPath, @RequestParam String pdfPath) {
        convertDocument.convertToPDF1(docPath, pdfPath);
        return "Conversion completed!";
    }

    @PostMapping("/convertToPDF1")
    public ResponseEntity<String> convertToPDF(@RequestParam String docPath, @RequestParam String pdfPath) {
        try {
            documentConversionService.convertToPDF(docPath, pdfPath);
            return ResponseEntity.ok("Conversion completed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Conversion failed: " + e.getMessage());
        }
    }

    @PostMapping("/convertFolder")
    public ResponseEntity<String> convertFolder(@RequestParam String inputFolderPath, @RequestParam String outputFolderPath) {
        try {
            documentConversionServiceFolder.convertFolderToPDF(inputFolderPath, outputFolderPath);
            return ResponseEntity.ok("Batch conversion completed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Batch conversion failed: " + e.getMessage());
        }
    }

    @PostMapping("/convertDocument")
    public ResponseEntity<String> convertDocument(@RequestParam String inputFolderPath, @RequestParam String outputFolderPath) {
        try {
            documentConversionServiceFolder.convertDocument(inputFolderPath, outputFolderPath);
            return ResponseEntity.ok("Batch conversion completed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Batch conversion failed: " + e.getMessage());
        }
    }
}
