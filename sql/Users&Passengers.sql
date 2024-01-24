
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
    User_id VARCHAR2(10)
);

INSERT INTO USERS(login,password)VALUES('ol',1234);
select * from Users;
DELETE FROM users WHERE login    ='ol';
Select count(*) from users
where login  ='oo';
select * from bookings; where bookingDate+1<SYSDATE;

CREATE TABLE PASSENGERS(
    passport VARCHAR2(10),
    LastName VARCHAR2(30),
    FirstName VARCHAR2(20),
    Seat VARCHAR2(4),
    Flight VARCHAR2(7)
);
