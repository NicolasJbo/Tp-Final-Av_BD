DROP DATABASE IF EXISTS tp_baseDatos;
CREATE DATABASE IF NOT EXISTS tp_baseDatos;
USE tp_baseDatos;

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
  `id` INT NOT NULL AUTO_INCREMENT,
  `is_client` TINYINT(1) DEFAULT '1',
  `mail` VARCHAR(255) DEFAULT NULL,
  `password` VARCHAR(255) DEFAULT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT unq_mail UNIQUE (`mail`)) ENGINE=INNODB;


DROP TABLE IF EXISTS tariffs;
CREATE TABLE IF NOT EXISTS tariffs (
  `id` INT NOT NULL AUTO_INCREMENT,
  `amount` FLOAT NOT NULL,
  `name` VARCHAR(2) DEFAULT NULL,
  CONSTRAINT pk_tariff PRIMARY KEY (id),
  CONSTRAINT unq_tariffName UNIQUE (`name`)) ENGINE=INNODB;


DROP TABLE IF EXISTS clients;
CREATE TABLE IF NOT EXISTS clients (
  `id` INT NOT NULL AUTO_INCREMENT,
  `birthday` DATETIME(6) NOT NULL,
  `dni` VARCHAR(255) DEFAULT NULL,
  `last_name` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `id_user` INT DEFAULT NULL,
  CONSTRAINT pk_client PRIMARY KEY (id),
  CONSTRAINT unq_clientDni UNIQUE (`dni`),
  CONSTRAINT fk_clientUser FOREIGN KEY (`id_user`) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE) ENGINE=INNODB;


DROP TABLE IF EXISTS meter_brands;
CREATE TABLE IF NOT EXISTS meter_brands (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  CONSTRAINT pk_meterBrand PRIMARY KEY (id),
  CONSTRAINT unq_brandName UNIQUE (`name`)) ENGINE=INNODB;


DROP TABLE IF EXISTS meter_models;
CREATE TABLE IF NOT EXISTS `meter_models` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  CONSTRAINT pk_meterModel PRIMARY KEY (id),
  CONSTRAINT unq_modelName UNIQUE (`name`)) ENGINE=INNODB;


DROP TABLE IF EXISTS energy_meters;
CREATE TABLE IF NOT EXISTS energy_meters (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pass_word` VARCHAR(255) DEFAULT NULL,
  `serial_number` VARCHAR(255) DEFAULT NULL,
  `id_brand` INT DEFAULT NULL,
  `id_model` INT DEFAULT NULL,
  CONSTRAINT pk_energyMeter PRIMARY KEY (id),
  CONSTRAINT unq_serialNumber UNIQUE (`serial_number`),
  CONSTRAINT fk_energyMeterModel FOREIGN KEY (`id_model`) REFERENCES meter_models(id),
  CONSTRAINT fk_energyMeterBrand FOREIGN KEY (`id_brand`) REFERENCES meter_brands(id)) ENGINE=INNODB;


DROP TABLE IF EXISTS residences;
CREATE TABLE IF NOT EXISTS residences (
  `id` INT NOT NULL AUTO_INCREMENT,
  `apartament` VARCHAR(255) DEFAULT NULL,
  `floor` VARCHAR(255) DEFAULT NULL,
  `number` INT NOT NULL,
  `street` VARCHAR(255) NOT NULL,
  `id_client` INT DEFAULT NULL,
  `id_energy_meter` INT DEFAULT NULL,
  `id_tariff` INT DEFAULT NULL,
  CONSTRAINT pk_residence PRIMARY KEY (id),
  CONSTRAINT fk_residenceClient FOREIGN KEY (`id_client`) REFERENCES clients(id),
  CONSTRAINT fk_residenceTariff FOREIGN KEY (`id_tariff`) REFERENCES tariffs(id),
  CONSTRAINT fk_residenceEnergyMeter FOREIGN KEY (`id_energy_meter`) REFERENCES energy_meters(id)) ENGINE=INNODB;


DROP TABLE IF EXISTS measures;
CREATE TABLE IF NOT EXISTS measures (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME(6) NOT NULL,
  `kw` FLOAT DEFAULT NULL,
  `price` FLOAT DEFAULT NULL,
  `is_billed` TINYINT(1) DEFAULT '0',
  `id_residence` INT DEFAULT NULL,
  CONSTRAINT pk_measure PRIMARY KEY (id),
  CONSTRAINT fk_measureResidence FOREIGN KEY (id_residence) REFERENCES residences(id)ON UPDATE CASCADE ON DELETE CASCADE) ENGINE=INNODB;


DROP TABLE IF EXISTS bills;
CREATE TABLE IF NOT EXISTS bills (
  `id` INT NOT NULL AUTO_INCREMENT,
  `expiration_date` DATETIME(6) DEFAULT NULL,
  `final_amount` FLOAT DEFAULT NULL,
  `final_date` DATETIME(6) DEFAULT NULL,
  `final_medition` FLOAT DEFAULT NULL,
  `initial_date` DATETIME(6) DEFAULT NULL,
  `initial_medition` FLOAT DEFAULT NULL,
  `is_paid` BIT(1) DEFAULT NULL,
  `total_energy` FLOAT DEFAULT NULL,
  `id_energy_meter` INT DEFAULT NULL,
  `id_residence` INT DEFAULT NULL,
  `id_tariff` INT DEFAULT NULL,
  CONSTRAINT pk_bill PRIMARY KEY (id),
  CONSTRAINT fk_billResidence FOREIGN KEY (`id_residence`) REFERENCES residences(id),
  CONSTRAINT fk_billTariff FOREIGN KEY (`id_tariff`) REFERENCES tariffs(id),
  CONSTRAINT fk_billEnergyMeter FOREIGN KEY (`id_energy_meter`) REFERENCES energy_meters(id)) ENGINE=INNODB;

