package EJE3;

import java.time.LocalDateTime;

public class Pie {

    private static final String NOMBRE_AUTOR_MODIFICO = "Haytham Qelai";

    // Atributos
    private String nombreAutorModifico;
    private LocalDateTime fechaModificacion;


    public static final int LEN_NOMBRE_AUTOR_MODIFICO  = 20;
    public static final int LEN_FECHA_MODIFICACION = 40;

    public static final int LEN_LINE = LEN_NOMBRE_AUTOR_MODIFICO + 1 + LEN_FECHA_MODIFICACION + 1;



    // Constructor 1:
    public Pie() {
        this(NOMBRE_AUTOR_MODIFICO);
    }


    // Constructor 2:
    public Pie(String nombreAutorModifico) {
        this.nombreAutorModifico = (nombreAutorModifico != null) ? nombreAutorModifico : NOMBRE_AUTOR_MODIFICO;
        this.fechaModificacion = LocalDateTime.now();
    }




    // Getter Y Setter
    public String getNombreAutorModifico() {
        return nombreAutorModifico;
    }

    public void setNombreAutorModifico(String nombreAutorModifico) {
        this.nombreAutorModifico = nombreAutorModifico;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }



    // Formato de escritura del pie de pagina dentro del fichero
    public String toFich() {
        String fechaModificacionString = String.valueOf(getFechaModificacion());

        return FormatoUtils.pad(nombreAutorModifico, LEN_NOMBRE_AUTOR_MODIFICO) + "," + FormatoUtils.pad(fechaModificacionString, LEN_FECHA_MODIFICACION) + "\n";
    }
}
