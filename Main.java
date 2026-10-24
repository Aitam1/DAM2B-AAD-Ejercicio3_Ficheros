

import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    static Scanner numero = new Scanner(System.in);
    static Scanner texto = new Scanner(System.in);
    static File directorio = new File("EJE3_Z2467351P\\FILES");
    static LinkedList<String> ficherosCreados = new LinkedList<>();

    static File ficheroActual = null;




    // CASE 1: Crear fichero nuevo
    public static void crearFicheroNuevo(String nomFichero) {



        Cabecera cabecera = new Cabecera(0);
        Pie pie = new Pie();

        try {
            File fichero = Fichero.crearFichero(nomFichero, directorio);
            // Asignar el fichero creado para trabajar con él
            ficheroActual = fichero;

            try(RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {

                if(fichero.length() != 0)
                    System.out.println("Este fichero ya existe !!");


                else {

                    // Escribir la cabecera dentro del fichero
                    raf.write(cabecera.toFich().getBytes("UTF-8"));

                    // Escribir el pie dentro del fichero
                    raf.write(pie.toFich().getBytes("UTF-8"));


                    // Anadir el nombre del fichero en la lista de los ficheros creados
                    ficherosCreados.add(nomFichero);
                }
            }catch(Exception e) {
                e.printStackTrace();
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
    }







    // CASE 2: Insertar registro en la última posición libre disponible
    public static void insertarRegistroUltimaPosicion(File fichero) throws Exception {

        LinkedList<Cuerpo> registros = crearRegistros();

        // Comprobar que el fichero existe, es un fichero, tiene permiso de lectura y escritura
        if(!Fichero.ficheroValido(fichero)) {
            System.out.println("\nEl fichero al que quiere acceder no es valido !!");
            return;
        }


        try(RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            // Primero hay que leer cuantos registros hay
            int posNumRegistros = Cabecera.LEN_NOMBRE_AUTOR + 1 + Cabecera.LEN_FECHA_CREACION + 1;

            raf.seek(posNumRegistros);
            byte[] buf = new byte[Cabecera.LEN_NUMERO_REGISTROS];
            raf.readFully(buf);

            int numeroRegistros = Integer.parseInt(new String(buf, "UTF-8").trim());


            // Posicionar en la última posición
            Long posicionFinal = (long) (Cabecera.LEN_LINE + (numeroRegistros * Cuerpo.LEN_LINE));

            raf.seek(posicionFinal);

            for(Cuerpo r : registros) {
                raf.write(r.toFich().getBytes("UTF-8"));
            }


            // Modificar el número de registros
            raf.seek(posNumRegistros);
            // Código . . .

        }

    }






    // Crear el fichero o ficheros al que quiera insertar en el fichero
    public static LinkedList<Cuerpo> crearRegistros() {
        LinkedList<Cuerpo> registros = new LinkedList<>();
        // Crear los registros
        String dni = "";
        do {
            System.out.print("Inserta el dni  ('salir' para terminar):  ");
            dni = texto.nextLine();

            registros.add(new Cuerpo(dni));
        }while(!dni.equalsIgnoreCase("salir"));

        registros.remove(registros.getLast());
        return registros;
    }



    public static String eligirFichero() {
        // Elige un fichero de los ficheros creados

        for(int i = 0; i < ficherosCreados.size(); i++)
            System.out.println(i + " " + ficherosCreados.get(i));

        System.out.print("\nIntroduce el numero del fichero  :  ");
        int idFichero = numero.nextInt();

        return ficherosCreados.get(idFichero);
    }


    // mostrar todos los registros que se hayan insertado
    public static void mostrarTodosRegistros() throws Exception {
        
    }












    public static void main(String[] args) {


        int op = -1;
        String menu = "===================== MENU PRINCIPAL =====================\n" +
                    "1. crear fichero nuevo.\n" +
                    "2. fichero actual de trabajo.\n" +
                    "3. cambiar fichero de trabajo.\n" +
                    "4. insertar registros en la ultima posición libre disponible del fichero.\n" +
                    "5. mostrar todos los registros que se han insertado.\n"  +
                    "6. modificar un registro completo en función a un DNI.\n" +
                    "7. mostrar el nombre del creador del fichero y mostrar el nombre de la ultima persona que modifico el fichero.\n" +
                    "8. mostrar todas las fechas en las que se ha modificado el fichero.\n" +
                    "9. salir.\n" +
                    "Elige una opción  :  ";




        do {
            System.out.print(menu);
            op = numero.nextInt();


            switch(op) {

                case 1:
                    System.out.println("\n===== CREAR FICHERO NUEVO =====");
                    System.out.println("Introduce el nombre del fichero que quiere crear.  (ejemplo : fichero.txt)  :  ");
                    String nomFichero = texto.nextLine();

                    crearFicheroNuevo(nomFichero);
                    break;


                case 2:
                    System.out.println("El fichero activo es  :  " + ficheroActual);
                    break;


                case 3:
                    String fichero = eligirFichero();
                    ficheroActual = new File(directorio, fichero);
                    System.out.println("Hemos cambiado al fichero  :  " + fichero);
                    break;



                case 3:
                    File file = new File(directorio, ficherosCreados.get(eligirFichero()));


                    break;

            }

        }while(op != 9);


    }
}