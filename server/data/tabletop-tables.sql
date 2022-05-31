drop database if exists tabletop_tables;
create database tabletop_tables;
use tabletop_tables;

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
