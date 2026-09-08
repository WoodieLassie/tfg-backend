# To do list

Quiero modificar completamente todo lo que no entiendo del proyecto para que este hecho de una forma que si pueda comprender. La lista de cosas a hacer es la siguiente:

- Revisar los DTO para ver si hay alguna forma de simplificar algo de lo que esta hecho

Ademas, pendiente de hacer:

Features:

- Sistema de amistades
- En la relación de user a user para amistades (many to many), user1 (userSender?) es el que manda la solicitud y user2 (userReceiver?) es el que la recibe. Sacar datos según estos campos para sacar solicitudes de amistad pendientes enviadas y recibidas por separado, para el frontend. También usar para validar quién puede aceptar la solicitud (evitar que el sender envie un patch de aceptar solicitud)
- Solo poder ver perfiles de amistades (puedes ver sus datos de usuario, reviews y listas)
- Sistema de mensajeria entre amistades
- Sistema de recomendaciones entre amistades
- DTO para cuando saques datos de un usuario a raiz de su perfil (esconder email y datos sensibles)
- Otro DTO para cuando el usuario sea una amistad? Y según el estado de amistad (amigo o no amigo) sacar un DTO u otro?

Pulir:

- Documentar correctamente nuevos endpoints y cambios en el sistema de seguridad en swagger
- Añadir logs
- Todos los endpoints GET deben estar paginados
- Mover código a funciones en los servicios, para limpiar el código de los controladores
- Eliminar el controlador /sorted de Show, y unificarlo con el findall, para que se pueda buscar todas las series o solo las que coincidan con una búsqueda
- Sonar sin advertencias