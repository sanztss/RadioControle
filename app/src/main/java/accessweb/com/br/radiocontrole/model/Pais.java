package accessweb.com.br.radiocontrole.model;

import android.content.Context;

/**
 * Created by Des. Android on 11/07/2017.
 */

public class Pais {
    private String nomePais;
    private String codigoPais;
    private String bandeiraPais;
    private Boolean principal = false;

    public Pais() {
    }

    public Pais(String nomePais, String codigoPais, String bandeiraPais, Boolean principal) {
        this.nomePais = nomePais;
        this.codigoPais = codigoPais;
        this.bandeiraPais = bandeiraPais;
        this.principal = principal;
    }

    public String getNomePais() {
        return nomePais;
    }

    public void setNomePais(String nomePais) {
        this.nomePais = nomePais;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getBandeiraPais() {
        return bandeiraPais;
    }

    public void setBandeiraPais(String bandeiraPais) {
        this.bandeiraPais = bandeiraPais;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }
}
