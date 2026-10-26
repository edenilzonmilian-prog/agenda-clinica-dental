INSERT INTO tratamientos (nombre, descripcion, precio)
VALUES
    ('Revision dental', 'Evaluacion general', 100.00),
    ('Resina dental', 'Reparacion de caries', 200.00),
    ('Extraccion simple', 'Extraccion dental', 350.00),
    ('Blanqueamiento dental', 'Tratamiento estetico', 650.00);

INSERT INTO citas
(paciente, fecha, hora, tratamiento_id, observaciones, estado)
VALUES
    ('Ana Torres', '2026-11-10', '08:00:00',
     (SELECT id FROM tratamientos WHERE nombre = 'Revision dental' ORDER BY id DESC LIMIT 1),
    'Consulta inicial', 'PROGRAMADA'),

('Luis Gomez', '2026-11-10', '09:00:00',
 (SELECT id FROM tratamientos WHERE nombre = 'Resina dental' ORDER BY id DESC LIMIT 1),
 'Tratamiento de caries', 'PROGRAMADA'),

('Sofia Lopez', '2026-11-10', '10:00:00',
 (SELECT id FROM tratamientos WHERE nombre = 'Blanqueamiento dental' ORDER BY id DESC LIMIT 1),
 'Primera sesion', 'PROGRAMADA'),

('Carlos Perez', '2026-11-11', '08:30:00',
 (SELECT id FROM tratamientos WHERE nombre = 'Extraccion simple' ORDER BY id DESC LIMIT 1),
 'Extraccion programada', 'PROGRAMADA'),

('Diana Morales', '2026-11-11', '10:30:00',
 (SELECT id FROM tratamientos WHERE nombre = 'Revision dental' ORDER BY id DESC LIMIT 1),
 'Revision general', 'PROGRAMADA'),

('Pedro Ramirez', '2026-11-12', '09:30:00',
 (SELECT id FROM tratamientos WHERE nombre = 'Resina dental' ORDER BY id DESC LIMIT 1),
 'Tratamiento programado', 'PROGRAMADA'),

('Elena Castro', '2026-11-12', '11:00:00',
 (SELECT id FROM tratamientos WHERE nombre = 'Blanqueamiento dental' ORDER BY id DESC LIMIT 1),
 'Tratamiento realizado', 'ATENDIDA'),

('Mario Diaz', '2026-11-13', '08:00:00',
 (SELECT id FROM tratamientos WHERE nombre = 'Extraccion simple' ORDER BY id DESC LIMIT 1),
 'Atencion finalizada', 'ATENDIDA'),

('Laura Mejia', '2026-11-13', '09:00:00',
 (SELECT id FROM tratamientos WHERE nombre = 'Revision dental' ORDER BY id DESC LIMIT 1),
 'Cita cancelada', 'CANCELADA'),

('Jose Alvarez', '2026-11-14', '10:00:00',
 (SELECT id FROM tratamientos WHERE nombre = 'Resina dental' ORDER BY id DESC LIMIT 1),
 'Cancelada por paciente', 'CANCELADA');