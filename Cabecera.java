import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Cabecera {

    // Esto por si estoy seguro de que siempre voy a ser el creador.
    private static final String NOMBRE_CREADOR = "Haytham Qelai";



    // Atributos
    private String nombreAutor; // Por si algún día el creador cambiaria.
    private String fechaCreacion;
    private int numRegistros;

    public static final int LEN_NOMBRE_AUTOR = 20;
    public static final int LEN_FECHA_CREACION = 8;
    public static final int LEN_NUMERO_REGISTROS = 2;

    public static final int LEN_LINE = LEN_NOMBRE_AUTOR + 1 + LEN_FECHA_CREACION + 1 + LEN_NUMERO_REGISTROS + 1;    // Incluye las comas de separación y salto de línea.



    // Constructor 1 : Si no se introduce el nombre del autor va a ser por defecto: Haytham Qelai.
    public Cabecera(int numRegistros) {
        this(NOMBRE_CREADOR, numRegistros);
    }


    // Constructor 2: por si querría cambiar el nombre del autor por otro nuevo.
    public Cabecera(String nombreAutor, int numRegistros) {
        this.nombreAutor = (nombreAutor != null) ? nombreAutor : NOMBRE_CREADOR;
        this.fechaCreacion = formatoFecha(LocalDate.now());
        this.numRegistros = numRegistros;
    }



    // Cambiar el formato de la fecha
    public String formatoFecha(LocalDate fechaActual) {
        DateTimeFormatter formateada = DateTimeFormatter.ofPattern("ddMMyyyy");

        return fechaActual.format(formateada);
    }


    // Getter Y Setter
    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getNumRegistros() {
        return numRegistros;
    }

    public void setNumRegistros(int numRegistros) {
        this.numRegistros = numRegistros;
    }






    // Formato de escritura de la cabecera dentro del fichero
    public String toFich() {
        return FormatoUtils.pad(nombreAutor, LEN_NOMBRE_AUTOR) + "," + FormatoUtils.pad(fechaCreacion, LEN_FECHA_CREACION) + "," + FormatoUtils.pad(String.valueOf(numRegistros), LEN_NUMERO_REGISTROS) + "\n";

    }
}
