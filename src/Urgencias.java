/* =====================================================================
 *  EXAMEN PARCIAL 1 - ESTRUCTURAS DE DATOS
 *  Escenario A - "Sala de Urgencias"
 *
 *  INSTRUCCIONES
 *  1. Copia este archivo dentro de "src/" del proyecto de Java
 *     de la estructura de datos que elegiste (uno de tus 5 laboratorios),
 *     junto a sus archivos (por ejemplo Queue.java, QueueInterface.java,
 *     QueueNode.java).
 *  2. Completa TODOS los bloques marcados con el comentario // TODO
 *  3. Ejecuta la clase Urgencias (metodo main) desde tu IDE.
 *
 *  RESTRICCIÓN: la sala de espera debe almacenarse en TU estructura de
 *  datos y sus métodos implementados.
 *  Se permite el uso de ArrayList, LinkedList, java.util.PriorityQueue,
 *  arreglos redimensionables de la libreria estandar,etc. En caso que su ED no esté
 *  terminada o no funcione bien, pero se califica sobre 3.5.
 *  Si se permite: Scanner, System.out, String y tipos primitivos.
 * =====================================================================
 *
 *  JUSTIFICACION DE LA ESTRUCTURA DE DATOS ELEGIDA
 *  (rellena aqui o en el archivo .md del enunciado)
 *
 *  Estructura elegida .....: Queue (Cola con soporte de Cola de Prioridad)
 *  Laboratorio de origen ..: ( 05 )
 *
 *  Por que esta estructura y no otra?
 *  - Nuestra clase Queue ya cuenta con soporte nativo para Cola de Prioridad (isPriorityQueue = true) mediante el metodo sobrecargado insert(Object, int).
 *  - Al insertar con la condicion (current.next.priority <= prioridad), ubica al paciente segun su urgencia (menor numero primero) y garantiza automaticamente el desempate FIFO (orden de llegada) sin calculos extra.
 *  - La operacion atender() se ejecuta en tiempo constante O(1) invocando directamente el metodo extract() de la cola, que retira y devuelve siempre la cabeza (head).
 *  - El recorrido para estado() es lineal O(n) y directo desde head hasta tail porque los nodos (QueueNode) ya permanecen en el orden exacto de atencion.
 *  - Es una estructura dinamica enlazada sin limite fijo de memoria, ideal para una sala de urgencias con flujo variable de pacientes.
 *
 * ===================================================================== */

import java.util.Scanner;

public class Urgencias {

    // =================================================================
    // COMO SE REPRESENTA UN PACIENTE
    //
    // Cada paciente se guarda como UN TEXTO con 3 datos separados por ';'
    //
    //        nombre ; prioridad ; ordenDeLlegada
    //
    // Ejemplos:   "Ana;3;0"      "Luis;1;1"      "Marta;3;2"
    //
    // Usa las funciones de ayuda de mas abajo para leer cada parte:
    //        nombreDe("Ana;3;0")     ->  "Ana"
    //        prioridadDe("Ana;3;0")  ->  3
    //        ordenDe("Ana;3;0")      ->  0
    //
    // 'ordenDeLlegada' es 0, 1, 2, ... segun el orden en que llegaron.
    // Sirve para desempatar: a IGUAL prioridad, primero el numero menor.
    // =================================================================
    static final String SEP = ";";

    /**
     * Contador de llegadas: 0 para el primer paciente, 1 para el segundo, ...
     */
    int contadorLlegadas = 0;

    // =================================================================
    // PASO 1 - Conecta AQUI tu estructura de datos.
    // TODO:
    // Descomenta / adapta UNA de estas lineas segun tu laboratorio.
    // La sala de espera vive dentro de esta estructura y cada elemento
    // que guardes en ella es un String con el formato de arriba.
    //
    //   MiLista         sala = new MiLista();          // Lab 01 - Lista simple
    //   MiListaDoble    sala = new MiListaDoble();     // Lab 02 - Lista doble
    //   MiListaCircular sala = new MiListaCircular();  // Lab 03 - Lista circular
    //   ListStack       sala = new ListStack();        // Lab 04 - Pila
    Queue           sala = new Queue(true);        // Lab 05 - Cola (activada como Cola de Prioridad)
    //
    // Recuerda: tus estructuras guardan Nodos, asi que al
    // guardar un elemento deberás instanciar (new) un Nodo y ponerle la información del Paciente.
    // -----------------------------------------------------------------
    // OPERACION 1 - Llega un paciente  (no produce salida)
    // -----------------------------------------------------------------
    public void llega(String nombre, int prioridad) {
        String paciente = nombre + SEP + prioridad + SEP + contadorLlegadas;
        contadorLlegadas++;

        // TODO: inserta 'paciente' (un String) en tu estructura de datos.
        //
        // Si decides mantener la sala ORDENADA al insertar, este es el
        // lugar para recorrer y ubicar 'paciente' comparando con su prioridad.
        // Si tu estructura ya ordena por prioridad, basta con insertarlo.

        sala.insert(paciente, prioridad);
    }

