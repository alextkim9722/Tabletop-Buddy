drop database if exists tabletop_tables_test;
create database tabletop_tables_test;
use tabletop_tables_test;

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
    session_count int not null,
    max_players int not null,
    constraint fk_campaign_user_id
        foreign key (user_id)
        references user(user_id)
);

create table session (
	session_id int primary key auto_increment,
    campaign_id int not null,
    start_date date not null,
    end_date date not null,
    constraint fk_session_campaign_id
        foreign key (campaign_id)
        references campaign(campaign_id)
);

create table user_schedule (
	user_schedule_id int primary key auto_increment,
    user_id int not null,
    session_id int,
    start_date date not null,
    end_date date not null,
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
	delete from user;
    alter table user auto_increment = 1;
    delete from campaign;
    alter table campaign auto_increment = 1;
	delete from session;
	alter table session auto_increment = 1;
    delete from user_schedule;
    alter table user_schedule auto_increment = 1;
    delete from session_user;
    delete from campaign_user;
    
-- database test
insert into user (username, password_hash, city, state, disabled, `description`) values
	("bob", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", "LA", "california", 0, "fake account"),
    ("dale", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", "Sacramento", "california", 0, "fake account");
    
insert into campaign (user_id, `name`, `description`, `type`, city, state, session_count, max_players) values
	(1, "My DnD", "Fake DnD campaign.", "DnD", "LA", "California", 3, 5),
    (1, "My Other DnD", "Fake DnD campaign.", "DnD", "LA", "California", 2, 5);
    
insert into session (campaign_id, start_date, end_date) values
	(1, '2003-03-10', '2003-03-14'),
    (1, '2003-04-01', '2003-04-02'),
    (1, '2003-04-03', '2003-04-04'),
    (2, '2003-04-05', '2003-04-06'),
    (2, '2003-04-07', '2003-04-08');
    
insert into campaign_user (campaign_id, user_id) values
	(1, 2),
    (2, 2);

insert into session_user (session_id, user_id) values
	(1, 2),
    (2, 2),
    (3, 2),
    (4, 2),
    (5, 2);
    
end;
    
/*
select
	s.start_date,
    s.end_date,
    u.username
from session_user su
inner join user u on su.user_id = u.user_id
inner join session s on su.session_id = s.session_id;

select
	s.start_date,
    s.end_date,
    c.name
from session s
inner join campaign c on s.campaign_id = c.campaign_id;
*/
