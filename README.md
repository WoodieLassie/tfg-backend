# To do list

- Crear DTOs especiales para los endpoints difíciles de documentar en Swagger
- Todos los endpoints GET deben estar paginados (GenericBOImpl findAll(pageable) --> Revisar e implementar?)
- Endpoints PATCH que permitan modificaciones parciales (si un campo no existe, hacer un "set" con la información ya existente)
- CREATED endpoints return entidad. Añadir body a los FORBIDDEN. Comprobar si el resto de error codes se están usando de forma compliant. Fijarse en la rúbrica (error, code...)
- Revisar BO de Episode para ver qué métodos están en desuso
- Mover código a funciones en los servicios, para limpiar el código de los controladores
- Actualizar el proyecto para solucionar vulnerabilidades
- Organizar los pom.xml y el application.yml (eliminar perfiles? No se usan. Hacer un application.yml global y listo)
- Rehacer tests
- Sonar sin advertencias