package com.hotel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {
        inicialitzarPreus();

        int opcio = 0;
        do {
            mostrarMenu();
            opcio = llegirEnter("Seleccione una opció: ");
            gestionarOpcio(opcio);
        } while (opcio != 6);

        System.out.println("Eixint del sistema... Gràcies per utilitzar el gestor de reserves!");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Reservar una habitació");
        System.out.println("2. Alliberar una habitació");
        System.out.println("3. Consultar disponibilitat");
        System.out.println("4. Obtindre una reserva");
        System.out.println("5. Llistar reserves per tipus");
        System.out.println("6. Ixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent.
     */
    public static void gestionarOpcio(int opcio) {
        // TODO:

        switch (opcio) {
            case 1:
                reservarHabitacio();
                break;
            case 2:
                alliberarHabitacio();
                break;
            case 3:
                consultarDisponibilitat();
                break;
            case 4:
                obtindreReserva();
                break;
            case 5:
                obtindreReservaPerTipus();
                break;
            case 6:
                break;
            default:
                System.out.println("\nNúmero inválido. Inténtelo de nuevo.\n");
        }
    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        System.out.println("\n===== RESERVAR HABITACIÓ =====");
        // TODO:

        // Controla que se haya seleccionado un tipo de habitación válido
        String tipoHabitacion = seleccionarTipusHabitacioDisponible();
        if (tipoHabitacion == null) {
            return;
        }

        // Seleccionar servicios adicionales sin repetir
        ArrayList<String> servicios = seleccionarServeis();

        // Mostramos el subtotal y los servicios seleccionados

        System.out.println("\nCalculamos el total...");

        // Calcular precio total y generar codigo de la reserva
        float precioTotal = calcularPreuTotal(tipoHabitacion, servicios);
        int codigo = generarCodiReserva();

        System.out.printf("\nTOTAL: %.2f€\n", precioTotal);
        System.out.println("\n¡Reserva creada con éxito!");
        System.out.println("\nCódigo de reserva: " + codigo);

        // Guardamos la reserva en el HashMap de reservas
        ArrayList<String> datosReserva = new ArrayList<>();
        datosReserva.add(tipoHabitacion);
        datosReserva.add(String.valueOf(precioTotal));
        datosReserva.addAll(servicios);
        reserves.put(codigo, datosReserva);

        // Actualizamos la disponibilidad de habitaciones
        int disponibles = disponibilitatHabitacions.get(tipoHabitacion);
        disponibilitatHabitacions.put(tipoHabitacion, disponibles - 1);
    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i
     * retorna el nom del tipus.
     */
    public static String seleccionarTipusHabitacio() {
        // TODO:

        // Pasamos las claves del HashMap a un ArrayList
        ArrayList<String> tipos = new ArrayList<>(preusHabitacions.keySet());

        // Para poder ordenar los tipos en función del precio ascendente
        ArrayList<String> tiposOrdenados = new ArrayList<>();

        // Ordenacion ascendente
        while (!tipos.isEmpty()) {
            float minPrecio = Float.MAX_VALUE;
            String tipoMasBarato = "";

            // Buscar el tipo de habitación más barato
            for (String tipo : tipos) {
                float precioTipo = preusHabitacions.get(tipo);
                if (precioTipo < minPrecio) {
                    minPrecio = precioTipo;
                    tipoMasBarato = tipo;
                }
            }
            // Añadimos a la lista ordenada
            tiposOrdenados.add(tipoMasBarato);

            // Eliminamos el tipo ya añadido para no repetirlo en la ordenación
            tipos.remove(tipoMasBarato);
        }

        // Mostrar tipos de habitaciones ordenados en función del precio ascendente
        System.out.println("\nSeleccione el tipo:");
        int i = 1;
        for (String tipo : tiposOrdenados) {
            System.out.printf("\n %d. %s", i, tipo);
            i++;
        }
        System.out.println();

        // Pedir tipo de habitación al usuario
        int opcionHabitacion = llegirEnter("\nOpción: ");
        sc.nextLine();
        if (opcionHabitacion >= 1 && opcionHabitacion <= tiposOrdenados.size()) {
            String tipoSeleccionado = tiposOrdenados.get(opcionHabitacion - 1);
            return tipoSeleccionado;
        } else {
            System.out.println("\nNúmero no válido. Inténtelo de nuevo.\n");
            return null;
        }
    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {
        System.out.println("\nTipus d'habitació disponibles:");
        // TODO:

        // Pasamos las claves del HashMap a un ArrayList
        ArrayList<String> tipos = new ArrayList<>(preusHabitacions.keySet());

        // Para poder ordenar los tipos en función del precio ascendente
        ArrayList<String> tiposOrdenados = new ArrayList<>();

        // Ordenacion ascendente
        while (!tipos.isEmpty()) {
            float minPrecio = Float.MAX_VALUE;
            String tipoMasBarato = "";

            // Buscar el tipo de habitación más barato
            for (String tipo : tipos) {
                float precioTipo = preusHabitacions.get(tipo);
                if (precioTipo < minPrecio) {
                    minPrecio = precioTipo;
                    tipoMasBarato = tipo;
                }
            }

            // Añadimos a la lista ordenada
            tiposOrdenados.add(tipoMasBarato);

            // Eliminamos el tipo ya añadido para no repetirlo en la ordenación
            tipos.remove(tipoMasBarato);
        }
        System.out.println();

        // Mostrar disponibilidad y precio en orden ascendente
        int i = 1;
        for (String tipo : tiposOrdenados) {
            int disponibles = disponibilitatHabitacions.get(tipo);
            float precio = preusHabitacions.get(tipo);
            System.out.printf("%d. %s - %d diponibles - %.2f€\n", i, tipo, disponibles, precio);
            i++;
        }

        // Pedir tipo de habitación al usuario
        int opcionTipo = llegirEnter("\nSeleccione tipo de habitación: ");
        sc.nextLine();

        // Comprobar disponibilidad del tipo seleccionado
        if (opcionTipo >= 1 && opcionTipo <= tiposOrdenados.size()) {
            String tipoSeleccionado = tiposOrdenados.get(opcionTipo - 1);
            if (disponibilitatHabitacions.get(tipoSeleccionado) > 0) {
                return tipoSeleccionado;
            } else {
                System.out.println("\nLo sentimos, en este momento, no hay habitaciones disponibles de este tipo.\n");
                return null;
            }
        } else {
            System.out.println("\nNúmero inválido. Inténtelo de nuevo.");
            return null;
        }

    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String.
     */
    public static ArrayList<String> seleccionarServeis() {
        // TODO:

        // Creamos array dinánmico, en función de cuántos servicios escoja el cliente
        ArrayList<String> serviciosSeleccionados = new ArrayList<>();

        // Pasamos las claves del HashMap a un ArrayList
        ArrayList<String> servicios = new ArrayList<>(preusServeis.keySet());

        // Para poder ordenar los servicios en función del precio ascendente
        ArrayList<String> serviciosOrdenados = new ArrayList<>();

        // Ordenación ascendente
        while (!servicios.isEmpty()) {
            float minPrecio = Float.MAX_VALUE;
            String servicioNasBarato = "";

            // Buscar el servicio más barato
            for (String servicio : servicios) {
                float precioServicio = preusServeis.get(servicio);
                if (precioServicio < minPrecio) {
                    minPrecio = precioServicio;
                    servicioNasBarato = servicio;
                }
            }

            // Añadirlo a la lista ordenada
            serviciosOrdenados.add(servicioNasBarato);

            // Eliminamos el tipo ya añadido para no repetirlo en la ordenación
            servicios.remove(servicioNasBarato);
        }

        // Mostrar los servicios ofertados en orden ascendente
        System.out.println("\nServicios adicionales (0-4):");
        System.out.println("\n0. Finalizar");

        // Mapear cada servicio a un número, de manera que el cliente pueda elegir por
        // índice
        int i = 1;
        for (String servicio : serviciosOrdenados) {
            float precioServicio = preusServeis.get(servicio);
            System.out.printf("%d. %s (%.2f€)\n", i, servicio, precioServicio);
            i++;
        }

        // Bucle while para controlar la elección del cliente
        while (true) {
            System.out.print("\n¿Desea agregar un servicio? (s/n): ");

            // Quitamos espacios e ignoramos mayúsculas
            String agregarServicio = sc.nextLine().trim().toLowerCase();

            // Comprobramos si el string está vacío o que no corresponda a las opciones
            // definidas
            if (agregarServicio.isEmpty() || (!agregarServicio.equals("s") && !agregarServicio.equals("n"))) {
                System.out.println("\nEntrada inválida. Debe elegir entre 's' o 'n'.");
                continue;
            }
            if (agregarServicio.equals("n")) {
                break;
            }

            // Verificar si ya alcanzó el límite de servicios (4) antes de permitir agregar
            // otro servicio
            if (serviciosSeleccionados.size() >= 4) {
                System.out.println("\nHa alcanzado el número máximo de servicios (4).\n");
                break;
            }

            // Pedir tipo de servicio al usuario
            int opcionServicio = llegirEnter("\nSeleccione un servicio: ");
            sc.nextLine();
            if (opcionServicio == 0) {
                break;
            }
            if (opcionServicio >= 1 && opcionServicio <= preusServeis.size()) {
                String servicio = serviciosOrdenados.get(opcionServicio - 1);
                if (!serviciosSeleccionados.contains(servicio)) {
                    serviciosSeleccionados.add(servicio);
                    System.out.println("\nServicio agregado: " + servicio);
                } else {
                    System.out.println("\nYa seleccionó este servicio.\n");
                }
            } else {
                System.out.println("\nOpción inválida. Inténtelo de nuevo.\n");
            }
        }

        // Devuelve los servicios seleccionados
        return serviciosSeleccionados;
    }

    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {
        // TODO:

        float precioTotal = preusHabitacions.get(tipusHabitacio);
        System.out.printf("\nPrecio habitación: %.2f€\n", precioTotal);

        // En caso de seleccionar servicios adicionales
        if (!serveisSeleccionats.isEmpty()) {
            String servicios = "\nServicios: ";
            for (int i = 0; i < serveisSeleccionats.size(); i++) {
                String servicio = serveisSeleccionats.get(i);

                // Concatenamos el servicio y su precio
                servicios += servicio + " (" + preusServeis.get(servicio) + "€)";

                // Añadimos coma sola si no es el último
                if (i < serveisSeleccionats.size() - 1) {
                    servicios += ", ";
                }
                // Sumamos el precio de cada servicio al total
                precioTotal += preusServeis.get(servicio);
            }
            System.out.println(servicios);
        }

        System.out.printf("\nSubtotal: %.2f€\n", precioTotal);
        float iva = precioTotal * IVA;
        System.out.printf("\nIVA (21%%): %.2f€\n", iva);
        precioTotal += iva;
        return precioTotal;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        // TODO:

        // Comprobamos si ya existen los 900 códigos posibles (100-999)
        if (reserves.size() >= 900) {
            return -1;
        }

        int codigo;
        do {
            codigo = random.nextInt(900) + 100;
        } while (reserves.containsKey(codigo));
        return codigo;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        System.out.println("\n===== ALLIBERAR HABITACIÓ =====");
        // TODO: Demanar codi, tornar habitació i eliminar reserva

        // Pedir codigo al usuario
        int codigo = llegirEnter("\nIntroduzca el código de la reserva a anular: ");
        sc.nextLine();
        if (!reserves.containsKey(codigo)) {
            System.out.println("\nReserva no encontrada. Inténtelo de nuevo.");
            return;
        }

        System.out.println("\n¡Reserva encontrada!");

        // Actualizar el Hashnap disponibilitatHabitacions
        String tipoHabitacion = reserves.get(codigo).get(0);
        disponibilitatHabitacions.put(tipoHabitacion, (disponibilitatHabitacions.get(tipoHabitacion) + 1));
        reserves.remove(codigo);
        System.out.println("\nHabitación liberada correctamente.");
        System.out.println("\nDisponibilidad actualizada.");
    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        // TODO: Mostrar lliures i ocupades

        System.out.println("\n===== HABITACIONES DISPONIBLES =====");
        System.out.printf("\n%8s\t%s\t%s\n", "Tipos", "Libres", "Ocupados");
        mostrarDisponibilitatTipus(TIPUS_ESTANDARD);
        mostrarDisponibilitatTipus(TIPUS_SUITE);
        mostrarDisponibilitatTipus(TIPUS_DELUXE);
    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {
        // TODO: Implementar recursivitat

        // Caso base
        if (codis.length == 0) {
            return;
        }

        // Caso general
        int codi = codis[0];
        if (reserves.get(codi).get(0).equals(tipus)) {
            System.out.println("\nCódigo: " + codi);
            mostrarDadesReserva(codi);
        }

        // Array se va acortando cada vez sin el primer código para la siguiente llamada
        // recursiva
        int[] newCodis = new int[codis.length - 1];
        System.arraycopy(codis, 1, newCodis, 0, newCodis.length);
        llistarReservesPerTipus(newCodis, tipus);
    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        System.out.println("\n===== CONSULTAR RESERVA =====");
        // TODO: Mostrar dades d'una reserva concreta

        // Pedir código al usuario
        int codigo = llegirEnter("\nIntroduzca el código de la reserva: ");
        sc.nextLine();
        if (!reserves.containsKey(codigo)) {
            System.out.println("\nNo se ha encontrado ninguna reserva con este código. Inténtelo de nuevo.\n");
            return;
        } else {
            System.out.println("\nDatos de la reserva:");
            mostrarDadesReserva(codigo);
        }
    }

    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");
        // TODO: Llistar reserves per tipus

        String tipo = seleccionarTipusHabitacio();
        if (tipo == null) {
            return;
        }
        System.out.printf("\nReservas del tipo \"%s\":\n", tipo);
        int[] codigos = new int[reserves.size()];
        int i = 0;
        for (int codigo : reserves.keySet()) {
            codigos[i++] = codigo;
        }

        // Llamada recursiva que comprueba todas las reservas y muestra las que
        // coinciden con el tipo de habitación
        llistarReservesPerTipus(codigos, tipo);

        // Mensaje final, al terminar de listar reservas del mismo tipo
        System.out.println("\n(No hay más reservas de este tipo.)");
    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {
        // TODO: Imprimir tota la informació d'una reserva

        // Acceder a los datos de la reserva con el código
        ArrayList<String> datos = reserves.get(codi);
        System.out.println("\n Tipo de habitación: " + datos.get(0));
        System.out.println(" Coste total: " + datos.get(1) + "€");

        // Mostrar los servicios adicionales si los hay
        if (datos.size() > 2) {
            System.out.println(" Servicios adicionales:");
            for (int i = 2; i < datos.size(); i++) {
                System.out.println(" - " + datos.get(i));
            }
        } else {
            System.out.println(" Sin servicios adicionales");
        }

    }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    static int llegirEnter(String missatge) {
        int valor = 0;
        boolean correcte = false;
        while (!correcte) {
            System.out.print(missatge);
            valor = sc.nextInt();
            correcte = true;
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + "€");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t" + ocupades);
    }
}
