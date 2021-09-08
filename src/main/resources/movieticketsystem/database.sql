drop database if exists MovieApp;
SET GLOBAL time_zone = '+3:00';

create database if not exists MovieApp;

use MovieApp;

create table if not exists movies(
	 id int not null auto_increment,
     title varchar(50) not null,
     genre varchar(50) not null,
     released_at year not null,
     primary key(id)
);

create table if not exists users(
     username varchar(50) not null,
     firstName varchar(50) not null,
     lastName varchar(50) not null,
     admin boolean

);

create table if not exists booking

SELECT * FROM movies;