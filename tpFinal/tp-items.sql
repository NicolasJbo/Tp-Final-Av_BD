
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

#-------------------------------->> GENERAR FACTURA DE AJUSTE POR TARIFA ACTUALIZADA  <<-------------------

DROP TRIGGER IF EXISTS tbi_MeasurePrice; 
DELIMITER //
CREATE TRIGGER tbi_MeasurePrice BEFORE INSERT ON measures FOR EACH ROW
BEGIN
    DECLARE vDate DATETIME DEFAULT NULL;
    DECLARE vLastMueasureKW FLOAT DEFAULT 0;
    DECLARE vLastMueasurePrice FLOAT DEFAULT 0;

    CALL getResidenceTariffAmount(new.id_residence,vLastMueasurePrice);

    SET vDate = (SELECT IFNULL(MAX(DATE),0) FROM measures WHERE measures.id_residence = new.id_residence LIMIT 1);

    IF( vDate <> 0) THEN
        SET vLastMueasureKW = (SELECT MAX(m.kw)FROM measures AS m WHERE (m.date = vDate) AND (m.id_residence = new.id_residence)) ;
	
	IF(new.kw <= vLastMueasureKW) THEN
		SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT="The measure kws must be acumulatives!";
	ELSE
		SET new.price= (new.kw - vLastMueasureKW)*vLastMueasurePrice;
	END IF;
    ELSE
        SET new.price =new.kw * vLastMueasurePrice;
    END IF;
END;
//
#-------------------------------->> GENERAR FACTURA DE AJUSTE POR TARIFA ACTUALIZADA  <<-------------------


#----- [PUNTO 2] Generar una factura ---------------------------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS getResidenceTariffId;
DELIMITER //
CREATE PROCEDURE getResidenceTariffId(IN idResidence INT, OUT id INT)
BEGIN 

    SELECT t.id INTO id FROM tariffs AS  t
    JOIN residences AS r
    ON  r.id_tariff = t.id
    WHERE r.id = idResidence
    LIMIT 1;
    
END;
//

DROP PROCEDURE IF EXISTS getResidenceEnergyMeterId;
DELIMITER //
CREATE PROCEDURE getResidenceEnergyMeterId(IN idResidence INT, OUT id INT)
BEGIN 
    
    SELECT e.id INTO id FROM energy_meters AS e
    JOIN residences AS r
    ON  r.id_energy_meter = e.id
    WHERE r.id = idResidence
    LIMIT 1;
 
END;
//

DROP PROCEDURE IF EXISTS getFinalDateAndMedition;
DELIMITER //
CREATE PROCEDURE getFinalDateAndMedition(IN idResidence INT, OUT dateMedition DATETIME, OUT medition FLOAT)
BEGIN 

    SELECT m.date, m.kw INTO dateMedition, medition
    FROM measures AS m
    INNER JOIN residences AS r
    ON r.id = m.id_residence
    WHERE (m.id_residence = idResidence) AND (m.id_bill=0)
    ORDER BY m.date DESC
    LIMIT 1;
    
END;
//

DROP PROCEDURE IF EXISTS getInitialDateAndMedition;
DELIMITER //
CREATE PROCEDURE getInitialDateAndMedition(IN idResidence INT, OUT dateMedition DATETIME, OUT medition FLOAT)
BEGIN 

    SELECT m.date, m.kw INTO dateMedition, medition
    FROM measures AS m
    INNER JOIN residences AS r
    ON r.id = m.id_residence
    WHERE (m.id_residence = idResidence) AND (m.id_bill=0)
    ORDER BY m.date ASC
    LIMIT 1;
    
END;
//

DROP PROCEDURE IF EXISTS getCountMeditions;
DELIMITER //
CREATE PROCEDURE getCountMeditions(IN idResidence INT, OUT totalCount INT)
BEGIN 

    SELECT COUNT(*) INTO totalCount 
    FROM measures 
    WHERE ((id_residence = idResidence) AND (id_bill=0));
    
END;
//


#--------------------------------------->> GENERAR UNA UNICA FACTURA  <<-----------------------------------

