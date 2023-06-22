<h1 align="center">Plaza de comidas</h1>
<h2>Microservicio Usuario</h2>
<img src="https://img.shields.io/badge/%E2%98%95%20Java-%23c98524.svg?style=logoColor=white" alt="Logo Java" />
<img src="https://img.shields.io/badge/-MySQL-005C84?style=flat-square&logo=mysql&logoColor=black" alt="Logo Mysql" />
<img src="https://img.shields.io/badge/Swagger-%2385EA2D.svg?&style=flat-square&logo=swagger&logoColor=blue" alt="Logo Swagger" />
<img src="https://img.shields.io/badge/Spring%20Security-%23569A31.svg?&style=flat-square&logo=spring&logoColor=white" alt="Logo Spring Security" />
<img src="https://img.shields.io/badge/Amazon%20RDS%20MariaDB-%23FF9900.svg?&style=flat-square&logo=amazonaws&logoColor=white&color=FF9900" alt="Amazon RDS MariaDB" />

<p>Encargado de administrar los roles, creacion de cuentas y login de los usuarios de la plaza de comidas.
<br></p>

## Descripción del proyecto

<p>Para el inicio de sesion nos autenticamos con nuestro email y password.
como resultado obtenemos un token donde contiene nuestro email, rol, nombre y apellido. Que usaremos para cada peticion</p>

    {
        "email" : String,
        "password" : String
    }

Se usa <span title="Es un algoritmo de hash de contraseñas diseñado específicamente para el almacenamiento seguro de contraseñas. Es conocido por su robustez y capacidad para resistir ataques de fuerza bruta. Bcrypt utiliza una combinación de hashing y salting para proteger las contraseñas.">Bcrypt</span> para la encriptacion del password. Para la creacion de una cuenta como CLIENTE no se necesita ingresar el token.

    {
        "name" : String,
        "lastName" : String,
        "identificationDocument" : Long,
        "cellPhone" : String,
        "email" : String,
        "password" : String,
        "idRol" : Long
    }

Pero para la creacion de cuentas para un propietario de un restaurante se debe tener un rol de ADMINISTRADOR y haberse logeado e ingresado el token en el header de la peticion.

    {
        "name" : String,
        "lastName" : String,
        "identificationDocument" : Long,
        "cellPhone" : String,
        "email" : String,
        "password" : String,
    }


Para crear cuentas para los empleados de los restaurantes, se requiere un rol de PROPIETARIO y un token válido.
Es necesario que el restaurante ya haya sido creado previamente. Se utiliza WebClient para consumir un servicio REST que guarda el id del restaurante y el id del empleado en la tabla "restaurante_empleado" de la DB plazoleta. El email se obtiene del token y se utiliza para buscar la información del usuario propietario. Luego se envían en el cuerpo de la peticion los id para obtener el restaurante asociado al propietario y se guarda el registro en la DB(DataBase).

<p align="center"><img src="https://github.com/JaiderMartinez/Usuario/assets/119683594/60eb085a-20be-44e6-99b1-f452e523b589" width="200" alt="tabla del restaurante_empleado"> <br>id_restaurante_empleado: llave primaria de la tabla.<br>id_restaurante: campo que especifica a que restaurante pertenece.<br>id_usuario_empleado: campo que funcionara como llave foranea a la tabla usuarios.</p>

    {
        "name" : String,
        "lastName" : String,
        "identificationDocument" : Long,
        "cellPhone" : String,
        "email" : String,
        "password" : String,
        "idRol" : Long,
        "idRestaurant" : Long
    }

## Requisitos previos

<ul>
    <li>Java 8 o superior</li>
    <li>Gradle - Groovy</li>
    <li>Instancia del servicio de RDS de AWS</li>
</ul>

## Funcionalidades
- `Historia de usuario 1`: Crear Propietario
- `Historia de usuario 6`: Crear cuenta empleado
- `Historia de usuario 8`: Crear cuenta Cliente

## Configuración de la instancia de RDS de MariaDB
Para utilizar el microservicio Usuario, es necesario configurar y tener acceso a una instancia de Amazon RDS con una base de datos MariaDB. A continuación, se detallan los pasos para configurar la instancia:

<ul>
    <li>Creación de la instancia de RDS de MariaDB</li>
    <li>Obtención de los detalles de conexión</li>
    <li>Obtención de los detalles de conexión</li>
    <li>Configuración en el microservicio Usuario: </li>Actualiza la configuración de la base de datos con los detalles de conexión de tu instancia de RDS:

    spring.datasource.url=jdbc:mysql://<endpoint-de-rds>:<puerto>/<nombre-database>
    spring.datasource.username=<usuario-de-la-database>
    spring.datasource.password=<contraseña>    

</ul>

## Spring Security

La API está protegida mediante Spring Security y el control de acceso basado en roles.

<li>Url de swagger: <strong>http://localhost:8090/swagger-ui/index.html</strong></li>
<li>Url para el login: <strong>localhost:8090/user-micro/auth/login</strong> como resultado me devuelve un access token</li>

    access.token.validity.seconds=<duracion-expiración-token>
    access.token.secret=<clave-secreta>    

### Generación de tokens
Cuando un usuario inicia sesión correctamente, se genera un token de acceso que contiene la información de autenticación y autorización necesaria para realizar solicitudes posteriores. El token se genera utilizando el algoritmo de firma JSON Web Tokens (JWT) y se firma con una clave secreta compartida.

El token contiene la siguiente información:

Subject (sub): Email único del usuario autenticado.<br>
Roles (rol): Los roles asignados al usuario, que determinan sus permisos en la aplicación.<br>
Fecha de expiración (exp): La fecha y hora en la que el token expirará y ya no será válido.    
Name: nombre del usuario autenticado<br>
LastName: Apellido del usuario autenticado. 

## 🛠️ Abre y ejecuta el proyecto

<ul>
    <li>Diagramas de wireframes, Url: <strong><a href="https://app.moqups.com/OUdC5drISYQhJ9c7UvgCrmkoWVqBrYtx/view/page/a3afcac31?ui=0">create account user</a></strong></li>
    <li>Url para crear usuario con rol de cliente: <strong>localhost:8090/user-micro/user/customer</strong></li>
    <li>Url para crear usuario con rol de propietario: <strong>localhost:8090/user-micro/user/owner</strong> tener en cuenta que para crear esta cuenta necesito haberme autenticado y tener el rol de ADMINISTRADOR e ingresar el token en el header</li>
    <li>Url para crear usuario con rol de empleado: <strong>localhost:8090/user-micro/user/employee</strong> tener en cuenta que para crear esta cuenta necesito haberme autenticado y tener el rol de PROPIETARIO e ingresar el token en el header</li>
</ul>

## Siguiente Microservicio <a href="https://github.com/JaiderMartinez/power_up_v2_plazoleta.git">Plazoleta</a>