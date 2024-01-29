DROP USER administrator CASCADE;

CREATE user administrator IDENTIFIED BY 12345;

grant create session, resource, CREATE VIEW to administrator;
