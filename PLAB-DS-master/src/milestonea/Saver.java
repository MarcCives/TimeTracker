package milestonea;

import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class Saver implements Visitor {

  private FileWriter fw;
  private JSONObject tree;
  private final Logger logger = LoggerFactory.getLogger("Visitor.Saver");

  /**
   * Esto es un comentario para javadoc.
   */

  public Saver(String name) {

    try {
      //Se crea el fichero JSON
      logger.debug("Creando el fichero json");
      fw = new FileWriter(name);
      logger.trace("Nombre del fichero: " + name);

    } catch (IOException e) {
      logger.warn("Excepcion 'IOException'");
      e.printStackTrace();
    }
  }

  //inicia el guardado
  public void save(Actividad root) {
    logger.debug("Iniciando el guardado");
    root.acceptVisitor(this);
  }

  @Override
  public void visitarTarea(Tarea t) {
    //JSONObject para la tarea y JSONArray para todos los intérvalos que contenga
    final JSONObject tarea = t.getJson();
    JSONArray intervalos = new JSONArray();

    //Se recorren todos los intérvalos de la tarea, que llamará a getJSON de la clase
    // Intérvalo para obtener sus datos y añadirlos al JSONArray de intérvalos
    logger.debug("Recorriendo los intervalos de la tarea y llamando getJson de la clase");
    for (Intervalo i : t.getIntervalos()) {
      if (i == null) {
        logger.warn("No se ha encontrado el intervalo");
      }

      assert i != null;
      i.acceptVisitor(this);
      intervalos.put(tree);
    }

    //Se añaden todos los intérvalos existentes de la tarea
    logger.debug("Anadiendo intervalos existentes de la tarea");
    tarea.put("intervals", intervalos);

    //Variable global para mantener la información de la tarea
    // y construir el árbol desde abajo hasta arriba
    tree = tarea;
  }

  @Override
  public void visitarIntervalo(Intervalo i) {
    logger.debug("Visitando intervalo del Saver");
    tree = i.getJson();
    if (tree == null) {
      logger.warn("Tree null");
    }
  }

  @Override
  public void visitarProyecto(Proyecto p) {
    //JSONObject para el proyecto y JSONArray para todas las actividades que contenga
    final JSONObject proyecto = p.getJson();
    JSONArray actividades = new JSONArray();

    //Se recorren todas las actividades del proyecto, que llamarán a
    // visitarTarea o visitarProyecto para repetir el proceso recursivamente
    logger.debug("Recorriendo todas las actividades del proyecto "
            + "que llamaran a visitarTarea o a vistarProyecto");
    for (Actividad a : p.getActividades()) {
      if (a == null) {
        logger.warn("Actividad no encontrada");
      } else {
        a.acceptVisitor(this);
      }
      //Se añade la actividad a la JSONArray de proyecto para mantener la jerarquía
      logger.debug("Anadiendo actividad a la JSONArray del proyecto para mantener la jerarquia");
      actividades.put(tree);
    }

    //Se añaden todas las actividades existentes del proyecto
    logger.debug("Anadiendo actividades existentes del proyecto");
    proyecto.put("activities", actividades);

    //Variable global para mantener la información del proyecto
    // y construir el árbol desde abajo hasta arriba
    tree = proyecto;

    //Cuando el árbol se genere al completo (de abajo hasta arriba)
    // se escribe el fichero JSON
    if (p.padre == null) {
      logger.debug("Escribiendo el fichero JSON");
      try {
        Identifier identifier = Identifier.getInstancia();
        proyecto.put("globalId", identifier.getLastId());
        fw.write(proyecto.toString());
        fw.close();
        logger.trace("Proyecto: " + proyecto);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
