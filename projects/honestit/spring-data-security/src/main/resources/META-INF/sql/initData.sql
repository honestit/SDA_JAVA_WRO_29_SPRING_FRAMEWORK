-- Utworzenie bazy danych, jeżeli potrzebne
# CREATE DATABASE knowledge_db CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci ;

-- Wyczyszczenie bazy
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS users_known_sources;
DROP TABLE IF EXISTS knowledge_sources_skills;
DROP TABLE IF EXISTS knowledge_sources;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS users;

-- Tabela z użytkownikami
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_unique_login` (`login`)
);

-- Tabela z rolami użytkowników
CREATE TABLE `users_roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

-- Tabela z umiejętnościami
CREATE TABLE `skills` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `skills_unique_name` (`name`)
);

-- Tabela ze źródłami wiedzy
CREATE TABLE `knowledge_sources` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` boolean DEFAULT NULL,
  `description` text,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `knowledge_sources_unique_name` (`name`)
);

-- Tabela z przypisaniem umiejętności do źródeł
CREATE TABLE `knowledge_sources_skills` (
  `source_id` bigint NOT NULL,
  `skill_id` bigint NOT NULL,
  PRIMARY KEY (`source_id`,`skill_id`),
  CONSTRAINT `knowledge_sources_skills_source_id` FOREIGN KEY (`source_id`) REFERENCES `knowledge_sources` (`id`),
  CONSTRAINT `knowledge_sources_skills_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`)
);

-- Tabela z przypisaniem źródeł wiedzy do użytkownika
CREATE TABLE `users_known_sources` (
  `user_id` bigint NOT NULL,
  `source_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`source_id`),
  CONSTRAINT `users_known_sources_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_known_sources_source_id` FOREIGN KEY (`source_id`) REFERENCES `knowledge_sources` (`id`)
);