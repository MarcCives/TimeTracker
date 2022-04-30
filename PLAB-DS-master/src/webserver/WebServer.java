package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import milestonea.Actividad;
import milestonea.Proyecto;
import milestonea.Saver;
import milestonea.Tarea;
import milestoneb.Searcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Based on
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// http://www.jcgonzalez.com/java-socket-mini-server-http-example
/**
 * Esto es un comentario para javadoc.
 */
public class WebServer {
  private static final int PORT = 8080; // port to listen to
  private final Actividad root;
  private final List<Integer> recientesId;

  private final Logger logger = LoggerFactory.getLogger("WebServer");

  /**
   * Esto es un comentario para javadoc.
   */
  public WebServer(Actividad root) {
    logger.debug("Inicializando WebServer");
    this.root = root;
    logger.info("Información del root: " + root);
    //System.out.println(root);
    recientesId = new ArrayList<>();

    try {
      ServerSocket serverConnect = new ServerSocket(PORT);
      logger.info("Server started.\nListening for connections on port : " + PORT + " ...\n");
      //System.out.println("Server started.\nListening for
      // connections on port : " + PORT + " ...\n");
      // we listen until user halts server execution
      while (true) {
        // each client connection will be managed in a dedicated Thread
        logger.debug("Creacion de un nuevo socketThread");
        new SocketThread(serverConnect.accept());
        // create dedicated thread to manage the client connection
      }
    } catch (IOException e) {
      logger.warn("\"Server Connection error : " + e.getMessage());
      //System.err.println("\"Server Connection error : \" + e.getMessage()");
    }
  }

  private Actividad findActivityById(int id) {
    logger.debug("Devolviendo la actividad correspondiente al id pasado por parametros");
    return root.findActivityById(id);
  }

  private class SocketThread extends Thread {
    // SocketThread sees WebServer attributes
    private final Socket insocked;
    private final Logger logger = LoggerFactory.getLogger("Thread.SocketThread");
    // Client Connection via Socket Class

    SocketThread(Socket insocket) {
      logger.debug("Inicializacion del SocketThread");
      this.insocked = insocket;
      this.start();
    }

    @Override
    public void run() {
      // we manage our particular client connection
      BufferedReader in;
      PrintWriter out;
      String resource;

      logger.debug("Iniciando funcion run");
      try {
        // we read characters from the client via input stream on the socket
        in = new BufferedReader(new InputStreamReader(insocked.getInputStream()));
        // we get character output stream to client
        out = new PrintWriter(insocked.getOutputStream());
        // get first line of the request from the client
        String input = in.readLine();
        // we parse the request with a string tokenizer
        logger.info("sockedthread : " + input);
        //System.out.println("sockedthread : " + input);

        StringTokenizer parse = new StringTokenizer(input);
        String method = parse.nextToken().toUpperCase();
        // we get the HTTP method of the client
        if (!method.equals("GET")) {
          logger.info("501 Not Implemented : " + method + " method.");
          //System.out.println("501 Not Implemented : " + method + " method.");
        } else {
          // what comes after "localhost:8080"
          resource = parse.nextToken();
          logger.info("input " + input);
          logger.info("method " + method);
          logger.info("resource " + resource);
          //System.out.println("input " + input);
          //System.out.println("method " + method);
          //System.out.println("resource " + resource);

          parse = new StringTokenizer(resource, "/[?]=&");
          int i = 0;
          logger.trace("Variable i con valor: " + i);
          String[] tokens = new String[20];
          // more than the actual number of parameters
          while (parse.hasMoreTokens()) {
            tokens[i] = parse.nextToken();
            logger.info("token " + i + "=" + tokens[i]);
            //System.out.println("token " + i + "=" + tokens[i]);
            i++;
          }

          // Make the answer as a JSON string, to be sent to the Javascript client
          String answer = makeHeaderAnswer() + makeBodyAnswer(tokens);
          logger.info("answer\n" + answer);
          //System.out.println("answer\n" + answer);
          // Here we send the response to the client
          out.println(answer);
          out.flush(); // flush character output stream buffer
          logger.debug("Guardamos la información del root con la funció saveData");
          saveData(root);
        }

        in.close();
        out.close();
        insocked.close(); // we close socket connection
      } catch (Exception e) {
        logger.warn("Exception : " + e);
        //System.err.println("Exception : " + e);
      }
    }

