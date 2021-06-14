`measures`USE tp_basedatos;

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

#-------------------------------------------->> CARGA DE DATOS <<--------------------------------------------

SELECT * FROM residences;
SELECT * FROM meter_models;
SELECT * FROM meter_brands;
SELECT * FROM users;
SELECT * FROM clients;
SELECT * FROM energy_meters;
SELECT * FROM residences;


#Obtener amount de tarifa
DROP PROCEDURE IF EXISTS getResidenceTariffAmount;
DELIMITER //
CREATE PROCEDURE getResidenceTariffAmount(IN idResidence INT, OUT price FLOAT)
BEGIN 

    SELECT IFNULL(t.amount,0) INTO price FROM tariffs AS  t
    JOIN residences AS r
    ON  r.id_tariff = t.id
    WHERE r.id = idResidence
    LIMIT 1;
END;
//

CALL getMeasurePrice(1,@hola);
SELECT @hola

#Actualiza el precio de la medicion
DROP TRIGGER IF EXISTS tbi_MeasurePrice;
DELIMITER //
CREATE TRIGGER tbi_MeasurePrice BEFORE INSERT ON measures FOR EACH ROW
BEGIN
    DECLARE vDate DATETIME DEFAULT NULL;
    DECLARE vLastMueasureKW FLOAT DEFAULT 0;
    DECLARE vLastMueasurePrice FLOAT DEFAULT 0;

    CALL getResidenceTariffAmount(new.id_residence,vLastMueasurePrice);

    SET vDate = (SELECT MAX(DATE) FROM measures WHERE measures.date<new.date AND measures.id_residence = new.id_residence LIMIT 1);

    IF( vDate IS NOT NULL) THEN
        SET vLastMueasureKW = (SELECT MAX(m.kw)FROM measures AS m WHERE (m.date = vDate) AND (m.id_residence = new.id_residence)) ;
        SET new.price= (new.kw - vLastMueasureKW)*vLastMueasurePrice;
    ELSE
        SET new.price =new.kw * vLastMueasurePrice;
    END IF;
END;
//

INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:00:00 000000", 5, 1), ("2021-06-13 00:05:00 000000", 10, 1),
						   ("2021-06-13 00:10:00 000000", 20, 1), ("2021-06-13 00:15:00 000000", 45, 1),
						   ("2021-06-13 00:00:00 000000", 10, 2), ("2021-06-13 00:00:00 000000", 4, 2);


SELECT * FROM measures;
DELETE FROM measures;
 


#----- [PUNTO 2] Generar una factura ---------------------------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS getResidenceTariffId;
DELIMITER //
CREATE PROCEDURE getResidenceTariffId(IN idResidence INT, OUT id FLOAT)
BEGIN 
    
    SELECT t.id INTO id FROM tariffs AS  t
    JOIN residences AS r
    ON  r.id_tariff = t.id
    WHERE r.id = idResidence
    LIMIT 1;
    
END;
//

DROP PROCEDURE IF EXISTS getFinalDateAndMedition;
DELIMITER //
CREATE PROCEDURE getFinalDateAndMedition(IN idResidence INT, OUT DATE DATETIME, OUT medition FLOAT)
BEGIN 
    
    SELECT MAX(m.date), m.kw
    FROM measures AS m
    INNER JOIN residences AS r
    ON r.id = m.id_residence
    WHERE m.id_residence = 1
 
  
END;
//

SELECT * FROM measures;

DROP PROCEDURE IF EXISTS generateBill;
DELIMITER //
CREATE PROCEDURE generateBill(IN idResidence INT)
BEGIN 
	DECLARE vExpiration DATETIME DEFAULT NOW();
	SELECT DATE_ADD(NOW(),INTERVAL 15 DAY) INTO vExpiration;     #Agrega 15 días a la fecha actual
	
	DECLARE vTariff INT DEFAULT 0;
	CALL getResidenceTariffId(idResidence, vTariff);
	
	DECLARE vTotalAmount FLOAT DEFAULT 0;
	SELECT INTO vTotalAmount SUM(m.price) FROM measures AS m
				 INNER JOIN residences AS r
				 ON r.id = m.id_residence
				 WHERE (m.is_billed = FALSE) AND (m.id_residence = idResidence);
				 
				 
	
	
END;
//

CALL generateBill(4);





