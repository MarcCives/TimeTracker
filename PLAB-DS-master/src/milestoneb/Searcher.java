package milestoneb;

import java.util.ArrayList;
import milestonea.Actividad;
import milestonea.Intervalo;
import milestonea.Proyecto;
import milestonea.Tarea;
import milestonea.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class Searcher implements Visitor {
  private String etiqueta;
  private final ArrayList<Actividad> searched;

  private final Logger logger = LoggerFactory.getLogger("Visitor.Searcher");

  /**
   * Esto es un comentario para javadoc.
   */

  public Searcher() {
    logger.debug("Inicializamos searcher");
    etiqueta = " ";
    searched = new ArrayList<>();
    logger.trace("Tamano del intervalo: " + 0); //Inicialmente siempre sera 0
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public ArrayList<Actividad> search(Actividad root, String tag) {
    logger.debug("Implementando función search");
    etiqueta = tag.toLowerCase();
    logger.trace("Nombre de la etiqueta: " + etiqueta);
    logger.debug("Aceptando el visitor");
    root.acceptVisitor(this);
    return searched;
  }

  @Override
  public void visitarTarea(Tarea t) {
    
    logger.debug("Visitando tarea correspondiente");
    if (t.getTags() != null) {
      logger.debug("Recorriendo tags y anadiendo al array");
      for (String tag : t.getTags()) {
        if (tag == null) {
          logger.warn("Tag no encontrado");
        }
        if (etiqueta.equals(tag != null ? tag.toLowerCase() : null)) {
          searched.add(t);
          logger.trace("Tamano del array searched despues de anadir tags: " + searched.size());
          break;
        }
      }
    } else {
      logger.warn("Tags null");
    }
  }

  @Override
  public void visitarProyecto(Proyecto p) {
    logger.debug("Visitamos proyecto correspondiente");
    if (p.getTags() != null) {
      logger.debug("Recorriendo tags y anadiendo al array");
      for (String tag : p.getTags()) {
        if (tag == null) {
          logger.warn("Tag no encontrado");
        }
        if (etiqueta.equals(tag != null ? tag.toLowerCase() : null)) {
          searched.add(p);
          logger.trace("Tamano del array searched despues de añadir tags: " + searched.size());
          break;
        }
      }
    } else {
      logger.warn("Tags null");
    }
    logger.debug("Aceptamos el visitor de todas las actividades");
    for (Actividad a : p.getActividades()) {
      if (a == null) {
        logger.warn("Actividad no encontrada");
      } else {
        a.acceptVisitor(this);
      }
    }

  }

  @Override
  public void visitarIntervalo(Intervalo i) {}
}
