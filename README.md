# Servidor Web Multi-hilos en Java

- **Realizado por:** Juan Jose Aponza (JuanJoe-18)

Implementación de un servidor web HTTP multi-hilos desarrollado en Java utilizando `ServerSocket` y la interfaz `Runnable`.

## Descripción del Proyecto

El servidor atiende solicitudes HTTP (método GET) en el puerto 6789. Cada conexión entrante enviada por un cliente es procesada en un hilo de ejecución independiente (`Thread`), lo que permite atender múltiples peticiones concurrentes de forma simultánea.

## Respuestas HTTP
* **200 OK**: Se retorna cuando el archivo solicitado existe en el directorio local del servidor.
* **404 Not Found**: Se retorna cuando el archivo solicitado no existe, enviando un mensaje de error en formato HTML.

## Estructura del Proyecto

* **ServidorWeb.java**: Contiene la clase principal `ServidorWeb` que escucha las conexiones TCP y la clase `SolicitudHttp` que implementa `Runnable` para procesar cada petición HTTP.
* **index.html**: Archivo HTML de prueba principal.
* **miarchivo.html**: Archivo HTML de prueba secundario.
* **cr7_test.jpg**: Imagen de prueba en formato JPG.
* **cr7_test.gif**: GIF animado de prueba.
* **video.mp4**: Video de prueba transmitido en bloques de bytes.

## Compilación y Ejecución

### Compilación
```bash
javac ServidorWeb.java
```

### Ejecución
```bash
java ServidorWeb
```

## Pruebas de Funcionamiento

Para verificar la ejecución, abre un navegador e ingresa a las siguientes direcciones:

* **Página principal (200 OK)**: `http://localhost:6789/index.html`
* **Página secundaria (200 OK)**: `http://localhost:6789/miarchivo.html`
* **Prueba de imagen (200 OK)**: `http://localhost:6789/cr7_test.jpg`
* **Prueba de gif (200 OK)**: `http://localhost:6789/cr7_test.gif`
* **Prueba de video (200 OK)**: `http://localhost:6789/video.mp4`
* **Servicio Fecha**: `http://localhost:6789/fecha`
* **Servicio Estado**: `http://localhost:6789/estado`
* **Prueba de archivo no existente (404 Not Found)**: `http://localhost:6789/noexiste.html`
