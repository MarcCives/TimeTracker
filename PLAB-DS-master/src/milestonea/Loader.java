package milestonea;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class Loader implements Visitor {

  //Variable utilizada para guardar el objeto JSON que se va a leer
  private JSONObject file;

  private Actividad padre;

  private final Logger logger = LoggerFactory.getLogger("Visitor.Loader");

  /**
   * Esto es un comentario para javadoc.
   */

  public Loader(String name) {
    logger.debug("Inicializando Loader");
    padre = null;

    //Lectura del fichero JSON
    logger.debug("Leyendo fichero JSON");
    logger.trace("Nombre loader: " + name);
    try (InputStream is = new FileInputStream(name)) {
      //Objeto JSONTokener para dividir el fichero en tokens
      JSONTokener tokener = new JSONTokener(is);
      file = new JSONObject(tokener);
    } catch (IOException e) {
      e.printStackTrace();
      logger.warn("Loader IOException");
    }
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public Actividad load() {
    logger.debug("Aceptando visitor del load");
    Actividad root;
    if (file != null) {
      root = new Proyecto();
      Identifier identifier = Identifier.getInstancia();
      identifier.setGlobalId(file.getInt("globalId"));
      root.acceptVisitor(this);
    } else {
      root = new Proyecto("root", null);
    }
    return root;
  }

  @Override
  public void visitarTarea(Tarea t) {
    logger.debug("Visitando Tarea");
    //Se llama al método load de la clase Tarea para obtener los atributos
    logger.trace("Tarea a la que visitamos: " + t.getNombre());
    t.load(file);
    //Se asigna el padre correspondiente a la tarea
    logger.debug("Asignando padre a la tarea");
    t.setPadre((Proyecto) padre);
    logger.trace("Asignamos al padre: " + padre.getNombre());
    //Se crea la array de intervalos que contiene la tarea actual
    logger.debug("Creando JSONArray intervalos de la tarea");
    JSONArray intervalos = file.getJSONArray("intervals");
    padre = t;

    //Se recorren todos los intervalos y se actualiza
    // la variable file para leer el intervalo correspondiente
    logger.debug("Recorriendo intervalos");
    for (int i = 0; i < intervalos.length(); i++) {
      file = (JSONObject) intervalos.get(i);
      Intervalo intervalo = new Intervalo();
      intervalo.acceptVisitor(this);
      padre = t;
    }
  }

  @Override
  public void visitarIntervalo(Intervalo i) {
    logger.debug("Visitando Intervalo");
    //Se llama al método load de la clase Intervalo para obtener los atributos
    i.load(file);
    //Se asigna el padre correspondiente al intervalo
    logger.debug("Asignando padre a intervalo");
    i.setPadre((Tarea) padre);
    logger.trace("Padre al que le asignamos: " + padre.getNombre());
  }

  @Override
  public void visitarProyecto(Proyecto p) {
    logger.debug("Visitando Proyecto");
    //Se llama al método load de la clase Proyecto para obtener los atributos
    p.load(file);

    //Se asigna el padre correspondiente al proyecto
    // y se actualiza la variable para la siguiente llamada
    logger.debug("Asignando padre al proyecto");
    p.setPadre((Proyecto) padre);
    logger.trace("Padre al que asignamos: " + p.getNombre());
    logger.debug("Actualizando variable");
    padre = p;

    //Se crea la array de actividades que contiene el proyecto actual
    logger.debug("Creando array actividades");
    JSONArray actividades = file.getJSONArray("activities");

    //Se recorren todas las actividades y se actualiza
    // la variable file para leer la actividad correspondiente
    logger.debug("Recorriendo todas las actividades");
    for (int i = 0; i < actividades.length(); i++) {
      if (i > actividades.length()) {
        logger.warn("Contador supera limite actividades");
      }
      file = (JSONObject) actividades.get(i);
      Actividad actividad;

      //Se comprueba el tipo de actividad y se crea un objeto de la clase correspondiente
      logger.debug("Comprobando tipo de actividad");
      if (file.getString("class").equals("project")) {
        actividad = new Proyecto();
        logger.debug("Creando proyecto");
      } else {
        actividad = new Tarea();
        logger.debug("Creando tarea");
      }

      //Se redirecciona a visitarProyecto o a visitarTarea según el tipo de Actividad
      logger.debug("Redireccionamiento segun tipo de Actividad");
      actividad.acceptVisitor(this);

      padre = p;
    }
  }
}
