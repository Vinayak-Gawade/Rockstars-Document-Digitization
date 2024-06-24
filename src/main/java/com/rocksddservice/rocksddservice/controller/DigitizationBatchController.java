package com.rocksddservice.rocksddservice.controller;

import com.rocksddservice.rocksddservice.dao.DigitizationBatch;
import com.rocksddservice.rocksddservice.dao.DigitizationBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/digitization-batch")
public class DigitizationBatchController {

    @Autowired
    private DigitizationBatchRepository digitizationBatchRepository;

    @PostMapping("/create-batch")
    public DigitizationBatch createDigitizationBatch(@RequestBody DigitizationBatch digitizationBatch) {
        return digitizationBatchRepository.save(digitizationBatch);
    }

    @GetMapping("/fetch-batch")
    public DigitizationBatch getDigitizationBatch(@RequestParam Long id) {
        return digitizationBatchRepository.findById(id).orElse(null);
    }

    @GetMapping("/fetch-all-batches")
    public Iterable<DigitizationBatch> getAllDigitizationBatches() {
        return digitizationBatchRepository.findAll();
    }

    @PutMapping("/update-batch")
    public DigitizationBatch updateDigitizationBatch(@RequestParam Long id, @RequestBody DigitizationBatch updatedBatch) {
        Optional<DigitizationBatch> optionalBatch = digitizationBatchRepository.findById(id);
        if (optionalBatch.isPresent()) {
            DigitizationBatch existingBatch = optionalBatch.get();
            existingBatch.setBatchId(updatedBatch.getBatchId());
            existingBatch.setCaseId(updatedBatch.getCaseId());
            existingBatch.setCreatedDateTime(updatedBatch.getCreatedDateTime());
            existingBatch.setModifiedDateTime(updatedBatch.getModifiedDateTime());
            existingBatch.setStatus(updatedBatch.getStatus());
            existingBatch.setDigitizedData(updatedBatch.getDigitizedData());
            existingBatch.setLastMntDateTime(updatedBatch.getLastMntDateTime());
            existingBatch.setReference(updatedBatch.getReference());
            return digitizationBatchRepository.save(existingBatch);
        } else {
            return null; // Or throw an exception
        }
    }

    @DeleteMapping("/delete-batch")
    public void deleteDigitizationBatch(@RequestParam Long id) {
        digitizationBatchRepository.deleteById(id);
    }
}

