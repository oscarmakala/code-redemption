CREATE USER 'cr_voucher_service_user'@'%' IDENTIFIED BY 'cr_voucher_service_password';
create DATABASE cr_voucher_service;
GRANT ALL PRIVILEGES ON cr_voucher_service.* TO 'cr_voucher_service_user'@'%' WITH GRANT OPTION;
USE cr_voucher_service;
DROP table IF EXISTS events;
DROP table IF EXISTS entities;
DROP table IF EXISTS snapshots;
DROP table IF EXISTS cdc_monitoring;

create table events
(
    event_id         VARCHAR(255) PRIMARY KEY,
    event_type       LONGTEXT,
    event_data       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    entity_type      VARCHAR(255)                                              NOT NULL,
    entity_id        VARCHAR(255)                                              NOT NULL,
    triggering_event LONGTEXT,
    metadata         LONGTEXT,
    published        TINYINT DEFAULT 0
);

CREATE INDEX events_idx ON events (entity_type, entity_id, event_id);
CREATE INDEX events_published_idx ON events (published, event_id);

create table entities
(
    entity_type    VARCHAR(255),
    entity_id      VARCHAR(255),
    entity_version LONGTEXT NOT NULL,
    PRIMARY KEY (entity_type, entity_id)
);

CREATE INDEX entities_idx ON entities (entity_type, entity_id);

create table snapshots
(
    entity_type       VARCHAR(255),
    entity_id         VARCHAR(255),
    entity_version    VARCHAR(255),
    snapshot_type     LONGTEXT NOT NULL,
    snapshot_json     LONGTEXT NOT NULL,
    triggering_events LONGTEXT,
    PRIMARY KEY (entity_type, entity_id, entity_version)
);

create table cdc_monitoring
(
    reader_id VARCHAR(255) PRIMARY KEY,
    last_time BIGINT
);


DROP Table IF Exists saga_instance_participants;
DROP Table IF Exists saga_instance;
DROP Table IF Exists saga_lock_table;
DROP Table IF Exists saga_stash_table;

CREATE TABLE saga_instance_participants
(
    saga_type   VARCHAR(255) NOT NULL,
    saga_id     VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    resource    VARCHAR(100) NOT NULL,
    PRIMARY KEY (saga_type, saga_id, destination, resource)
);


CREATE TABLE saga_instance
(
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    state_name      VARCHAR(100)  NOT NULL,
    last_request_id VARCHAR(100),
    end_state       INT(1),
    compensating    INT(1),
    saga_data_type  VARCHAR(1000) NOT NULL,
    saga_data_json  VARCHAR(1000) NOT NULL,
    PRIMARY KEY (saga_type, saga_id)
);

create table saga_lock_table
(
    target    VARCHAR(100) PRIMARY KEY,
    saga_type VARCHAR(255) NOT NULL,
    saga_Id   VARCHAR(100) NOT NULL
);

create table saga_stash_table
(
    message_id      VARCHAR(100) PRIMARY KEY,
    target          VARCHAR(100)  NOT NULL,
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    message_headers VARCHAR(1000) NOT NULL,
    message_payload VARCHAR(1000) NOT NULL
);
DROP Table IF Exists message;
DROP Table IF Exists received_messages;
DROP Table IF exists offset_store;

CREATE TABLE message
(
    id            VARCHAR(255) PRIMARY KEY,
    destination   LONGTEXT                                                  NOT NULL,
    headers       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    payload       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    published     SMALLINT DEFAULT 0,
    creation_time BIGINT
);

CREATE INDEX message_published_idx ON message (published, id);

CREATE TABLE received_messages
(
    consumer_id   VARCHAR(255),
    message_id    VARCHAR(255),
    creation_time BIGINT,
    published     SMALLINT DEFAULT 0,
    PRIMARY KEY (consumer_id, message_id)
);

CREATE TABLE offset_store
(
    client_name       VARCHAR(255) NOT NULL PRIMARY KEY,
    serialized_offset LONGTEXT
);

