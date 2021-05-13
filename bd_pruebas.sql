CREATE DATABASE tp_udee

INSERT INTO clients (dni, NAME, last_name, mail, PASSWORD)VALUES (123, "Pepe", "Mujica", "hola@gmail.com", "23")
INSERT INTO clients (dni, NAME, last_name, mail, PASSWORD)VALUES (456, "Valen", "Rodlan", "cahu@gmail.com", "485")


SELECT id, dni, NAME, last_name, mail, PASSWORD, birthday FROM clients;

SELECT * FROM clients WHERE NAME = "Pepe"

SELECT is_paid, initial_medition, initial_date, final_medition, final_date, total_energy, final_amount, expiration_date
FROM bills;

INSERT INTO bills (is_paid, initial_medition, initial_date, final_medition, final_date, total_energy, final_amount, expiration_date)
VALUES (FALSE, 0.5, "2021-05-12", 2.0, "2021-05-13", 40, 1500, "2021-12-12"), (FALSE, 0.3, "2021-05-12", 1.0, "20201-05-13", 25, 800, "2021-12-12")
INSERT INTO bills (is_paid, initial_medition, initial_date, final_medition, final_date, total_energy, final_amount, expiration_date)
VALUES (FALSE, 0.5, "2021-01-05", 2.0, "2021-01-06", 120, 3300, "2021-12-12"), (FALSE, 0.3, "2021-01-07", 1.0, "2021-01-10", 50, 1460, "2021-12-12")


#consulta de facturas por rango de fechas, falta unir con cliente
SELECT b.id
FROM bills AS b
WHERE b.initial_date BETWEEN "2021-01-01" AND "2021-01-10"