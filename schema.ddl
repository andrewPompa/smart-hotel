;             
CREATE USER IF NOT EXISTS SA SALT '4f9cf655cb42bffa' HASH 'b6e8be0347a8ff708b5bfa325fd8e99210b29d9e6e214fc001f244e26843e46a' ADMIN;           
CREATE SEQUENCE PUBLIC.HIBERNATE_SEQUENCE START WITH 11;      
CREATE CACHED TABLE PUBLIC.CHANGED_PRICE_PERIOD(
    ID BIGINT NOT NULL,
    FROM_DAY DATE,
    PRICE_FACTOR DOUBLE NOT NULL,
    TO_DAY DATE
);         
ALTER TABLE PUBLIC.CHANGED_PRICE_PERIOD ADD CONSTRAINT PUBLIC.CONSTRAINT_F PRIMARY KEY(ID);   
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.CHANGED_PRICE_PERIOD;    
CREATE CACHED TABLE PUBLIC.CLIENT(
    ID BIGINT NOT NULL,
    CITY VARCHAR(255),
    DEBT DOUBLE NOT NULL,
    FIRST_NAME VARCHAR(255) NOT NULL,
    FLAT_NUMBER VARCHAR(255),
    LAST_NAME VARCHAR(255) NOT NULL,
    LOGIN VARCHAR(255) NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    STREET VARCHAR(255)
);
ALTER TABLE PUBLIC.CLIENT ADD CONSTRAINT PUBLIC.CONSTRAINT_7 PRIMARY KEY(ID); 
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.CLIENT;  
CREATE CACHED TABLE PUBLIC.RECEPTIONIST(
    ID BIGINT NOT NULL,
    FIRST_NAME VARCHAR(255) NOT NULL,
    LAST_NAME VARCHAR(255) NOT NULL,
    LOGIN VARCHAR(255) NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL
);      
ALTER TABLE PUBLIC.RECEPTIONIST ADD CONSTRAINT PUBLIC.CONSTRAINT_D PRIMARY KEY(ID);           
-- 2 +/- SELECT COUNT(*) FROM PUBLIC.RECEPTIONIST;            
CREATE CACHED TABLE PUBLIC.RESERVATION(
    ID BIGINT NOT NULL,
    FROM_DAY DATE NOT NULL,
    ROOM_PRICE DOUBLE NOT NULL,
    TO_DAY DATE NOT NULL,
    CLIENT BIGINT NOT NULL,
    RECEPTIONIST BIGINT
);           
ALTER TABLE PUBLIC.RESERVATION ADD CONSTRAINT PUBLIC.CONSTRAINT_2 PRIMARY KEY(ID);            
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.RESERVATION;             
CREATE CACHED TABLE PUBLIC.RESERVATION_ROOMS(
    RESERVATIONS_ID BIGINT NOT NULL,
    ROOMS_ID BIGINT NOT NULL
);         
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.RESERVATION_ROOMS;       
CREATE CACHED TABLE PUBLIC.ROOM(
    ID BIGINT NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    SIZE INTEGER NOT NULL,
    TYPE INTEGER NOT NULL
);         
ALTER TABLE PUBLIC.ROOM ADD CONSTRAINT PUBLIC.CONSTRAINT_26 PRIMARY KEY(ID);  
-- 8 +/- SELECT COUNT(*) FROM PUBLIC.ROOM;    
CREATE CACHED TABLE PUBLIC.SEASON_PRICE(
    ID BIGINT NOT NULL,
    FROM_DAY INTEGER NOT NULL,
    FROM_MONTH INTEGER NOT NULL,
    PRICE_FACTOR DOUBLE NOT NULL,
    TO_DAY INTEGER NOT NULL,
    TO_MONTH INTEGER NOT NULL
);       
ALTER TABLE PUBLIC.SEASON_PRICE ADD CONSTRAINT PUBLIC.CONSTRAINT_8 PRIMARY KEY(ID);           
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.SEASON_PRICE;            
ALTER TABLE PUBLIC.CLIENT ADD CONSTRAINT PUBLIC.UK_GG7OAFSUBULCAHOLSS4I5KFGL UNIQUE(LOGIN);   
ALTER TABLE PUBLIC.RECEPTIONIST ADD CONSTRAINT PUBLIC.UK_F3PRCJK3SJ7GRH6CA1OX040M9 UNIQUE(LOGIN);             
ALTER TABLE PUBLIC.ROOM ADD CONSTRAINT PUBLIC.UK_4L8MM4FQOOS6FCBX76RVQXER UNIQUE(NAME);       
ALTER TABLE PUBLIC.RESERVATION ADD CONSTRAINT PUBLIC.FK5CWHDG7LIAKX43L2ITAKSBX8S FOREIGN KEY(CLIENT) REFERENCES PUBLIC.CLIENT(ID) NOCHECK;    
ALTER TABLE PUBLIC.RESERVATION ADD CONSTRAINT PUBLIC.FK8HLKK1NOBXRO4EIINUH2NCJPP FOREIGN KEY(RECEPTIONIST) REFERENCES PUBLIC.RECEPTIONIST(ID) NOCHECK;        
ALTER TABLE PUBLIC.RESERVATION_ROOMS ADD CONSTRAINT PUBLIC.FK3909F3XR1EGHX60B7Q08NSOBJ FOREIGN KEY(ROOMS_ID) REFERENCES PUBLIC.ROOM(ID) NOCHECK;              
ALTER TABLE PUBLIC.RESERVATION_ROOMS ADD CONSTRAINT PUBLIC.FKBW4YU0XOF7K6B9TPRP8Q5XYGN FOREIGN KEY(RESERVATIONS_ID) REFERENCES PUBLIC.RESERVATION(ID) NOCHECK;
