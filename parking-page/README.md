# Parking Neology - Frontend

Aplicación Angular 17 con Angular Material para operar el backend Spring Boot de `../parking`.

## Puesta en marcha

1. Inicia el backend: desde `../parking`, ejecuta `./mvnw spring-boot:run`.
2. Instala las dependencias del frontend: `npm install`.
3. Inicia Angular: `npm start`.
4. Abre `http://localhost:4200`.

La URL de la API es `http://localhost:8080/neo` y está centralizada en `src/environments/environment.ts`.

## Pantallas

- Panel: alta de vehículos oficiales, residentes o no residentes e inicio de mes.
- Vehículos: listado con filtro por placa, orden, paginación y acceso al detalle.
- Estancias: registro de entrada y salida.
- Pagos residentes: informe mensual y total acumulado.

## Calidad

Los formularios usan Reactive Forms y validan placas antes de enviar datos. Angular escapa el contenido interpolado y la utilidad de placas elimina caracteres no admitidos. Las pruebas se ejecutan con `npm test` (Jasmine + Karma).
