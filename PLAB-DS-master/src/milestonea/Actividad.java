package milestonea;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.GetTree;

/**
 * Esto es un comentario para javadoc.
 */

public abstract class Actividad {

  protected String nombre;
  protected int id;
  protected Proyecto padre;
  protected long duracion;
  protected LocalDateTime fechaInicial;
  protected LocalDateTime fechaFinalizacion;
  protected final ArrayList<String> tags;
  public final Reloj reloj;

  private final Logger logger = LoggerFactory.getLogger("Actividad");

  /**
   * Esto es un comentario para javadoc.
   */

  public Actividad() {
    id = 0;
    logger.debug("Inicializando actividad");
    nombre = null;
    padre = null;
    duracion = 0;
    reloj = Reloj.getInstancia();
    fechaInicial = null;
    fechaFinalizacion = null;
    tags = new ArrayList<>();
    logger.trace("Tamano inicial del array de tags: " + 0);

    //Se crea la jerarquía para saber que la actividad creada
    // es hija de un proyecto anterior.
    logger.debug("Creando jerarquia actividad con padre no null");
    if (padre != null) {
      padre.anadirActividad(this);
      if (padre.getActividades() == null) {
        logger.warn("Despues de anadir actividad, el array es nulo");
      }
      logger.trace("Tamano del array despues de anadir actividad: "
              + padre.getActividades().size());
    } else {
      logger.debug("Padre null");
    }
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public Actividad(String name, Proyecto dad) {
    assert (name = name.trim()).length() > 0 : "blank_name";
    logger.debug("Inicializando actividad parametros");
    Identifier identifier = Identifier.getInstancia();
    id = identifier.getId();
    nombre = name;
    padre = dad;
    duracion = 0;
    reloj = Reloj.getInstancia();
    fechaInicial = null;
    fechaFinalizacion = null;
    tags = new ArrayList<>();
    logger.trace("Tamano inicial del array de tags: " + 0);
    //Se crea la jerarquía para saber que la actividad creada es hija de un proyecto anterior.
    logger.debug("Creando jerarquia actividad con padre no null");
    if (padre != null) {
      padre.anadirActividad(this);
      if (padre.getActividades() == null) {
        logger.warn("Despues de anadir actividad, el array es nulo");
      }
      logger.trace("Tamano del padre de actividad despues de anadirla: "
          + padre.getActividades().size());
    } else {
      logger.debug("Actividad sin padre");
    }
  }

  public String getNombre() {
    return nombre;
  }

  public Proyecto getPadre() {
    return padre;
  }

  public long getDuracion() {
    return duracion;
  }

  public int getId() {
    return id;
  }

  public abstract Actividad findActivityById(int id);
  /**
   * Esto es un comentario para javadoc.
   */

  public void addTag(String tag) {
    logger.debug("Anadiendo tag");
    final int copyNum = tags.size() + 1;
    tags.add(tag);
    logger.trace("Tamano del array de tags despues de anadir otro tag: " + tags.size());
    assert (copyNum == tags.size());
  }

  public ArrayList<String> getTags() {
    return tags;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public String getFechaInicial() {
    if (fechaInicial == null) {
      logger.debug("Fecha incial sin definir");
      return null;
    }
    logger.trace("Fecha inicial del getter: " + reloj.toString(fechaInicial));
    return reloj.toString(fechaInicial);
  }

  //Utilizada por el Visitor (Saver) para guardar los datos de la actividad

  /**
   * Esto es un comentario para javadoc.
   */

  public JSONObject getJson() { //getJSON
    logger.debug("Guardando datos de actividad");
    JSONObject actividad = new JSONObject();
    actividad.put("id", getId());
    actividad.put("name", getNombre());
    actividad.put("duration", getDuracion());
    actividad.put("initialDate",
            (getFechaInicial() == null) ? JSONObject.NULL : getFechaInicial());
    actividad.put("finalDate",
            (getFechaFinalizacion() == null) ? JSONObject.NULL : getFechaFinalizacion());
    actividad.put("tags", new JSONArray(getTags()));
    return actividad;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  //Utilizada por el Visitor (Loader) para cargar los datos de la actividad
  public void load(JSONObject file) {
    logger.debug("Cargando los datos de actividad");
    id = file.getInt("id");
    nombre = file.getString("name");
    logger.trace("Nombre del fichero a cargar: " + nombre);

    duracion = file.getLong("duration");

    fechaInicial = (file.get("initialDate") == JSONObject.NULL) ? null :
            LocalDateTime.parse(file.getString("fechaInicial"), reloj.formatter);

    fechaFinalizacion = (file.get("finalDate") == JSONObject.NULL) ? null :
            LocalDateTime.parse(file.getString("finalDate"), reloj.formatter);
  }

  /**
   * Esto es un comentario para javadoc.
   */

  //Utilizada por el Visitor (Loader) para crear la jerarquía al cargar el JSON.
  public void setPadre(Proyecto dad) {
    logger.debug("Inicializando padre diferente de null");
    padre = dad;
    if (padre != null) {
      padre.anadirActividad(this);
      if (padre.getActividades() == null) {
        logger.warn("Despues de añadir actividad, el array es nulo");
      }
      logger.trace("Tamano array actividad despues de anadir al padre: "
              + padre.getActividades().size());
    } else {
      logger.debug("Padre null");
    }
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public String getFechaFinalizacion() {
    if (fechaFinalizacion == null) {
      logger.debug("Fecha finalizacion no definida");
      return null;
    }
    logger.debug("Proporcionando fecha finalizacion");
    logger.trace("Fecha finalizacion despues del getter: " + reloj.toString(fechaFinalizacion));
    return reloj.toString(fechaFinalizacion);
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public void setFechaFinalizacion(LocalDateTime a, long d) {
    logger.debug("Inicializando Fecha Finalizacion");
    fechaFinalizacion = a;
    duracion += d;
    logger.trace("Duracion actualizada con valor: " + duracion);
    if (padre != null) {
      padre.setFechaFinalizacion(a, d);
    } else {
      logger.debug("Actividad con padre nulo");
    }
  }

  public abstract void acceptVisitor(Visitor v);

  /**
   * Esto es un comentario para javadoc.
   */
  public JSONObject toJson(int i) {
    logger.debug("Convirtiendo el arbol a JSON");
    GetTree t = new GetTree();
    return t.toJson(this, i);
  }
}