# SitiEnergia - Prototipo funcional inicial

Sistema Integrado de Tareas e Incidencias Energéticas. Primera entrega del TCC: autenticación con JWT y CRUD básico de usuarios y categorías de incidencias.

## Estructura

- `backend/`: API Spring Boot 3, Java 17, Spring Security, JWT, JPA y PostgreSQL.
- `frontend/`: aplicación Flutter con Material 3.

## Backend

Requisitos: Java 17 y Maven 3.9+.

```powershell
cd backend
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080` localmente y puede desplegarse en Render usando las variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `ADMIN_PASSWORD` y `PORT`.

La contraseña se lee desde la variable de entorno `DB_PASSWORD`. Define la contraseña local antes de iniciar:

```powershell
$env:DB_PASSWORD = "tu_contraseña"
$env:JWT_SECRET = "una_clave_larga_de_al_menos_32_caracteres"
$env:ADMIN_PASSWORD = "tu_contraseña_de_administrador"
```

Usuario inicial:

- El usuario administrador se crea con el correo `admin@sitienergia.com`.
- La contraseña corresponde al valor definido en `ADMIN_PASSWORD`.

## Endpoints principales

| Método | Ruta | Acceso |
| --- | --- | --- |
| POST | `/api/auth/login` | Público |
| POST | `/api/auth/register` | Público; crea clientes |
| GET | `/api/usuarios` | ADMIN |
| POST | `/api/usuarios` | ADMIN |
| PUT | `/api/usuarios/{id}` | ADMIN |
| DELETE | `/api/usuarios/{id}` | ADMIN; desactiva |
| GET | `/api/categorias` | Usuario autenticado |
| POST/PUT/DELETE | `/api/categorias[/{id}]` | ADMIN |

Las rutas protegidas reciben `Authorization: Bearer <token>`.

## Frontend

Requisitos: Flutter 3.16+.

```powershell
cd frontend
flutter pub get
flutter run
```

Para probar desde el teléfono conectado por USB, se usa `adb reverse tcp:8080 tcp:8080`. Para probar desde otros dispositivos o redes, inicia `ngrok http 8080` y ejecuta Flutter con `--dart-define=API_BASE_URL=https://tu-url.ngrok-free.app/api`.

## Alcance de esta entrega

- Login y registro con contraseñas protegidas mediante BCrypt.
- JWT con rol (`ADMIN`, `TECNICO`, `CLIENTE`) y autorización por endpoint.
- Alta, consulta, actualización y desactivación lógica de usuarios.
- Alta, consulta, actualización y desactivación lógica de categorías.
- Datos iniciales para probar el panel administrativo sin configuración adicional.