USE tp_basedatos;


CREATE USER 'EJEMPLO'@'%' IDENTIFIED BY '1';

#todos los privilegios
GRANT ALL PRIVILEGES ON *.* TO 'EJEMPLO';
REVOKE ALL PRIVILEGES ON *.* FROM 'facturacion';

GRANT ALL PRIVILEGES ON tp_basedatos.* TO 'EJEMPLO';

SHOW GRANTS FOR 'clientes';

#usuario maneje clientes, medidores y tarifas >> BACKOFFICE      AGREGAR PROCEDURES DE INDICES
CREATE USER 'backoffice'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON tp_basedatos.clients TO 'backoffice';
GRANT ALL PRIVILEGES ON tp_basedatos.energy_meters TO 'backoffice';
GRANT ALL PRIVILEGES ON tp_basedatos.tariffs TO 'backoffice';

#usuario que solo vea mediciones y facturas >> CLIENTES
CREATE USER 'clientes'@'%' IDENTIFIED BY '1234';
GRANT SELECT ON tp_basedatos.measures TO 'clientes';
GRANT SELECT ON tp_basedatos.bills TO 'clientes';
GRANT EXECUTE ON PROCEDURE tp_basedatos.getClientUnpaidBills TO 'clientes';
GRANT EXECUTE ON PROCEDURE tp_basedatos.getClientTotalEnergyAndAmountByDates TO 'clientes';
GRANT EXECUTE ON PROCEDURE tp_basedatos.getClientMeasuresByDates TO 'clientes'; #cuidados 

#usuario que enviara mediciones  >> MEDIDORES
CREATE USER 'medidores'@'%' IDENTIFIED BY '1234';
GRANT INSERT ON tp_basedatos.measures TO 'medidores';

#usuario encargado del proceso automatico  >> FACTURACION
CREATE USER 'facturacion'@'%' IDENTIFIED BY '1234';
GRANT INSERT ON tp_basedatos.measures TO 'facturacion';
GRANT EXECUTE ON PROCEDURE tp_basedatos.generateBill TO 'facturacion';
GRANT EXECUTE ON PROCEDURE tp_basedatos.generateAllBills TO 'facturacion';

