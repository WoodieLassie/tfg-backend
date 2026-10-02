# To do list

Features:

- Sistema de mensajería entre amistades
- Creo que ChatMessage va a necesitar un InputDTO? Y por ende, un DTO de lectura. De otra manera no veo como enviar un objeto User mediante el websocket

Pulir:

- Documentar correctamente nuevos endpoints y cambios en el sistema de seguridad en swagger
- Añadir logs
- Todos los endpoints GET deben estar paginados
- Mover código a funciones en los servicios, para limpiar el código de los controladores
- Sonar sin advertencias
- Revisar los DTO para ver si hay alguna forma de simplificar algo de lo que está hecho
- Añadir bien las anotaciones a las entidades (@NotNull, @Column, etc...)