package webserver;

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

public class SearcherId implements Visitor {
  private int id;
  private Actividad act;

  private final Logger logger = LoggerFactory.getLogger("Visitor.SearcherId");

  /**
   * Esto es un comentario para javadoc.
   */

  public SearcherId() {
    logger.debug("Inicializamos searcher");
    id = 0;
    act = null;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public Actividad search(Proyecto root, int identificador) {
    logger.debug("Implementando función search");
    id = identificador;
    logger.trace("Identificador: " + id);
    logger.debug("Aceptando el visitor");
    root.acceptVisitor(this);
    return act;
  }

  @Override
  public void visitarTarea(Tarea t) {
    logger.debug("Visitando tarea correspondiente y consultando id");
    if (t.getId() == id) {
      act = t;
    }
  }

  @SuppressWarnings("checkstyle:WhitespaceAround")
  @Override
  public void visitarProyecto(Proyecto p) {
    logger.debug("Visitamos proyecto correspondiente");

    if (p.getId() == id) {
      act = p;
    } else {
      for (Actividad a : p.getActividades()) {
        if (a == null) {
          logger.warn("Actividad no encontrada");
        } else {
          a.acceptVisitor(this);
        }
      }
    }
  }

  @Override
  public void visitarIntervalo(Intervalo i) {}
}