CREATE
    USER 'cr_order_service_user'@'%' IDENTIFIED BY 'cr_order_service_password';
create
    database cr_order_service;
GRANT ALL PRIVILEGES ON cr_order_service.* TO
    'cr_order_service_user'@'%' WITH GRANT OPTION;
USE cr_order_service;
DROP table IF EXISTS events;
DROP table IF EXISTS entities;
DROP table IF EXISTS snapshots;
DROP table IF EXISTS cdc_monitoring;

create table events
(
    event_id         VARCHAR(255) PRIMARY KEY,
    event_type       LONGTEXT,
    event_data       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    entity_type      VARCHAR(255)                                              NOT NULL,
    entity_id        VARCHAR(255)                                              NOT NULL,
    triggering_event LONGTEXT,
    metadata         LONGTEXT,
    published        TINYINT DEFAULT 0
);

CREATE INDEX events_idx ON events (entity_type, entity_id, event_id);
CREATE INDEX events_published_idx ON events (published, event_id);

create table entities
(
    entity_type    VARCHAR(255),
    entity_id      VARCHAR(255),
    entity_version LONGTEXT NOT NULL,
    PRIMARY KEY (entity_type, entity_id)
);

CREATE INDEX entities_idx ON entities (entity_type, entity_id);

create table snapshots
(
    entity_type       VARCHAR(255),
    entity_id         VARCHAR(255),
    entity_version    VARCHAR(255),
    snapshot_type     LONGTEXT NOT NULL,
    snapshot_json     LONGTEXT NOT NULL,
    triggering_events LONGTEXT,
    PRIMARY KEY (entity_type, entity_id, entity_version)
);

create table cdc_monitoring
(
    reader_id VARCHAR(255) PRIMARY KEY,
    last_time BIGINT
);


DROP Table IF Exists saga_instance_participants;
DROP Table IF Exists saga_instance;
DROP Table IF Exists saga_lock_table;
DROP Table IF Exists saga_stash_table;

CREATE TABLE saga_instance_participants
(
    saga_type   VARCHAR(255) NOT NULL,
    saga_id     VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    resource    VARCHAR(100) NOT NULL,
    PRIMARY KEY (saga_type, saga_id, destination, resource)
);


CREATE TABLE saga_instance
(
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    state_name      VARCHAR(100)  NOT NULL,
    last_request_id VARCHAR(100),
    end_state       INT(1),
    compensating    INT(1),
    saga_data_type  VARCHAR(1000) NOT NULL,
    saga_data_json  VARCHAR(1000) NOT NULL,
    PRIMARY KEY (saga_type, saga_id)
);

create table saga_lock_table
(
    target    VARCHAR(100) PRIMARY KEY,
    saga_type VARCHAR(255) NOT NULL,
    saga_Id   VARCHAR(100) NOT NULL
);

create table saga_stash_table
(
    message_id      VARCHAR(100) PRIMARY KEY,
    target          VARCHAR(100)  NOT NULL,
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    message_headers VARCHAR(1000) NOT NULL,
    message_payload VARCHAR(1000) NOT NULL
);

DROP Table IF Exists message;
DROP Table IF Exists received_messages;
DROP Table IF exists offset_store;

CREATE TABLE message
(
    id            VARCHAR(255) PRIMARY KEY,
    destination   LONGTEXT                                                  NOT NULL,
    headers       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    payload       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    published     SMALLINT DEFAULT 0,
    creation_time BIGINT
);

CREATE INDEX message_published_idx ON message (published, id);

CREATE TABLE received_messages
(
    consumer_id   VARCHAR(255),
    message_id    VARCHAR(255),
    creation_time BIGINT,
    published     SMALLINT DEFAULT 0,
    PRIMARY KEY (consumer_id, message_id)
);

CREATE TABLE offset_store
(
    client_name       VARCHAR(255) NOT NULL PRIMARY KEY,
    serialized_offset LONGTEXT
);


