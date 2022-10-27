create table useraccount (
   userid varchar2(10) primary key,
   password varchar2(15),
   username varchar2(20),
   phoneno varchar2(20),
   postcode varchar2(7),
   address varchar2(30),
   email varchar2(50),
   birthday date
);

select * from useraccount