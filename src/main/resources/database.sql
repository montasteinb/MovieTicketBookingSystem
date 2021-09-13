drop database if exists MovieBooking;
SET GLOBAL time_zone = '+3:00';

create database if not exists MovieBooking;

use MovieBooking;

create table if not exists rooms(
    number int,
    capacity int,
    primary key(number)
    );

create table if not exists movies(
    id int not null auto_increment,
    name varchar(50) not null,
    genre varchar(50) not null,
    primary key(id)
    );

create table if not exists users(
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    username varchar(50) not null,
    password text not null,
    isAdmin boolean,
    primary key(username)
    );


create table if not exists screenings(
    id int not null auto_increment,
    date datetime,
    attendance int,
    roomNo int,
    movie_id int,
    primary key(id),
    foreign key(movie_id) references movies(id),
    foreign key(roomNo) references rooms(number)
    );

create table if not exists bookings(
    id int not null auto_increment,
    movie_id int,
    roomNo int,
    username varchar(50),
    date datetime,
    primary key(id),
    foreign key(movie_id) references movies(id),
    foreign key(roomNo) references screenings(roomNo),
    foreign key(username) references users(username)
    );

update screenings set attendance = 0 where id;

INSERT INTO `moviebooking`.`rooms` (`number`, `capacity`) VALUES ('1', '10');
INSERT INTO `moviebooking`.`rooms` (`number`, `capacity`) VALUES ('2', '15');
INSERT INTO `moviebooking`.`rooms` (`number`, `capacity`) VALUES ('3', '20');
INSERT INTO `moviebooking`.`rooms` (`number`, `capacity`) VALUES ('4', '30');
