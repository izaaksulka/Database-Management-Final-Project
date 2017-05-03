drop table if exists LACrimeData;
drop table if exists chicagoCrimeData;
drop table if exists denverCrimeData;
create table LACrimeData (incidentID serial, date text, description text, address text, zip text);
/**/
insert into LACrimeData (date, description, address, zip) values ('date 1', 'description 1', 'address 1', 'zip 1');
insert into LACrimeData (date, description, address, zip) values ('date 2', 'description 2', 'address 2', 'zip 2');
select * from LACrimeData;
/**/
create table chicagoCrimeData (incidentID serial, date text, description text, block text);
insert into chicagoCrimeData (date, description, block) values ('date 1', 'description 1', 'block 1');
insert into chicagoCrimeData (date, description, block) values ('date 2', 'description 2', 'block 2');
select * from chicagoCrimeData;
/**/
create table denverCrimeData (incidentID serial, date text, description text, address text, neighborhood text);
insert into denverCrimeData (date, description, address, neighborhood) values ('date 1', 'description 1', 'address 1', 'neighborhood 1');
insert into denverCrimeData (date, description, address, neighborhood) values ('date 2', 'description 2', 'address 2', 'neighborhood 2');
select * from denverCrimeData;