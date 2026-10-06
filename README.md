# To do list

Features:

- Sistema de mensajería entre amistades
- Creo que ChatMessage va a necesitar un InputDTO? Y por ende, un DTO de lectura. De otra manera no veo como enviar un objeto User mediante el websocket

Pulir:

- Crear DTOs especiales para los endpoints dificiles de documentar en Swagger
- Añadir logs
- Todos los endpoints GET deben estar paginados
- Mover código a funciones en los servicios, para limpiar el código de los controladores
- Sonar sin advertencias
- Añadir body a los ResponseEntity 403
- Endpoints PATCH que permitan modificaciones parciales (si un campo no existe, hacer un "set" con la información ya existente)
- Actualizar el proyecto para solucionar vulnerabilidades
- Organizar los pom.xml y el application.yml (eliminar perfiles? No se usan. Hacer un application.yml global y listo)
- CREATED endpoints return entidad