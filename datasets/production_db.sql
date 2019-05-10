
CREATE DATABASE book_advisor_db_production;
CREATE USER book_advisor_user WITH PASSWORD '7fRnJmnZ';

ALTER ROLE book_advisor_user SET client_encoding TO 'utf8';
ALTER ROLE book_advisor_user SET default_transaction_isolation TO 'read committed';
ALTER ROLE book_advisor_user SET timezone TO 'UTC';

GRANT ALL PRIVILEGES ON DATABASE book_advisor_db_production TO book_advisor_user;
