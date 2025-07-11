CREATE TABLE `lotte_result` (
                 `id` INT NOT NULL AUTO_INCREMENT,
                 `loai_ve_so` VARCHAR(2000) NULL,
                 `ky_quay` VARCHAR(100) NULL,
                 `ngay_mo_thuong` DATETIME NULL,
                 `day_so_trung_thuong` VARCHAR(2000) NULL,
                 `jackpot` VARCHAR(3000) NULL,
                 `create_date` DATETIME NULL,
                 PRIMARY KEY (`id`));
