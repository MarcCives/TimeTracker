package milestonea;

import java.util.ArrayList;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.SearcherId;

/**
 * Esto es un comentario para javadoc.
 */

public class Proyecto extends Actividad {

  private int numActivitadades;
  private final ArrayList<Actividad> arrayActividad;

  private final Logger logger = LoggerFactory.getLogger("Actividad.Proyecto");

  /**
   * Esto es un comentario para javadoc.
   */

  public Proyecto() {
    super();
    logger.debug("Inicializando Proyecto");
    numActivitadades = 0;
    logger.trace("Numero actividades = " + numActivitadades);
    arrayActividad = new ArrayList<>();
    logger.trace("Tamano array actividades = " + 0);
    assert (invariant());
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public Proyecto(String name, Proyecto padre) {
    super(name, padre);
    logger.debug("Inicializando Proyecto parametros");
    numActivitadades = 0;
    logger.trace("Numero actividades = " + numActivitadades);
    arrayActividad = new ArrayList<>();
    logger.trace("Tamano array actividades = " + 0);
    assert (invariant());
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public void anadirActividad(Actividad a) {
    logger.debug("Anadiendo actividad");
    if (a == null) {
      logger.warn("Actividad null");
      throw new IllegalArgumentException("Actividad nulo");
    }
    final int copyArray = arrayActividad.size() + 1;
    final int copyNum = numActivitadades + 1;

    arrayActividad.add(a);
    logger.trace("Tamano array despues de anadir actividad: " + arrayActividad.size());
    assert (invariant());

    numActivitadades++;
    if (numActivitadades <= 0) {
      logger.warn("Num actividades inferior o igual a 0 despues de sumarlo");
    }
    logger.trace("Num Actividades despues de anadir una actividad: " + numActivitadades);

    assert (copyArray == arrayActividad.size());
    assert (copyNum == numActivitadades);
  }

  /**
   * Esto es un comentario para javadoc.
   */

  //Se inicializa la fecha inicial del proyecto si no se había hecho antes
  // y se sube por el árbol para hacer lo mismo con los padres.
  public void start() {
    logger.debug("Inicializando fecha inicial del proyecto");
    assert (invariant());
    if (fechaInicial == null) {
      fechaInicial = reloj.getHoraActual();
      logger.trace("Fecha inicial al inicio del proyecto: " + fechaInicial.format(reloj.formatter));

      if (padre != null) {
        logger.debug("Inicializando padre");
        padre.start();
      }

    }
  }

  //Utilizada por el Visitor (Saver) para guardar los datos del proyecto
  @Override
  public JSONObject getJson() {
    logger.debug("Guardando datos Proyecto");
    JSONObject proyecto = super.getJson();
    assert (invariant());
    proyecto.put("class", "project");
    return proyecto;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public void acceptVisitor(Visitor v) {
    logger.debug("Visitor Proyecto");
    if (v == null) {
      logger.warn("Visitor null");
      throw new IllegalArgumentException("Visitor nulo");
    }
    assert (invariant());
    v.visitarProyecto(this);
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public ArrayList<Actividad> getActividades() {
    logger.debug("Proporcionando actividades");
    logger.trace("Array actividad con tamano: " + arrayActividad.size());
    assert (invariant());
    return arrayActividad;
  }

  private boolean invariant() {
    assert (nombre != null);
    assert (numActivitadades >= 0);
    assert (duracion >= 0);
    assert (tags != null);
    assert (arrayActividad != null);
    return true;
  }

  @Override
  public Actividad findActivityById(int id) {
    logger.debug("Creando searcher para la funcion findActivityById");
    SearcherId searcher = new SearcherId();
    logger.trace("Buscando actividad con id: " + id);
    return searcher.search(this, id);
  }
}
