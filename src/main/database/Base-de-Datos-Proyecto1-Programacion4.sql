-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema proyecto1_programacion4
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `proyecto1_programacion4` ;

-- -----------------------------------------------------
-- Schema proyecto1_programacion4
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `proyecto1_programacion4` DEFAULT CHARACTER SET utf8 ;
USE `proyecto1_programacion4` ;

-- -----------------------------------------------------
-- Table `proyecto1_programacion4`.`Usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `proyecto1_programacion4`.`Usuario` ;

CREATE TABLE IF NOT EXISTS `proyecto1_programacion4`.`Usuario` (
  `id` VARCHAR(45) NOT NULL,
  `clave` VARCHAR(255) NULL,
  `tipo` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1_programacion4`.`Empresa`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `proyecto1_programacion4`.`Empresa` ;

CREATE TABLE IF NOT EXISTS `proyecto1_programacion4`.`Empresa` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombreUsuario` VARCHAR(45) NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `localizacion` VARCHAR(45) NULL,
  `correoElectronico` VARCHAR(45) NULL,
  `telefono` VARCHAR(45) NULL,
  `descripcion` VARCHAR(255) NULL,
  `estado` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_Empresa_usuario1_idx` (`nombreUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Empresa_usuario1`
    FOREIGN KEY (`nombreUsuario`)
    REFERENCES `proyecto1_programacion4`.`Usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1_programacion4`.`Puesto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `proyecto1_programacion4`.`Puesto` ;

CREATE TABLE IF NOT EXISTS `proyecto1_programacion4`.`Puesto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `empresa` INT NOT NULL,
  `descripcion` VARCHAR(45) NULL,
  `salario` FLOAT NULL,
  `activo` TINYINT NULL,
  `tipoPublicacion` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Puesto_Empresa_idx` (`empresa` ASC) VISIBLE,
  CONSTRAINT `fk_Puesto_Empresa`
    FOREIGN KEY (`empresa`)
    REFERENCES `proyecto1_programacion4`.`Empresa` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1_programacion4`.`Oferente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `proyecto1_programacion4`.`Oferente` ;

CREATE TABLE IF NOT EXISTS `proyecto1_programacion4`.`Oferente` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombreUsuario` VARCHAR(45) NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `primerApellido` VARCHAR(45) NULL,
  `nacionalidad` VARCHAR(45) NULL,
  `telefono` VARCHAR(45) NULL,
  `correoElectronico` VARCHAR(45) NULL,
  `lugarResidencia` VARCHAR(45) NULL,
  `estado` TINYINT NULL,
  `curriculum` LONGBLOB NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Oferente_usuario1_idx` (`nombreUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Oferente_usuario1`
    FOREIGN KEY (`nombreUsuario`)
    REFERENCES `proyecto1_programacion4`.`Usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1_programacion4`.`Caracteristica`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `proyecto1_programacion4`.`Caracteristica` ;

CREATE TABLE IF NOT EXISTS `proyecto1_programacion4`.`Caracteristica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `padre` INT NULL,
  `nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Caracteristica_Caracteristica1_idx` (`padre` ASC) VISIBLE,
  CONSTRAINT `fk_Caracteristica_Caracteristica1`
    FOREIGN KEY (`padre`)
    REFERENCES `proyecto1_programacion4`.`Caracteristica` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1_programacion4`.`PuestoCaracteristica`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `proyecto1_programacion4`.`PuestoCaracteristica` ;

CREATE TABLE IF NOT EXISTS `proyecto1_programacion4`.`PuestoCaracteristica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `puesto` INT NOT NULL,
  `caracteristica` INT NOT NULL,
  `nivel` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_puestoCaracteristica_Puesto1_idx` (`puesto` ASC) VISIBLE,
  INDEX `fk_puestoCaracteristica_Caracteristica1_idx` (`caracteristica` ASC) VISIBLE,
  CONSTRAINT `fk_puestoCaracteristica_Puesto1`
    FOREIGN KEY (`puesto`)
    REFERENCES `proyecto1_programacion4`.`Puesto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_puestoCaracteristica_Caracteristica1`
    FOREIGN KEY (`caracteristica`)
    REFERENCES `proyecto1_programacion4`.`Caracteristica` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proyecto1_programacion4`.`OferenteCaracteristica`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `proyecto1_programacion4`.`OferenteCaracteristica` ;

CREATE TABLE IF NOT EXISTS `proyecto1_programacion4`.`OferenteCaracteristica` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nivel` INT NULL,
  `oferente` INT NOT NULL,
  `caracteristica` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_OferenteCaracteristica_Oferente1_idx` (`oferente` ASC) VISIBLE,
  INDEX `fk_OferenteCaracteristica_Caracteristica1_idx` (`caracteristica` ASC) VISIBLE,
  CONSTRAINT `fk_OferenteCaracteristica_Oferente1`
    FOREIGN KEY (`oferente`)
    REFERENCES `proyecto1_programacion4`.`Oferente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_OferenteCaracteristica_Caracteristica1`
    FOREIGN KEY (`caracteristica`)
    REFERENCES `proyecto1_programacion4`.`Caracteristica` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

USE `proyecto1_programacion4`;

-- Administradores con clave 1:
INSERT INTO `Usuario` (`id`, `clave`, `tipo`) VALUES
('1',     '$2a$10$Y9JcR78QYnYljw71lYH/o.cI8xxHfw1.DS6WfixvroX5FjhYxhiEa', 'Administrador'),
('admin', '$2a$10$Y9JcR78QYnYljw71lYH/o.cI8xxHfw1.DS6WfixvroX5FjhYxhiEa', 'Administrador'),
('root',  '$2a$10$Y9JcR78QYnYljw71lYH/o.cI8xxHfw1.DS6WfixvroX5FjhYxhiEa', 'Administrador');

