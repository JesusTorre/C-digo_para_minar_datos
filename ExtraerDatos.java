/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TripAdvisor_DataMiner;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//Importación de la librería Jsoup
//Para descargar la libreria entre a la siguiente dirección https://jsoup.org/download
//Versión utilizada para este proyecto jsoup-1.10.2.jar
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author JESUS-ALBERTO
 */
public class TripAdvisor_DataMiner {

    /**
     * @param args the command line arguments
     */
    //i; Expresa el identificador
    static int i = 10;
    //titutloComentario; Exresa el titulo del comentario
    static String tituloComentario;
    //resumenComentario; Expresa el comentario
    static String resumenComentario;
    //localizacion; Expresa la localización de la presona que realiza el comentario
    static String localizacion;
    //fecha; Expresa la fecha del comentario
    static String fecha;
    //dispositivo; Expresa el dispositivo que se utilizó para el comentario
    static String dispositivo;
    //limite; Expresa el número donde empiza la paginación
    static int limite = 10;
    //valor; Expresa la estrella dada al lugar turistico
    static char valor;
    //sitio; Expresa el lugar turistico
    static String sitio = "La Quemada";
    //municipio; Expresa el municipio del lugar turistico
    static String municipio = "Zacatecas";
    //usuario; Almacena el nombre del usuario
    static String usuario;
    //Aporte almacena la cadena donde viene el valor de # de aporte y el # de votos útiles.
    static String aportes;
    //tempArray; se almacena temporal los valores de aportes.
    static String[] tempArray;
    //numerosAportes; se alamaces los números de aportes y los votos útiles.
    static String[] numerosAportes = new String[]{"0", "0"};

    //umPaginacion; exprea el número de paginación que tiene el sitio web
    static int numPaginacion = 170;
    // doc; Expresa el docuemnto a evaluar
    Document doc = null;

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        //Llamado de los Métodos
        ExtraerDatos Datos = new ExtraerDatos();
        Datos.Extraer();

    }

    //Método que Extrae los datos
    public void Extraer() throws IOException {
        //While que controla las paginación de los comentarios
        /*El sitio web tiene 17 paginación pero el identificador va en 10 en 10, es decir:
            la paginación 1 su identificador es 10, la paginación 2 su identificacor es 20
            asi sucesivamente, por eso es que en numPaginacion es igual a 170.
            limite va incrementando 10 en 10*/
        while (limite <= numPaginacion) {

            try {
                //Se agrega la dirección de la pagina a evaluar. Donde se ubica la variable limite es donde va el identificador
                //Ejemplo de la paginación 1  //https://www.tripadvisor.com.mx/Attraction_Review-g152772-d153364-Reviews-or10-La_Quemada-Zacatecas_Central_Mexico_and_Gulf_Coast.html
                //Ejemplo de la paginación 2 //https://www.tripadvisor.com.mx/Attraction_Review-g152772-d153364-Reviews-or20-La_Quemada-Zacatecas_Central_Mexico_and_Gulf_Coast.html
                doc = Jsoup.connect("https://www.tripadvisor.com.mx/Attraction_Review-g152772-d153364-Reviews-or" + limite + "-La_Quemada-Zacatecas_Central_Mexico_and_Gulf_Coast.html").get();
                //Contenedor principal donde se almacena los datos a Extraer
                Elements body = doc.select("div[class=\"reviewSelector\"]");//review-container
                for (Element link : body) {

                    //Proceso para obtener las Evaluación del sitio arqueológico
                    //Se convierte todo el docuemnto a cadena 
                    String cadena = link.toString();
                    //Se busca la palabra donde se almacena la evaluación
                    int posicion = cadena.indexOf("ui_bubble_rating bubble_");
                    //Se corta la palabra dejando solo el numero como caracter.
                    valor = cadena.charAt(posicion + 24);
                    //Fin del proceso de obtener las Estrella

                    //Extracción del titulo del comentario y se almacena en titulo del comentario
                    tituloComentario = link.getElementsByClass("noQuotes").text();

                    //Extracción del comentario y se almacena en resumenComentario
                    resumenComentario = link.getElementsByClass("partial_entry").text();

                    //Extracción de la localización y se almacena en localizacion
                    localizacion = link.getElementsByClass("location").text();

                    //Extracción de la fecha y se lamacena en fecha
                    fecha = link.getElementsByClass("ratingDate relativeDate").text();

                    //Extracción de tipo del dispositivo y se almacena en dispositivo
                    dispositivo = link.getElementsByClass("viaMobile").text();

                    //Extracción del nombre del usuario.
                    usuario = link.getElementsByClass("username mo").text();

                    //Extración de la cadena donde viene el valor de # de aporte y el # de votos útiles.
                    aportes = link.getElementsByClass("badgetext").toString();

                    //Proceso para separa la cadena de "aportes" en dos datos
                    String prueba = aportes.replaceAll("[^0-9]+", " ");
                    tempArray = prueba.trim().split(" ");
                    numerosAportes = new String[]{"0", "0"};
                    for (int i = 0; i < tempArray.length; i++) {
                        numerosAportes[i] = tempArray[i];
                    }
                    //Fin del proceso de separación

                    //Se llama al Método CrearArchivo para guardar los datos
                    CrearArchivo();
                    //incremento del identificador
                    i++;
                    System.out.println(i);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            //Cambio de pagina
            limite += 10;
            //System.out.println("*"+limite); 
        }//fin del while
    }

    //Metodo para guardar los datos al un archivo .CSV
    public void CrearArchivo() throws IOException {
        //Se selecciona el nombre el archivo y la ruta
        String ruta = "datos.csv";
        File archivo = new File(ruta);
        BufferedWriter bw;
        //Codicion para verificar si el archivo existe
        if (archivo.exists()) {
            bw = new BufferedWriter(new FileWriter(archivo, true));
            //En caso de que ya exista manda a guardar los datos 

            bw.write("\n" + usuario + "~" + numerosAportes[0] + "~" + numerosAportes[1] + "~" + localizacion + "~" + fecha + "~" + valor + "~" + dispositivo + "~" + tituloComentario + "~" + resumenComentario + "~" + sitio + "~" + municipio);

        } else {
            //En saco de que no exista crea un archivo nuevo con las siguientes cabecera
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write("Nombre usuario~Total de aportes~Votos utiles~Procedencia del usuario~Fecha~Evaluación~Dispositivo~Encabezado~Comentario~Zona Arq~Destino");
        }
        bw.close();

    }

}
