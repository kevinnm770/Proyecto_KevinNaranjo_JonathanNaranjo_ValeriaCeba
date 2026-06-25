-- ============================================================
-- ESQUEMA DE LA BASE DE DATOS - Tienda de componentes (pedidos)
-- Motor: MySQL 8.  Se ejecuta automaticamente al arrancar la app.
-- "IF NOT EXISTS" evita errores si las tablas ya existen.
-- ============================================================

-- Usuarios del sistema (dos perfiles: ADMIN y CLIENTE)
CREATE TABLE IF NOT EXISTS usuarios (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(100) NOT NULL,
    password    VARCHAR(100) NOT NULL,          -- guardada cifrada con BCrypt
    nombre      VARCHAR(150),
    email       VARCHAR(150),
    rol         VARCHAR(20)  NOT NULL DEFAULT 'CLIENTE',  -- ADMIN | CLIENTE
    activo      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_usuarios_username (username)
);

-- Categorias de productos (CPU, GPU, Almacenamiento, etc.)
CREATE TABLE IF NOT EXISTS categorias (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    UNIQUE KEY uq_categorias_nombre (nombre)
);

-- Catalogo de productos (el "menu de productos")
CREATE TABLE IF NOT EXISTS productos (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(150)  NOT NULL,
    img_src      VARCHAR(500),
    descripcion  TEXT,
    precio       DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    stock        INT           NOT NULL DEFAULT 0,   -- inventario disponible
    categoria_id BIGINT,
    activo       BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at   DATETIME      DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_productos_categoria FOREIGN KEY (categoria_id)
        REFERENCES categorias(id) ON DELETE SET NULL
);

-- Pedidos realizados por los clientes
CREATE TABLE IF NOT EXISTS pedidos (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id  BIGINT        NOT NULL,
    estado      VARCHAR(20)   NOT NULL DEFAULT 'NUEVO',  -- NUEVO | EN_PREPARACION | ENTREGADO
    total       DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at  DATETIME      DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pedidos_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Lineas de cada pedido (que productos y cuantos) = "carrito" ya confirmado
CREATE TABLE IF NOT EXISTS detalle_pedidos (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id       BIGINT        NOT NULL,
    producto_id     BIGINT        NOT NULL,
    cantidad        INT           NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    subtotal        DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (pedido_id)
        REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id)
        REFERENCES productos(id)
);
