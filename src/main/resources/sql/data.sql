-- Insert Roles
INSERT INTO roles (id, role_name) VALUES
(1, 'ADMIN'),
(2, 'USER');

-- Insert Users
INSERT INTO users (id, username, password, is_enabled, account_no_expired, account_no_locked, credential_no_expired) VALUES
(1, 'Alexis', '$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC', true, true, true, true),
(2, 'Jose', '$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC', true, true, true, true),
(3, 'Pepe', '$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC', true, true, true, true),
(4, 'Israel', '$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC', true, true, true, true);

-- Assign Roles to Users (using the join table 'user_roles')
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- Alexis tiene el rol ADMIN (role_id = 1)
(2, 2), -- Jose tiene el rol USER (role_id = 2)
(3, 2), -- Pepe tiene el rol USER (role_id = 2)
(4, 2); -- Israel tiene el rol USER (role_id = 2)

-- Insert Events
INSERT INTO events (event_id , user_id, title, description, start_time, end_time, location) VALUES
(1, 1, 'Reunión de Equipo', 'Discusión sobre el proyecto X.', '2024-01-20 10:00:00', '2024-01-20 11:00:00', 'Sala de Conferencias'),
(2, 2, 'Taller de Diseño', 'Taller práctico sobre diseño UX/UI.', '2024-01-25 14:00:00', '2024-01-25 16:00:00', 'Laboratorio de Diseño'),
(3, 1, 'Presentación de Resultados', 'Presentación de los resultados del trimestre.', '2024-02-01 09:00:00', '2024-02-01 10:00:00', 'Auditorio Principal'),
(4, 3, 'Evento Social', 'Fiesta de fin de año.', '2024-02-15 19:00:00', '2024-02-15 23:00:00', 'Salón de Eventos');

-- Insert Invitations
INSERT INTO invitations (invitation_id, user_id, event_id, status) VALUES
(1, 1, 2, 'ACEPTADA'),
(2, 2, 3, 'PENDIENTE'),
(3, 3, 1, 'ACEPTADA'),
(4, 4, 4, 'RECHAZADA');

INSERT INTO user_invitations_table (fk_user_id, fk_invitation_id) VALUES
(1,1),
(2,2),
(3,3),
(4,4);