`users`SELECT * FROM clients;
SELECT * FROM users;
SELECT * FROM residences;
SELECT * FROM energy_meters;

INSERT INTO bills (is_paid, initial_medition, initial_date, final_medition, final_date, total_energy, 
		   final_amount, expiration_date, id_residence)
VALUES(FALSE, 0.5, "2021-01-12 17:35:20.111111", 13.5, "2021-01-27 17:35:20.111111", 40, 1500, "2021-12-12 17:35:20.111111",1), #lau
      (TRUE, 0.2, "2021-01-15 17:35:20.111111", 7.4, "2021-01-30 17:35:20.111111", 23, 877, "2021-12-12 17:35:20.111111",1), #lau
      (FALSE, 0.7, "2021-02-20 17:35:20.111111", 22.0, "2021-03-03 17:35:20.111111", 56, 769, "2021-12-12 17:35:20.111111",1) #lau
      /*(FALSE, 2.5, "2021-02-04 17:35:20.111111", 71.8, "2021-02-25 17:35:20.111111", 90, 3200, "2021-12-12 17:35:20.111111",3), #lau
      (FALSE, 2.5, "2021-03-15 17:35:20.111111", 82.3, "2021-03-29 17:35:20.111111", 55, 456, "2021-12-12 17:35:20.111111",3), #lau
      (FALSE, 1.9, "2021-01-02 17:00:00.111111", 53.1, "2021-01-17 17:00:00.111111", 37, 550, "2021-12-12 17:00:00.111111",5), #valen
      (FALSE, 1.5, "2021-02-10 17:00:00.111111", 42.7, "2021-02-27 17:00:00.111111", 26, 955, "2021-12-12 17:00:00.111111",5) #valen*/
       
SELECT id,is_paid, id_residence, initial_medition, initial_date, final_medition, final_date, total_energy, final_amount, expiration_date
FROM bills;

INSERT INTO measures(kw, id_residence, DATE) 
VALUES(12, 1, "2021-05-20 17:00:00.111111"), (85, 1, "2021-05-20 17:10:00.111111"), (22, 1, "2021-05-20 17:05:00.111111"),(100, 1, "2021-05-30 17:30:00.111111"), #lau(1)
      (49, 1, "2021-05-20 17:00:00.111111"), (50, 1, "2021-05-20 17:03:00.111111"), (11, 1, "2021-05-20 17:10:00.111111"), #lau(3)
      (36, 1, "2021-05-20 17:00:00.111111"), (67, 1, "2021-05-20 17:03:00.111111"), (30, 1, "2021-05-20 17:15:00.111111")  #valen(5)
			
