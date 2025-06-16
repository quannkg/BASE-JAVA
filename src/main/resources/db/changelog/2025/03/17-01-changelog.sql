CREATE TABLE `auth_user` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                             `last_login` datetime(6) DEFAULT NULL,
                             `is_superuser` tinyint(1) NOT NULL,
                             `username` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                             `first_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                             `last_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                             `email` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                             `is_staff` tinyint(1) NOT NULL,
                             `is_active` tinyint(1) NOT NULL,
                             `date_joined` datetime(6) NOT NULL,
                             `refresh_token` int DEFAULT NULL,
                             `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                             `password_retry_count` int DEFAULT '0',
                             `last_change_password_time` datetime DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `username` (`username`),
                             UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=689 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `auth_userprofile` (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                    `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                    `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                    `year_of_birth` int DEFAULT NULL,
                                    `gender` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `mailing_address` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                                    `city` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                                    `country` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `user_id` int NOT NULL,
                                    `phone_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `state` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `image_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                                    `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `date_of_birth` datetime(6) DEFAULT NULL,
                                    `country_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `district_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `provice_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `ward_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `card_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `approved_by` int DEFAULT NULL,
                                    `approved_date` datetime DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `user_id` (`user_id`),
                                    KEY `auth_userprofile_name_50909f10` (`name`),
                                    FULLTEXT KEY `name` (`name`),
                                    CONSTRAINT `auth_userprofile_user_id_62634b27_fk_auth_user_id` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=573 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `auth_permission` (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `content_type_id` int NOT NULL,
                                   `codename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `auth_permission_content_type_id_codename_01ab375a_uniq` (`content_type_id`,`codename`),
                                   KEY `permission_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2452 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `auth_group` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                              `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                              `created_date` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                              `description` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                              `status` tinyint(1) NOT NULL DEFAULT '1',
                              `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `auth_position` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                 `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                 `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
                                 `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`),
                                 KEY `auth_position_code_idx` (`code`),
                                 KEY `auth_position_name_idx` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `auth_user_user_permissions` (
                                              `id` int NOT NULL AUTO_INCREMENT,
                                              `user_id` int NOT NULL,
                                              `permission_id` int NOT NULL,
                                              `university_id` int DEFAULT NULL,
                                              PRIMARY KEY (`id`),
                                              UNIQUE KEY `auth_user_user_permissions_user_id_permission_id_14a6b632_uniq` (`user_id`,`permission_id`),
                                              KEY `auth_user_user_permi_permission_id_1fbb5f2c_fk_auth_perm` (`permission_id`),
                                              CONSTRAINT `auth_user_user_permi_permission_id_1fbb5f2c_fk_auth_perm` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`),
                                              CONSTRAINT `auth_user_user_permissions_user_id_a95ead1b_fk_auth_user_id` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1140 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `auth_user_groups` (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `user_id` int NOT NULL,
                                    `group_id` int NOT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `auth_user_groups_group_id_97559544_fk_auth_group_id` (`group_id`),
                                    KEY `auth_user_groups_user_id_fk` (`user_id`),
                                    CONSTRAINT `auth_user_groups_group_id_fk` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`),
                                    CONSTRAINT `auth_user_groups_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2412 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `auth_user_positions` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `userprofile_id` int NOT NULL,
                                       `position_id` int NOT NULL,
                                       PRIMARY KEY (`id`),
                                       KEY `auth_user_positions_userprofile_id_idx` (`userprofile_id`),
                                       KEY `fk_auth_user_positions_pk_auth_position` (`position_id`),
                                       CONSTRAINT `fk_auth_user_positions_pk_auth_position` FOREIGN KEY (`position_id`) REFERENCES `auth_position` (`id`),
                                       CONSTRAINT `fk_auth_user_positions_pk_auth_userprofile` FOREIGN KEY (`userprofile_id`) REFERENCES `auth_userprofile` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=917 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `auth_user_refresh_token` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `user_id` int NOT NULL,
                                           `token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                           `is_revoked` tinyint NOT NULL,
                                           `created_date` datetime NOT NULL,
                                           `revoked_at` datetime DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           KEY `user_id_idx` (`user_id`),
                                           CONSTRAINT `fk_auth_user_pk_auth_user_refresh_token` FOREIGN KEY (`user_id`) REFERENCES `auth_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `auth_group_permissions` (
                                          `id` int NOT NULL AUTO_INCREMENT,
                                          `group_id` int NOT NULL,
                                          `permission_id` int NOT NULL,
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `auth_group_permissions_group_id_permission_id_0cd325b0_uniq` (`group_id`,`permission_id`),
                                          KEY `auth_group_permissio_permission_id_84c5c92e_fk_auth_perm` (`permission_id`),
                                          CONSTRAINT `auth_group_permissio_permission_id_84c5c92e_fk_auth_perm` FOREIGN KEY (`permission_id`) REFERENCES `auth_permission` (`id`),
                                          CONSTRAINT `auth_group_permissions_group_id_b120cbf9_fk_auth_group_id` FOREIGN KEY (`group_id`) REFERENCES `auth_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7209 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



