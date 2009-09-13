create table people	(
			id 				int not null auto_increment,
	 		name 			varchar(30) not null,
	 		lastname		varchar(30) not null,
	 		rg				varchar(20) not null
			cpf				varchar(11),
	 		address	 		varchar(10),
	 		fone 			varchar(7),
	 		birthday		date,
			gender 			char(1),
			status			char(1),
			
	constraint pkpeople	primary key(id, rg)
);

create table wayinout	(
			id	 				int not null auto_increment,
			id_in				int not null auto_increment, 
			id_out				int	not	null auto_increment,
			id_people 			int not null,
			id_rec_mode_in		int not null,
			id_rec_mode_out		int not null,
			id_sensor_in		int not null,
			id_sensor_out		int not null,
			db_timestamp_in		timestamp,
			sys_timestamp_in	timestamp,
			db_timestamp_out	timestamp,
			sys_timestamp_out	timestamp,
			
	constraint  pkways primary key (id, id_in, id_out)
);


create table sensor	(
			id 				int not null auto_increment,
			sensor_uuid		int not null,
			id_location		int not null,
			id_status		int not null,
			id_rec_mode		int not null,
			comment			varchar(500) not null,
			brand			varchar(100),

	constraint pksensor primary key (id, sensor_uuid)
);

create table sensorstatus(
			id 				int not null auto_increment,
			comment			varchar(500) not null,

	constraint pksensor primary key (id)
);

create table location(
			id 				int not null auto_increment,
			comment			varchar(500) not null,
			latitude		int,
			longitude		int,

	constraint pksensor primary key (id)
);


create table recognition_mode	(
			id 				int not null auto_increment,
			comment 		varchar(3000),
	
	constraint pkrec_mode primary key(id)
);


create table log	(
			 id 			int not null auto_increment,
			 comment 		varchar(3000),
	
	constraint pklog primary key(id)
);

--Constraints for wayin table:
--		id_people
alter table entrance add constraint fkid_people 
      foreign key (id_people) references people(id) 
	ON DELETE CASCADE ON UPDATE CASCADE;
	
alter table entrance add constraint fkid_rec_mode 
      foreign key (id_rec_mode) references people(recognition_mode)
	ON DELETE CASCADE ON UPDATE CASCADE;


