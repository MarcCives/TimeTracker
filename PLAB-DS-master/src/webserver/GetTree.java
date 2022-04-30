package webserver;

import milestonea.Actividad;
import milestonea.Intervalo;
import milestonea.Proyecto;
import milestonea.Tarea;
import milestonea.Visitor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class GetTree implements Visitor {
  private JSONObject tree;
  private int profundidad;
  private final Logger logger = LoggerFactory.getLogger("Visitor.GetTree");

  public GetTree() {
    profundidad = 0;
  }

  /**
   * Esto es un comentario para javadoc.
   */
  //inicia el guardado
  public JSONObject toJson(Actividad root, int p) {
    profundidad = p;
    root.acceptVisitor(this);
    return tree;
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
      logger.debug("Llamando al visitor de los intervalos y añadiendo el arbol al intervalo");
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

    int prof = profundidad;

    //Se recorren todas las actividades del proyecto, que llamarán a
    // visitarTarea o visitarProyecto para repetir el proceso recursivamente
    logger.debug("Recorriendo todas las actividades del proyecto "
        + "que llamaran a visitarTarea o a vistarProyecto");
    if (prof > 0) {
      profundidad--;
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
    }
    //Se añaden todas las actividades existentes del proyecto
    logger.debug("Anadiendo actividades existentes del proyecto");
    if (!actividades.isEmpty()) {
      proyecto.put("activities", actividades);
    }
    profundidad = prof;
    //Variable global para mantener la información del proyecto
    // y construir el árbol desde abajo hasta arriba
    tree = proyecto;
  }
}
