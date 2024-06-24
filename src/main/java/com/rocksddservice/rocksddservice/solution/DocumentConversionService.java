package com.rocksddservice.rocksddservice.solution;

public interface DocumentConversionService {
    void convertToPDF(String docPath, String pdfPath) throws Exception;
}
