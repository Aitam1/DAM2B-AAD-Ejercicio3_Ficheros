

import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    static Scanner numero = new Scanner(System.in);
    static Scanner texto = new Scanner(System.in);
    static File directorio = new File("EJE3_Z2467351P\\FILES");

    static long posNumRegistros = Cabecera.getPosicionNumeroRegistro();
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
                    System.out.println("\nEste fichero ya existe !!");


                else {

                    // Escribir la cabecera dentro del fichero
                    raf.write(cabecera.toFich().getBytes("UTF-8"));


                    // Escribir 99 dni vacíos
                    long i = 0L;
                    StringBuilder sb = new StringBuilder();
                    while(i < 99L) {
                        sb.append(FormatoUtils.pad("", Cuerpo.LEN_DNI)).append("\n");
                        i++;
                    }
                    raf.write(sb.toString().getBytes("UTF-8"));


                    // Escribir el pie dentro del fichero
                    raf.write(pie.toFich().getBytes("UTF-8"));


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

            // Leer numero de los registros
            raf.seek(posNumRegistros);
            byte[] buf = new byte[Cabecera.LEN_NUMERO_REGISTROS];
            raf.readFully(buf);

            long numeroRegistros = Long.parseLong(new String(buf, "UTF-8").trim());


            // Posicionar en la última posición
            long posicionFinal = (long) (Cabecera.LEN_LINE + (numeroRegistros * Cuerpo.LEN_LINE));

            raf.seek(posicionFinal);
            // Escribir los registros en el fichero
            for(Cuerpo r : registros) {
                raf.write(r.toFich().getBytes("UTF-8"));
            }


            // Modificar el número de registros
            long numeroRegistrosActual = numeroRegistros + (long) (registros.size());

            String s = FormatoUtils.pad(String.valueOf(numeroRegistrosActual), Cabecera.LEN_NUMERO_REGISTROS);
            raf.seek(posNumRegistros);

            raf.write(s.getBytes("UTF-8"));


            // Apuntar la modificación en el pie de la página
            raf.seek(fichero.length());
            raf.write(new Pie().toFich().getBytes("UTF-8"));

        }
    }






    // Crear el fichero o ficheros al que quiera insertar en el fichero
    public static LinkedList<Cuerpo> crearRegistros() {
        LinkedList<Cuerpo> registros = new LinkedList<>();
        // Crear los registros
        String dni = "";

        System.out.println();
        do {
            System.out.print("Inserta el dni  ('salir' para terminar):  ");
            dni = texto.nextLine();

            registros.add(new Cuerpo(dni));
        }while(!dni.equalsIgnoreCase("salir"));

        registros.remove(registros.getLast());
        return registros;
    }







    public static File eligirFichero() {
        // Elige un fichero de los ficheros creados
        File[] ficheros = directorio.listFiles();


        if(ficheros.length != 0 && ficheros != null) {
            System.out.println("\n==== FICHEROS ====");
            for (int i = 0; i < ficheros.length; i++)
                System.out.println(i + ". " + ficheros[i].getName());
            System.out.print("\nElige el id del fichero con el cual quiere trabajar :  ");
            int idFichero = numero.nextInt();
            return ficheros[idFichero];
        }


        return null;
    }








    // mostrar todos los registros que se hayan insertado
    public static void mostrarTodosRegistros() throws Exception {

        try(RandomAccessFile raf = new RandomAccessFile(ficheroActual, "r")) {
            // Leer el numero de registros
            raf.seek(posNumRegistros);
            byte[] buf = new byte[Cabecera.LEN_NUMERO_REGISTROS];
            raf.readFully(buf);
            long numeroRegistros = Long.parseLong(new String(buf, "UTF-8").trim());

            // Calcular la posicion inicial y final de los registros
            long posInicioRegistro = Cabecera.LEN_LINE;
            long posFinalRegistro =  Cabecera.LEN_LINE + (numeroRegistros * Cuerpo.LEN_LINE);

            // Leer los registros
            System.out.println("\n========== MOSTRANDO LOS REGISTROS ==========");
            System.out.println("====== " + numeroRegistros + " REGISTROS ======");
            for(long i = 0; i < numeroRegistros; i++) {
                long pos = posInicioRegistro + (Cuerpo.LEN_LINE * i);

                raf.seek(pos);
                byte[] buffer = new byte[Cuerpo.LEN_DNI];
                raf.readFully(buffer);

                System.out.println(new String(buffer, "UTF-8"));
            }

        }
    }




    // Modificar un registro completo en función a un DNI
    public static boolean modificarRegistro(String dniAnterior, String dniNuevo) throws Exception {

        try(RandomAccessFile raf = new RandomAccessFile(ficheroActual, "rw")) {
            // Leer el numero de registros
            raf.seek(posNumRegistros);
            byte[] buf = new byte[Cabecera.LEN_NUMERO_REGISTROS];
            raf.readFully(buf);
            long numeroRegistros = Long.parseLong(new String(buf, "UTF-8").trim());

            // Calcular la posicion inicial y final de los registros
            long posInicioRegistro = Cabecera.LEN_LINE;
            long posFinalRegistro =  Cabecera.LEN_LINE + (numeroRegistros * Cuerpo.LEN_LINE);


            // Leer los registros
            for(long i = 0; i < numeroRegistros; i++) {
                long pos = posInicioRegistro + (Cuerpo.LEN_LINE * i);

                raf.seek(pos);
                byte[] buffer = new byte[Cuerpo.LEN_DNI];
                raf.readFully(buffer);

                String dni = new String(buffer, "UTF-8").trim();
                // Verificar si existe el dni y modificarlo
                if(dni.equalsIgnoreCase(dniAnterior)) {
                    raf.seek(pos);
                    String s = FormatoUtils.pad(dniNuevo, Cuerpo.LEN_DNI);
                    raf.write(s.getBytes("UTF-8"));

                    // Escribir la fecha de modificación
                    raf.seek(ficheroActual.length());
                    raf.write(new Pie().toFich().getBytes("UTF-8"));
                    return true;
                }
            }
        }
        return false;
    }






    // Mostrar nombre del creador del fichero y de la última persona que modifico el fichero
    public static void mostrarNombreCreador() throws Exception {
        System.out.println("\n====== MOSTRANDO NOMBRE CREADOR Y LA ULTIMA PERSONA QUE MODIFICO EL FICHERO ======");

         try(RandomAccessFile raf = new RandomAccessFile(ficheroActual, "r")) {
             //Leer el nombre del creador
             raf.seek(0);
             byte[] buf = new byte[Cabecera.LEN_NOMBRE_AUTOR];
             raf.readFully(buf);
             String nombreCreador = new String(buf, "UTF-8").trim();


             // Leer el nombre de la última persona que modifico el fichero
             long posUltimaPersona = ficheroActual.length() - Pie.LEN_LINE;
             raf.seek(posUltimaPersona);
             buf = new byte[Pie.LEN_NOMBRE_AUTOR_MODIFICO];
             raf.readFully(buf);
             String nombreUltimaPersona = new String(buf, "UTF-8").trim();


             System.out.println("Nombre del creador del fichero  :  " + nombreCreador);
             System.out.println("Nombre la ultima persona que modifico el fichero  :  " + nombreUltimaPersona);
         }
    }






    public static void main(String[] args) {


        int op = -1;
        String menu = "\n===================== MENU PRINCIPAL =====================\n" +
                    "1. crear fichero nuevo.\n" +
                    "2. eliminar un fichero.\n" +
                    "3. fichero actual de trabajo.\n" +
                    "4. eligir/cambiar fichero de trabajo.\n" +
                    "5. insertar registros en la ultima posición libre disponible del fichero.\n" +
                    "6. mostrar todos los registros que se han insertado.\n"  +
                    "7. modificar un registro completo en función a un DNI.\n" +
                    "8. mostrar el nombre del creador del fichero y mostrar el nombre de la ultima persona que modifico el fichero.\n" +
                    "9. mostrar todas las fechas en las que se ha modificado el fichero.\n" +
                    "10. salir.\n" +
                    "Elige una opción  :  ";




        do {
            System.out.print(menu);
            op = numero.nextInt();

            // Al ejecutar de nuevo el programa hay que eligir un fichero para trabajar o crear uno
            if(ficheroActual == null && op != 1)
                op = 4;

            switch(op) {

                case 1:
                    System.out.println("\n===== CREAR FICHERO NUEVO =====");
                    System.out.print("Introduce el nombre del fichero que quiere crear.  (ejemplo : fichero.txt)  :  ");
                    String nomFichero = texto.nextLine();

                    crearFicheroNuevo(nomFichero);
                    break;



                case 2:
                    File fichero = eligirFichero();
                    if(fichero == null)
                        System.out.println("\nTodavía no hay ningún fichero!");
                    else
                        Fichero.eliminarFichero(fichero);

                    break;



                case 3:
                    if(ficheroActual == null)
                        System.out.println("\nPRIMERO : elige el fichero con el cual quiera trabajar.");
                    else
                        System.out.println("El fichero activo es  :  " + ficheroActual.getName());
                    break;


                case 4:
                    File file = eligirFichero();

                    if(file == null)
                        System.out.println("\nTiene que crear un fichero porque todavía no hay.");
                    else {
                        ficheroActual = file;
                        System.out.println("Hemos cambiado al fichero  :  " + file.getName());
                    }
                    break;



                case 5:
                    try {
                        insertarRegistroUltimaPosicion(ficheroActual);
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;



                case 6:
                    try {
                        mostrarTodosRegistros();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;



                case 7:
                    System.out.println("\n==========MODIFICAR UN REGISTRO ==========");
                    System.out.print("Inserta el DNI que desea modificar  :  ");
                    String dniAnterior = texto.nextLine();
                    System.out.print("Inserta el nuevo DNI  :  ");
                    String dniNuevo = texto.nextLine();

                    try {
                        if(modificarRegistro(dniAnterior, dniNuevo))
                            System.out.println("\nDNI encontrado y modificado.");
                        else
                            System.out.println("\nDNI no encontrado!!");
                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                    break;




                case 8:
                    try {
                        mostrarNombreCreador();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case 10:
                    System.out.println("\nSaliendo del programa. . .");
                    break;

                default:
                    System.out.println("Opción invalida !!");
            }

        }while(op != 10);


    }
}