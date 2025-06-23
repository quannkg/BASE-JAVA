ALTER TABLE `facebook_comment`
    ADD COLUMN `positive` TINYINT(1) NULL DEFAULT '0' AFTER `created_at`,
ADD COLUMN `neutral` TINYINT(1) NULL DEFAULT '0' AFTER `positive`,
ADD COLUMN `negative` TINYINT(1) NULL DEFAULT '0' AFTER `neutral`;
