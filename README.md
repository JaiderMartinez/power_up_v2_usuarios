# <h1 align="center">Plaza de comidas</h1></center>
<h2>Microservicio Usuario</h2>
<img src="https://img.shields.io/badge/%E2%98%95%20Java-%23c98524.svg?style=logoColor=white" alt="Logo Java" />
<img src="https://img.shields.io/badge/-MySQL-005C84?style=flat-square&logo=mysql&logoColor=black" alt="Logo Mysql" />
<img src="https://img.shields.io/badge/Swagger-%2385EA2D.svg?&style=flat-square&logo=swagger&logoColor=blue" alt="Logo Swagger" />
<p>Encargado de administrar los roles, creacion de cuentas y login de los usuarios de la plaza de comidas.
<br></p>

## Descripción del proyecto

<p>Para el inicio de sesion se debe ingresar con email y password.
como resultado obtenemos un token donde contiene nuestro email, rol, nombre y apellido</p>

    {
        "email" : String,
        "password" : String
    }

Se usa Bcrypt para la encriptacion del password. Para la creacion de una cuenta como CLIENTE no se necesita haberse autenticado.

    {
        "name" : String,
        "lastName" : String,
        "identificationDocument" : Long,
        "cellPhone" : String,
        "email" : String,
        "password" : String,
        "idRol" : Long
    }

Pero para la creacion de cuentas para un propietario de un restaurante se debe tener un rol de ADMIN y haber ingresado el token del resultado del incio de sesion.

    {
        "name" : String,
        "lastName" : String,
        "identificationDocument" : Long,
        "cellPhone" : String,
        "email" : String,
        "password" : String,
    }


Para crear cuentas para los empleados de los restaurantes, se requiere un rol de PROPIETARIO y un token válido. Es necesario que el restaurante ya haya sido creado previamente. Se utiliza WebClient para consumir un servicio REST que guarda el id del restaurante y el id del empleado en la tabla "restaurante_empleado" de la DB plazoleta. El email se obtiene del token y se utiliza para buscar la información del usuario propietario. Luego se envían en el cuerpo de la peticion los id para obtener el restaurante asociado al propietario y se guarda el registro en la DB(DataBase).

<center><img src="https://github.com/JaiderMartinez/Usuario/assets/119683594/60eb085a-20be-44e6-99b1-f452e523b589" width="200"> <br>id_restaurante_empleado: llave primaria de la tabla.<br>id_restaurante: campo que especifica a que restaurante pertenece.<br>id_usuario_empleado: campo que funcionara como llave foranea a la tabla usuarios.</center>

    {
        "name" : String,
        "lastName" : String,
        "identificationDocument" : Long,
        "cellPhone" : String,
        "email" : String,
        "password" : String,
        "idRol" : Long  
    }

## Estado del proyecto

<p style="text-align:center;" ><img src="https://img.shields.io/badge/STATUS-EN%20DESARROLLO-green" alt="Status en desarollo">
<h4 style="text-align:center;" >
👷 Proyecto en construcción 🚧
</h4>

## Funcionalidades
- `Historia de usuario 1`: Crear Propietario <a href="https://github.com/JaiderMartinez/Usuario/tree/hu1">ir</a>
- `Historia de usuario 6`: Crear cuenta empleado <a href="https://github.com/JaiderMartinez/Usuario/tree/hu6">ir</a>
- `Historia de usuario 8`: Crear cuenta Cliente <a href="https://github.com/JaiderMartinez/Usuario/tree/hu8">ir</a>

## Autores

Johana Forero Mendoza <a href="https://github.com/JohanaForero">GITHUB</a>
<br>
Jaider Martinez Sarmiento <a href="https://github.com/JaiderMartinez">GITHUB</a>

## 🛠️ Abre y ejecuta el proyecto

<ul>
    <li>Puerto: <strong>8090</strong></li>
    <li>Url de swagger: <strong>/swagger-ui/index.html</strong></li>
    <li>Nombre de la base de datos: <strong>usuarios</strong></li>
    <li>Url para el login: <strong>ip:puerto/user-micro/auth/login</strong> como resultado me devuelve un access token</li>
    <li>Para cada peticion un recurso debo ingresar un token</li>
    <li>El token es ingresado en cada peticion en el Header como key el valor de "Authorization" y en el value se agrega el prefijo "Bearer " mas el token</li>
    <li>Diagramas de wireframes, Url: <strong><a href="https://app.moqups.com/OUdC5drISYQhJ9c7UvgCrmkoWVqBrYtx/view/page/a3afcac31?ui=0&fit_width=1">create account user</a></strong></li>
    <li>Url para crear usuario con rol de cliente: <strong>ip:puerto/user-micro/user/customer</strong></li>
    <li>Url para crear usuario con rol de propietario: <strong>ip:puerto/user-micro/user/</strong> tener en cuenta que para crear esta cuenta necesito haberme autenticado y tener el rol de ADMINISTRADOR e ingresar el token en el header</li>
    <li>Url para crear usuario con rol de empleado: <strong>ip:puerto/user-micro/user/employee</strong> tener en cuenta que para crear esta cuenta necesito haberme autenticado y tener el rol de PROPIETARIO e ingresar el token en el header</li>
</ul>

## Siguiente Microservicio <a href="https://github.com/JaiderMartinez/plazoleta.git">Plazoleta</a>