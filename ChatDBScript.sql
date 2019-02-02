-- Script to create DB
create database chatDB;
create table Message(
	identifier binary(255) primary key not null,
	sender binary(255),
	recipient binary(255),
	textMsg varchar(255),
	dateMsg Date
);
create table User(
	userId  binary(255) primary key not null,
	nick varchar(255),
	statusText varchar(255),
	userStatus varchar(255),
    userPassword int(11)
);
-- Script to check DB
use chatDB;
show tables;
select * from message;
select * from user;
describe  message;
describe user;
drop table message;
drop table user;
-- Http request example
-- localhost:8080/createUser
-- {
-- "userId":"eab33e9f-18d1-4ec2-b153-5a94da6e0bf7",
-- "nick":"root",
-- "statusText":"to ja admin",
-- "userStatus":"away",
-- "userPassword":"admin",
-- "logStatus":"false"
-- }