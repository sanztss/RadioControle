package accessweb.com.br.radiocontrole.model;

/**
 * Created by Des. Android on 19/07/2017.
 */

public class RadioApp {

    private String urlImagemRadio;
    private String nomeRadio;
    private String localizacaoRadio;
    private String idRadio;

    public RadioApp() {
    }

    public RadioApp(String urlImagemRadio, String nomeRadio, String localizacaoRadio, String idRadio) {
        this.urlImagemRadio = urlImagemRadio;
        this.nomeRadio = nomeRadio;
        this.localizacaoRadio = localizacaoRadio;
        this.idRadio = idRadio;
    }

    public String getUrlImagemRadio() {
        return urlImagemRadio;
    }

    public void setUrlImagemRadio(String urlImagemRadio) {
        this.urlImagemRadio = urlImagemRadio;
    }

    public String getNomeRadio() {
        return nomeRadio;
    }

    public void setNomeRadio(String nomeRadio) {
        this.nomeRadio = nomeRadio;
    }

    public String getLocalizacaoRadio() {
        return localizacaoRadio;
    }

    public void setLocalizacaoRadio(String localizacaoRadio) {
        this.localizacaoRadio = localizacaoRadio;
    }

    public String getIdRadio() {
        return idRadio;
    }

    public void setIdRadio(String idRadio) {
        this.idRadio = idRadio;
    }
}
