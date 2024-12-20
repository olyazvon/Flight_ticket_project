alter session set nls_date_format='DD/MM/YYYY hh24:mi';

-- adding/removing flights(admin can add flight only for existig airport)
INSERT INTO airports VALUES('VKO','Moscow','Russia');
INSERT INTO flights VALUES('ARTHNYS','VKO','TLV','23/02/2024 13:15','23/02/2024 17:15',450,670);
Select *  from flights where flight_id='ARTHNYS';
DELETE flights Where flight_id='ARTHNYS';

--� managing the prices (increase price by 10% in econom)
UPDATE flights set Priceeconom=((Select priceeconom from flights Where flight_id='ARTHNYS')*1.1) Where flight_id='ARTHNYS';
Select *  from flights where flight_id='ARTHNYS';

--� managing the countries (where each flight can take)
Select distinct  country from airports;--Select all countries

Select * from airports where country='Poland';
Select iata from airports where country='Poland';
Select *  from flights where airport_from='WAW';
Delete airports where country='Poland';--when admin delete country all flights with this country also deleted
  --to create new country you should add atleast one airport in this country
INSERT INTO airports VALUES('WAW','Warsaw','Poland');
INSERT INTO Flights VALUES ('YX6055R' ,'WAW' ,'YWG','07/05/2024 21:58','08/05/2024 05:06',682.83,955.96);
INSERT INTO Flights VALUES ('W6E986R' ,'WAW' ,'SJU','19/06/2024 19:22','20/06/2024 07:48',900.56,1260.78);
INSERT INTO Flights VALUES ('7BHP9JR' ,'WAW' ,'HND','14/08/2024 01:08','14/08/2024 08:19',957.28,1340.19);
INSERT INTO Flights VALUES ('3UNFMVR' ,'WAW' ,'RNO','11/05/2024 00:41','11/05/2024 08:43',559.57,783.4);
INSERT INTO Flights VALUES ('GQHN39R' ,'WAW' ,'BOG','20/04/2024 23:33','21/04/2024 09:54',917.69,1284.77);
INSERT INTO Flights VALUES ('1GXF2QR' ,'WAW' ,'SFB','24/04/2024 00:57','24/04/2024 09:36',613.66,859.12);
INSERT INTO Flights VALUES ('O4GG2VR' ,'WAW' ,'SEA','08/08/2024 00:03','08/08/2024 14:30',628.94,880.52);
INSERT INTO Flights VALUES ('TO2GWDR' ,'WAW' ,'ORF','03/05/2024 09:05','03/05/2024 19:15',954.47,1336.26);
INSERT INTO Flights VALUES ('H4J2WMR' ,'WAW' ,'TXL','22/02/2024 06:13','22/02/2024 06:57',753.72,1055.21);

--� managing the number of seats in each airplane

SELECT count(seat) from seats group by flight_id;

--� adding the number of seats for each flight
SELECT seat from seats where flight_id='02EKPYM';
INSERT INTO seats VALUES ('02EKPYM', '30A', null, 0, 'Economy');
SELECT count(seat) from seats where flight_id='02EKPYM';
Delete Seats where flight_id='02EKPYM' and seat='30A';

--� at least 10 countries are required.
Select count (distinct  country) from airports;


-- delete unvalid bookings:(booking is valid for 24 hours)
DELETE FROM Administrator.Bookings WHERE BookingDate + 1 < SYSDATE;


--Select all flights with seats and more than 1 hour departure time

SELECT fl.flight_id,airport_from,airport_to, to_char(Departure, 'dd.mm.yy hh24:mi'), to_char(Arrival, 'dd.mm.yy hh24:mi'), 
       PriceEconom,PriceBusiness, s_economy.seats_left_economy, s_business.seats_left_business 
FROM Administrator.flights fl,
    (SELECT flight_id, (count(seat) - count(booked)) AS seats_left_Business
        FROM Administrator.seats WHERE TicketClass= 'Business' GROUP BY flight_id) s_business,
     (SELECT flight_id, (count(seat) - count(booked)) AS seats_left_Economy 
        FROM Administrator.seats WHERE TicketClass= 'Economy' GROUP BY flight_id) s_economy 
WHERE fl.flight_id=s_economy.flight_id and fl.flight_id=s_business.flight_id  and
     (seats_left_business > 0 OR seats_left_economy > 0) AND 
     Departure > (SElect SYSDATE + 1/24 from dual);
     
     
     --Select flight by flight_id with seats and more than 1 hour departure time

SELECT fl.flight_id,airport_from,airport_to, to_char(Departure, 'dd.mm.yy hh24:mi'), to_char(Arrival, 'dd.mm.yy hh24:mi'), 
       PriceEconom,PriceBusiness, s_economy.seats_left_economy, s_business.seats_left_business 
FROM Administrator.flights fl,
    (SELECT flight_id, (count(seat) - count(booked)) AS seats_left_Business
        FROM Administrator.seats WHERE TicketClass= 'Business' GROUP BY flight_id) s_business,
     (SELECT flight_id, (count(seat) - count(booked)) AS seats_left_Economy 
        FROM Administrator.seats WHERE TicketClass= 'Economy' GROUP BY flight_id) s_economy 
WHERE fl.flight_id=s_economy.flight_id and fl.flight_id=s_business.flight_id  and
     (seats_left_business > 0 OR seats_left_economy > 0) AND 
     Departure > (SElect SYSDATE + 1/24 from dual) and fl.flight_id='3U9QSVG';
     
     --insert new booking  into bookings:     
     
     INSERT INTO Administrator.Bookings VALUES (43255, 1499.76, TO_DATE('2024-01-28', 'YYYY-MM-DD'))

 -- insert pasenger  
INSERT INTO Administrator.PASSENGERS VALUES ('13258','iii','uuu','7G','TQUISY3')