CREATE USER 'cr_account_service_user'@'%' IDENTIFIED BY 'cr_account_service_password';
create database cr_account_service;
GRANT ALL PRIVILEGES ON cr_account_service.* TO 'cr_account_service_user'@'%' WITH GRANT OPTION;
USE cr_account_service;
DROP table IF EXISTS events;
DROP table IF EXISTS entities;
DROP table IF EXISTS snapshots;
DROP table IF EXISTS cdc_monitoring;

create table events
(
    event_id         VARCHAR(255) PRIMARY KEY,
    event_type       LONGTEXT,
    event_data       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    entity_type      VARCHAR(255)                                              NOT NULL,
    entity_id        VARCHAR(255)                                              NOT NULL,
    triggering_event LONGTEXT,
    metadata         LONGTEXT,
    published        TINYINT DEFAULT 0
);

CREATE INDEX events_idx ON events (entity_type, entity_id, event_id);
CREATE INDEX events_published_idx ON events (published, event_id);

create table entities
(
    entity_type    VARCHAR(255),
    entity_id      VARCHAR(255),
    entity_version LONGTEXT NOT NULL,
    PRIMARY KEY (entity_type, entity_id)
);

CREATE INDEX entities_idx ON entities (entity_type, entity_id);

create table snapshots
(
    entity_type       VARCHAR(255),
    entity_id         VARCHAR(255),
    entity_version    VARCHAR(255),
    snapshot_type     LONGTEXT NOT NULL,
    snapshot_json     LONGTEXT NOT NULL,
    triggering_events LONGTEXT,
    PRIMARY KEY (entity_type, entity_id, entity_version)
);

create table cdc_monitoring
(
    reader_id VARCHAR(255) PRIMARY KEY,
    last_time BIGINT
);


DROP Table IF Exists saga_instance_participants;
DROP Table IF Exists saga_instance;
DROP Table IF Exists saga_lock_table;
DROP Table IF Exists saga_stash_table;

CREATE TABLE saga_instance_participants
(
    saga_type   VARCHAR(255) NOT NULL,
    saga_id     VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    resource    VARCHAR(100) NOT NULL,
    PRIMARY KEY (saga_type, saga_id, destination, resource)
);


CREATE TABLE saga_instance
(
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    state_name      VARCHAR(100)  NOT NULL,
    last_request_id VARCHAR(100),
    end_state       INT(1),
    compensating    INT(1),
    saga_data_type  VARCHAR(1000) NOT NULL,
    saga_data_json  VARCHAR(1000) NOT NULL,
    PRIMARY KEY (saga_type, saga_id)
);

create table saga_lock_table
(
    target    VARCHAR(100) PRIMARY KEY,
    saga_type VARCHAR(255) NOT NULL,
    saga_Id   VARCHAR(100) NOT NULL
);

create table saga_stash_table
(
    message_id      VARCHAR(100) PRIMARY KEY,
    target          VARCHAR(100)  NOT NULL,
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    message_headers VARCHAR(1000) NOT NULL,
    message_payload VARCHAR(1000) NOT NULL
);

DROP Table IF Exists message;
DROP Table IF Exists received_messages;
DROP Table IF exists offset_store;

CREATE TABLE message
(
    id            VARCHAR(255) PRIMARY KEY,
    destination   LONGTEXT                                                  NOT NULL,
    headers       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    payload       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    published     SMALLINT DEFAULT 0,
    creation_time BIGINT
);

CREATE INDEX message_published_idx ON message (published, id);

CREATE TABLE received_messages
(
    consumer_id   VARCHAR(255),
    message_id    VARCHAR(255),
    creation_time BIGINT,
    published     SMALLINT DEFAULT 0,
    PRIMARY KEY (consumer_id, message_id)
);

CREATE TABLE offset_store
(
    client_name       VARCHAR(255) NOT NULL PRIMARY KEY,
    serialized_offset LONGTEXT
);


CREATE USER 'cr_notification_service_user'@'%' IDENTIFIED BY 'cr_notification_service_password';

