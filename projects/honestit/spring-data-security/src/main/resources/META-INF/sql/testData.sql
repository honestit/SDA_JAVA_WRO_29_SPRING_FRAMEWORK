-- Testowe umiejętności
INSERT INTO skills (name, category) VALUES ('Programowanie proceduralne', 'Java');
INSERT INTO skills (name, category) VALUES ('Programowanie obiektowe', 'Java');
INSERT INTO skills (name, category) VALUES ('Algorytmy i struktury danych', 'Java');
INSERT INTO skills (name, category) VALUES ('Strumienie i wyrażenia lambda', 'Java');
INSERT INTO skills (name, category) VALUES ('HTML i CSS', 'Frontend');
INSERT INTO skills (name, category) VALUES ('JvaScript', 'Frontend');
INSERT INTO skills (name, category) VALUES ('Servlety i JSP', 'Frameworki');
INSERT INTO skills (name, category) VALUES ('JPA i Hibernate', 'Frameworki');
INSERT INTO skills (name, category) VALUES ('Spring Framework', 'Frameworki');
INSERT INTO skills (name, category) VALUES ('Spring Data', 'Frameworki');
INSERT INTO skills (name, category) VALUES ('Spring Security', 'Frameworki');
INSERT INTO skills (name, category) VALUES ('Czysty kod', 'Inżynieria oprogramowania');
INSERT INTO skills (name, category) VALUES ('Wzorce Projektowe', 'Inżynieria oprogramowania');

-- Testowe źródła wiedzy i powiązania z umiejętnościami
INSERT INTO knowledge_sources (name, description, active) VALUES ('Java. Podstawy', 'Książka opisująca podstawy języka Java', true);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (1, 1);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (1, 2);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (1, 3);

INSERT INTO knowledge_sources (name, description, active) VALUES ('Java. Programowanie funkcyjne', 'Książka omawiająca programowanie proceduralne i funkcyjne w językuja Java', true);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (2, 1);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (2, 4);

INSERT INTO knowledge_sources (name, description, active) VALUES ('Java. Algorytmy. 2-edycja', 'Książka omawiająca algorytmy klasyczne bazujące na iteracji oraz programowaniu funkcyjnym', false);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (3, 3);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (3, 4);

INSERT INTO knowledge_sources (name, description, active) VALUES ('Podstawy HTML', 'Kurs przygotowujący do podstaw pracy z HTML, CSS i JavaScript', true);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (4, 5);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (4, 6);

INSERT INTO knowledge_sources (name, description, active) VALUES ('Servlety i JPA', 'Tutorial pokazujący tworzenie aplikacji w technologii Servletów i JPA', true);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (5, 7);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (5, 10);

INSERT INTO knowledge_sources (name, description, active) VALUES ('Nowoczesne aplikacje webowe', 'Kurs omawiający tworzenie aplikacji webowych wykorzystujących technologie Servletów i framework Spring', true);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (6, 7);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (6, 8);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (6, 9);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (6, 10);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (6, 11);

INSERT INTO knowledge_sources (name, description, active) VALUES ('Mistrzostwo Programowania', 'Podstawy wzorców projektowych i czystego kodu', false);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (7, 12);
INSERT INTO knowledge_sources_skills (source_id, skill_id) VALUES (7, 13);

-- Testowi użytkownicy z rolami i znanymi źródłami
INSERT INTO users (login, password, first_name, last_name) VALUES ('jnowak', 'jnowak', 'Jan', 'Nowak');
INSERT INTO users_roles (login, role) VALUES ('jnowak', 'ROLE_USER');
INSERT INTO users_known_sources (user_id, source_id) VALUES (1, 1);
INSERT INTO users_known_sources (user_id, source_id) VALUES (1, 4);
INSERT INTO users_known_sources (user_id, source_id) VALUES (1, 5);

INSERT INTO users (login, password, first_name, last_name) VALUES ('jkowalski', 'jkowalski', 'Jan', 'Kowalski');
INSERT INTO users_roles (login, role) VALUES ('kowalski', 'ROLE_USER');
INSERT INTO users_known_sources (user_id, source_id) VALUES (2, 1);
INSERT INTO users_known_sources (user_id, source_id) VALUES (2, 2);

INSERT INTO users (login, password, first_name, last_name) VALUES ('anowak', 'anowak', 'Anna', 'Nowak');
INSERT INTO users_roles (login, role) VALUES ('anowak', 'ROLE_USER');
INSERT INTO users_known_sources (user_id, source_id) VALUES (3, 1);
INSERT INTO users_known_sources (user_id, source_id) VALUES (3, 2);
INSERT INTO users_known_sources (user_id, source_id) VALUES (3, 4);
INSERT INTO users_known_sources (user_id, source_id) VALUES (3, 5);
INSERT INTO users_known_sources (user_id, source_id) VALUES (3, 6);

INSERT INTO users (login, password, first_name, last_name) VALUES ('admin', 'admin', 'Admin', 'Admin');
INSERT INTO users_roles (login, role) VALUES ('admin', 'ROLE_USER');
INSERT INTO users_roles (login, role) VALUES ('admin', 'ROLE_ADMIN');