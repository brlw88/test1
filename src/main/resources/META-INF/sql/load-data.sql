drop database test;

create database test default character set utf8 default collate utf8_general_ci;

use test;

drop table if exists User;

create table User (
    id int(8) not null auto_increment,
    version int(4) not null default 0,
    name varchar(25),
    age int(4),
    isAdmin bit,
    createdDate timestamp,
    primary key (id)
);

insert into User (name, age, isAdmin) values ('Chris Schaefer', 35, 1);
insert into User (name, age, isAdmin) values ('Scott Tiger', 26, 0);
insert into User (name, age, isAdmin) values ('John Smith', 52, 0);
insert into User (name, age, isAdmin) values ('Peter Jackson', 72, 0);
insert into User (name, age, isAdmin) values ('Jacky Chan', 61, 0);
insert into User (name, age, isAdmin) values ('Susan Boyle', 46, 0);
insert into User (name, age, isAdmin) values ('Tinner Turner', 49, 0);
insert into User (name, age, isAdmin) values ('Lotus Notes', 26, 0);
insert into User (name, age, isAdmin) values ('Henry Dickson', 19, 0);
insert into User (name, age, isAdmin) values ('Sam Davis', 15, 0);
insert into User (name, age, isAdmin) values ('Max Beckham', 14, 0);
insert into User (name, age, isAdmin) values ('Paul Simon', 14, 0);
insert into User (name, age, isAdmin) values ('Первый пользователь', 41, 0);
insert into User (name, age, isAdmin) values ('Второй пользователь', 42, 0);