    // -----------------------------------------------------------------
    // OPERACION 2 - Ver estado de la sala
    //   Con pacientes:  ESPERANDO: <n1>(p<pri1>), <n2>(p<pri2>), ...
    //   Sin pacientes:  SALA_VACIA
    //   El orden debe ser EXACTAMENTE el orden en que serian atendidos.
    // -----------------------------------------------------------------
    public String estado() {
        // TODO:
        // 1. Si la sala esta vacia -> return "SALA_VACIA";
        // 2. Recorre los pacientes EN ORDEN DE ATENCION y arma el texto:
        // Para cada paciente 'p' (un String):
        //        nombreDe(p) + "(p" + prioridadDe(p) + ")"
        //    separando un paciente del siguiente con ", " (coma y espacio)
        //    y con el prefijo "ESPERANDO: " al inicio.

        if (sala.isEmpty()) {
            return "SALA_VACIA";
        }

        String resultado = "ESPERANDO: ";
        QueueNode actual = sala.head;

        while (actual != null) {
            String p = (String) actual.object;
            resultado += nombreDe(p) + "(p" + prioridadDe(p) + ")";
            if (actual.next != null) {
                resultado += ", ";
            }
            actual = actual.next;
        }

        return resultado;
    }

    // -----------------------------------------------------------------
    // OPERACION 3 - Atender al siguiente paciente  (lo retira de la sala)
    //   Con paciente:  ATENDIDO: <nombre> (prioridad <prioridad>)
    //   Sin pacientes: SIN_PACIENTES
    // -----------------------------------------------------------------
    public String atender() {
        // TODO:
        // 1. Si la sala esta vacia -> return "SIN_PACIENTES";
        // 2. Obten el paciente que debe atenderse:
        //      - menor numero de prioridad primero;
        //      - a igual prioridad, el de menor ordenDe(...).
        //    Segun tu estructura, esto puede ser:
        //      - extraer la cabeza (si la mantienes ordenada), o
        //      - recorrer buscando el minimo en prioridad
        // 3. Retiralo de tu estructura.
        // 4. Con ese paciente 'p' (un String):
        //    return "ATENDIDO: " + nombreDe(p) + " (prioridad " + prioridadDe(p) + ")";

        if (sala.isEmpty()) {
            return "SIN_PACIENTES";
        }

        String p = (String) sala.extract();
        return "ATENDIDO: " + nombreDe(p) + " (prioridad " + prioridadDe(p) + ")";
    }

    // =================================================================
    // FUNCIONES DE AYUDA para leer un paciente guardado como texto.
    // No necesitas modificarlas.
    // =================================================================
    /**
     * De "Ana;3;0" devuelve "Ana".
     */
    static String nombreDe(String paciente) {
        return paciente.split(SEP)[0];
    }

    /**
     * De "Ana;3;0" devuelve 3.
     */
    static int prioridadDe(String paciente) {
        return Integer.parseInt(paciente.split(SEP)[1]);
    }

    /**
     * De "Ana;3;0" devuelve 0.
     */
    static int ordenDe(String paciente) {
        return Integer.parseInt(paciente.split(SEP)[2]);
    }

    // =================================================================
    // Bucle de menu. NO deberias necesitar cambiar nada de aqui abajo,
    // salvo que quieras mejorar los mensajes.
    // =================================================================
    public static void main(String[] args) {
        Urgencias sistema = new Urgencias();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("===== SALA DE URGENCIAS =====");
            System.out.println("1. Llega un paciente");
            System.out.println("2. Ver estado de la sala");
            System.out.println("3. Atender al siguiente paciente");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            String opcion = sc.hasNextLine() ? sc.nextLine().trim() : "0";

            switch (opcion) {
                case "1": {
                    System.out.print("Nombre: ");
                    String nombre = sc.hasNextLine() ? sc.nextLine().trim() : "";
                    System.out.print("Prioridad (1-5): ");
                    String raw = sc.hasNextLine() ? sc.nextLine().trim() : "";

                    int prioridad;
                    try {
                        prioridad = Integer.parseInt(raw);
                    } catch (NumberFormatException e) {
                        System.out.println("ENTRADA_INVALIDA");
                        break;
                    }
                    if (nombre.isEmpty() || nombre.contains(SEP)
                            || prioridad < 1 || prioridad > 5) {
                        System.out.println("ENTRADA_INVALIDA");
                        break;
                    }
                    sistema.llega(nombre, prioridad);
                    break;
                }
                case "2":
                    System.out.println(sistema.estado());
                    break;
                case "3":
                    System.out.println(sistema.atender());
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("OPCION_INVALIDA");
            }
        }

        sc.close();
    }
}