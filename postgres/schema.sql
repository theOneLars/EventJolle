-- DROP TABLES AND SEQUENCES
DROP TABLE public.measurement;
DROP SEQUENCE measurement_id_seq;

-- CREATE SEQUENCES
CREATE SEQUENCE measurement_id_seq;

ALTER SEQUENCE measurement_id_seq OWNER to postgres;

-- CREATE TABLES
CREATE TABLE public.measurement
(
  id integer NOT NULL DEFAULT nextval('measurement_id_seq'::regclass),
  path character varying(50) NOT NULL,
  "timestamp" timestamp(6) without time zone NOT NULL,
  measurement text NOT NULL,
  CONSTRAINT "IdPk" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.measurement
  OWNER TO postgres;
