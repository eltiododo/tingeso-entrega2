INSERT INTO karts (model, state) VALUES
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'AVAILABLE'),
('Sodikart RT8', 'MAINTENANCE');

INSERT INTO clients (first_name, last_name, email, birth_date) VALUES
                                                                   ('María', 'García', 'eldemomanxd@gmail.com', '1985-03-15'),
                                                                   ('Carlos', 'Rodríguez', 'lucarioroolz435@gmail.com', '1990-07-22'),
                                                                   ('Ana', 'Martínez', 'ana.martinez@example.com', '1988-11-05'),
                                                                   ('Luis', 'Hernández', 'luis.hernandez@example.com', '1992-01-30'),
                                                                   ('Sofía', 'López', 'sofia.lopez@example.com', '1987-09-18'),
                                                                   ('Jorge', 'Pérez', 'jorge.perez@example.com', '1995-04-25'),
                                                                   ('Elena', 'Sánchez', 'elena.sanchez@example.com', '1983-12-10'),
                                                                   ('Miguel', 'Ramírez', 'miguel.ramirez@example.com', '1991-06-08'),
                                                                   ('Isabel', 'Flores', 'isabel.flores@example.com', '1989-02-14'),
                                                                   ('Noelle', 'Holiday', 'noelle.holiday@example.com', '1993-08-19'),
                                                                   ('Walter', 'White', 'walter.white@example.com', '1965-09-07'),
                                                                   ('Jesse', 'Pinkman', 'jesse.pinkman@example.com', '1984-03-24'),
                                                                   ('Ricardo', 'Mendoza', 'ricardo.mendoza@example.com', '1986-05-12'),
                                                                   ('Patricia', 'Castillo', 'patricia.castillo@example.com', '1994-10-31'),
                                                                   ('Fernando', 'Gutiérrez', 'fernando.gutierrez@example.com', '1982-07-03'),
                                                                   ('Gabriela', 'Vargas', 'gabriela.vargas@example.com', '1996-11-27'),
                                                                   ('Oscar', 'Cruz', 'oscar.cruz@example.com', '1980-04-09'),
                                                                   ('Lucía', 'Reyes', 'lucia.reyes@example.com', '1997-12-15'),
                                                                   ('Antonio', 'Morales', 'antonio.morales@example.com', '1981-08-22'),
                                                                   ('Carmen', 'Ortega', 'carmen.ortega@example.com', '1998-01-07');

INSERT INTO reservations (booking_date, category, reservee_client_id) VALUES
-- TIER1 reservation (smaller group, lower cost)
('2025-05-15 14:30:00', 'TIER1',  3),

-- TIER2 reservations (medium group)
('2025-05-16 10:00:00', 'TIER2', 7),
('2025-05-17 16:30:00', 'TIER2', 12),  -- Jesse Pinkman's reservation

-- TIER3 reservations (larger group, higher cost)
('2025-05-18 11:30:00', 'TIER3', 1),   -- María García's reservation
('2025-05-19 19:00:00', 'TIER3', 11),  -- Walter White's reservation

-- Mixed categories
('2025-05-20 09:30:00', 'TIER1', 5),    -- Sofía López's reservation
('2025-05-21 15:00:00', 'TIER3', 9);    -- Isabel Flores's reservation

-- Insert receipts
INSERT INTO receipts (id_reservation, client_amount, cost_iva, cost_total) VALUES
                                                           (1, 4,16, 17400),
                                                           (2, 8,16, 23200),
                                                           (3, 10,16, 23200),
                                                           (4, 15,16, 29000),
                                                           (5, 13,16, 29000),
                                                           (6, 6,16, 17400),
                                                           (7, 3,16, 23200);

-- Insert client associations for each receipt
INSERT INTO reservation_entity_id_clients (reservation_entity_id_reservation, id_clients) VALUES
-- Receipt 1 clients (4 clients)
(1, 3), (1, 4), (1, 5), (1, 6),

-- Receipt 2 clients (8 clients)
(2, 7), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12), (2, 13), (2, 14),

-- Receipt 3 clients (Jesse Pinkman + 9 others)
(3, 12), (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7), (3, 8), (3, 9),

-- Receipt 4 clients (María García + 14 others)
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 6), (4, 7), (4, 8), (4, 9), (4, 10),
(4, 11), (4, 12), (4, 13), (4, 14), (4, 15),

-- Receipt 5 clients (Walter White + 12 others)
(5, 11), (5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 6), (5, 7), (5, 8), (5, 9),
(5, 10), (5, 12), (5, 13),

-- Receipt 6 clients (Sofía López + 5 others)
(6, 5), (6, 6), (6, 7), (6, 8), (6, 9), (6, 10),

-- Receipt 7 clients (3 clients)
(7, 9), (7, 10), (7, 11);