

CREATE USER 'cr_voucher_service_user'@'%' IDENTIFIED BY 'cr_voucher_service_password';
  create database cr_voucher_service;
  GRANT ALL PRIVILEGES ON cr_voucher_service.* TO 'cr_voucher_service_user'@'%' WITH GRANT OPTION;

USE cr_voucher_service;
DROP table IF EXISTS events;
DROP table IF EXISTS  entities;
DROP table IF EXISTS  snapshots;
DROP table IF EXISTS cdc_monitoring;

create table events (
  event_id VARCHAR(255) PRIMARY KEY,
  event_type LONGTEXT,
  event_data LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  entity_type VARCHAR(255) NOT NULL,
  entity_id VARCHAR(255) NOT NULL,
  triggering_event LONGTEXT,
  metadata LONGTEXT,
  published TINYINT DEFAULT 0
);

CREATE INDEX events_idx ON events(entity_type, entity_id, event_id);
CREATE INDEX events_published_idx ON events(published, event_id);

create table entities (
  entity_type VARCHAR(255),
  entity_id VARCHAR(255),
  entity_version LONGTEXT NOT NULL,
  PRIMARY KEY(entity_type, entity_id)
);

CREATE INDEX entities_idx ON entities(entity_type, entity_id);

create table snapshots (
  entity_type VARCHAR(255),
  entity_id VARCHAR(255),
  entity_version VARCHAR(255),
  snapshot_type LONGTEXT NOT NULL,
  snapshot_json LONGTEXT NOT NULL,
  triggering_events LONGTEXT,
  PRIMARY KEY(entity_type, entity_id, entity_version)
);

create table cdc_monitoring (
  reader_id VARCHAR(255) PRIMARY KEY,
  last_time BIGINT
);

DROP Table IF Exists message;
DROP Table IF Exists received_messages;

CREATE TABLE message (
  id VARCHAR(255) PRIMARY KEY,
  destination LONGTEXT NOT NULL,
  headers LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  payload LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

CREATE INDEX message_published_idx ON message(published, id);

CREATE TABLE received_messages (
  consumer_id VARCHAR(255),
  message_id VARCHAR(255),
  creation_time BIGINT,
  published SMALLINT DEFAULT 0,
  PRIMARY KEY(consumer_id, message_id)
);

CREATE TABLE offset_store(
  client_name VARCHAR(255) NOT NULL PRIMARY KEY,
  serialized_offset LONGTEXT
);

ALTER TABLE eventuate.message MODIFY payload JSON;
ALTER TABLE eventuate.message MODIFY headers JSON;


CREATE TABLE new_message (
  dbid BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id LONGTEXT,
  destination LONGTEXT NOT NULL,
  headers LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  payload LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

INSERT INTO new_message (id, dbid, destination, headers, payload, published, creation_time)
    VALUES ('', ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000), 'CDC-IGNORED', '{}', '\"ID-GENERATION-STARTING-VALUE\"', 1, ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000));

INSERT INTO new_message (id, destination, headers, payload, published, creation_time)
    SELECT id, destination, headers, payload, published, creation_time FROM message ORDER BY id;

DROP TABLE message;

ALTER TABLE new_message RENAME TO message;

CREATE INDEX message_published_idx ON message(published, dbid);

create table new_events (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  event_id VARCHAR(255),
  event_type LONGTEXT,
  event_data LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  entity_type VARCHAR(255) NOT NULL,
  entity_id VARCHAR(255) NOT NULL,
  triggering_event LONGTEXT,
  metadata LONGTEXT,
  published TINYINT DEFAULT 0
);

INSERT INTO new_events (id, event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    VALUES (ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000), '', 'CDC-IGNORED', 'ID-GENERATION-STARTING-VALUE', 'CDC-IGNORED', 'CDC-IGNORED', '', '', 1);

