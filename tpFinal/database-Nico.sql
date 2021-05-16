DROP PROCEDURE IF EXISTS getTop10Clients;
DELIMITER //
CREATE PROCEDURE getTop10Clients(IN first_date DATETIME, IN last_date DATETIME)
BEGIN
    IF( first_date > last_date ) THEN
		SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT='The entered dates are invalid';
ELSE
SELECT data.ClientName,
       data.ClientLastName,
       SUM(data.Total)AS Total
FROM(SELECT c.id,
            c.name AS ClientName,
            c.last_name AS ClientLastName,
            r.id AS ResidenceId,
            e.id AS Energy,
            MAX(m.total) - IF(COUNT(e.id) <> 1,MIN(m.total),0)  AS Total
     FROM clients c
              JOIN residences r
                   ON c.id= r.id_client
              JOIN energy_meters e
                   ON r.id_energy_meter = e.id
              JOIN measures m
                   ON m.id_residence = r.id
     WHERE DATE BETWEEN first_date AND last_date
     GROUP BY c.id,c.name,c.last_name,r.id,energy)DATA
GROUP BY data.ClientName
ORDER BY Total DESC
    LIMIT 0,10;
END IF;
END;
//
