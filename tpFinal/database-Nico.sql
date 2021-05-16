DROP PROCEDURE IF EXISTS getTop10Clients;
DELIMITER //
CREATE PROCEDURE getTop10Clients(IN first_date DATETIME, IN last_date DATETIME)
BEGIN
    IF( first_date > last_date ) THEN
		SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT='The entered dates are invalid';
ELSE
SELECT c.name as Name,c.last_name as LastName,
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
    LIMIT 0,10;
END IF;
END;
//
