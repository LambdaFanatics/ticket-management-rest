CREATE SEQUENCE ticket_id_seq;
CREATE TABLE ticket (
  no     TEXT PRIMARY KEY NOT NULL DEFAULT nextval('ticket_id_seq'),
  title  TEXT,
  status TEXT
);
