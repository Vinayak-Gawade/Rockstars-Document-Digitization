package com.rocksddservice.rocksddservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigitizationBatchRepository extends JpaRepository<DigitizationBatch, Long> {
}

