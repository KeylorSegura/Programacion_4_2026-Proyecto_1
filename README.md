# Proyecto 1 - Programacion 4 (2026)

Portal de empleo desarrollado con Spring Boot. Permite a empresas publicar puestos de trabajo y a oferentes buscar empleo segun sus habilidades.

## Requisitos

- Java 17
- MySQL 8+
- Maven (o usar el wrapper incluido `mvnw`)

## Configuracion de la base de datos

1. Crear la base de datos ejecutando el script:
   ```
   src/main/database/Base-de-Datos-Proyecto1-Programacion4.sql
   ```
2. La conexion por defecto usa `localhost:3306` con usuario `root` y clave `root`. Se puede cambiar en `src/main/resources/application.properties`.

## Como ejecutar

```bash
./mvnw spring-boot:run
```

La aplicacion estara disponible en `http://localhost:8080`.

## Roles de usuario

| Rol | Descripcion |
|---|---|
| Administrador | Aprueba empresas y oferentes, gestiona el arbol de habilidades |
| Empresa | Publica puestos de trabajo y consulta candidatos |
| Oferente | Registra habilidades, sube CV y busca puestos |

Las cuentas de Empresa y Oferente requieren aprobacion de un Administrador antes de poder iniciar sesion.

## Tecnologias

- Spring Boot 4.0.3
- Spring Security (autenticacion con BCrypt)
- Spring Data JPA + Hibernate
- Thymeleaf
- MySQL