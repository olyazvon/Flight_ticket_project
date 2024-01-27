--create tables 

Drop TABLE Users;
Drop TABLE Passengers;
Drop TABLE seats;
Drop TABLE airports;
Drop TABLE flights;
Drop TABLE bookings;


@@'C:\Users\admin\Documents\MSSE\Project\seats.sql';
@@'C:\Users\admin\Documents\MSSE\Project\Users_Passengers.sql';
@@'C:\Users\admin\Documents\MSSE\Project\airports.sql';
@@'C:\Users\admin\Documents\MSSE\Project\flights.sql' ;
@@'C:\Users\admin\Documents\MSSE\Project\Bookings.sql';


select count(*) from seats;