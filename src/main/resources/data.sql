-- ============================================================
-- DATOS DE EJEMPLO (se cargan al arrancar la app)
-- Se usan IDs explicitos para que sea simple y se pueda volver a
-- ejecutar sin duplicar: "ON DUPLICATE KEY UPDATE" no hace nada si
-- la fila ya existe (colision por la llave primaria id).
-- ============================================================

-- ---------- USUARIOS ----------
-- Contrasenas cifradas con BCrypt:
--   admin    / admin123
--   cliente  / cliente123
INSERT INTO usuarios (id, username, password, nombre, email, rol) VALUES
 (1, 'admin',   '$2b$10$i/NhWzW9M91pAtq0Hp1OP.Z7fEASoLK2WQMkBg8/QtGuxmupDZnjW', 'Administrador General', 'admin@tienda.com',   'ADMIN')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO usuarios (id, username, password, nombre, email, rol) VALUES
 (2, 'cliente', '$2b$10$xuxjLM/LcBNMvIkAz5joG.o86uNYbtdDbwpuOxQJcqHgMPnpUxNUe', 'Cliente de Prueba',     'cliente@correo.com', 'CLIENTE')
ON DUPLICATE KEY UPDATE id = id;

-- ---------- CATEGORIAS ----------
INSERT INTO categorias (id, nombre, descripcion) VALUES
 (1, 'Procesadores',     'CPUs para computadoras de escritorio') ON DUPLICATE KEY UPDATE id = id;
INSERT INTO categorias (id, nombre, descripcion) VALUES
 (2, 'Tarjetas de Video','GPUs y tarjetas graficas')             ON DUPLICATE KEY UPDATE id = id;
INSERT INTO categorias (id, nombre, descripcion) VALUES
 (3, 'Memorias RAM',     'Modulos de memoria DDR4 y DDR5')       ON DUPLICATE KEY UPDATE id = id;
INSERT INTO categorias (id, nombre, descripcion) VALUES
 (4, 'Almacenamiento',   'Discos SSD y HDD')                     ON DUPLICATE KEY UPDATE id = id;

-- ---------- PRODUCTOS ----------
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES
 (1, 'Procesador Ryzen 5 5600',         '6 nucleos, 12 hilos, socket AM4',            145.00, 15, 1) ON DUPLICATE KEY UPDATE id = id;
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES
 (2, 'Procesador Intel Core i5-12400F', '6 nucleos, socket LGA1700',                  160.00, 10, 1) ON DUPLICATE KEY UPDATE id = id;
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES
 (3, 'Tarjeta de Video RTX 4060',       '8GB GDDR6, ideal para gaming 1080p',         320.00,  8, 2) ON DUPLICATE KEY UPDATE id = id;
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES
 (4, 'Tarjeta de Video RX 6600',        '8GB GDDR6, gran relacion precio/rendimiento',250.00,  6, 2) ON DUPLICATE KEY UPDATE id = id;
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES
 (5, 'Memoria RAM 16GB DDR4 3200MHz',   'Kit 2x8GB, color negro',                      45.00, 25, 3) ON DUPLICATE KEY UPDATE id = id;
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES
 (6, 'Memoria RAM 32GB DDR5 6000MHz',   'Kit 2x16GB para plataformas nuevas',         110.00, 12, 3) ON DUPLICATE KEY UPDATE id = id;
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES
 (7, 'SSD NVMe 1TB',                     'Lectura hasta 3500 MB/s',                     75.00, 20, 4) ON DUPLICATE KEY UPDATE id = id;
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria_id) VALUES
 (8, 'Disco Duro HDD 2TB',               '7200 RPM, ideal para respaldo',               55.00, 18, 4) ON DUPLICATE KEY UPDATE id = id;

-- ---------- PEDIDO DE EJEMPLO ----------
-- Pedido del cliente (id 2) con dos lineas. Total = 320 + 45 = 365.
INSERT INTO pedidos (id, usuario_id, estado, total) VALUES
 (1, 2, 'NUEVO', 365.00) ON DUPLICATE KEY UPDATE id = id;

INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
 (1, 1, 3, 1, 320.00, 320.00) ON DUPLICATE KEY UPDATE id = id;
INSERT INTO detalle_pedidos (id, pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
 (2, 1, 5, 1,  45.00,  45.00) ON DUPLICATE KEY UPDATE id = id;
