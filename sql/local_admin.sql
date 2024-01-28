ALTER SESSION SET CURRENT_SCHEMA = administrator;
 CREATE user admin IDENTIFIED BY 1234;
GRANT CREATE SESSION TO admin;
GRANT CREAT;

 CREATE user clients IDENTIFIED BY 123;
GRANT CREATE SESSION TO clients;

--roles:
CREATE ROLE cl;
CREATE ROLE admin_role;

--privilages
GRANT SELECT ON AIRPORTS TO cl;
GRANT SELECT ON FLIGHTS TO cl;
GRANT SELECT ON SEATS TO cl;
GRANT UPDATE (booked) on seats to cl;
GRANT UPDATE (bought) on seats to cl;
GRANT Select,UPDATE,Insert on users to cl;
GRANT SELECT,UPDATE,INSERT,delete on passengers to cl;
GRANT SELECT,UPDATE,Insert,Delete on bookings to cl;


GRANT SELECT,UPDATE,INSERT,delete ON AIRPORTS TO admin_role;
GRANT SELECT,UPDATE,INSERT,delete ON FLIGHTS TO admin_role;
GRANT SELECT,UPDATE,INSERT,delete ON SEATS TO admin_role;
GRANT SELECT,UPDATE,INSERT,delete on users to admin_role;
GRANT SELECT,UPDATE,Insert,Delete on passengers to admin_role;

GRANT cl TO clients;
GRANT admin_role to admin;

GRANT CREATE SESSION TO clients;
GRANT CREATE SESSION TO admin;




