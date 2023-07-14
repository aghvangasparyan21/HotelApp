# HotelApp
To run this program, you only need to run the following queries in your MySQL Local Server and then put the username and password of your local server in the field to connect to the HotelApp class database.

create database Hotel;

Use Hotel;

CREATE TABLE rooms (
   id INT PRIMARY KEY AUTO_INCREMENT,
   type VARCHAR(20),
   isBooked BOOLEAN
);

CREATE TABLE customers (
   id INT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(50),
   email VARCHAR(50)
);

CREATE TABLE bookings (
   id INT PRIMARY KEY AUTO_INCREMENT,
   roomId INT,
   customerId INT
   startDate DATE,
   endDate DATE,
   FOREIGN KEY (roomId) REFERENCES rooms(id),
   FOREIGN KEY (customerId) REFERENCES customers(id)
);
