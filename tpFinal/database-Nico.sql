#--------------------------------------------------------------------------------------------------------------------
#Punto 4 --BACKOFICCE--> Consulta de facturas impagas por cliente y domicilio.

DELIMITER //
CREATE PROCEDURE getClientUnpaidBillsByResidence(IN residence_id INT)
BEGIN
SELECT b.id, b.is_paid, b.initial_medition, b.initial_date, b.final_medition,
       b.final_date, b.total_energy, b.final_amount, b.expiration_date,
       b.id_tariff, b.id_energy_meter, b.id_residence
FROM bills AS b
         INNER JOIN residences AS r
                    ON b.id_residence = r.id
WHERE r.id = 3 AND b.is_paid = FALSE;
END;
//
#--------------------------------------------------------------------------------------------------------------------
#Punto 5 --BACKOFICCE--> Consulta 10 clientes más consumidores en un rango de fechas.
DROP PROCEDURE IF EXISTS getTop10Clients;
DELIMITER //
CREATE PROCEDURE getTop10Clients(IN first_date DATETIME, IN last_date DATETIME)
BEGIN
	IF( first_date > last_date ) THEN
		SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT='The entered dates are invalid';
	ELSE
		SELECT c.name AS NAME,c.last_name AS LastName,
		       SUM(p.Final)AS Total
		FROM clients c
			 JOIN(
		    SELECT m.total,m.id_residence ,r.id_client,
			   SUM(m.total) AS Final
		    FROM measures m
			     JOIN
			 residences r
			 ON m.id_residence = r.id
		    WHERE m.date BETWEEN first_date AND last_date
		    GROUP BY m.total,m.id_residence,r.id_client
		    ORDER BY m.id_residence ASC)p
			     ON c.id=p.id_client
		GROUP BY c.name,c.last_name
		ORDER BY Total DESC
		LIMIT 0,10;
	END IF;
END;
//
#--------------------------------------------------------------------------------------------------------------------
#Punto 6 --BACKOFICCE--> Consulta de mediciones de un domicilio por rango de fechas  YA ESTA HECHO CON JPA
 CALL getMeasuresBetweenDaysByIdResidences(3,'2021-05-01','2021-01-30');
DELIMITER //
CREATE PROCEDURE getMeasuresBetweenD`measures`aysByIdResidences(IN id_residence INT ,IN first_date DATETIME, IN last_date DATETIME)
BEGIN
	 IF( first_date > last_date ) THEN
		SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT='The entered dates are invalid';
ELSE
SELECT m.date AS Fecha, r.number AS Numero, m.total AS Total, r.street AS Calle,
       c.name AS Nombre,c.dni AS Dni
FROM measures AS m
         INNER JOIN residences AS r
                    ON r.id = m.id_residence
         INNER JOIN clients AS c
                    ON c.id = r.id_client
WHERE r.id = id_residence AND m.date BETWEEN first_date AND last_date;
END IF;
END;
//


