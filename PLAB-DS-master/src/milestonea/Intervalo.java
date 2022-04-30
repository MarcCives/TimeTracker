package milestonea;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class Intervalo implements Observer {

  private LocalDateTime inicioIntervalo;

  private LocalDateTime finalIntervalo;

  private long duracion;

  private Tarea padre;

  private static Printer print;

  public final Reloj reloj;

  private final Logger logger = LoggerFactory.getLogger("Intervalo");

  private final int id;

  private boolean active;
  /**
   * Esto es un comentario para javadoc.
   */

  public Intervalo() {
    id = -1;
    logger.debug("Inicializando Intervalo");
    reloj = Reloj.getInstancia();
    inicioIntervalo = reloj.getHoraActual();
    logger.trace("Data intervalo inicio: " + inicioIntervalo.format(reloj.formatter));
    finalIntervalo = inicioIntervalo;
    logger.trace("Data final al inicio: " + finalIntervalo.format(reloj.formatter));
    print = new Printer();
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public Intervalo(Tarea t) {
    logger.debug("Inicializando Intervalo parametros");
    padre = t;
    logger.trace("Padre de la tarea: " + padre.getNombre());
    reloj = Reloj.getInstancia();
    inicioIntervalo = reloj.getHoraActual();
    logger.trace("Data intervalo inicio: " + inicioIntervalo.format(reloj.formatter));
    finalIntervalo = inicioIntervalo;
    logger.trace("Data final al inicio: " + finalIntervalo.format(reloj.formatter));
    print = new Printer();
    Identifier identifier = Identifier.getInstancia();
    id = identifier.getId();
    active = true;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  //Cálculo de duración restando la diferencia entre la fecha de inicio y la fecha actual
  //que se convierte en la fecha final del intérvalo

  public void actualizar() {
    logger.debug("Calculando duracion intervalo");
    duracion = Duration.between(inicioIntervalo.toLocalTime(), 
            reloj.getHoraActual().toLocalTime()).getSeconds();
    logger.trace("Duracion intervalo despues de actualizar: " + duracion);
    padre.setFechaFinalizacion(finalIntervalo, Duration.between(finalIntervalo.toLocalTime(), 
            reloj.getHoraActual().toLocalTime()).getSeconds());
    logger.trace("Fecha final del padre de Intervalo despues de actualizar: "
            + padre.getFechaFinalizacion());
    finalIntervalo = inicioIntervalo.plusSeconds(duracion);
    logger.trace("Final intervalo despues de actualizar: "
            + finalIntervalo.format(reloj.formatter));
  }

  //Utilizada por el Visitor (Saver) para guardar los datos del intérvalo

  /**
   * Esto es un comentario para javadoc.
   */
  public JSONObject getJson() {
    logger.debug("Guardando datos del intervalo");
    JSONObject intervalo = new JSONObject();
    intervalo.put("id", getId());
    intervalo.put("initialDate", getInicioIntervalo());
    intervalo.put("finalDate", getFinalIntervalo());
    intervalo.put("duration", getDuracion());
    intervalo.put("active", getActive());

    return intervalo;
  }

  /**
   * Esto es un comentario para javadoc.
   */
  public void load(JSONObject file) {
    logger.debug("Cargando datos del intervalo");
    duracion = file.getLong("duration");
    inicioIntervalo = LocalDateTime.parse(file.getString("initialDate"), reloj.formatter);
    logger.trace("Inicio intervalo despues del load del JSON: "
            + inicioIntervalo.format(reloj.formatter));
    finalIntervalo = LocalDateTime.parse(file.getString("finalDate"), reloj.formatter);
    logger.trace("Final intervalo después del load del JSON: "
            + finalIntervalo.format(reloj.formatter));
  }
  //Utilizada por el Visitor (Loader) para crear la jerarquía al cargar el JSON.

  /**
   * Esto es un comentario para javadoc.
   */

  public void setPadre(Tarea dad) {
    logger.debug("Inicializando padre");
    padre = dad;
    logger.trace("Nombre del padre que se le asigna: " + dad.getNombre());
    padre.loadIntervalo(this);
  }

  public void acceptVisitor(Visitor v) {
    logger.debug("Visitando Intervalo");
    v.visitarIntervalo(this);
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public long getDuracion() {
    logger.debug("Proporcionando duracion Intervalo");
    logger.trace("Valor de la duracion: " + duracion);
    return duracion;

  }

  /**
   * Esto es un comentario para javadoc.
   */

  public Tarea getTarea() {
    logger.debug("Proporcionando tarea intervalo");
    logger.trace("Tarea devuelta: " + padre.getNombre());
    return padre;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public String getInicioIntervalo() {
    logger.debug("Proporcionando inicio intervalo");
    logger.trace("Retorno del inicioIntervalo con valor: " + reloj.toString(inicioIntervalo));
    return reloj.toString(inicioIntervalo);
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public String getFinalIntervalo() {
    logger.debug("Proporcionando final intervalo");
    logger.trace("Retorno final intervalo con valor: " + reloj.toString(finalIntervalo));
    return reloj.toString(finalIntervalo);
  }

  /**
   * Esto es un comentario para javadoc.
   */
  public void update(Observable obs, Object arg) {
    logger.debug("Actualizando duracion del Intervalo");
    actualizar();
    logger.debug("Aceptando visitor de Intervalo");
    acceptVisitor(print);
    System.out.println();
  }

  public int getId() {
    return id;
  }

  public boolean getActive() {
    return active;
  }

  public void setActiveFalse() {
    active = false;
  }
}