    private String makeBodyAnswer(String[] tokens) {
      logger.debug("Creando Body");
      String body = "";
      logger.trace("Body con valor: " + body);

      switch (tokens[0]) {
        case "get_tree": {
          logger.debug("Case get_tree");
          int id = Integer.parseInt(tokens[1]);

          Actividad activity = findActivityById(id);
          if (activity.getClass() == Tarea.class) {
            if (!recientesId.contains(activity.getId())) {
              if (recientesId.size() >= 3) {
                recientesId.remove(0);
              }
              logger.debug("Añadiendo a recientes las actividades");
              recientesId.add(activity.getId());
              logger.trace("Tamaño del array recientesId: " + recientesId.size());
            }
          }
          logger.debug("Dando valor al body en formato JSON");
          body = activity.toJson(1).toString();
          break;
        }

        case "start": {
          logger.debug("Case Start");
          int id = Integer.parseInt(tokens[1]);

          Actividad activity = findActivityById(id);
          assert (activity != null);
          Tarea task = (Tarea) activity;
          logger.debug("Empezando una tarea");
          task.start();
          body = "{}";
          break;
        }

        case "stop": {
          logger.debug("Case Stop");
          int id = Integer.parseInt(tokens[1]);

          Actividad activity = findActivityById(id);
          assert (activity != null);
          Tarea task = (Tarea) activity;
          logger.debug("Parando una tarea");
          task.stop();
          body = "{}";
          break;
        }

        case "get_recents": {
          logger.debug("Case get_recents");
          logger.debug("Guardamos los recientes en array en formato JSON");
          JSONArray ar = new JSONArray();

          for (int i : recientesId) {
            ar.put(findActivityById(i).toJson(1));
          }

          Proyecto r = new Proyecto("Recientes", null);
          JSONObject jo = r.getJson();

          jo.put("activities", ar);
          logger.debug("Dando valor al body");
          body = jo.toString();
          break;
        }

        case "search": {
          logger.debug("Case search");
          String tag = tokens[1];
          Searcher search = new Searcher();
          logger.trace("Buscando actividades con el tag " + tag);
          List<Actividad> searched = search.search(root, tag);
          logger.debug("Transformando actividades encontradas a JSON");
          JSONArray ar = new JSONArray();

          for (Actividad act : searched) {
            JSONObject a = act.getJson();
            if (act.getClass() == Tarea.class) {
              a.put("intervals", new JSONArray());
            }
            ar.put(a);
          }

          Proyecto r = new Proyecto(tag, null);
          JSONObject jo = r.getJson();

          jo.put("activities", ar);
          logger.debug("Dando valor al body");
          body = jo.toString();
          break;
        }

        case "addActivity": {
          logger.debug("Case addActivity");
          int id = Integer.parseInt(tokens[1]);
          String tag = tokens[2];
          String name = tokens[3];
          String ret = tokens[4];
          logger.debug("Obteniendo información pasada por parametros");

          Actividad activity = findActivityById(id);
          assert (activity != null);

          String[] tags = tag.split(",%20");

          Actividad newActivity;
          if (ret.equals("Project")) {
            newActivity = new Proyecto(name, (Proyecto) activity);

            //((Proyecto) activity).anadirActividad(pAux);
          } else {
            newActivity = new Tarea(name, (Proyecto) activity);
            //((Proyecto) activity).anadirActividad(tAux);
          }
          logger.debug("Añadiendo actividad a JSON");
          for (String s : tags) {
            newActivity.addTag(tag);
          }

          logger.debug("Dando valor al body");
          body = activity.toJson(1).toString();
          break;
        }

        default:
          assert false;
      }
      logger.info(body);
      return body;
    }

    public static void saveData(Actividad root) {
      //logger.debug("Inciando Saver");
      Saver s = new Saver("datos.json");
      s.save(root);
    }

    private String makeHeaderAnswer() {
      String answer = "";
      answer += "HTTP/1.0 200 OK\r\n";
      answer += "Content-type: application/json\r\n";
      answer += "\r\n";
      // blank line between headers and content, very important !
      return answer;
    }
  } // SocketThread

} // WebServer