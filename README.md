# To do list

Quiero modificar completamente todo lo que no entiendo del proyecto para que este hecho de una forma que si pueda comprender. La lista de cosas a hacer es la siguiente:

- Rehacer todo el sistema de seguridad, lo que implica:
    - Modificar completamente las funciones de la clase Audit
    - Hacer desde cero todo lo que haya en la carpeta security, cambiando el sistema por uno mucho mas sencillo
- Revisar los DTO para ver si hay alguna forma de simplificar algo de lo que esta hecho

Ademas, pendiente de hacer:

- Sistema de amistades
- Solo poder ver perfiles de amistades (puedes ver sus datos de usuario, reviews y listas)
- Sistema de mensajeria entre amistades
- Sistema de recomendaciones entre amistades
- Añadir username a los users (por que no estaba hecho esto antes?)
- Cambiar todo el sistema de edicion de imagen de perfil, obtencion de datos de perfiles ajenos, y obtencion de datos del propio perfil (quizas no haga falta con los tokens JWT?)
- Cambiar forma de obtener la ID del usuario creador de un comentario, valoracion, etc (se extraera el username del token JWT, y se buscara la ID del usuario a traves de ese email. Optimizar buscando otra forma? Incluir ID en el token JWT si es seguro?)
- Es necesario un sistema que compruebe que el token tiene la informacion correcta, y que si el email o el rol cambian (cambio de email o promocion a admin) se pida de nuevo un token. Esto seguramente es cosa del frontend, pero hay que revisar
- Documentar correctamente nuevos endpoints y cambios en el sistema de seguridad en swagger
- Tener el rol en el token JWT puede ser poco seguro. Intentar ver si es posible obtener el rol para el frontend de otra manera. Endpoint que saque el rol del usuario segun el token? En el tutorial se crea una funcion en la clase JwtBOImpl para extraer username, quizas se puede hacer con el rol? Y convertirlo a un endpoint para obtener ese rol a partir del token de forma continua en el frontend