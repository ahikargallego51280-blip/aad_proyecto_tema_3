CREATE DATABASE IF NOT EXISTS gestion_proyectos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestion_proyectos;

CREATE TABLE IF NOT EXISTS proveedores (
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    direccion_postal VARCHAR(255) NOT NULL,
    PRIMARY KEY (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS piezas (
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    descripcion VARCHAR(1000) NULL,
    PRIMARY KEY (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS proyectos (
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    PRIMARY KEY (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS gestiones (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    cantidad INT NOT NULL,
    proveedor_codigo VARCHAR(20) NOT NULL,
    pieza_codigo VARCHAR(20) NOT NULL,
    proyecto_codigo VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_gestiones_proveedor
        FOREIGN KEY (proveedor_codigo) REFERENCES proveedores (codigo),
    CONSTRAINT fk_gestiones_pieza
        FOREIGN KEY (pieza_codigo) REFERENCES piezas (codigo),
    CONSTRAINT fk_gestiones_proyecto
        FOREIGN KEY (proyecto_codigo) REFERENCES proyectos (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
