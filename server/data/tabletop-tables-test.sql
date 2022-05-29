drop database if exists tabletop_tables_test;
create database tabletop_tables_test;
use tabletop_tables_test;
SET SQL_SAFE_UPDATES = 0;

-- create tables
create table user (
	user_id int primary key auto_increment,
    username varchar(250) not null unique,
    password_hash varchar(2048) not null,
    city varchar(250) not null,
    state varchar(250) not null,
    disabled bit not null default(0),
    `description` varchar(250) not null
);

create table role (
	role_id int primary key auto_increment,
    `name` varchar(250) not null unique
);

create table user_role(
	user_id int not null,
	role_id int not null,
	constraint pk_user_role
		primary key (user_id, role_id),
	constraint fk_user_role_user_id
		foreign key (user_id)
		references user(user_id),
	constraint fk_user_role_role_id
		foreign key (role_id)
		references role(role_id)
);

create table campaign (
	campaign_id int primary key auto_increment,
    user_id int not null,
    `name` varchar(250) not null,
    `description` varchar(250),
    `type` varchar(250) not null,
    city varchar(250) not null,
    state varchar(250) not null,
    session_count int not null default(0),
    max_players int not null,
    current_players int not null default(0),
    constraint fk_campaign_user_id
        foreign key (user_id)
        references user(user_id)
);

create table session (
	session_id int primary key auto_increment,
    campaign_id int not null,
    start_date datetime not null,
    end_date datetime not null,
    constraint fk_session_campaign_id
        foreign key (campaign_id)
        references campaign(campaign_id)
);

create table user_schedule (
	user_schedule_id int primary key auto_increment,
    user_id int not null,
    session_id int null,
    start_date datetime not null,
    end_date datetime not null,
    constraint fk_user_schedule_user_id
        foreign key (user_id)
        references user(user_id),
	constraint fk_user_schedule_session_id
        foreign key (session_id)
        references session(session_id)
);

create table campaign_user (
	campaign_id int not null,
    user_id int not null,
    constraint pk_campaign_user
        primary key (campaign_id, user_id),
    constraint fk_campaign_user_campaign_id
		foreign key (campaign_id)
        references campaign(campaign_id),
	constraint fk_campaign_user_user_id
		foreign key (user_id)
        references user(user_id)
);

create table session_user (
	session_id int not null,
    user_id int not null,
    constraint pk_session_user
        primary key (session_id, user_id),
    constraint fk_session_user_session_id
		foreign key (session_id)
        references session(session_id),
	constraint fk_session_user_user_id
		foreign key (user_id)
        references user(user_id)
);

insert into role (`name`) values
    ('USER'),
    ('ADMIN');


delimiter //
create procedure set_known_good_state()
begin
	delete from user_role;
    delete from user_schedule;
    alter table user_schedule auto_increment = 1;
    delete from session_user;
    delete from campaign_user;
    delete from session;
	alter table session auto_increment = 1;
    delete from campaign;
    alter table campaign auto_increment = 1;
    delete from user;
    alter table user auto_increment = 1;

-- database test
insert into user (username, password_hash, city, state, disabled, `description`) values
	("bob", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", "LA", "california", 0, "fake account"),
    ("dale", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", "Sacramento", "california", 0, "fake account");
    
insert into campaign (user_id, `name`, `description`, `type`, city, state, session_count, max_players) values
	(1, "My DnD", "Fake DnD campaign.", "DnD", "LA", "California", 3, 5),
    (1, "My Other DnD", "Fake DnD campaign.", "DnD", "LA", "California", 2, 5);
    
insert into session (campaign_id, start_date, end_date) values
	(1, '2003-03-10 12:00:00.000', '2003-03-14 12:00:00.000'),
    (1, '2003-04-01 12:00:00.000', '2003-04-02 12:00:00.000'),
    (1, '2003-04-03 12:00:00.000', '2003-04-04 12:00:00.000'),
    (2, '2003-04-05 12:00:00.000', '2003-04-06 12:00:00.000'),
    (2, '2003-04-07 12:00:00.000', '2003-04-08 12:00:00.000');
    
insert into user_schedule (user_id, session_id, start_date, end_date) values
	(1, 1, '2003-03-10 12:00:00.000', '2003-03-14 12:00:00.000'),
    (1, 2, '2003-04-01 12:00:00.000', '2003-04-02 12:00:00.000'),
    (1, 3, '2003-04-03 12:00:00.000', '2003-04-04 12:00:00.000'),
    (1, 4, '2003-04-05 12:00:00.000', '2003-04-06 12:00:00.000'),
    (1, 5, '2003-04-07 12:00:00.000', '2003-04-08 12:00:00.000');
    
insert into campaign_user (campaign_id, user_id) values
	(1, 2),
    (2, 2);

insert into session_user (session_id, user_id) values
	(1, 2),
    (2, 2),
    (3, 2),
    (4, 2),
    (5, 2);

insert into user_role(user_id, role_id) values
	(1, 1),
    (2, 2);

    
end //
delimiter ;




call set_known_good_state(); 