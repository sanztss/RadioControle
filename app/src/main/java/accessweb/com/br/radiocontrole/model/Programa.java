package accessweb.com.br.radiocontrole.model;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class Programa {
    private String horaInicioPrograma;
    private String nomePrograma;
    private String fotoLocutorPrograma;
    private String nomeLocutorPrograma;

    public Programa(){

    }

    public Programa(String horaInicioPrograma, String nomePrograma, String fotoLocutorPrograma, String nomeLocutorPrograma) {
        this.horaInicioPrograma = horaInicioPrograma;
        this.nomePrograma = nomePrograma;
        this.fotoLocutorPrograma = fotoLocutorPrograma;
        this.nomeLocutorPrograma = nomeLocutorPrograma;
    }

    public String getHoraInicioPrograma() {
        return horaInicioPrograma;
    }

    public void setHoraInicioPrograma(String horaInicioPrograma) {
        this.horaInicioPrograma = horaInicioPrograma;
    }

    public String getNomePrograma() {
        return nomePrograma;
    }

    public void setNomePrograma(String nomePrograma) {
        this.nomePrograma = nomePrograma;
    }

    public String getFotoLocutorPrograma() {
        return fotoLocutorPrograma;
    }

    public void setFotoLocutorPrograma(String fotoLocutorPrograma) {
        this.fotoLocutorPrograma = fotoLocutorPrograma;
    }

    public String getNomeLocutorPrograma() {
        return nomeLocutorPrograma;
    }

    public void setNomeLocutorPrograma(String nomeLocutorPrograma) {
        this.nomeLocutorPrograma = nomeLocutorPrograma;
    }
}