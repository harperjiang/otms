ALTER TABLE `otms`.`user` 
ADD COLUMN `source_system` VARCHAR(45) NULL AFTER `activate_code`,
ADD COLUMN `source_id` VARCHAR(100) NULL AFTER `source_system`,
ADD INDEX `INDX_user_source` (`source_system` ASC, `source_id` ASC);

ALTER TABLE `otms`.`user` 
CHANGE COLUMN `display_name` `display_name` VARCHAR(255) NOT NULL ,
CHANGE COLUMN `password` `password` VARCHAR(255) NULL ,
CHANGE COLUMN `time_zone` `time_zone` VARCHAR(50) NULL ;
