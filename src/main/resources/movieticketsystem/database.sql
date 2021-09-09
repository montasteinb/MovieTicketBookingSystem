drop database if exists MovieBooking;
SET GLOBAL time_zone = '+3:00';

create database if not exists MovieBooking;

use MovieBooking;

create table if not exists movies(
    id int not null auto_increment,
    title varchar(50) not null,
    genre varchar(50) not null,
    released_at year not null,
    status varchar(20),
    primary key(id)
    );

create table if not exists users(
    id int not null auto_increment,
    username varchar(50) not null,
    password text not null,
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    admin boolean,
    primary key(id)
    );
create table if not exists rooms(
    id int not null auto_increment,
    number int,
    primary key(id)
    );

create table if not exists bookings(
    id int not null auto_increment,
    movie_id int not null,
    room_id int not null,
    date varchar (20),
    primary key(id),
    foreign key(movie_id) references movies(id),
    foreign key(room_id) references rooms(id)
    );

create table if not exists screenings(
    id int not null auto_increment,
    date varchar(50) not null,
    movie_id int not null,
    foreign key(id) references movies(id)
    );