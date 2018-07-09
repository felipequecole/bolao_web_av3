drop table Palpite; 
create table Palpite
(
id integer not null generated always as identity (start with 1, increment by 1),
campeao varchar(256) not null,
vice varchar(256) not null,
terceiro varchar(256) not null, 
quarto varchar(256) not null, 
palpiteiro integer references Usuario (id)
);