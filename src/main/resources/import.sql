-- This file is used to import data into the database.
--
-- Data for Name: department;
--

INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d001', 'Marketing', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');
INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d002', 'Finance', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');
INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d003', 'Human Resources', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');
INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d004', 'Production', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');
INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d005', 'Development', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');
INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d006', 'Quality Management', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');
INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d007', 'Sales', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');
INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d008', 'Research', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');
INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d009', 'Customer Service', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');
INSERT INTO department (id, dept_name, active, created_by, created_date, updated_by, updated_date) VALUES ('d010', 'Information Technology', true, 1, '2024-10-07 17:51:24.616307+07', 1, '2024-10-07 17:51:24.616307+07');


--
-- Data for Name: users;
-- Password: P@ssw0rd
--

INSERT INTO users (id, username, password, email, firstname, lastname, is_enabled, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_deleted, account_expiration_date, credentials_expiration_date, user_type, last_login, created_by, created_date, updated_by, updated_date) VALUES (1, 'admin', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'admin@myemail.com', 'Admin', 'Admin', true, true, true, true, false, '2026-01-30 23:59:59+07', '2026-01-30 23:59:59+07', 'USER_ACCOUNT', '2025-05-09 12:00:59+07', 0, '2025-05-09 09:00:59+07', 0, '2025-05-09 09:00:59+07');
INSERT INTO users (id, username, password, email, firstname, lastname, is_enabled, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_deleted, account_expiration_date, credentials_expiration_date, user_type, last_login, created_by, created_date, updated_by, updated_date) VALUES (2, 'userone', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'userone@myemail.com', 'User', 'One', true, true, true, true, false, '2026-01-30 23:59:59+07', '2026-01-30 23:59:59+07', 'USER_ACCOUNT', '2025-05-09 12:00:59+07', 1, '2025-05-09 09:00:59+07', 1, '2025-05-09 09:00:59+07');
INSERT INTO users (id, username, password, email, firstname, lastname, is_enabled, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_deleted, account_expiration_date, credentials_expiration_date, user_type, last_login, created_by, created_date, updated_by, updated_date) VALUES (3, 'channelone', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'channelone@myemail.com', 'Channel', 'One', true, true, true, true, false, '2026-01-30 23:59:59+07', '2026-01-30 23:59:59+07', 'SERVICE_ACCOUNT', '2025-05-09 12:00:59+07', 1, '2025-05-09 09:00:59+07', 1, '2025-05-09 09:00:59+07');
INSERT INTO users (id, username, password, email, firstname, lastname, is_enabled, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_deleted, account_expiration_date, credentials_expiration_date, user_type, last_login, created_by, created_date, updated_by, updated_date) VALUES (4, 'usertest', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'usertest@myemail.com', 'User', 'Test', true, true, true, true, false, '2026-01-30 23:59:59+07', '2026-01-30 23:59:59+07', 'USER_ACCOUNT', '2025-05-09 12:00:59+07', 1, '2025-05-09 09:00:59+07', 1, '2025-05-09 09:00:59+07');
INSERT INTO users (id, username, password, email, firstname, lastname, is_enabled, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_deleted, account_expiration_date, credentials_expiration_date, user_type, last_login, created_by, created_date, updated_by, updated_date) VALUES (5, 'admintest', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'admintest@myemail.com', 'Admin', 'Test', true, true, true, true, false, '2026-01-30 23:59:59+07', '2026-01-30 23:59:59+07', 'USER_ACCOUNT', '2025-05-09 12:00:59+07', 1, '2025-05-09 09:00:59+07', 1, '2025-05-09 09:00:59+07');



--
-- Data for Name: roles;
--

INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');



--
-- Data for Name: user_roles;
--

INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (4, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (5, 2);



--
-- Name: roles_id_seq; This belongs to table roles
--

SELECT pg_catalog.setval('roles_id_seq', 3, true);


--
-- Name: users_id_seq; This belongs to table users
--

SELECT pg_catalog.setval('users_id_seq', 6, true);
