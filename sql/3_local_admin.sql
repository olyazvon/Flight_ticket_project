ALTER SESSION SET CURRENT_SCHEMA = administrator;

DROP USER admin;
DROP ROLE admin_role;
CREATE user admin IDENTIFIED BY 1234;

DROP USER clients;
DROP ROLE cl_role;
CREATE user clients IDENTIFIED BY 123;

--roles:
CREATE ROLE cl_role;
CREATE ROLE admin_role;

--privilages
GRANT SELECT ON AIRPORTS TO cl_role;
GRANT SELECT ON FLIGHTS TO cl_role;
GRANT SELECT ON SEATS TO cl_role;
GRANT UPDATE (booked) on seats to cl_role;
GRANT UPDATE (bought) on seats to cl_role;
GRANT Select,UPDATE,Insert on users to cl_role;
GRANT SELECT,UPDATE,INSERT,delete on passengers to cl_role;
GRANT SELECT,UPDATE,Insert,Delete on bookings to cl_role;


GRANT SELECT,UPDATE,INSERT,delete ON AIRPORTS TO admin_role;
GRANT SELECT,UPDATE,INSERT,delete ON FLIGHTS TO admin_role;
GRANT SELECT,UPDATE,INSERT,delete ON SEATS TO admin_role;
GRANT SELECT,UPDATE,INSERT,delete on users to admin_role;
GRANT SELECT,UPDATE,Insert,Delete on passengers to admin_role;

GRANT cl_role TO clients;
GRANT admin_role to admin;

GRANT CREATE SESSION TO clients;
GRANT CREATE SESSION TO admin;
