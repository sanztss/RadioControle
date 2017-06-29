package accessweb.com.br.radiocontrole.model;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class Premio {
    private String tituloPremio;
    private String imagemPremio;

    public Premio() {
    }

    public Premio(String tituloPremio, String imagemPremio) {
        this.tituloPremio = tituloPremio;
        this.imagemPremio = imagemPremio;
    }

    public String getTituloPremio() {
        return tituloPremio;
    }

    public void setTituloPremio(String tituloPremio) {
        this.tituloPremio = tituloPremio;
    }

    public String getImagemPremio() {
        return imagemPremio;
    }

    public void setImagemPremio(String imagemPremio) {
        this.imagemPremio = imagemPremio;
    }
}
