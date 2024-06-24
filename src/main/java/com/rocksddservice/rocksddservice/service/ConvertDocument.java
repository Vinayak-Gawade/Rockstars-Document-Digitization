package com.rocksddservice.rocksddservice.service;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ConvertDocument {
    public void convertToPDF1(String docPath, String pdfPath) {
        try {
            InputStream doc = new FileInputStream(new File(docPath));
            XWPFDocument document = new XWPFDocument(doc);
            PdfOptions options = PdfOptions.create();
            OutputStream out = new FileOutputStream(new File(pdfPath));
            PdfConverter.getInstance().convert(document, out, options);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void convertToPDF2(String docPath, String pdfPath) throws IOException {
        InputStream doc = new FileInputStream(docPath);
        XWPFDocument document = new XWPFDocument(doc);
        PdfOptions options = PdfOptions.create();
        OutputStream out = new FileOutputStream(pdfPath);
        PdfConverter.getInstance().convert(document, out, options);
        out.close();
        doc.close();
    }

    public void convertDocToPDF(String inputFilePath, String outputFilePath) throws Exception {
        String extractedText = null;
        OutputStream fileForPdf = null;

        try {
            if (inputFilePath.endsWith(".doc")) {
                HWPFDocument doc = new HWPFDocument(new FileInputStream(inputFilePath));
                WordExtractor we = new WordExtractor(doc);
                extractedText = we.getText();
                fileForPdf = new FileOutputStream(new File(outputFilePath));
                we.close();
            } else if (inputFilePath.endsWith(".docx")) {
                XWPFDocument docx = new XWPFDocument(new FileInputStream(inputFilePath));
                XWPFWordExtractor we = new XWPFWordExtractor(docx);
                extractedText = we.getText();
                fileForPdf = new FileOutputStream(new File(outputFilePath));
                we.close();
            }

            Document document = new Document();
            PdfWriter.getInstance(document, fileForPdf);

            document.open();
            document.add(new Paragraph(extractedText));
            document.close();
            fileForPdf.close();

        } catch (Exception e) {
            throw new Exception("Error converting document to PDF", e);
        }
    }

    public String readDocFile(String fileName) {
        String output = "";
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor we = new WordExtractor(doc);
            String[] paragraphs = we.getParagraphText();
            for (String para : paragraphs) {
                output = output + "\n" + para.toString() + "\n";
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public String readDocxFile(String fileName) {
        String output = "";
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                output = output + "\n" + para.getText() + "\n";
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public void writePdfFile(String output, String pdfPath) throws FileNotFoundException, DocumentException {
        File file = new File(pdfPath);
        FileOutputStream fileout = new FileOutputStream(file);
        Document document = new Document();
        PdfWriter.getInstance(document, fileout);
        document.addAuthor("Milind");
        document.addTitle("My Converted PDF");
        document.open();
        String[] splitter = output.split("\\n");
        for (int i = 0; i < splitter.length; i++) {
            Chunk chunk = new Chunk(splitter[i]);
            Font font = new Font();
            font.setStyle(Font.UNDERLINE);
            font.setStyle(Font.ITALIC);
            chunk.setFont(font);
            document.add(chunk);
            Paragraph paragraph = new Paragraph();
            paragraph.add("");
            document.add(paragraph);
        }
        document.close();

    }

    public void convertWordToPdf1(String filePath, String pdfPath) throws FileNotFoundException, DocumentException {
        String ext = FilenameUtils.getExtension(filePath);
        String output = "";
        if ("docx".equalsIgnoreCase(ext)) {
            output = readDocxFile(filePath);
        } else if ("doc".equalsIgnoreCase(ext)) {
            output = readDocFile(filePath);
        } else {
            System.out.println("INVALID FILE TYPE. ONLY .doc and .docx are permitted.");
        }
        writePdfFile(output, pdfPath);
    }

    public void convertWordToPdf2(String inputFilePath, String outputFilePath) throws Exception {
        POIFSFileSystem fs = null;
        Document document = new Document();

        try {
            fs = new POIFSFileSystem(new FileInputStream(inputFilePath));

            HWPFDocument doc = new HWPFDocument(fs);
            WordExtractor we = new WordExtractor(doc);

            OutputStream file = new FileOutputStream(new File(outputFilePath));

            PdfWriter writer = PdfWriter.getInstance(document, file);

            Range range = doc.getRange();
            document.open();
            writer.setPageEmpty(true);
            document.newPage();
            writer.setPageEmpty(true);

            String[] paragraphs = we.getParagraphText();
            for (int i = 0; i < paragraphs.length; i++) {
                org.apache.poi.hwpf.usermodel.Paragraph pr = range.getParagraph(i);
                paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");
                document.add(new Paragraph(paragraphs[i]));
            }

        } catch (Exception e) {
            throw new Exception("Error converting Word document to PDF", e);
        } finally {
            document.close();
        }
    }

    public void docToPdf(String docPath, String pdfPath) {
        File inputWord = new File(docPath);
        File outputFile = new File(pdfPath);
        try  {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
