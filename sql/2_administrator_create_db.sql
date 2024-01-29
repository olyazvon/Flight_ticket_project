ALTER SESSION SET CURRENT_SCHEMA = administrator;

Drop TABLE Users;
Drop TABLE Passengers;
Drop TABLE seats;
Drop TABLE flights;
Drop TABLE airports;
Drop TABLE bookings;

--create tables

@@'C:\Users\admin\Documents\MSSE\Project\seats.sql';
@@'C:\Users\admin\Documents\MSSE\Project\users_passengers.sql';
@@'C:\Users\admin\Documents\MSSE\Project\airports.sql';
@@'C:\Users\admin\Documents\MSSE\Project\flights.sql' ;
@@'C:\Users\admin\Documents\MSSE\Project\bookings.sql';

select count(*) from users;
select count(*) from passengers;
select count(*) from airports;
select count(*) from flights;
select count(*) from seats;
select count(*) from bookings;