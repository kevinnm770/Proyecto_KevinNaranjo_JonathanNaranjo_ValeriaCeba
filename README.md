# TecnoTienda - Sistema de pedidos (Proyecto Final Programacion Web 2)

Tienda de componentes electronicos / hardware. Permite a los clientes ver el catalogo y
crear pedidos, y al administrador gestionar inventario y estados de pedidos.

## Tecnologias
- Spring Boot (Web MVC + Thymeleaf)
- Spring Data JDBC + MySQL
- Spring Security (perfiles ADMIN y CLIENTE)
- Maven

## Como ejecutar (paso a paso)

1. **Levantar la base de datos con Docker** (en la carpeta del proyecto):
   ```
   docker compose up -d
   ```
   Esto crea una base MySQL llamada `tiendadb` con usuario `tienda` / clave `tienda123`.

2. **Correr la aplicacion** desde IntelliJ IDEA (boton Run sobre `TiendaPedidosApplication`)
   o por consola:
   ```
   ./mvnw spring-boot:run
   ```
   Al arrancar se ejecutan `schema.sql` y `data.sql` automaticamente.

3. **Abrir en el navegador:** http://localhost:8080

## Usuarios de prueba
| Usuario  | Contrasena  | Rol     |
|----------|-------------|---------|
| admin    | admin123    | ADMIN   |
| cliente  | cliente123  | CLIENTE |

## API REST (probar con Postman)
- `GET    /api/v1/productos`        -> lista de productos
- `GET    /api/v1/productos/{id}`   -> un producto
- `POST   /api/v1/productos`        -> crear producto
- `PATCH  /api/v1/productos/{id}`   -> actualizar producto
- `GET    /api/v1/pedidos`          -> lista de pedidos
- `PATCH  /api/v1/pedidos/{id}`     -> cambiar estado del pedido

> En `postman/` hay una coleccion lista para importar.

## Estructura por capas
```
data/        -> entidades (Usuario, Categoria, Producto, Pedido, DetallePedido)
repository/  -> interfaces Spring Data JDBC
service/     -> logica de negocio
controller/  -> vistas Thymeleaf (@Controller)
api/         -> endpoints REST (@RestController)
security/    -> configuracion de seguridad y perfiles
```

## Pendientes (TODO) para el equipo
Buscar los comentarios `TODO` en el codigo: ahi esta lo que falta implementar.
