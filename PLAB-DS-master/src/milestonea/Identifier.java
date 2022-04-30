package milestonea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class Identifier {
  private int idGlobal;
  private static Identifier id;

  private final Logger logger = LoggerFactory.getLogger("Identifier");

  private Identifier() {
    logger.debug("Inicializando Identifier");
    idGlobal = -1;
    logger.trace("Valor inicial del idGlobal: " + idGlobal);
  }

  private static synchronized void crearInstancia() {
    if (id == null) {
      //logger.debug("Creando instancia del identificador");
      id = new Identifier();
    }
  }

  /**
   * Esto es un comentario para javadoc.
   */
  public static Identifier getInstancia() {
    //logger.debug("Devolviendo instancia deL Identificador");
    crearInstancia();
    return id;
  }

  /**
   * Esto es un comentario para javadoc.
   */
  public int getId() {
    logger.debug("Incrementado IdGlobal y devolviendolo");
    idGlobal++;
    return idGlobal;
  }

  public int getLastId() {
    logger.debug("Devolviendo IdGlobal");
    return idGlobal;
  }

  public void setGlobalId(int id) {
    idGlobal = id;
  }
}