DROP PROCEDURE IF EXISTS generateBill;
DELIMITER //
CREATE PROCEDURE generateBill(IN idResidence INT)
BEGIN 
    DECLARE existsResidence INT DEFAULT 1;
    DECLARE existsMeasures INT DEFAULT 0;
    
    DECLARE vFinalDate DATETIME ;
    DECLARE vFinalMedition FLOAT DEFAULT 0 ; 
    DECLARE vInitialDate DATETIME ;
    DECLARE vInitialMedition FLOAT DEFAULT 0 ;
    DECLARE vTariff INT DEFAULT 0; 
    DECLARE vExpiration DATETIME DEFAULT NOW();
    DECLARE vTotalAmount FLOAT DEFAULT 0;
    DECLARE vTotalEnergy FLOAT DEFAULT 0;
    DECLARE vEnergyMeter INT ;
    
    SELECT COUNT(*) INTO existsResidence FROM residences WHERE id = idResidence;
    CALL getCountMeditions(idResidence,existsMeasures);
    
    IF((existsResidence>0) AND (existsMeasures>1)) THEN 
        
        SELECT DATE_ADD(NOW(),INTERVAL 15 DAY) INTO vExpiration;     #Agrega 15 dias a la fecha actual
        CALL getResidenceTariffId(idResidence, vTariff);
        
        SELECT SUM(m.price)INTO vTotalAmount FROM measures AS m
                     INNER JOIN residences AS r
                     ON r.id = m.id_residence
                     WHERE (m.id_bill = 0) AND (m.id_residence = idResidence);
                        
    
        SELECT MAX(m.kw)INTO vTotalEnergy FROM measures AS m
                     INNER JOIN residences AS r
                     ON r.id = m.id_residence
                     WHERE (m.id_bill = 0) AND (m.id_residence = idResidence);                
        
        CALL getInitialDateAndMedition(idResidence, vInitialDate, vInitialMedition);   
        CALL getFinalDateAndMedition(idResidence, vFinalDate, vFinalMedition);    
                      
        CALL getResidenceEnergyMeterId(idResidence, vEnergyMeter);
        
        INSERT INTO bills(id_residence
        , id_tariff,
         id_energy_meter,
          initial_date,
           initial_medition,
          final_date,
           final_medition,
            final_amount,
             total_energy,
              expiration_date)
        VALUE(idResidence
        , vTariff,
        vEnergyMeter,
        vInitialDate,
         vInitialMedition,
          vFinalDate, 
        vFinalMedition, 
        vTotalAmount,
        vTotalEnergy, 
        vExpiration);    
        
    UPDATE measures  SET id_bill = LAST_INSERT_ID() WHERE id_residence = idResidence AND id_bill =0;     
    ELSE 
        SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT='The residence do not exists or we have not mesures to work!';
    END IF;
    
END;
//

CALL generateBill(2);
#--------------------------------------->> GENERAR UNA UNICA FACTURA  <<-----------------------------------

INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:00:00 000000", 5, 1), ("2021-06-13 00:05:00 000000", 10, 1);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:10:00 000000", 20, 1), ("2021-06-13 00:15:00 000000", 45, 1);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-06-13 00:00:00 000000", 10, 2), ("2021-06-13 00:05:00 000000", 15, 2);
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-07-13 00:00:00 000000", 50, 1),("2021-07-13 00:12:00 000000", 72, 1) ;
INSERT INTO measures(DATE, kw, id_residence) VALUES("2021-08-13 00:10:00 000000", 25, 2), ("2021-06-13 00:30:00 000000", 35, 2);

SELECT * FROM bills;                
SELECT * FROM measures;                
                               
DELETE FROM bills;
DELETE FROM measures;                

#------------------------------------------>> GENERAR TODAS LAS FACTURA  <<---------------------------------
DROP PROCEDURE IF EXISTS generateAllBills;
DELIMITER //
CREATE PROCEDURE generateAllBills()
BEGIN 
	DECLARE vResidenceId INT DEFAULT 0; 
	
	DECLARE vContinue INT DEFAULT 1;
	DECLARE residencesCursor CURSOR FOR (SELECT id FROM residences);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET vContinue = 0;

	OPEN residencesCursor;
    
	get_residences: LOOP
		FETCH residencesCursor INTO vResidenceId;
		IF(vContinue = 0) THEN
			LEAVE get_residences;
		END IF;
		CALL generateBill(vResidenceId);
	END LOOP get_residences;
	
	CLOSE residencesCursor; 
END;
//

CALL generateAllBills();                

#------------------------------------------>> GENERAR UNA FACTURA UNICA <<---------------------------------


#--------------------------------->> REALIZAR LLAMADO UNA VEZ AL MES <<------------------------------------                
DELIMITER //
CREATE EVENT billingDay ON SCHEDULE EVERY 1 MONTH
STARTS NOW() DO
BEGIN 
	CALL generateAllBills();
END;
//                    
#--------------------------------->> REALIZAR LLAMADO UNA VEZ AL MES <<------------------------------------                




