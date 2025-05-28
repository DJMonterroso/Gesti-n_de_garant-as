# Sistema de Gestión de Garantías

Este proyecto es un sistema de gestión de garantías para computadoras, desarrollado en Java. Implementa una estructura FIFO para procesar equipos a través de distintas fases técnicas: **Recepción → Inspección → Reparación → Control de Calidad → Entrega**. Cada computadora registrada mantiene un historial completo de eventos y estados.

##  Estructura del Proyecto

- `Gestion_de_garantias.java`: Clase principal que controla el menú, el flujo del sistema y la lógica de procesamiento por colas.
- `Computadora.java`: Representa cada equipo, almacena datos del cliente, descripción del problema y su historial.
- `ManejadorDeArchivos.java`: Maneja la persistencia de datos. Guarda el historial en un archivo `.txt` y permite visualizarlo desde consola.
- `historial.txt`: Archivo de texto donde se registra el historial de cada computadora.
- `datos_garantias.ser`: Archivo serializado que conserva el estado de las colas entre sesiones.

## 🛠 Funcionalidades

- Registro de computadoras con validación de datos.
- Procesamiento paso a paso mediante colas FIFO.
- Control de calidad con opción de rechazar y devolver a reparación.
- Historial detallado para cada computadora.
- Persistencia automática con archivos `.txt` y `.ser`.
- Menú interactivo en consola.

##  Ejemplo de flujo

1. Registrar nueva computadora
2. Procesar desde Recepción → Inspección
3. Procesar desde Inspección → Reparación
4. Procesar desde Reparación → Control de Calidad
5. Aprobado: se mueve a Entrega. Rechazado: vuelve a Reparación.
6. Marcar como entregada
7. Visualizar historial y estado actual de colas

##  Requisitos

- Java 8 o superior
- Consola habilitada para entrada y salida estándar

##  Cómo ejecutar

1. Compilar el programa:
   ```bash
   javac Gestion_de_garantias.java
   
Ejecutar el sistema:
 ```bash
java Gestion_de_garantias
```
El sistema guarda los datos automáticamente en ``` datos_garantias.ser ```y el historial en ```bash historial.txt```.

## Validaciones implementadas
- Email válido
- Teléfono de 8 dígitos
- Nombre solo con letras
- Fechas con formato dd-MM-yyyy
- Descripciones no vacías ni numéricas puras

## Notas
- El ServiceTag se ingresa manualmente. Se validan duplicados al avanzar en las fases.
- El sistema puede adaptarse fácilmente para incluir más fases o integrarse con una base de datos.

## Autor
Desarrollado por Diego Monterroso como parte de un proyecto académico universitario.
