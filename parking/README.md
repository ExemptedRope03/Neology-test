# Parking Neology

API REST para gestión de estacionamiento con Spring Boot 3, Java 17, JPA/Hibernate y H2 en memoria.

## Ejecutar y probar

```bash
./mvnw spring-boot:run
./mvnw test
```

Swagger está disponible en `http://localhost:8080/swagger-ui/index.html`; la consola H2 en `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:parking`).

## Endpoints

| Método | Ruta | Ejemplo de cuerpo |
| --- | --- | --- |
| POST | `/neo/vehiculos/oficiales` | `{"placa":"OFI-001"}` |
| POST | `/neo/vehiculos/residentes` | `{"placa":"RES-001"}` |
| POST | `/neo/vehiculos/no-residentes` | `{"placa":"VIS-001"}` |
| POST | `/neo/estancias/entrada` | `{"placa":"RES-001"}` |
| POST | `/neo/estancias/salida` | `{"placa":"RES-001"}` |
| GET | `/neo/residentes/pagos` | — |
| POST | `/neo/mes/iniciar` | — |

Las estancias se miden en minutos completos. Los residentes acumulan minutos al salir y se cobran en el informe mensual ($0.05/min). Los no residentes se cobran en su salida ($0.50/min); los oficiales no pagan.
