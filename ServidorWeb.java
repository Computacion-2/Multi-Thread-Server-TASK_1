import java.io.*;
import java.net.*;
import java.util.*;

public final class ServidorWeb
{
    public static void main(String argv[]) throws Exception
    {
        int puerto = 6789;
        ServerSocket socketdeEscucha = new ServerSocket(puerto);

        while (true) {
            Socket socketdeConexion = socketdeEscucha.accept();
            SolicitudHttp solicitud = new SolicitudHttp(socketdeConexion);
            Thread hilo = new Thread(solicitud);
            hilo.start();
        }
    }
}

final class SolicitudHttp implements Runnable
{
    final static String CRLF = "\r\n";
    Socket socket;

    public SolicitudHttp(Socket socket) throws Exception 
    {
        this.socket = socket;
    }

    public void run()
    {
        try {
            proceseSolicitud();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void proceseSolicitud() throws Exception
    {
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String lineaDeSolicitud = br.readLine();

        System.out.println();
        System.out.println(lineaDeSolicitud);

        if (lineaDeSolicitud == null) {
            os.close();
            br.close();
            socket.close();
            return;
        }

        String lineaDelHeader = null;
        while ((lineaDelHeader = br.readLine()) != null && lineaDelHeader.length() != 0) {
            System.out.println(lineaDelHeader);
        }

        StringTokenizer lineaSeparada = new StringTokenizer(lineaDeSolicitud);
        lineaSeparada.nextToken();
        String nombreArchivo = lineaSeparada.nextToken();

        if (nombreArchivo.equals("/")) {
            nombreArchivo = "/index.html";
        }

        String lineaDeEstado = null;
        String lineaHeaderTipoContenido = null;
        String cuerpoMensajeHtml = null;
        FileInputStream fis = null;
        boolean existeArchivo = false;

        if (nombreArchivo.equalsIgnoreCase("/fecha")) {
            lineaDeEstado = "HTTP/1.0 200 OK" + CRLF;
            lineaHeaderTipoContenido = "Content-Type: text/html" + CRLF;
            cuerpoMensajeHtml = "<HTML><BODY><H1>Servicio: Fecha y Hora del Servidor</H1><P>Fecha actual: " + new Date() + "</P><P><A HREF='/index.html'>Volver al inicio</A></P></BODY></HTML>";
        } else if (nombreArchivo.equalsIgnoreCase("/estado")) {
            lineaDeEstado = "HTTP/1.0 200 OK" + CRLF;
            lineaHeaderTipoContenido = "Content-Type: text/html" + CRLF;
            cuerpoMensajeHtml = "<HTML><BODY><H1>Servicio: Estado del Servidor</H1><P>Estado: ACTIVO (Multi-hilos)</P><P>Hilos de ejecucion activos: " + Thread.activeCount() + "</P><P><A HREF='/index.html'>Volver al inicio</A></P></BODY></HTML>";
        } else {
            nombreArchivo = "." + nombreArchivo;
            try {
                fis = new FileInputStream(nombreArchivo);
                existeArchivo = true;
            } catch (FileNotFoundException e) {
                existeArchivo = false;
            }

            if (existeArchivo) {
                lineaDeEstado = "HTTP/1.0 200 OK" + CRLF;
                lineaHeaderTipoContenido = "Content-Type: " + contentType(nombreArchivo) + CRLF;
            } else {
                lineaDeEstado = "HTTP/1.0 404 Not Found" + CRLF;
                lineaHeaderTipoContenido = "Content-Type: text/html" + CRLF;
                cuerpoMensajeHtml = "<HTML><HEAD><TITLE>404 Not Found</TITLE></HEAD><BODY><H1>404 Not Found</H1><P><A HREF='/index.html'>Volver al inicio</A></P></BODY></HTML>";
            }
        }

        os.writeBytes(lineaDeEstado);
        os.writeBytes(lineaHeaderTipoContenido);
        os.writeBytes(CRLF);

        if (existeArchivo && fis != null) {
            enviarBytes(fis, os);
            fis.close();
        } else {
            os.writeBytes(cuerpoMensajeHtml);
        }

        os.close();
        br.close();
        socket.close();
    }

    private static void enviarBytes(FileInputStream fis, OutputStream os) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while((bytes = fis.read(buffer)) != -1 ) {
            os.write(buffer, 0, bytes);
        }
    }

    private static String contentType(String nombreArchivo) {
        String nombreLower = nombreArchivo.toLowerCase();
        if(nombreLower.endsWith(".htm") || nombreLower.endsWith(".html")) {
            return "text/html";
        }
        if(nombreLower.endsWith(".gif")) {
            return "image/gif";
        }
        if(nombreLower.endsWith(".jpg") || nombreLower.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if(nombreLower.endsWith(".png")) {
            return "image/png";
        }
        if(nombreLower.endsWith(".mp3")) {
            return "audio/mpeg";
        }
        if(nombreLower.endsWith(".mp4")) {
            return "video/mp4";
        }
        return "application/octet-stream";
    }
}
