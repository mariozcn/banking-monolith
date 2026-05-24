DROP TABLE IF EXISTS audit_log;

CREATE TABLE audit_log(
    id BIGINT auto_increment primary key ,
    action varchar(100) not null ,
    date TIMESTAMP not null default now(),
    details text,
    entity_type varchar(50),
    entity_id BIGINT
) ENGINE=InnoDB;