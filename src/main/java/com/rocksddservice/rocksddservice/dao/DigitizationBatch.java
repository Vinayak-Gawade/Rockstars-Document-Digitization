package com.rocksddservice.rocksddservice.dao;

import com.rocksddservice.rocksddservice.solution.JsonAttributeConverter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "DigitizationBatch")
@TypeDefs({
//        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "json", typeClass = JsonType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@EntityListeners(AuditingEntityListener.class)
public class DigitizationBatch {

//    @Id
//    @Column(name = "id")
//    private UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "batch_id")
    private Integer batchId;

    @Column(name = "case_id")
    private Integer caseId;

    @Column(name = "created_datetime")
    private LocalDateTime createdDateTime;

    @Column(name = "modified_datetime")
    private ZonedDateTime modifiedDateTime;

    @Column(name = "status")
    private String status;

//    @Type(type = "jsonb")
//    @Column(name = "digitized_data", columnDefinition = "jsonb")
//    private String digitizedData;

//    @Profile("postgresql")
//    @Type(type = "jsonb")
//    @Column(name = "digitized_data", columnDefinition = "jsonb")
//    private String digitizedDataPostgres;

//    @Profile("h2")
//    @Type(type = "json")
//    @Column(name = "digitized_data", columnDefinition = "text")
    @Column(columnDefinition = "CLOB")
    @Convert(converter = JsonAttributeConverter.class)
//    private String digitizedDataH2;
    private String digitizedData;

    @LastModifiedDate
    @Column(name = "last_mnt_datetime")
    private LocalDateTime lastMntDateTime;

    @Column(name = "reference")
    private String reference;
}