CREATE    database cr_notification_service;

GRANT ALL PRIVILEGES ON cr_notification_service.* TO 'cr_notification_service_user'@'%' WITH GRANT OPTION;

USE cr_notification_service;
DROP table IF EXISTS events;
DROP table IF EXISTS entities;
DROP table IF EXISTS snapshots;
DROP table IF EXISTS cdc_monitoring;

create table events
(
    event_id         VARCHAR(255) PRIMARY KEY,
    event_type       LONGTEXT,
    event_data       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    entity_type      VARCHAR(255)                                              NOT NULL,
    entity_id        VARCHAR(255)                                              NOT NULL,
    triggering_event LONGTEXT,
    metadata         LONGTEXT,
    published        TINYINT DEFAULT 0
);

CREATE INDEX events_idx ON events (entity_type, entity_id, event_id);
CREATE INDEX events_published_idx ON events (published, event_id);

create table entities
(
    entity_type    VARCHAR(255),
    entity_id      VARCHAR(255),
    entity_version LONGTEXT NOT NULL,
    PRIMARY KEY (entity_type, entity_id)
);

CREATE INDEX entities_idx ON entities (entity_type, entity_id);

create table snapshots
(
    entity_type       VARCHAR(255),
    entity_id         VARCHAR(255),
    entity_version    VARCHAR(255),
    snapshot_type     LONGTEXT NOT NULL,
    snapshot_json     LONGTEXT NOT NULL,
    triggering_events LONGTEXT,
    PRIMARY KEY (entity_type, entity_id, entity_version)
);

create table cdc_monitoring
(
    reader_id VARCHAR(255) PRIMARY KEY,
    last_time BIGINT
);


DROP Table IF Exists saga_instance_participants;
DROP Table IF Exists saga_instance;
DROP Table IF Exists saga_lock_table;
DROP Table IF Exists saga_stash_table;

CREATE TABLE saga_instance_participants
(
    saga_type   VARCHAR(255) NOT NULL,
    saga_id     VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    resource    VARCHAR(100) NOT NULL,
    PRIMARY KEY (saga_type, saga_id, destination, resource)
);


CREATE TABLE saga_instance
(
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    state_name      VARCHAR(100)  NOT NULL,
    last_request_id VARCHAR(100),
    end_state       INT(1),
    compensating    INT(1),
    saga_data_type  VARCHAR(1000) NOT NULL,
    saga_data_json  VARCHAR(1000) NOT NULL,
    PRIMARY KEY (saga_type, saga_id)
);

create table saga_lock_table
(
    target    VARCHAR(100) PRIMARY KEY,
    saga_type VARCHAR(255) NOT NULL,
    saga_Id   VARCHAR(100) NOT NULL
);

create table saga_stash_table
(
    message_id      VARCHAR(100) PRIMARY KEY,
    target          VARCHAR(100)  NOT NULL,
    saga_type       VARCHAR(255)  NOT NULL,
    saga_id         VARCHAR(100)  NOT NULL,
    message_headers VARCHAR(1000) NOT NULL,
    message_payload VARCHAR(1000) NOT NULL
);

DROP Table IF Exists message;

DROP Table IF exists offset_store;

CREATE TABLE message
(
    id            VARCHAR(255) PRIMARY KEY,
    destination   LONGTEXT                                                  NOT NULL,
    headers       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    payload       LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    published     SMALLINT DEFAULT 0,
    creation_time BIGINT
);

CREATE INDEX message_published_idx ON message (published, id);
DROP Table IF Exists received_messages;
CREATE TABLE received_messages
(
    consumer_id   VARCHAR(255),
    message_id    VARCHAR(255),
    creation_time BIGINT,
    published     SMALLINT DEFAULT 0,
    PRIMARY KEY (consumer_id, message_id)
);

CREATE TABLE offset_store
(
    client_name       VARCHAR(255) NOT NULL PRIMARY KEY,
    serialized_offset LONGTEXT
);
