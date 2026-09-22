# SitiEnergia

## Sistema Integrado de Tareas e Incidencias Energéticas

SitiEnergia es el prototipo funcional inicial desarrollado para el Trabajo Colaborativo Contextualizado de la asignatura Desarrollo de Apps. El sistema está orientado al registro y gestión de incidencias y servicios técnicos del sector eléctrico.

## Objetivo del prototipo

La primera entrega implementa:

- Autenticación de usuarios.
- Registro de nuevos clientes.
- Inicio de sesión con correo y contraseña.
- Autorización mediante roles.
- CRUD básico de usuarios.
- CRUD básico de categorías de incidencias.
- Persistencia de información en PostgreSQL.
- Aplicación móvil Flutter para Android.

## Arquitectura

El proyecto utiliza una arquitectura cliente-servidor de tres capas:

### Frontend

Aplicación móvil desarrollada con Flutter y Dart. Permite:

- Iniciar sesión.
- Visualizar el panel principal.
- Consultar categorías de incidencias.
- Crear nuevas categorías desde el panel administrativo.
- Editar y desactivar categorías desde el panel administrativo.
- Consultar usuarios registrados.
- Editar y desactivar usuarios desde el panel administrativo.
- Registrar nuevos clientes desde la pantalla de creación de cuenta.
- Cerrar sesión.

### Backend

API REST desarrollada con Spring Boot y Java 17. Contiene:

- Controladores REST.
- Servicios de autenticación.
- Seguridad con Spring Security.
- Tokens JWT.
- Contraseñas protegidas con BCrypt.
- Repositorios mediante Spring Data JPA.
- Validación de datos recibidos.
- Control de acceso por rol.

### Base de datos

Se utiliza PostgreSQL con la base de datos:

```text
sitienergia
```

La conexión se configura en `backend/src/main/resources/application.yml`.

Entidades principales:

- `usuarios`
- `categorias_incidencia`

Hibernate utiliza `ddl-auto: update`, por lo que crea o actualiza las tablas automáticamente al iniciar el backend.

## Roles del sistema

El sistema contempla tres roles:

| Rol | Descripción |
| --- | --- |
| `ADMIN` | Administra usuarios y categorías. Tiene acceso completo al sistema. |
| `TECNICO` | Rol destinado a la atención y actualización de incidencias asignadas. |
| `CLIENTE` | Puede registrarse, iniciar sesión y consultar sus solicitudes. |

El usuario administrador inicial es:

```text
Correo: admin@sitienergia.com
Contraseña: definida mediante `ADMIN_PASSWORD`
```

## Endpoints implementados

### Autenticación

| Método | Ruta | Acceso | Función |
| --- | --- | --- | --- |
| `POST` | `/api/auth/login` | Público | Inicia sesión y devuelve un JWT. |
| `POST` | `/api/auth/register` | Público | Registra un nuevo cliente. |

### Usuarios

| Método | Ruta | Acceso | Función |
| --- | --- | --- | --- |
| `GET` | `/api/usuarios` | `ADMIN` | Lista todos los usuarios. |
| `POST` | `/api/usuarios` | `ADMIN` | Crea un usuario. |
| `PUT` | `/api/usuarios/{id}` | `ADMIN` | Actualiza un usuario. |
| `DELETE` | `/api/usuarios/{id}` | `ADMIN` | Desactiva un usuario. |

### Categorías

| Método | Ruta | Acceso | Función |
| --- | --- | --- | --- |
| `GET` | `/api/categorias` | Usuario autenticado | Lista categorías. |
| `POST` | `/api/categorias` | `ADMIN` | Crea una categoría. |
| `PUT` | `/api/categorias/{id}` | `ADMIN` | Actualiza una categoría. |
| `DELETE` | `/api/categorias/{id}` | `ADMIN` | Desactiva una categoría. |

Las rutas protegidas utilizan el encabezado:

```text
Authorization: Bearer <token>
```

## Categorías iniciales

Al iniciar la aplicación se cargan tres categorías de ejemplo:

- Falla en red de baja tensión - Redes Eléctricas.
- Poste averiado - Obras Civiles.
- Revisión de medidor - Comercial.

## Tecnologías utilizadas

- Flutter 3.47.4.
- Dart 3.13.3.
- Spring Boot 3.5.16.
- Java 21 instalado; el proyecto compila con compatibilidad Java 17.
- Spring Security.
- JWT mediante JJWT.
- BCrypt.
- Spring Data JPA.
- PostgreSQL 18.6.
- Maven 3.9.16.
- Android SDK.
- Git como herramienta prevista para control de versiones.

## Configuración de PostgreSQL

El archivo `application.yml` utiliza:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/sitienergia
    username: postgres
    password: ${DB_PASSWORD}
```

La contraseña puede establecerse mediante una variable de entorno:

```powershell
$env:DB_PASSWORD="tu_contraseña"
```

Para un entorno real se recomienda no guardar contraseñas directamente en archivos de configuración y utilizar variables de entorno o un gestor de secretos.

## Cómo iniciar el backend

Abrir una terminal en la carpeta `backend` y ejecutar:

```powershell
cd "C:\Users\luisa\OneDrive\Desktop\MATERIAS U\Desarrollo de Apps\TCC\Registro_incidencias\backend"
$env:DB_PASSWORD="tu_contraseña"
mvn spring-boot:run
```

El backend queda disponible en:

```text
http://localhost:8080
```

Para probarlo desde un teléfono conectado a la misma red Wi-Fi, el backend está configurado para escuchar en todas las interfaces mediante `0.0.0.0`.

## Cómo iniciar Flutter

Abrir otra terminal en la carpeta `frontend`:

```powershell
cd "C:\Users\luisa\OneDrive\Desktop\MATERIAS U\Desarrollo de Apps\TCC\Registro_incidencias\frontend"
flutter pub get
flutter run -d KN7THQHYORAILFMF
```

`KN7THQHYORAILFMF` corresponde al teléfono Android conectado por USB. También puede reemplazarse por el identificador de otro dispositivo disponible.

La aplicación utiliza un túnel USB hacia el backend:

```text
http://127.0.0.1:8080/api
```

Antes de ejecutar Flutter debe crearse el túnel con `adb reverse tcp:8080 tcp:8080`.

## Validaciones realizadas

- El backend compila correctamente con Maven.
- `mvn test` termina con `BUILD SUCCESS`.
- Spring Boot inicia correctamente.
- PostgreSQL acepta la conexión del backend.
- Hibernate crea o actualiza las tablas de la base de datos.
- El login con JWT fue probado correctamente.
- La consulta autenticada de categorías fue probada correctamente.
- Flutter pasa `flutter analyze` sin errores.
- Las pruebas Flutter pasan correctamente.
- El APK de depuración se genera correctamente.
- La aplicación se instaló y ejecutó en un emulador Android.

## Alcance actual y trabajo futuro

El prototipo cumple con la primera entrega de autenticación y CRUD básico visible desde la aplicación. El registro público crea usuarios con rol `CLIENTE` y el administrador puede modificar roles, editar datos y desactivar cuentas o categorías.

Como ampliaciones futuras se pueden implementar:

- Pantallas visuales para editar y desactivar usuarios.
- Edición y desactivación de categorías desde Flutter.
- Registro completo de incidencias.
- Asignación de técnicos.
- Actualización del estado de incidencias.
- Historial de estados.
- Servicios comunitarios.
- Evidencias fotográficas.
- Notificaciones push.
- Reportes y estadísticas.
- Configuración de producción con PostgreSQL y variables seguras.
