package com.rocksddservice.rocksddservice.controller;

import com.rocksddservice.rocksddservice.service.CreateDocument;
import com.rocksddservice.rocksddservice.service.ReadParseDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DigitizationController {

    @Autowired
    CreateDocument createDocument;

    @Autowired
    ReadParseDocument readParseDocument;

    @GetMapping("create-word-doc")
    public String createWordDocument(@RequestParam String fileNameWithPath) {
        createDocument.createWordDocument(fileNameWithPath);
        return fileNameWithPath;
    }

    @GetMapping("read-word-doc")
    public String readWordDocument(@RequestParam String fileNameWithPath) {
        return readParseDocument.readWordDocument(fileNameWithPath);
    }
}
