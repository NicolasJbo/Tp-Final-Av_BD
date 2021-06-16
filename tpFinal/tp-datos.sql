
#-------------------------------------------->> CARGA DE DATOS <<--------------------------------------------
INSERT INTO tariffs(NAME,amount) VALUES ("A", 10), ("B", 5),("U", 54),("L", 10),("H", 30),("GH", 52),("V", 1);

INSERT INTO meter_models(NAME) VALUES("Modelo1"),("Modelo2"),("modelo3"),("MoDeLo4"),("MODELO5");
INSERT INTO meter_brands(NAME) VALUES("Samsung"),("Nokia"),("ALcatel"),("Ericsson"),("Motorola");
INSERT INTO energy_meters(serial_number,id_brand, id_model, pass_word) VALUES("001",1,1,"1234"), ("002",1,2,"1234"),
("003",2,3,"1234"),("004",4,2,"1234"),("005",3,3,"1234"),("007",5,5,"1234");`energy_meters`

INSERT INTO users(is_client, mail, PASSWORD) VALUES(FALSE, "admin@gmail.com", "1234"),(TRUE, "lautaro@gmail.com", "1234"),(TRUE, "nicolas@gmail.com", "1234"),	
						    (TRUE, "DArio@gmail.com", "1234"),(TRUE, "Juan@gmail.com", "1234"),(TRUE, "Carlos@gmail.com", "1234"),(TRUE, "aaa@gmail.com", "1234");

INSERT INTO clients(birthday,dni,last_name,NAME,id_user) VALUES("09-03-2001 00:00:00 000000","43317286","Fullone","Lautaro", 1 ),
								   ( "02-01-1998 00:00:00 000000","40875465","Bertuccio","Nicolas",2),
								   ('0000-00-00 00:00:00.000000','416611234','Perez','Carlos',3),
								   ('0000-00-00 00:00:00.000000','12345678','Martin','Juan',4),
								('0000-00-00 00:00:00.000000','22222222','Angel','Dario',5),
								('0000-00-00 00:00:00.000000','11111111','Medina','Lucas',6);
								   
							   
INSERT INTO residences(street, NUMBER, FLOOR, apartament,id_energy_meter,id_tariff,id_client) VALUES("España", 3571, NULL,NULL, 1,1,1),
												    ("Corrientes", 2804, 4,"F", 2,2,2),
												    ("Las Heras", 2341, 2,"C", 3,1,3),
												    ("Colon", 111, NULL,NULL, 4,4,3),
												    ("Cordoba", 3152, NULL,NULL, 5,2,4),
												    ("San luis", 2, NULL,NULL, 6,1,5); 

#LAS MEDICIONES SE VAN ENVIANDO DE MANERA ACUMULATIVA
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:00:00 000000", 5, 1), ("2021-06-13 00:05:00 000000", 10, 1);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:10:00 000000", 20, 1), ("2021-06-13 00:15:00 000000", 45, 1);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:00:00 000000", 10, 2), ("2021-06-13 00:00:00 000000", 15, 2);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-07-13 00:00:00 000000", 50, 1),("2021-07-13 00:12:00 000000", 72, 1) ;
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:00:00 000000", 25, 2), ("2021-06-13 00:00:00 000000", 35, 2);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-07-13 00:20:00 000000", 80, 1),("2021-07-13 00:30:00 000000", 90, 1) ;
#Casa 5 --> cliente 4 
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-07-13 00:20:00 000000", 5, 5),("2021-07-13 00:20:00 000001", 10, 5),("2021-07-13 00:20:00 000002", 15, 5),("2021-07-13 00:20:00 000003", 20, 5) 
,("2021-07-13 00:22:00 000000", 25, 5),("2021-07-13 00:30:00 000000", 30, 5),("2021-07-13 00:35:00 000000", 35, 5),("2021-07-14 00:20:00 000000", 40, 5),("2021-07-14 00:30:00 000000", 45, 5),
("2021-07-14 16:20:00 000000", 60, 5),("2021-07-16 00:20:00 000000", 70, 5);
#("2021-07-16 11:20:00 000000", 72, 5),("2021-07-18 14:20:00 000000", 75, 5),("2021-07-18 18:20:00 000000", 80, 5),("2021-07-19 21:20:00 000000", 85, 5),("2021-07-20 22:50:00 000000", 90, 5);
#Casa 6 --> cliente 5 
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-07-13 00:20:00 000000", 5, 6),("2021-07-13 00:20:00 000001", 10, 6),("2021-07-13 00:20:00 000002", 15, 6),("2021-07-13 00:20:00 000003", 20, 6) 
,("2021-07-13 00:22:00 000000", 25, 6),("2021-07-13 00:30:00 000000", 30, 6),("2021-07-13 00:35:00 000000", 35, 6),("2021-07-14 00:20:00 000000", 40, 6),("2021-07-14 00:30:00 000000", 45, 6),
("2021-07-14 16:20:00 000000", 60, 6),("2021-07-16 00:20:00 000000", 100, 6);


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