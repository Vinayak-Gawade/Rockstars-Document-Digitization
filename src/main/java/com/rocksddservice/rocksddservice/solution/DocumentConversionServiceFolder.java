package com.rocksddservice.rocksddservice.solution;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
public class DocumentConversionServiceFolder {

    private static final String[] SUPPORTED_EXTENSIONS = {"doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "html"};

    public void convertFolderToPDF(String inputFolderPath, String outputFolderPath) throws Exception {
        File inputFolder = new File(inputFolderPath);
        File outputFolder = new File(outputFolderPath);

/*        if (!inputFolder.isDirectory() || !outputFolder.isDirectory()) {
            throw new Exception("Both input and output paths must be directories.");
        }*/

        // Verify if inputFolderPath and outputFolderPath are directories
        if (!inputFolder.exists() || !inputFolder.isDirectory()) {
            throw new Exception("Input path must be an existing directory: " + inputFolderPath);
        }
        if (!outputFolder.exists() || !outputFolder.isDirectory()) {
            throw new Exception("Output path must be an existing directory: " + outputFolderPath);
        }

        File[] files = inputFolder.listFiles((dir, name) -> {
//            String fileExtension = getFileExtension(name);
            String fileExtension = FilenameUtils.getExtension(name);
            for (String extension : SUPPORTED_EXTENSIONS) {
                if (extension.equalsIgnoreCase(fileExtension)) {
                    return true;
                }
            }
            return false;
        });

        if (files == null || files.length == 0) {
            throw new Exception("No supported files found in the input directory.");
        }

        for (File file : files) {
            String outputFilePath = outputFolderPath + File.separator + file.getName().replaceFirst("[.][^.]+$", "") + ".pdf";
            convertToPDF(file.getAbsolutePath(), outputFilePath);
        }
    }

    private void convertToPDF(String sourcePath, String targetPath) throws Exception {
        File inputFile = new File(sourcePath);
        File outputFile = new File(targetPath);
//        String fileExtension = getFileExtension(inputFile.getName());
        String fileExtension = FilenameUtils.getExtension(inputFile.getName());


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
//                case "ppt":
//                    converter.convert(inputStream).as(DocumentType.PPT).to(outputStream).as(DocumentType.PDF).execute();
//                    break;
//                case "pptx":
//                    converter.convert(inputStream).as(DocumentType.PPTX).to(outputStream).as(DocumentType.PDF).execute();
//                    break;
                case "txt":
                    converter.convert(inputStream).as(DocumentType.TEXT).to(outputStream).as(DocumentType.PDF).execute();
                    break;
//                case "html":
//                    converter.convert(inputStream).as(DocumentType.HTML).to(outputStream).as(DocumentType.PDF).execute();
//                    break;
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


    private static final Path TEMP_DIR = Paths.get("temp");

    public void convertDocument(String inputFilePath, String outputFilePath) throws Exception {
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        String fileExtension = getFileExtension(inputFile.getName());

        // Ensure the temporary directory exists
        if (!Files.exists(TEMP_DIR)) {
            Files.createDirectories(TEMP_DIR);
        }

        try (InputStream inputStream = new FileInputStream(inputFile);
             OutputStream outputStream = new FileOutputStream(outputFile)) {

            IConverter converter = LocalConverter.builder()
                    .baseFolder(TEMP_DIR.toFile())
                    .workerPool(20, 25, 2, TimeUnit.MINUTES)
                    .processTimeout(5, TimeUnit.MINUTES)
                    .build();

            switch (fileExtension.toLowerCase()) {
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
        } catch (FileNotFoundException e) {
            throw new Exception("File not found: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new Exception("I/O error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Conversion error: " + e.getMessage(), e);
        }
    }
}

