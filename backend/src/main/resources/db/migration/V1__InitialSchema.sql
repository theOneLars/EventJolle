CREATE SEQUENCE measurement_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE public.measurement
(
  id integer NOT NULL DEFAULT nextval('measurement_id_seq'::regclass),
  path character varying(50) NOT NULL,
  "timestamp" timestamp(6) without time zone NOT NULL,
  measurement text NOT NULL,
  PRIMARY KEY (id)
)