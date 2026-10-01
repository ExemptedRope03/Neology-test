# Prueba Técnica Neology - Parking

Aplicación de estacionamiento compuesta por un backend Spring Boot y un frontend Angular.

| Proyecto | Tecnología | Puerto |
| --- | --- | --- |
| `parking/` | Java 17, Spring Boot, JPA, H2 y Maven | `8080` |
| `parking-page/` | Angular 17, Angular Material y TypeScript | `4200` |

## Requisitos

- Java 17 o superior.
- Node.js 18.13 o superior y npm.
- No se requiere instalar una base de datos externa: H2 está incluida.

## Levantar el backend

Desde la raíz del repositorio:

```bash
cd parking
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`. Puedes consultar Swagger en `http://localhost:8080/swagger-ui/index.html` y la consola H2 en `http://localhost:8080/h2-console`.

El backend permite peticiones del frontend ubicado en `http://localhost:4200` mediante CORS.

## Levantar el frontend

En otra terminal, desde la raíz del repositorio:

```bash
cd parking-page
npm install
npm start
```

Abre `http://localhost:4200`. La URL de la API está centralizada en `parking-page/src/environments/environment.ts` y apunta a `http://localhost:8080/neo`.

## Ejecutar pruebas

### Backend

```bash
cd parking
./mvnw clean test
```

Las pruebas unitarias usan JUnit 5 y Mockito; los repositorios son mocks, por lo que no modifican H2. El comando también genera cobertura JaCoCo en:

```text
parking/target/site/jacoco/index.html
```

### Frontend

```bash
cd parking-page
npm test
```

Las pruebas de Angular se ejecutan con Jasmine y Karma.

## Extras de frontend incluidos

- Filtro por placa, paginación y ordenamiento en el listado de vehículos.
- Formularios reactivos con campos obligatorios y validación de placa.
- Angular Material para tablas, formularios, diálogos, botones y notificaciones.
- Protección de entradas mediante lista permitida para placas (`letras`, `números` y `-`); Angular usa interpolación segura y no inserta contenido de usuario con `innerHTML`.
- Pruebas Jasmine/Karma para la normalización/validación de placa y el servicio HTTP.

## Capturas del frontend

### Panel de estacionamiento

![Panel de estacionamiento](docs/images/View%20panel.png)

### Vehículos

Listado de vehículos con búsqueda por placa, ordenamiento, paginación y acciones disponibles.

![Listado de vehículos](docs/images/View%20vehicles.png)

![Búsqueda por placa](docs/images/Search%20vehicle%20plate.png)

![Alta de vehículo](docs/images/Vehicle%20create.png)

### Estancias

![Vista de estancias](docs/images/View%20stay.png)

![Registro de entrada o salida](docs/images/Register%20stay.png)

### Reporte de pagos

![Reporte de pagos](docs/images/View%20payment%20report.png)

![Selector de filtro del reporte](docs/images/Filter%20dropdown%20payment%20report.png)

![Reporte filtrado](docs/images/Filteres%20payment%20report.png)

![Generación de CSV](docs/images/Generate%20csv.png)

### Configuración

![Inicio de nuevo mes](docs/images/Conf%20.png)

## Perfiles de base de datos H2

El backend tiene dos perfiles para elegir el tipo de base de datos.
La base de datos se puede consultar una vez desplegada la aplicacion aqui:
   - http://localhost:8080/h2-console/

### Base limpia en memoria (predeterminada)

No requiere configuración adicional:

```bash
cd parking
./mvnw spring-boot:run
```

Usa `jdbc:h2:mem:parking`. Las tablas se crean al iniciar y se eliminan al detener el servidor; es la opción indicada para desarrollar o ejecutar pruebas manuales desde cero.

En la consola H2 utiliza:

```text
JDBC URL: jdbc:h2:mem:parking
Usuario: sa
Contraseña: [vacía]
```

### Base de prueba persistente `parkingdb` (opcional)

La base incluida está en `parking/data/parkingdb.mv.db`. Para usarla, activa el perfil `parkingdb`:

```bash
cd parking
./mvnw spring-boot:run -Dspring-boot.run.profiles=parkingdb
```

Este perfil usa `jdbc:h2:file:./data/parkingdb` y `spring.jpa.hibernate.ddl-auto=update`, por lo que conserva la información del archivo entre reinicios. No usa `create-drop`, evitando que se borren sus datos al apagar el servidor.

En la consola H2 utiliza:

```text
JDBC URL: jdbc:h2:file:./data/parkingdb
Usuario: sa
Contraseña: [vacía]
```

Solo ejecuta una instancia del backend a la vez, porque ambas configuraciones usan el puerto `8080`.

## Endpoints principales

| Método | Ruta | Descripción |
| --- | --- | --- |
| POST | `/neo/vehiculos/oficiales` | Alta de vehículo oficial |
| POST | `/neo/vehiculos/residentes` | Alta de vehículo residente |
| POST | `/neo/vehiculos/no-residentes` | Alta de vehículo no residente |
| GET | `/neo/vehiculos` | Listado de vehículos |
| GET | `/neo/vehiculos/{placa}` | Detalle y estancias del vehículo |
| PATCH | `/neo/vehiculos/{placa}` | Cambia el estado con `{ "status": "ACTIVE" }` o `{ "status": "INACTIVE" }` |
| GET | `/neo/estancias` | Listado de estancias con estado y costo |
| POST | `/neo/estancias/entrada` | Registro de entrada |
| POST | `/neo/estancias/salida` | Registro de salida |
| GET | `/neo/residentes/pagos` | Reporte de pagos acumulados |
| GET | `/neo/reportes/pagos?tipo={tipo}` | Reporte de pagos por tipo (parámetro opcional) |
| POST | `/neo/mes/iniciar` | Inicio de un nuevo mes |
