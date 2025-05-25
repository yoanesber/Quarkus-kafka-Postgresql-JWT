-- This file is used to import data into the database.
--
-- Data for Name: department;
--

INSERT INTO department (id, dept_name, active, created_by) VALUES 
    ('d001', 'Marketing', true, 1),
    ('d002', 'Finance', true, 1),
    ('d003', 'Human Resources', true, 1),
    ('d004', 'Production', true, 1),
    ('d005', 'Development', true, 1),
    ('d006', 'Quality Management', true, 1),
    ('d007', 'Sales', true, 1),
    ('d008', 'Research', true, 1),
    ('d009', 'Customer Service', true, 1),
    ('d010', 'Information Technology', true, 1);


--
-- Data for Name: users;
-- Password for all users: P@ssw0rd
--

INSERT INTO users (id, username, password, email, firstname, lastname, is_enabled, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_deleted, account_expiration_date, credentials_expiration_date, user_type, last_login, created_by) VALUES 
    (1, 'admin', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'admin@myemail.com', 'Admin', 'Admin', true, true, true, true, false, '2026-01-30 23:59:59+07', '2026-01-30 23:59:59+07', 'USER_ACCOUNT', '2025-05-09 12:00:59+07', 0),
    (2, 'userone', '$2a$10$eP5Sddi7Q5Jv6seppeF93.XsWGY8r4PnsqprWGb5AxsZ9TpwULIGa', 'userone@myemail.com', 'User', 'One', true, true, true, true, false, '2026-01-30 23:59:59+07', '2026-01-30 23:59:59+07', 'USER_ACCOUNT', '2025-05-09 12:00:59+07', 1);



--
-- Data for Name: roles;
--

INSERT INTO roles (id, name) VALUES 
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN');



--
-- Data for Name: user_roles;
--

INSERT INTO user_roles (user_id, role_id) VALUES 
    (1, 2),
    (2, 1);



--
-- Name: roles_id_seq; This belongs to table roles
--

SELECT pg_catalog.setval('roles_id_seq', 3, true);


--
-- Name: users_id_seq; This belongs to table users
--

SELECT pg_catalog.setval('users_id_seq', 6, true);
