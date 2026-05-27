DROP TABLE IF EXISTS notifications;

create table notifications(
    id BIGINT auto_increment primary key ,
    receiver varchar(13) not null ,
    message text not null ,
    type varchar(50) not null ,
    status varchar(50) not null ,
    date timestamp not null default current_timestamp
) ENGINE=InnoDB;