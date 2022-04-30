package milestonea;

import java.util.ArrayList;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class Tarea extends Actividad {

  private final ArrayList<Intervalo> intervalos;

  private final Logger logger = LoggerFactory.getLogger("Actividad.Tarea");

  private boolean active;

  /**
   * Esto es un comentario para javadoc.
   */

  public Tarea() {
    super();
    logger.debug("Inicializando tarea");
    intervalos = new ArrayList<>();
    logger.trace("Tamano intervalo: " + 0);
    active = false;
    assert (invariant());
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public Tarea(String nombre, Proyecto padre) {
    super(nombre, padre);
    logger.debug("Inicializando tarea por parametros");
    intervalos = new ArrayList<>();
    logger.trace("Tamano intervalo: " + 0);
    assert (invariant());
  }

  /**
   * Esto es un comentario para javadoc.
   */

  //Se inicia la tarea con la adición de un nuevo intérvalo
  public void start() {
    logger.debug("Iniciando tarea");
    active = true;
    int copy = intervalos.size();
    logger.trace("Tamano intervalo: " + copy);

    if (intervalos.size() == 0) {
      logger.debug("Sin intervalos");
      //Se actualiza la fecha inicial de la tarea si no hay intérvalos anteriores.
      fechaInicial = reloj.getHoraActual();

      logger.trace("Valor fecha inicial: " + fechaInicial);

      logger.debug("Iniciando padre");
      padre.start();
    }
    logger.debug("Anadiendo intervalo y observers");
    intervalos.add(new Intervalo(this));
    assert (invariant());
    logger.trace("Despues de anadir el intervalo, su tamano es: " + intervalos.size());
    reloj.addObserver(intervalos.get(intervalos.size() - 1));
    logger.info(nombre + " starts");

    assert ((copy + 1) == intervalos.size());
  }

  /**
   * Esto es un comentario para javadoc.
   */

  //Se para la tarea, actualizando la fecha final y la duración del intérvalo correspondiente.
  public void stop() {
    logger.debug("Parando tarea y actualizando fechas correspondientes");
    active = false;
    int copy = intervalos.size();
    assert (copy > 0);

    logger.info(nombre + " stops");
    Intervalo act = intervalos.get(intervalos.size() - 1);
    act.actualizar();
    act.setActiveFalse();
    assert (invariant());
    logger.trace("Tamano del intervalo: " + intervalos.size());
    logger.debug("Eliminando observer");
    reloj.deleteObserver(act);
    System.out.println();

    assert (copy == intervalos.size());
  }

  public ArrayList<Intervalo> getIntervalos() {
    return intervalos;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public void loadIntervalo(Intervalo i) {
    if (i == null) {
      logger.warn("Intervalo null");
      throw new IllegalArgumentException("Intervalo nulo");
    }

    logger.debug("Cargando el intervalo no nulo");
    final int copy = intervalos.size();
    assert (invariant());
    intervalos.add(i);
    logger.trace("Tamano del array despues de anadir el intervalo: " + intervalos.size());

    assert ((copy + 1) == intervalos.size());
  }

  //Utilizada por el Visitor (Saver) para guardar los datos de la tarea
  @Override
  public JSONObject getJson() {
    logger.debug("Guardando datos de la tarea");
    JSONObject tarea = super.getJson();
    tarea.put("class", "task");
    tarea.put("active", getActive());
    assert (invariant());
    return tarea;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public void acceptVisitor(Visitor v) {
    if (v == null) {
      logger.warn("Visitor null");
      throw new IllegalArgumentException("Visitor nulo");
    }

    assert (invariant());
    logger.debug("Visitando tarea correspondiente");
    v.visitarTarea(this);
  }

  private boolean invariant() {
    assert (padre != null);
    assert (nombre != null);
    assert (duracion >= 0);
    assert (tags != null);
    assert (intervalos != null);
    return true;
  }

  @Override
  public Actividad findActivityById(int id) {
    return null;
  }

  public boolean getActive() {
    return active;
  }
}
