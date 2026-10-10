# To do list

- Todos los endpoints GET deben estar paginados (GenericBOImpl findAll(pageable) --> Revisar e implementar?)
- Endpoints PATCH que permitan modificaciones parciales (si un campo no existe, hacer un "set" con la información ya existente)
- Añadir body a los FORBIDDEN. Comprobar si el resto de error codes se están usando de forma compliant. Fijarse en la rúbrica (error, code...)
- Revisar BO de Episode para ver qué métodos están en desuso
- Mover código a funciones en los servicios, para limpiar el código de los controladores
- Checks de longitud en las strings en los controladores para evitar error 500
- Revisar InputDTOs y añadir allFieldsArePresent() en todos los que sea necesarios, e implementarlos en sus POST mappings
- Actualizar el proyecto para solucionar vulnerabilidades
- Organizar los pom.xml y el application.yml (eliminar perfiles? No se usan. Hacer un application.yml global y listo)
- Rehacer tests
- Sonar sin advertencias
- Corregir documentación de Swagger (prioridad baja)