INSERT INTO new_events (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    SELECT event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published FROM events ORDER BY event_id;

DROP TABLE events;

ALTER TABLE new_events RENAME TO events;

CREATE INDEX events_idx ON events(entity_type, entity_id, id);

CREATE INDEX events_published_idx ON events(published, id);
  CREATE USER 'cr_order_service_user'@'%' IDENTIFIED BY 'cr_order_service_password';
  create database cr_order_service;
  GRANT ALL PRIVILEGES ON cr_order_service.* TO 'cr_order_service_user'@'%' WITH GRANT OPTION;
  USE cr_order_service;
DROP table IF EXISTS events;
DROP table IF EXISTS  entities;
DROP table IF EXISTS  snapshots;
DROP table IF EXISTS cdc_monitoring;

create table events (
  event_id VARCHAR(255) PRIMARY KEY,
  event_type LONGTEXT,
  event_data LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  entity_type VARCHAR(255) NOT NULL,
  entity_id VARCHAR(255) NOT NULL,
  triggering_event LONGTEXT,
  metadata LONGTEXT,
  published TINYINT DEFAULT 0
);

CREATE INDEX events_idx ON events(entity_type, entity_id, event_id);
CREATE INDEX events_published_idx ON events(published, event_id);

create table entities (
  entity_type VARCHAR(255),
  entity_id VARCHAR(255),
  entity_version LONGTEXT NOT NULL,
  PRIMARY KEY(entity_type, entity_id)
);

CREATE INDEX entities_idx ON entities(entity_type, entity_id);

create table snapshots (
  entity_type VARCHAR(255),
  entity_id VARCHAR(255),
  entity_version VARCHAR(255),
  snapshot_type LONGTEXT NOT NULL,
  snapshot_json LONGTEXT NOT NULL,
  triggering_events LONGTEXT,
  PRIMARY KEY(entity_type, entity_id, entity_version)
);

create table cdc_monitoring (
  reader_id VARCHAR(255) PRIMARY KEY,
  last_time BIGINT
);

DROP Table IF Exists message;
DROP Table IF Exists received_messages;

CREATE TABLE message (
  id VARCHAR(255) PRIMARY KEY,
  destination LONGTEXT NOT NULL,
  headers LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  payload LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

CREATE INDEX message_published_idx ON message(published, id);

CREATE TABLE received_messages (
  consumer_id VARCHAR(255),
  message_id VARCHAR(255),
  creation_time BIGINT,
  published SMALLINT DEFAULT 0,
  PRIMARY KEY(consumer_id, message_id)
);

CREATE TABLE offset_store(
  client_name VARCHAR(255) NOT NULL PRIMARY KEY,
  serialized_offset LONGTEXT
);

ALTER TABLE eventuate.message MODIFY payload JSON;
ALTER TABLE eventuate.message MODIFY headers JSON;


CREATE TABLE new_message (
  dbid BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id LONGTEXT,
  destination LONGTEXT NOT NULL,
  headers LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  payload LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

INSERT INTO new_message (id, dbid, destination, headers, payload, published, creation_time)
    VALUES ('', ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000), 'CDC-IGNORED', '{}', '\"ID-GENERATION-STARTING-VALUE\"', 1, ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000));

INSERT INTO new_message (id, destination, headers, payload, published, creation_time)
    SELECT id, destination, headers, payload, published, creation_time FROM message ORDER BY id;

DROP TABLE message;

ALTER TABLE new_message RENAME TO message;

CREATE INDEX message_published_idx ON message(published, dbid);

create table new_events (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  event_id VARCHAR(255),
  event_type LONGTEXT,
  event_data LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  entity_type VARCHAR(255) NOT NULL,
  entity_id VARCHAR(255) NOT NULL,
  triggering_event LONGTEXT,
  metadata LONGTEXT,
  published TINYINT DEFAULT 0
);

INSERT INTO new_events (id, event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    VALUES (ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000), '', 'CDC-IGNORED', 'ID-GENERATION-STARTING-VALUE', 'CDC-IGNORED', 'CDC-IGNORED', '', '', 1);

INSERT INTO new_events (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    SELECT event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published FROM events ORDER BY event_id;

DROP TABLE events;

ALTER TABLE new_events RENAME TO events;

CREATE INDEX events_idx ON events(entity_type, entity_id, id);

CREATE INDEX events_published_idx ON events(published, id);
  CREATE USER 'cr_account_service_user'@'%' IDENTIFIED BY 'cr_account_service_password';
  create database cr_account_service;
  GRANT ALL PRIVILEGES ON cr_account_service.* TO 'cr_account_service_user'@'%' WITH GRANT OPTION;
  USE cr_account_service;
DROP table IF EXISTS events;
DROP table IF EXISTS  entities;
DROP table IF EXISTS  snapshots;
DROP table IF EXISTS cdc_monitoring;

create table events (
  event_id VARCHAR(255) PRIMARY KEY,
  event_type LONGTEXT,
  event_data LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  entity_type VARCHAR(255) NOT NULL,
  entity_id VARCHAR(255) NOT NULL,
  triggering_event LONGTEXT,
  metadata LONGTEXT,
  published TINYINT DEFAULT 0
);

CREATE INDEX events_idx ON events(entity_type, entity_id, event_id);
CREATE INDEX events_published_idx ON events(published, event_id);

create table entities (
  entity_type VARCHAR(255),
  entity_id VARCHAR(255),
  entity_version LONGTEXT NOT NULL,
  PRIMARY KEY(entity_type, entity_id)
);

CREATE INDEX entities_idx ON entities(entity_type, entity_id);

create table snapshots (
  entity_type VARCHAR(255),
  entity_id VARCHAR(255),
  entity_version VARCHAR(255),
  snapshot_type LONGTEXT NOT NULL,
  snapshot_json LONGTEXT NOT NULL,
  triggering_events LONGTEXT,
  PRIMARY KEY(entity_type, entity_id, entity_version)
);

create table cdc_monitoring (
  reader_id VARCHAR(255) PRIMARY KEY,
  last_time BIGINT
);

DROP Table IF Exists message;
DROP Table IF Exists received_messages;

CREATE TABLE message (
  id VARCHAR(255) PRIMARY KEY,
  destination LONGTEXT NOT NULL,
  headers LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  payload LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

CREATE INDEX message_published_idx ON message(published, id);

CREATE TABLE received_messages (
  consumer_id VARCHAR(255),
  message_id VARCHAR(255),
  creation_time BIGINT,
  published SMALLINT DEFAULT 0,
  PRIMARY KEY(consumer_id, message_id)
);

CREATE TABLE offset_store(
  client_name VARCHAR(255) NOT NULL PRIMARY KEY,
  serialized_offset LONGTEXT
);

ALTER TABLE eventuate.message MODIFY payload JSON;
ALTER TABLE eventuate.message MODIFY headers JSON;


CREATE TABLE new_message (
  dbid BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id LONGTEXT,
  destination LONGTEXT NOT NULL,
  headers LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  payload LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

INSERT INTO new_message (id, dbid, destination, headers, payload, published, creation_time)
    VALUES ('', ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000), 'CDC-IGNORED', '{}', '\"ID-GENERATION-STARTING-VALUE\"', 1, ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000));

INSERT INTO new_message (id, destination, headers, payload, published, creation_time)
    SELECT id, destination, headers, payload, published, creation_time FROM message ORDER BY id;

DROP TABLE message;

ALTER TABLE new_message RENAME TO message;

CREATE INDEX message_published_idx ON message(published, dbid);

create table new_events (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  event_id VARCHAR(255),
  event_type LONGTEXT,
  event_data LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  entity_type VARCHAR(255) NOT NULL,
  entity_id VARCHAR(255) NOT NULL,
  triggering_event LONGTEXT,
  metadata LONGTEXT,
  published TINYINT DEFAULT 0
);

INSERT INTO new_events (id, event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    VALUES (ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000), '', 'CDC-IGNORED', 'ID-GENERATION-STARTING-VALUE', 'CDC-IGNORED', 'CDC-IGNORED', '', '', 1);

INSERT INTO new_events (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    SELECT event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published FROM events ORDER BY event_id;

DROP TABLE events;

ALTER TABLE new_events RENAME TO events;

CREATE INDEX events_idx ON events(entity_type, entity_id, id);

CREATE INDEX events_published_idx ON events(published, id);
  CREATE USER 'cr_notification_service_user'@'%' IDENTIFIED BY 'cr_notification_service_password';
  create database cr_notification_service;
  GRANT ALL PRIVILEGES ON cr_notification_service.* TO 'cr_notification_service_user'@'%' WITH GRANT OPTION;
  USE cr_notification_service;
DROP table IF EXISTS events;
DROP table IF EXISTS  entities;
DROP table IF EXISTS  snapshots;
DROP table IF EXISTS cdc_monitoring;

create table events (
  event_id VARCHAR(255) PRIMARY KEY,
  event_type LONGTEXT,
  event_data LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  entity_type VARCHAR(255) NOT NULL,
  entity_id VARCHAR(255) NOT NULL,
  triggering_event LONGTEXT,
  metadata LONGTEXT,
  published TINYINT DEFAULT 0
);

CREATE INDEX events_idx ON events(entity_type, entity_id, event_id);
CREATE INDEX events_published_idx ON events(published, event_id);

create table entities (
  entity_type VARCHAR(255),
  entity_id VARCHAR(255),
  entity_version LONGTEXT NOT NULL,
  PRIMARY KEY(entity_type, entity_id)
);

CREATE INDEX entities_idx ON entities(entity_type, entity_id);

create table snapshots (
  entity_type VARCHAR(255),
  entity_id VARCHAR(255),
  entity_version VARCHAR(255),
  snapshot_type LONGTEXT NOT NULL,
  snapshot_json LONGTEXT NOT NULL,
  triggering_events LONGTEXT,
  PRIMARY KEY(entity_type, entity_id, entity_version)
);

create table cdc_monitoring (
  reader_id VARCHAR(255) PRIMARY KEY,
  last_time BIGINT
);

DROP Table IF Exists message;
DROP Table IF Exists received_messages;

CREATE TABLE message (
  id VARCHAR(255) PRIMARY KEY,
  destination LONGTEXT NOT NULL,
  headers LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  payload LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

CREATE INDEX message_published_idx ON message(published, id);

CREATE TABLE received_messages (
  consumer_id VARCHAR(255),
  message_id VARCHAR(255),
  creation_time BIGINT,
  published SMALLINT DEFAULT 0,
  PRIMARY KEY(consumer_id, message_id)
);

CREATE TABLE offset_store(
  client_name VARCHAR(255) NOT NULL PRIMARY KEY,
  serialized_offset LONGTEXT
);

ALTER TABLE eventuate.message MODIFY payload JSON;
ALTER TABLE eventuate.message MODIFY headers JSON;


CREATE TABLE new_message (
  dbid BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  id LONGTEXT,
  destination LONGTEXT NOT NULL,
  headers LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  payload LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  published SMALLINT DEFAULT 0,
  creation_time BIGINT
);

INSERT INTO new_message (id, dbid, destination, headers, payload, published, creation_time)
    VALUES ('', ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000), 'CDC-IGNORED', '{}', '\"ID-GENERATION-STARTING-VALUE\"', 1, ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000));

INSERT INTO new_message (id, destination, headers, payload, published, creation_time)
    SELECT id, destination, headers, payload, published, creation_time FROM message ORDER BY id;

DROP TABLE message;

ALTER TABLE new_message RENAME TO message;

CREATE INDEX message_published_idx ON message(published, dbid);

create table new_events (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  event_id VARCHAR(255),
  event_type LONGTEXT,
  event_data LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  entity_type VARCHAR(255) NOT NULL,
  entity_id VARCHAR(255) NOT NULL,
  triggering_event LONGTEXT,
  metadata LONGTEXT,
  published TINYINT DEFAULT 0
);

INSERT INTO new_events (id, event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    VALUES (ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000), '', 'CDC-IGNORED', 'ID-GENERATION-STARTING-VALUE', 'CDC-IGNORED', 'CDC-IGNORED', '', '', 1);

INSERT INTO new_events (event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published)
    SELECT event_id, event_type, event_data, entity_type, entity_id, triggering_event, metadata, published FROM events ORDER BY event_id;

DROP TABLE events;

ALTER TABLE new_events RENAME TO events;

CREATE INDEX events_idx ON events(entity_type, entity_id, id);

CREATE INDEX events_published_idx ON events(published, id);
