PRAGMA foreign_keys = ON;

INSERT OR IGNORE INTO SeatingChart (FLOOR_NO, SEAT_NO) VALUES
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 1), (2, 2), (2, 3), (2, 4),
(3, 1), (3, 2), (3, 3), (3, 4),
(4, 1), (4, 2), (4, 3), (4, 4);

INSERT OR IGNORE INTO Employee (EMP_ID, NAME, EMAIL, FLOOR_SEAT_SEQ) VALUES
('11221', 'Alice', 'alice@example.com', 12),
('12006', 'Bob', 'bob@example.com', 3),
('13040', 'Carol', 'carol@example.com', 9),
('16142', 'David', 'david@example.com', 7),
('16722', 'Eric', 'eric@example.com', 15),
('17081', 'Fiona', 'fiona@example.com', 10),
('18888', 'Grace', 'grace@example.com', NULL);
