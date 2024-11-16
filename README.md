# Documentación Técnico-Funcional: Sports Supplements

## Información del Proyecto

- **Título:** Sports Supplements
- **Enlace:** [frangd.duckdns.org](https://frangd.duckdns.org/)
- **Autor:** Francisco José Gallego Dorado

---

## Descripción del Proyecto

Este proyecto tiene como objetivo desarrollar una aplicación web para la venta de suplementos deportivos. Los usuarios podrán registrarse, activar sus cuentas, modificar contraseñas, iniciar sesión, gestionar perfiles, agregar suplementos al carrito, realizar compras y visualizar sus pedidos. Los administradores tendrán control sobre la gestión de usuarios y suplementos.

El proyecto interactúa con una base de datos externa MariaDB versión 11.3.2.

Tecnologías utilizadas:
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.2.5
- **Motor de Plantillas:** Thymeleaf
- **Correo Electrónico:** Spring Mail
- **Pasarela de Pago:** API de PayPal

---

## Funcionalidades de la Aplicación Web

### Usuario

- **Registro de Usuarios:** Permite a los usuarios registrarse en la plataforma.
- **Activación de Cuenta:** Activación mediante enlace enviado por correo electrónico.
- **Autenticación:** Inicio de sesión seguro para los usuarios.
- **Recuperación de Contraseña:** Proceso de recuperación de contraseña mediante correo electrónico.
- **Visualización de Suplementos:** Ver todos los suplementos disponibles con opciones de filtrado.
- **Gestión de Carrito:** Agregar y eliminar suplementos del carrito de compras.
- **Proceso de Compra:** Realizar la compra de los suplementos agregados al carrito.
- **Historial de Pedidos:** Ver todos los pedidos realizados por el usuario.
- **Generación de PDF:** Generar un PDF con los detalles de un pedido.
- **Gestión de Perfil:** Editar información del perfil del usuario.
- **Eliminación de Cuenta:** Permite a los usuarios eliminar su cuenta de la plataforma.

### Administrador

- **Autenticación:** Inicio de sesión seguro para administradores.
- **Recuperación de Contraseña:** Proceso de recuperación de contraseña mediante correo electrónico.
- **Visualización de Suplementos:** Ver todos los suplementos disponibles con opciones de filtrado.
- **Gestión de Carrito:** Agregar y eliminar suplementos del carrito de compras.
- **Proceso de Compra:** Realizar la compra de los suplementos agregados al carrito.
- **Historial de Pedidos:** Ver todos los pedidos realizados.
- **Generación de PDF:** Generar un PDF con los detalles de un pedido.
- **Gestión de Usuarios:** Agregar, modificar y eliminar usuarios.
- **Gestión de Suplementos:** Agregar, modificar y eliminar suplementos.

---

## Arquitectura del Proyecto

El proyecto sigue una arquitectura Modelo-Vista-Controlador (MVC), que separa la lógica de negocio, la interfaz de usuario y el control de flujo, facilitando la mantenibilidad y escalabilidad de la aplicación.

---

## Configuración del Entorno de Desarrollo

### Requisitos Previos

- **Java 17:** Asegúrate de tener instalado JDK 17.
- **Maven:** Necesario para la gestión de dependencias y la construcción del proyecto.
- **MariaDB 11.3.2:** Base de datos utilizada por la aplicación.
- **IDE:** Se recomienda usar Eclipse, Visual Studio Code o IntelliJ IDEA.
- **Lombok:** Será necesario tener instalado Lombok para el funcionamiento del proyecto. En el caso de no tenerlo se tendrá que modificar todos los DAOs (Entidades) y generar Getters, Setters y constructores.

### Instalación

1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/fragalado/ProyectoFinalDAW.git
   cd ProyectoFinalDAW

2. **Configurar application.properties**
- Primero tendrás que crear una base de datos con el nombre que quieras.
- Segundo tendrás que actualizar el applicacion.properties poniendo el nombre de la base de datos, usuario y contraseña.
- Tercero tendrás que actualizar los datos del mail para que funcione el envío de correos.
- Cuarto tendrás que actualizar los datos de paypal para que funcione.

3. **Configurar datos.properties**
- Actualizar los datos de datos.properties para que funcione el envío de correos y demás funciones como Paypal y Log.

---

## Contacto
Para cualquier duda o consulta, puedes contactar con el autor a través del email frandanigd@gmail.com.
