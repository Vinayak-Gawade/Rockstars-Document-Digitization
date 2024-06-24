CREATE DATABASE rocksddservice;

BACKUP DATABASE databasename
TO DISK = 'filepath';

CREATE TABLE public.digidocsinfo (
	docid uuid NOT NULL,
	docname varchar NULL,
	docdata json NULL,
	CONSTRAINT digidocsinfo_pk PRIMARY KEY (docid)
);

INSERT
	INTO
	public.digidocsinfo (docid,
	docname,
	docdata)
VALUES(?,
'',
'');

-- TRUNCATE TABLE public.digidocsinfo;

-- DROP TABLE public.digidocsinfo;

-- DROP DATABASE rocksddservice;

CREATE TABLE digitization_batch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id INTEGER,
    case_id INTEGER,
    created_datetime TIMESTAMP,
    modified_datetime TIMESTAMP WITH TIME ZONE,
    status VARCHAR(255),
    digitized_data CLOB,
    last_mnt_datetime TIMESTAMP,
    reference VARCHAR(255)
);

-- TRUNCATE TABLE public.digitization_batch;

-- DROP TABLE public.digitization_batch;