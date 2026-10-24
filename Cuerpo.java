public class Cuerpo {

    // Atributos
    private String dni;

    public static final int LEN_DNI = 9;
    public static final int LEN_LINE = LEN_DNI + 1;  // incluye el salto de linea.


    // Constructor
    public Cuerpo(String dni) {
        this.dni = dni;
    }


    // Getter Y Setter
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }






    // el formato para escribir los registros en el fichero
    public String toFich() {
        return FormatoUtils.pad(dni, LEN_DNI) + "\n";
    }
}
