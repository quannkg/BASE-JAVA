CREATE TABLE `api_integrated` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `name` VARCHAR(5000) NULL,
   `code` VARCHAR(45) NULL,
   `api_key` TEXT NULL,
   `created_by` INT NULL,
   `created_date` DATETIME NULL,
   `updated_date` DATETIME NULL,
   `is_deleted` TINYINT NULL DEFAULT 0,
   PRIMARY KEY (`id`));

