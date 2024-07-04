# Users_WorkShop

# Shopping User Microservice

Este repositorio contiene el código fuente de un microservicio de gestión de usuarios para una tienda online, desarrollado con Java Spring Boot. El microservicio permite gestionar operaciones relacionadas con usuarios, como crear usuarios nuevos, obtener información de usuarios existentes, actualizar datos de usuarios y eliminar usuarios.

## Tabla de Contenidos

- [Descripción](#descripción)
- [Características](#características)
- [Arquitectura](#arquitectura)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [API Endpoints](#api-endpoints)

## Descripción

El microservicio de gestión de usuarios permite administrar usuarios en una tienda online. Proporciona endpoints RESTful para crear nuevos usuarios, obtener información detallada de usuarios, actualizar datos de usuarios y eliminar usuarios de forma segura.

## Características

- Crear nuevos usuarios
- Obtener información detallada de usuarios
- Actualizar datos de usuarios
- Eliminar usuarios
- Validación de datos de usuarios (por ejemplo, correo electrónico único)
- Seguridad de acceso a los endpoints

## Arquitectura

Este microservicio sigue una arquitectura de microservicios basada en Spring Boot. Utiliza una base de datos relacional para almacenar los datos de usuarios y proporciona una API RESTful para interactuar con los usuarios.

## Tecnologías Utilizadas

- Java 11
- Spring Boot
- Spring Data JPA
- H2 Database (para pruebas y desarrollo)
- MySQL (para producción)
- Maven

## Requisitos Previos

- JDK 11 o superior
- Maven 3.6.0 o superior
- MySQL (para entorno de producción)

## Instalación

1. Clona este repositorio:
   ```bash
   git clone https://github.com/nurvoz/User_microservice.git
    ```
2. Navega al directorio del proyecto:
    ```bash
    cd User_microservice
    ```
3. Compila y ejecuta el microservicio:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

