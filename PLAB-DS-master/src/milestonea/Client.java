package milestonea;

import static java.lang.System.exit;

import java.util.ArrayList;
import milestoneb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class Client {

  /**
   * Esto es un comentario para javadoc.
   */

  public static void main(String[] args) {

    Logger logger = LoggerFactory.getLogger("Client");

    try {
      //creacion de tareas

      logger.debug("Creando proyectos, tareas y anadimos los tags");
      final Reloj r = Reloj.getInstancia(); // an object

      Proyecto root = new Proyecto("root", null);
      logger.trace("Nombre del root: " + root.getNombre());

      Proyecto p1 = new Proyecto("software design", root);
      logger.trace("Nombre del proyecto p1; " + p1.getNombre());

      p1.addTag("java");
      p1.addTag("flutter");
      Proyecto p2 = new Proyecto("software testing", root);
      logger.trace("Nombre del proyecto p2: " + p2.getNombre());

      p2.addTag("c++");
      p2.addTag("Java");
      p2.addTag("python");
      Proyecto p3 = new Proyecto("database", root);
      logger.trace("Nombre del proyecto p3: " + p3.getNombre());

      p3.addTag("SQL");
      p3.addTag("python");
      p3.addTag("C++");
      Tarea t1 = new Tarea("transportation", root);
      logger.trace("Nombre de la tarea t1: " + t1.getNombre());

      Proyecto p11 = new Proyecto("problems", p1);
      logger.trace("Nombre del proyecto p11: " + p11.getNombre());

      Proyecto p12 = new Proyecto("project time tracker", p1);
      logger.trace("Nombre del proyecto p12: " + p12.getNombre());

      final Tarea t111 = new Tarea("First list", p11);
      logger.trace("Nombre de la tarea t111: " + t111.getNombre());

      t111.addTag("java");
      final Tarea t112 = new Tarea("Second list", p11);
      logger.trace("Nombre de la tarea t112: " + t112.getNombre());

      t112.addTag("Dart");
      Tarea t121 = new Tarea("Read handout", p12);
      logger.trace("Nombre de la tarea t121: " + t121.getNombre());

      Tarea t122 = new Tarea("First milestone", p12);
      logger.trace("Nombre de la tarea t122: " + t112.getNombre());

      t122.addTag("Java");
      t122.addTag("IntelliJ");

      //ejecucion de tareas
      //      logger.debug("Ejecutamos las tareas");
      //      //1
      //      t1.start();
      //      Thread.sleep(4000);
      //      t1.stop();
      //      //2
      //      Thread.sleep(2000);
      //      //3
      //      t111.start();
      //      Thread.sleep(6000);
      //      //4
      //      t112.start();
      //      Thread.sleep(4000);
      //      //5
      //      t111.stop();
      //      //6
      //      Thread.sleep(2000);
      //      t112.stop();
      //      //7
      //      Thread.sleep(2000);
      //      //8
      //      t1.start();
      //      Thread.sleep(4000);
      //      t1.stop();
      //
      //      r.stop();
      //
      //      System.out.println();
      //      logger.info("fin counting time tests");
      //
      //      Thread.sleep(1000);
      //
      //      //guardado de las tareas en fichero
      //      logger.debug("Guardando tareas en fichero");
      //      Saver s = new Saver("prueba.json");
      //      s.save(root);
      //
      //      //nuevo proyecto vacio
      //      logger.debug("Creando proyecto vacio");
      //      Proyecto root2 = new Proyecto();
      //      logger.trace("Nombre del proyecto vacio root2: " + root2.getNombre());
      //
      //      //cargar las tareas del fichero
      //      logger.debug("Cargando tareas fichero");
      //      Loader l = new Loader("prueba.json");
      //      l.load(root2);

      Searcher search = new Searcher();

      ArrayList<Actividad> s1 = search.search(root, "DART");

      if (s1 == null) {
        logger.debug("Arraylist de actividades nula");
      }
      Thread.sleep(1000);

    } catch (InterruptedException e) {
      logger.warn("Ejecucion interrumpida");
      e.printStackTrace();
    }
    exit(0);
  }
}
