public class FormatoUtils {


    // Formato de la insertion de los datos dentro del fichero, Cada campo tendrá un tamaño fijo.
    public static String pad(String s, int len) {
        StringBuilder sb = new StringBuilder((s != null) ? s : "");

        while(sb.length() < len) sb.append(" ");
        return sb.substring(0, len);
    }
}
