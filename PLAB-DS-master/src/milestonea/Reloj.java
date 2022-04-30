package milestonea;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esto es un comentario para javadoc.
 */

public class Reloj extends Observable {

  private static Reloj reloj = null;
  private LocalDateTime horaActual;
  public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private Timer timer; //thread

  private final Logger logger = LoggerFactory.getLogger("Reloj");

  private Reloj() {
    super();
    logger.debug("Inicializando Reloj");
    timer = new Timer(true);
    horaActual = LocalDateTime.now();
    logger.trace("Hora actual: " + horaActual);

    long period = 2000;
    logger.trace("Periodo: " + period);

    //Creación de un Thread para que el reloj se ejecute paralelamente al resto del programa
    logger.debug("Creando un thread para ejecutar el reloj paralelamente");
    final TimerTask timerTask;
    timerTask = new TimerTask() {
        public void run() {
            horaActual = LocalDateTime.now();
            notificar();
        }
    };

    timer = new Timer();
    timer.schedule(timerTask, 0, period);
  }

  private static synchronized  void crearInstancia() {
    //logger.debug("Creando instancia de Reloj");
    if (reloj == null) {
      reloj = new Reloj();
    }
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public static Reloj getInstancia() {
    //logger.debug("Devolviendo instancia de Reloj");
    crearInstancia();
    return reloj;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  public void stop() {
    logger.debug("Para el contador del tiempo");
    timer.cancel();
    logger.trace("Hora actual: " + LocalDateTime.now().format(formatter));
  }

  public LocalDateTime getHoraActual() {
    logger.trace("Hora actual: " + horaActual.format(formatter));
    return horaActual;
  }

  /**
   * Esto es un comentario para javadoc.
   */

  //Notifica al intérvalo la hora actual
  public void notificar() {
    logger.debug("Notificando hora actual a Intervalo");
    setChanged();
    notifyObservers(this);
  }

  //Se transforma el formato de la fecha para que sea más entendible y manejable
  public String toString(LocalDateTime l) {
    logger.debug("Transformando formato de fecha");
    return l.format(formatter);
  }
}
