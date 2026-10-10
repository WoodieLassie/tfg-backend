# To do list

- Todos los endpoints GET deben estar paginados (GenericBOImpl findAll(pageable) --> Revisar e implementar?)
- Añadir body a los FORBIDDEN. Comprobar si el resto de error codes se están usando de forma compliant. Fijarse en la rúbrica (error, code...)
- Mover código a funciones en los servicios, para limpiar el código de los controladores
- Actualizar el proyecto para solucionar vulnerabilidades
- Organizar los pom.xml y el application.yml (eliminar perfiles? No se usan. Hacer un application.yml global y listo)
- Rehacer tests
- Sonar sin advertencias