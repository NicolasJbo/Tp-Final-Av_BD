CREATE DATABASE tp_udee

INSERT INTO clients (dni, NAME, last_name, mail, PASSWORD)VALUES (123, "Pepe", "Mujica", "hola@gmail.com", "23")
INSERT INTO clients (dni, NAME, last_name, mail, PASSWORD)VALUES (456, "Valen", "Rodlan", "cahu@gmail.com", "485")


SELECT id, dni, NAME, last_name, mail, PASSWORD, birthday FROM clients;

SELECT * FROM clients WHERE NAME = "Pepe"



INSERT INTO bills (is_paid, initial_medition, initial_date, final_medition, final_date, total_energy, final_amount, expiration_date)
VALUES (FALSE, 0.5, "2021-05-12", 2.0, "2021-05-13", 40, 1500, "2021-12-12"), (FALSE, 0.3, "2021-05-12", 1.0, "20201-05-13", 25, 800, "2021-12-12")
INSERT INTO bills (is_paid, initial_medition, initial_date, final_medition, final_date, total_energy, final_amount, expiration_date)
VALUES (FALSE, 0.5, "2021-01-05", 2.0, "2021-01-06", 120, 3300, "2021-12-12"), (FALSE, 0.3, "2021-01-07", 1.0, "2021-01-10", 50, 1460, "2021-12-12")




DELETE FROM bills

#--------------------TARIFAS---------------------

INSERT INTO tariffs (NAME, amount) VALUES ('A1', 2.5),('A2',1.50), ('B1', 0.25), ('B2', 0.5);
SELECT NAME, amount FROM tariffs;

#--------------------TARIFAS---------------------

#--------------------FACTURAS---------------------

INSERT INTO bills (is_paid, initial_medition, initial_date, final_medition, final_date, total_energy, final_amount, expiration_date)
VALUES(FALSE, 0.5, "2021-05-12", 13.5, "2021-05-27", 40, 1500, "2021-12-12"),
       (FALSE, 0.7, "2021-04-20", 22.0, "2021-05-03", 56, 769, "2021-12-12"),
       (FALSE, 2.5, "2021-05-04", 82.3, "2021-05-30", 90, 3200, "2021-12-12"),
       (FALSE, 1.9, "2021-04-02", 53.1, "2021-04-17", 37, 550, "2021-12-12")
       
SELECT id,is_paid, initial_medition, initial_date, final_medition, final_date, total_energy, final_amount, expiration_date
FROM bills;

#--------------------FACTURAS---------------------

#consulta de facturas por rango de fechas, falta unir con cliente

SELECT b.id, b.is_paid, b.initial_medition, b.initial_date, b.final_medition, 
		       b.final_date, b.total_energy, b.final_amount, b.expiration_date,
		       b.id_tariff, b.id_energy_meter, b.id_residence
FROM bills AS b
WHERE b.initial_date BETWEEN "2021-01-01" AND "2021-05-01"


DELIMITER //
CREATE PROCEDURE getBillsByDates(IN first_date DATETIME, IN last_date DATETIME)
BEGIN 
    IF( first_date > last_date ) THEN
		SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT='The entered dates are invalid';
	ELSE 
		SELECT b.id, b.is_paid, b.initial_medition, b.initial_date, b.final_medition, 
		       b.final_date, b.total_energy, b.final_amount, b.expiration_date,
		       b.id_tariff, b.id_energy_meter, b.id_residence
		FROM bills AS b
		WHERE b.initial_date BETWEEN first_date AND last_date
	END IF;
END;
//
