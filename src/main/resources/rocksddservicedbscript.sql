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

--TRUNCATE TABLE public.digidocsinfo;

-- DROP TABLE public.digidocsinfo;

--DROP DATABASE rocksddservice;