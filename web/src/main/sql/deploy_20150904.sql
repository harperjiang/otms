ALTER TABLE `otms`.`user` 
ADD COLUMN `source_system` VARCHAR(45) NULL AFTER `activate_code`,
ADD COLUMN `source_id` VARCHAR(100) NULL AFTER `source_system`,
ADD INDEX `INDX_user_source` (`source_system` ASC, `source_id` ASC);

ALTER TABLE `otms`.`user` 
CHANGE COLUMN `display_name` `display_name` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `password` `password` VARCHAR(255) NULL ,
CHANGE COLUMN `time_zone` `time_zone` VARCHAR(50) NULL ;

CREATE TABLE `otms`.`contact_us` (
  `id` INT(11) NOT NULL,
  `from` VARCHAR(45) NOT NULL,
  `content` TEXT NOT NULL,
  `status` INT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`));

ALTER TABLE `otms`.`contact_us` 
ADD COLUMN `user_id` INT(11) NULL AFTER `status`;
ALTER TABLE `otms`.`contact_us` 
ADD COLUMN `email` VARCHAR(255) NOT NULL AFTER `user_id`;
ALTER TABLE `otms`.`contact_us` 
CHANGE COLUMN `from` `from_name` VARCHAR(45) NOT NULL ;
ALTER TABLE `otms`.`contact_us` 
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;

