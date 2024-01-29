
DROP TABLE USERS;
DROP TABLE PASSENGERS;

CREATE TABLE USERS(
login VARCHAR2(10) Primary key,
password VARCHAR2(10),
CardNumber VARCHAR2(16),
CardCVV VARCHAR(3),
CardDate Date,
CardHolderName VARCHAR2(30),
BookingNumber Number (7),
User_id VARCHAR2(10));

CREATE TABLE PASSENGERS(
passport VARCHAR2(10), 
LastName VARCHAR2(30) not null,
FirstName VARCHAR2(20) not null,
Seat VARCHAR2(4),
Flight CHAR(7),
Primary key (passport,seat,Flight),
CONSTRAINT fk FOREIGN KEY(Flight, Seat) REFERENCES SEATS(Flight_id, Seat));

--Insert into passengers VALUES('123','o','lya','1C','02EKPYM');
