package com.rocksddservice.rocksddservice.solution;

public interface IDocumentConversionService {
    void convertToPDFSingleMode(String sourcePath, String targetPath) throws Exception;
    void convertToPDFBatchMode(String sourcePath, String targetPath) throws Exception;
}
