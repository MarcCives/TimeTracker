package milestonea;

/**
 * Esto es un comentario para javadoc.
 */

public interface Visitor {

  void visitarTarea(Tarea t);

  void visitarProyecto(Proyecto p);

  void visitarIntervalo(Intervalo i);

}


