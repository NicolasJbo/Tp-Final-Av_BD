
#-------------------------------------------->> CARGA DE DATOS <<--------------------------------------------
INSERT INTO tariffs(NAME,amount) VALUES ("A", 10), ("B", 5);

INSERT INTO meter_models(NAME) VALUES("Modelo1"),("Modelo2");
INSERT INTO meter_brands(NAME) VALUES("Samsung"),("Nokia");
INSERT INTO energy_meters(serial_number,id_brand, id_model, pass_word) VALUES("001",1,1,"1234"), ("002",1,2,"1234");	

INSERT INTO users(is_client, mail, PASSWORD) VALUES(FALSE, "admin@gmail.com", "1234"),(TRUE, "lautaro@gmail.com", "1234"),(TRUE, "nicolas@gmail.com", "1234");
INSERT INTO clients(NAME, last_name, id_user, dni, birthday) VALUES("Lautaro", "Fullone", 1, "43317286","09-03-2001 00:00:00 000000"),
								   ("Nicolas", "Bertuccio", 2, "40875465","02-01-1998 00:00:00 000000");
								   
							   
INSERT INTO residences(street, NUMBER, FLOOR, apartament,id_energy_meter,id_tariff,id_client) VALUES("España", 3571, NULL,NULL, 1,1,1),
												    ("Corrientes", 2804, 4,"F", 2,1,2); 

#LAS MEDICIONES SE VAN ENVIANDO DE MANERA ACUMULATIVA
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:00:00 000000", 5, 1), ("2021-06-13 00:05:00 000000", 10, 1);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:10:00 000000", 20, 1), ("2021-06-13 00:15:00 000000", 45, 1);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:00:00 000000", 10, 2), ("2021-06-13 00:00:00 000000", 15, 2);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-07-13 00:00:00 000000", 50, 1),("2021-07-13 00:12:00 000000", 72, 1) ;
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:00:00 000000", 25, 2), ("2021-06-13 00:00:00 000000", 35, 2);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-07-13 00:20:00 000000", 80, 1),("2021-07-13 00:30:00 000000", 90, 1) ;

#-------------------------------------------->> CARGA DE DATOS <<--------------------------------------------


SELECT * FROM residences;
SELECT * FROM meter_models;
SELECT * FROM meter_brands;
SELECT * FROM users;
SELECT * FROM clients;
SELECT * FROM energy_meters;
SELECT * FROM residences;
SELECT * FROM bills;
SELECT * FROM measures;