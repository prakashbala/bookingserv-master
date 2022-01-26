drop table if exists BOOKINGS;
drop table if exists ADDRESSES;

create table BOOKINGS(
  ID int not null,
  FIRST_NAME varchar(255) not null,
  LAST_NAME varchar(255) not null,
  DATE_OF_BIRTH date not null,
  CHECK_IN TIMESTAMP not null,
  CHECK_OUT TIMESTAMP not null,
  TOTAL_PRICE NUMERIC not null,
  DEPOSIT NUMERIC not null,
  ADDRESS_ID int not null,
  PRIMARY KEY ( ID )
);

create table ADDRESSES (
  ID int not null,
  LINE1 varchar(255) not null,
  LINE2 varchar(150),
  CITY varchar(255) not null,
  STATE varchar(255) not null,
  COUNTRY varchar(255) not null,
  ZIP_CODE varchar(12) not null,
  PRIMARY KEY (ID)
)