package milestonea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class Printer implements Visitor {

  private final Logger logger = LoggerFactory.getLogger("Visitor.Printer");

  public Printer() {}

  @Override
  public void visitarTarea(Tarea t) {
    logger.debug("Printando tarea");
    logger.info("Task " + t.getNombre() + " "
            + "child of " + t.getPadre().getNombre() + " " + t.getFechaInicial() + " "
            + t.getFechaFinalizacion() + " " + t.getDuracion());
    t.getPadre().acceptVisitor(this);
  }

  @Override
  public void visitarProyecto(Proyecto p) {
    if (p.padre != null) {
      logger.debug("Printando Proyecto con padre no null");
      logger.info("Project " + p.getNombre(),
              "child of " + p.getPadre().getNombre() + " " + p.getFechaInicial() + " "
                      + p.getFechaFinalizacion() + " " +  p.getDuracion());
      p.getPadre().acceptVisitor(this);
    } else {
      logger.debug("Printando proyecto con padre null");
      logger.info("Project " + p.getNombre() + " "
              + "child of null " + p.getFechaInicial() + " "
              + p.getFechaFinalizacion() + " " + p.getDuracion());
    }
  }

  @Override
  public void visitarIntervalo(Intervalo i) {
    logger.debug("Printando Intervalo");
    logger.info("Interval "
            + "child of " + i.getTarea().getNombre() + " " + i.getInicioIntervalo() + " "
            + i.getFinalIntervalo() + " " + i.getDuracion());
    i.getTarea().acceptVisitor(this);
  }
}
