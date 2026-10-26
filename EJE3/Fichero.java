package EJE3;

import java.io.File;

public class Fichero {


    // crear un nuevo fichero
    public static File crearFichero(String nombreFich, File directorio) throws Exception {

        // Comprobar la carpeta donde vamos a crear el fichero, si no existe la creamos.
        if(!carpetaExiste(directorio)) {
            boolean carpCreada = directorio.mkdirs();
            System.out.println("\nLa carpeta ha sido creada  :  " + carpCreada);
        }


        File fichero = new File(directorio, nombreFich);
        if(!fichero.exists()) {
            boolean fichCreado = fichero.createNewFile();
            System.out.println("\nEl fichero ha sido creado  :  " + fichCreado);

        }else
            System.out.println("\nEl fichero ya existe !!");

        return fichero;
    }



    // Comprobar que la carpeta existe, si no la creo
    public static boolean carpetaExiste(File directorio) {
        return directorio.exists() && directorio.isDirectory();
    }


    // Comprobar el fichero existe, es un fichero, tiene permiso de lectura y escritura
    public static boolean ficheroValido(File fichero) {
        return fichero.exists() && fichero.isFile() && fichero.canRead() && fichero.canWrite();
    }



    // Eliminar un fichero
    public static void eliminarFichero(File fichero) {

        if(fichero.exists()) {
            boolean fichEliminado = fichero.delete();
            System.out.println("\nEl fichero se ha eliminado  :  " + fichEliminado);
        }else
            System.out.println("\nEste fichero no existe!");
    }


}
