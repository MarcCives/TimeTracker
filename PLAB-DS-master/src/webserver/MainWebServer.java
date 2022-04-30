package webserver;

import milestonea.Actividad;
import milestonea.Loader;
import milestonea.Proyecto;
import milestonea.Tarea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */
public class MainWebServer {
  private final Logger logger = LoggerFactory.getLogger("MainWebServer");

  public static void main(String[] args) {
    webServer();
  }

  /**
   * Esto es un comentario para javadoc.
   */
  public static Actividad makeTreeCourses() {
    //logger.debug("Añadiendo el arbol correspondiente para mostrar en el Flutter");

    Proyecto root = new Proyecto("root", null);
    Proyecto p1 = new Proyecto("software design", root);
    p1.addTag("java");
    p1.addTag("flutter");
    Proyecto p2 = new Proyecto("software testing", root);
    p2.addTag("c++");
    p2.addTag("Java");
    p2.addTag("python");
    Proyecto p3 = new Proyecto("database", root);
    p3.addTag("SQL");
    p3.addTag("python");
    p3.addTag("C++");
    Tarea t1 = new Tarea("transportation", root);
    Proyecto p11 = new Proyecto("problems", p1);
    Proyecto p12 = new Proyecto("project time tracker", p1);
    final Tarea t111 = new Tarea("First list", p11);
    t111.addTag("java");
    final Tarea t112 = new Tarea("Second list", p11);
    t112.addTag("Dart");
    Tarea t121 = new Tarea("Read handout", p12);
    Tarea t122 = new Tarea("First milestone", p12);
    t122.addTag("Java");
    t122.addTag("IntelliJ");
    return root;
  }
  /**
   * Esto es un comentario para javadoc.
   */

  public static Actividad loadData() {
    //logger.debug("Cargando los datos en el loader y devolver la información correspondiente");
    Loader l = new Loader("datos.json");
    return l.load();
  }

  /**
   * Esto es un comentario para javadoc.
   */
  public static void webServer() {
    final Actividad root = makeTreeCourses();
    // implement this method that returns the tree of
    // appendix A in the practicum handout

    // start your clock

    //logger.debug("Creando Actividad root y llamando a loadData");
    //final Actividad root = loadData();
    new WebServer(root);
  }

}