INSERT INTO measures(total, id_residence, DATE) 
VALUES(
#------------------------------------------------------------------------------------------
#PROGRAMACION PUNTO 2 -> Consulta de facturas por rango de fechas, falta unir con cliente

SELECT b.id, b.is_paid, b.initial_medition, b.initial_date, b.final_medition, 
       b.final_date, b.total_energy, b.final_amount, b.expiration_date,
       b.id_tariff, b.id_energy_meter, b.id_residence
FROM bills AS b
INNER JOIN residences AS r
ON b.id_residence = r.id
INNER JOIN clients AS c
ON c.id = r.id_client
WHERE c.id = 6 AND b.initial_date BETWEEN "2021-02-01" AND "2021-03-01"

DELIMITER //
CREATE PROCEDURE getClientBillsByDates(IN client_id INT,IN first_date DATETIME, IN last_date DATETIME)
BEGIN 
    IF( first_date > last_date ) THEN
		SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT='The entered dates are invalid';
	ELSE 
		SELECT b.id, b.is_paid, b.initial_medition, b.initial_date, b.final_medition, 
		       b.final_date, b.total_energy, b.final_amount, b.expiration_date,
		       b.id_tariff, b.id_energy_meter, b.id_residence
		FROM bills AS b
		INNER JOIN residences AS r
		ON b.id_residence = r.id
		INNER JOIN clients AS c
		ON c.id = r.id_client
		WHERE c.id = client_id AND b.initial_date BETWEEN first_date AND last_date;
	END IF;
END;
//

CALL getClientBillsByDates(1,"2021-01-01" , "2021-03-01");



#------------------------------------------------------------------------------------------

#------------------------------------------------------------------------------------------
#PROGRAMACION PUNTO 3 -> Consulta de facturas por rango de fechas, falta unir con cliente

SELECT b.id, b.is_paid, b.initial_medition, b.initial_date, b.final_medition, 
       b.final_date, b.total_energy, b.final_amount, b.expiration_date,
       b.id_tariff, b.id_energy_meter, b.id_residence
FROM bills AS b
INNER JOIN residences AS r
ON b.id_residence = r.id
INNER JOIN clients AS c
ON c.id = r.id_client
WHERE c.id = 6 AND b.initial_date BETWEEN "2021-02-01" AND "2021-03-01"

DROP PROCEDURE IF EXISTS getClientUnpaidBills;
DELIMITER //
CREATE PROCEDURE getClientUnpaidBills(IN client_id INT)
BEGIN 
	SELECT b.id, b.is_paid, b.initial_medition, b.initial_date, b.final_medition, 
	       b.final_date, b.total_energy, b.final_amount, b.expiration_date,
	       b.id_tariff, b.id_energy_meter, b.id_residence
	FROM bills AS b
	INNER JOIN residences AS r
	ON b.id_residence = r.id
	INNER JOIN clients AS c
	ON c.id = r.id_client
	WHERE c.id = client_id AND b.is_paid = FALSE;
END;
//

SELECT * FROM residences;
SELECT * FROM bills;

CALL getClientUnpaidBills(6);

#------------------------------------------------------------------------------------------
#PROGRAMACION PUNTO 4 -> Consulta consumo pr rango de fechas

DELIMITER //
DROP PROCEDURE IF EXISTS getClientTotalEnergyAndAmountByDates
CREATE PROCEDURE getClientTotalEnergyAndAmountByDates(IN client_id INT,IN first_date DATETIME, IN last_date DATETIME)
BEGIN 
	SELECT c.name AS nameClient, c.last_name AS lastnameClient,
		SUM(b.total_energy)AS totalEnergy,SUM(b.final_amount) AS totalAmount
	       
	FROM bills AS b
	INNER JOIN residences AS r
	ON b.id_residence = r.id
	INNER JOIN clients AS c
	ON c.id = r.id_client
	WHERE c.id = client_id AND b.initial_date BETWEEN first_date AND last_date;
END;
DELIMITER //

CALL getClientTotalEnergyAndAmountByDates(1, "2021-01-01", "2021-03-01")

#------------------------------------------------------------------------------------------
#PROGRAMACION PUNTO 5 -> Consulta de mediciones por rango de fechas

SELECT * FROM measures
SELECT * FROM residences
SELECT * FROM clients

SELECT * 
FROM measures AS m
INNER JOIN residences AS r
ON r.id = m.id_residence
INNER JOIN clients AS c
ON c.id = r.id_client
WHERE c.id = 6 AND m.date BETWEEN "2021-05-20 17:00:00.111111" AND "2021-05-20 17:05:00.111111";

DROP PROCEDURE IF EXISTS getClientMeasuresByDates;
DELIMITER//
CREATE PROCEDURE getClientMeasuresByDates(IN client_id INT,IN first_date DATETIME, IN last_date DATETIME)
BEGIN 
	SELECT m.date AS measureDate, m.total AS measureTotal, r.street AS residenceStreet , r.number AS residenceNumber
	FROM measures AS m
	INNER JOIN residences AS r
	ON r.id = m.id_residence
	INNER JOIN clients AS c
	ON c.id = r.id_client
	WHERE c.id = 6 AND m.date BETWEEN first_date AND last_date;
END;
//

CALL getClientMeasuresByDates(1,"2021-01-01 17:00:00.111111" , "2021-05-01 17:00:00.111111");

SELECT * FROM `residences`
SELECT * FROM `clients`
SELECT * FROM users
SELECT * FROM tariffs
SELECT * FROM measures
SELECT * FROM energy_meters
SELECT * FROM bills

INSERT INTO meter_brands(NAME) VALUES ("Samsung"),("Nokia");
INSERT INTO meter_models(NAME) VALUES ("model1"),("model2");

#--------------------------------------------------------------------------------------------------------------------------