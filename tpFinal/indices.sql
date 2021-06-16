/*
B-Tree --> BUSQUEDAS POR RANGO
	
HASH ---> BUSQUEDA VALOR UNICO DIFERENTE


B-TREE
				EDAD
				50
		30	40		60   90

	15	    35


HASH
NOMBRE - APELLIDO
NICO	PEREZ	 --> 1 REGISTRO
NICO	BERTUCCIO --> 2 REGISTROS



HASH CLIENTE
NOMBRE , APELLIDO --> INDICE (DNI)
CREATE UNIQUE INDEX idx_unq_client_dni ON clients (dni) using hash;


HASH MEDIDOR
SERIAL NUMBER --> INDICE(ID) ó ID--> INDICE(SERIAL NUMBER)
CREATE UNIQUE INDEX idx_unq_energy_meter_serialNumber ON energy_meters (serial_number) using hash;

HASH FECHA
ID , KW , AMOUNT ---> INDICE(FECHA INICIAL)
CREATE UNIQUE INDEX idx_unq_measure_date ON measures (date) using hash;




*/
#BORRAR INDICE
#alter table clients drop index idx_unq_residende_idClient



CREATE UNIQUE INDEX idx_unq_client_dni ON clients (dni) USING HASH;
CREATE  INDEX idx_unq_client_name_lastname ON clients (NAME,last_name) USING HASH;
CREATE  INDEX idx_unq_measure_date ON measures (DATE) USING HASH;
CREATE  INDEX idx_unq_user_id ON clients (id_user) USING HASH;


SELECT * FROM bills


CREATE VIEW report AS

 SELECT c.name AS NAME ,c.last_name AS LastName,c.dni AS DNI,c.id_user AS USER,
	e.serial_number AS MeterSerialNum,
	m.kw AS KW,m.price AS Price,m.date AS DATE
FROM clients c
JOIN residences r
ON c.id=r.id_client
JOIN energy_meters e
ON r.id_energy_meter = e.id
JOIN measures m
ON m.id_residence = r.id;





