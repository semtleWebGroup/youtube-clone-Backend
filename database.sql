-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema youtube_clone
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema youtube_clone
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `youtube_clone` DEFAULT CHARACTER SET utf8 ;
USE `youtube_clone` ;

-- -----------------------------------------------------
-- Table `youtube_clone`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`member` (
  `member_id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `name` NVARCHAR(15) NOT NULL DEFAULT 'name from provider',
  `provider` VARCHAR(100) NOT NULL DEFAULT 'oauth provider',
  PRIMARY KEY (`member_id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtube_clone`.`channel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`channel` (
  `channe_id` INT NOT NULL AUTO_INCREMENT,
  `title` NVARCHAR(15) NOT NULL,
  `description` NVARCHAR(50) NULL,
  `profile_img` BLOB NULL,
  `member_id` INT NOT NULL,
  PRIMARY KEY (`channe_id`),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE,
  INDEX `fk_channel_member_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_channel_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `youtube_clone`.`member` (`member_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtube_clone`.`subscribe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`subscribe` (
  `subscribe_id` INT NOT NULL AUTO_INCREMENT,
  `subscriber` INT NOT NULL,
  `channel` INT NOT NULL,
  PRIMARY KEY (`subscribe_id`),
  INDEX `fk_subscribe_channel1_idx` (`subscriber` ASC) VISIBLE,
  INDEX `fk_subscribe_channel2_idx` (`channel` ASC) VISIBLE,
  CONSTRAINT `fk_subscribe_channel1`
    FOREIGN KEY (`subscriber`)
    REFERENCES `youtube_clone`.`channel` (`channe_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subscribe_channel2`
    FOREIGN KEY (`channel`)
    REFERENCES `youtube_clone`.`channel` (`channe_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtube_clone`.`video`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`video` (
  `video_id` BINARY(16) NOT NULL,
  `video_second` DOUBLE NOT NULL,
  `isCashed` TINYINT NOT NULL,
  `channel_id` INT NOT NULL,
  PRIMARY KEY (`video_id`),
  INDEX `fk_video_channel1_idx` (`channel_id` ASC) VISIBLE,
  CONSTRAINT `fk_video_channel1`
    FOREIGN KEY (`channel_id`)
    REFERENCES `youtube_clone`.`channel` (`channe_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtube_clone`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`comment` (
  `comment_id` INT NOT NULL AUTO_INCREMENT,
  `contents` NVARCHAR(800) NOT NULL,
  `created_time` DATETIME NOT NULL,
  `updated_time` DATETIME NOT NULL,
  `channel_id` INT NOT NULL,
  `root_comment_id` INT NOT NULL,
  `video_id` BINARY(16) NOT NULL,
  PRIMARY KEY (`comment_id`),
  INDEX `fk_comment_channel1_idx` (`channel_id` ASC) VISIBLE,
  INDEX `fk_comment_comment1_idx` (`root_comment_id` ASC) VISIBLE,
  INDEX `fk_comment_video1_idx` (`video_id` ASC) VISIBLE,
  CONSTRAINT `fk_comment_channel1`
    FOREIGN KEY (`channel_id`)
    REFERENCES `youtube_clone`.`channel` (`channe_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comment_comment1`
    FOREIGN KEY (`root_comment_id`)
    REFERENCES `youtube_clone`.`comment` (`comment_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comment_video1`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtube_clone`.`video` (`video_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtube_clone`.`video_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`video_like` (
  `video_like_id` INT NOT NULL AUTO_INCREMENT,
  `channel_id` INT NOT NULL,
  `video_id` BINARY(16) NOT NULL,
  PRIMARY KEY (`video_like_id`),
  INDEX `fk_video_like_channel1_idx` (`channel_id` ASC) VISIBLE,
  INDEX `fk_video_like_video1_idx` (`video_id` ASC) VISIBLE,
  CONSTRAINT `fk_video_like_channel1`
    FOREIGN KEY (`channel_id`)
    REFERENCES `youtube_clone`.`channel` (`channe_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_video_like_video1`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtube_clone`.`video` (`video_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtube_clone`.`comment_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`comment_like` (
  `comment_like_id` INT NOT NULL AUTO_INCREMENT,
  `channel_id` INT NOT NULL,
  `comment_id` INT NOT NULL,
  PRIMARY KEY (`comment_like_id`),
  INDEX `fk_comment_like_channel1_idx` (`channel_id` ASC) VISIBLE,
  INDEX `fk_comment_like_comment1_idx` (`comment_id` ASC) VISIBLE,
  CONSTRAINT `fk_comment_like_channel1`
    FOREIGN KEY (`channel_id`)
    REFERENCES `youtube_clone`.`channel` (`channe_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comment_like_comment1`
    FOREIGN KEY (`comment_id`)
    REFERENCES `youtube_clone`.`comment` (`comment_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtube_clone`.`video_media`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`video_media` (
  `media_id` BINARY(16) NOT NULL,
  `width` INT NOT NULL,
  `height` INT NOT NULL,
  `frame` INT NOT NULL,
  `audio_channel` INT NOT NULL,
  `file_size` BIGINT NOT NULL,
  `video_format` VARCHAR(10) NOT NULL,
  `file_path` VARCHAR(260) NOT NULL,
  `video_id` BINARY(16) NOT NULL,
  PRIMARY KEY (`media_id`),
  INDEX `fk_video_media_video1_idx` (`video_id` ASC) VISIBLE,
  UNIQUE INDEX `file_path_UNIQUE` (`file_path` ASC) VISIBLE,
  CONSTRAINT `fk_video_media_video1`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtube_clone`.`video` (`video_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtube_clone`.`video_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`video_info` (
  `video_info_id` INT NOT NULL AUTO_INCREMENT,
  `thumb_img` BLOB NULL,
  `title` NVARCHAR(50) NOT NULL,
  `description` NVARCHAR(100) NULL,
  `created_time` DATETIME NOT NULL,
  `updated_time` DATETIME NOT NULL,
  `view_count` INT NOT NULL,
  `video_id` BINARY(16) NOT NULL,
  PRIMARY KEY (`video_info_id`),
  INDEX `fk_video_info_video1_idx` (`video_id` ASC) VISIBLE,
  CONSTRAINT `fk_video_info_video1`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtube_clone`.`video` (`video_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `youtube_clone`.`cached_video_media`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `youtube_clone`.`cached_video_media` (
  `cached_media_id` INT NOT NULL AUTO_INCREMENT,
  `file_path` NVARCHAR(260) NOT NULL,
  `created_time` DATETIME NOT NULL,
  `video_id` BINARY(16) NOT NULL,
  PRIMARY KEY (`cached_media_id`),
  INDEX `fk_cached_video_media_video1_idx` (`video_id` ASC) VISIBLE,
  UNIQUE INDEX `file_path_UNIQUE` (`file_path` ASC) VISIBLE,
  CONSTRAINT `fk_cached_video_media_video1`
    FOREIGN KEY (`video_id`)
    REFERENCES `youtube_clone`.`video` (`video_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
