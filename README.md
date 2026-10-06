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
- Añadir body a los ResponseEntity 403
- Endpoints PATCH que permitan modificaciones parciales (si un campo no existe, hacer un "set" con la información ya existente)
- Actualizar el proyecto para solucionar vulnerabilidades
- Organizar los pom.xml y el application.yml (eliminar perfiles? No se usan. Hacer un application.yml global y listo)
- CREATED endpoints return entidad