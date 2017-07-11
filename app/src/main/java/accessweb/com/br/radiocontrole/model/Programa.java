package accessweb.com.br.radiocontrole.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class Programa {
    private String horaInicioPrograma;
    private String nomePrograma;
    private String fotoLocutorPrograma;
    private String nomeLocutorPrograma;
    private Calendar dataPrograma;
    private Boolean notificarPrograma = false;
    private BigDecimal idPrograma;
    private String diaPrograma;

    public Programa(){

    }

    public Programa(String horaInicioPrograma, String nomePrograma, String fotoLocutorPrograma, String nomeLocutorPrograma, Calendar dataPrograma, Boolean notificarPrograma, BigDecimal idPrograma, String diaPrograma) {
        this.horaInicioPrograma = horaInicioPrograma;
        this.nomePrograma = nomePrograma;
        this.fotoLocutorPrograma = fotoLocutorPrograma;
        this.nomeLocutorPrograma = nomeLocutorPrograma;
        this.dataPrograma = dataPrograma;
        this.notificarPrograma = notificarPrograma;
        this.idPrograma = idPrograma;
        this.diaPrograma = diaPrograma;
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

    public Calendar getDataPrograma() {
        return dataPrograma;
    }

    public void setDataPrograma(Calendar dataPrograma) {
        this.dataPrograma = dataPrograma;
    }

    public Boolean getNotificarPrograma() {
        return notificarPrograma;
    }

    public void setNotificarPrograma(Boolean notificarPrograma) {
        this.notificarPrograma = notificarPrograma;
    }

    public BigDecimal getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(BigDecimal idPrograma) {
        this.idPrograma = idPrograma;
    }

    public String getDiaPrograma() {
        return diaPrograma;
    }

    public void setDiaPrograma(String diaPrograma) {
        this.diaPrograma = diaPrograma;
    }
}
