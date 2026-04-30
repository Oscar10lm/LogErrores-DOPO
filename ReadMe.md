# Sistema de Inventario y Manejo de Logs

## Introducción
Este proyecto consiste en la implementación de un sistema de gestión de inventario en Java, diseñado para demostrar el manejo robusto de excepciones y la persistencia de errores a través de archivos de registro (logs). Su función principal es administrar un catálogo de productos (permitiendo agregar, buscar y eliminar registros) mientras simula y captura de forma controlada escenarios de error, tanto de negocio como del sistema.

* **Autores:** Juan Gaitan, Oscar Lasso, Cristian Moreno, David Contreras
* **Fecha:** 29 de abril de 2026


## Diseño
El sistema sigue un diseño orientado a objetos claro y desacoplado:
* **Estructura de Datos:** Se emplea un contenedor `HashMap<Integer, Producto>` dentro de la clase `Inventario`, asegurando un acceso eficiente y directo a los elementos utilizando el ID del producto como clave.
* **Manejo de Excepciones:** Se creó la clase personalizada `InventarioException` para centralizar las reglas de negocio, especificando constantes para errores como `PRODUCT_NOT_FOUND_EXCEPTION` y `EMPTY_INVENTORY_EXCEPTION`.
* **Lógica de Logs:** La gestión de registros se delegó completamente a la clase utilitaria `ErrorLogger`. Esta clase implementa métodos estáticos que hacen uso de `FileWriter` (en modo *append*) y `PrintWriter` para separar la información en dos canales distintos:
  * `expected_errors_log.txt`: Registra los mensajes de excepciones de negocio previstas (errores de usuario o de lógica de inventario).
  * `inventario_log.txt`: Captura errores imprevistos (como fallos matemáticos o nulos), almacenando el *stack trace* completo para los programadores.

## Aprendizaje
El desarrollo de este ejercicio subraya la importancia crítica de implementar un sistema de logs estructurado en cualquier proyecto de software. 

La principal lección radica en la separación de responsabilidades al capturar errores: no todos los fallos requieren el mismo tratamiento. Registrar las excepciones esperadas ayuda a entender el comportamiento del usuario y el flujo de la aplicación, mientras que aislar los errores inesperados junto con su traza de ejecución (`e.printStackTrace()`) es vital para que el equipo de desarrollo pueda realizar un diagnóstico *post-mortem* ágil y preciso. Implementar esto mediante bloques `try-catch` adecuados y flujos de entrada/salida eficientes garantiza que la aplicación no se detenga abruptamente y que la información vital de depuración no se pierda.
