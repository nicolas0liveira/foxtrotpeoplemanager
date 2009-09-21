CREATE TABLE fingerprints(
 								id 						int NOT NULL AUTO_INCREMENT,
 								griaule_template 		blob,
 								id_hand 				int NOT NULL,
 								id_finger				int NOT NULL,
 								id_people				int NOT NULL,
				
 					constraint  fingerprints primary key (id)
)