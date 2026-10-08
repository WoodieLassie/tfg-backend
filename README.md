# To do list

- Creo que ChatMessage va a necesitar un InputDTO? Y por ende, un DTO de lectura. De otra manera no veo como enviar un objeto User mediante el websocket
- Crear DTOs especiales para los endpoints dificiles de documentar en Swagger
- Todos los endpoints GET deben estar paginados (GenericBOImpl findAll(pageable) --> Revisar e implementar?)
- Mover código a funciones en los servicios, para limpiar el código de los controladores
- Sonar sin advertencias
- Endpoints PATCH que permitan modificaciones parciales (si un campo no existe, hacer un "set" con la información ya existente)
- Actualizar el proyecto para solucionar vulnerabilidades
- Organizar los pom.xml y el application.yml (eliminar perfiles? No se usan. Hacer un application.yml global y listo)
- CREATED endpoints return entidad. Añadir body a los FORBIDDEN. Comprobar si el resto de error codes se están usando de forma compliant. Fijarse en la rúbrica (error, code...)
- Rehacer tests
- Si un BO tiene que acceder a los datos de otra entidad, que no se inyecte el repositorio de esa entidad, si no el BO, que para algo está. Revisar todos los BO
- Revisar BO de Episode para ver qué metodos están en desuso