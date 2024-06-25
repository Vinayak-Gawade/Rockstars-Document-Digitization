package com.rocksddservice.rocksddservice.solution;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class DocumentConversionService implements IDocumentConversionService {

    private static final String[] SUPPORTED_EXTENSIONS = {"doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "html"};

    @Override
    public void convertToPDFBatchMode(String sourcePath, String targetPath) throws Exception {
        File inputFolder = new File(sourcePath);
        File outputFolder = new File(targetPath);

/*        if (!inputFolder.isDirectory() || !outputFolder.isDirectory()) {
            throw new Exception("Both input and output paths must be directories.");
        }*/

        // Verify if inputFolderPath and outputFolderPath are directories
        if (!inputFolder.exists() || !inputFolder.isDirectory()) {
            throw new Exception("Input path must be an existing directory: " + sourcePath);
        }
        if (!outputFolder.exists() || !outputFolder.isDirectory()) {
            throw new Exception("Output path must be an existing directory: " + targetPath);
        }

        File[] files = getSupportedFiles(inputFolder);

        for (File file : files) {
            String outputFilePath = targetPath + File.separator + file.getName().replaceFirst("[.][^.]+$", "") + ".pdf";
            convertToPDFSingleMode(file.getAbsolutePath(), outputFilePath);
        }
    }

    private File[] getSupportedFiles(File inputFolder) throws Exception {
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
        return files;
    }

    @Override
    public void convertToPDFSingleMode(String sourcePath, String targetPath) throws Exception {
        File inputFile = new File(sourcePath);
        File outputFile = new File(targetPath);

//        String fileExtension = getFileExtension(inputFile.getName());
        String fileExtension = FilenameUtils.getExtension(inputFile.getName());

        try (InputStream inputStream = new FileInputStream(inputFile);
             OutputStream outputStream = new FileOutputStream(outputFile)) {
            IConverter converter = LocalConverter.builder().build();

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
//                case "ppt":
//                    converter.convert(inputStream).as(DocumentType.PPT).to(outputStream).as(DocumentType.PDF).execute();
//                    break;
//                case "pptx":
//                    converter.convert(inputStream).as(DocumentType.PPTX).to(outputStream).as(DocumentType.PDF).execute();
//                    break;
                case "txt":
                    converter.convert(inputStream).as(DocumentType.TEXT).to(outputStream).as(DocumentType.PDF).execute();
                    break;
                case "html":
                    converter.convert(inputStream).as(DocumentType.HTML).to(outputStream).as(DocumentType.PDF).execute();
                    break;
                default:
                    throw new Exception("Unsupported file type: " + fileExtension);
            }
        } catch (FileNotFoundException e) {
            throw new Exception("File not found: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new Exception("I/O error: " + e.getMessage(), e);
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

