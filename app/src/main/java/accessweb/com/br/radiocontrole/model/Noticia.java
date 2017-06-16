package accessweb.com.br.radiocontrole.model;


import java.io.Serializable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Des. Android on 14/06/2017.
 */

@Root(name = "item", strict = false)
public class Noticia implements Serializable {

    @Element(name = "pubDate", required = true )
    private String tempoNoticia;

    @Element(name = "title", required = true )
    private String tituloNoticia;

    @Element(name = "description", required = true )
    private String resumoNoticia;

    @Element(name = "link", required = true )
    private String linkNoticia;

    public Noticia() {

    }

    public Noticia(String tempoNoticia, String tituloNoticia, String resumoNoticia, String linkNoticia) {
        this.tempoNoticia = tempoNoticia;
        this.tituloNoticia = tituloNoticia;
        this.resumoNoticia = resumoNoticia;
        this.linkNoticia = linkNoticia;
    }

    public String getTempoNoticia() {
        return tempoNoticia;
    }

    public boolean isEqualTo(Noticia n) {
        if (n.getTituloNoticia().equals(tituloNoticia) &&
                n.getResumoNoticia().equals(resumoNoticia) &&
                n.getTempoNoticia().equals(tempoNoticia)) {
            return true;
        }
        else
            return false;
    }

    public void setTempoNoticia(String tempoNoticia) {
        this.tempoNoticia = tempoNoticia;
    }

    public String getTituloNoticia() {
        return tituloNoticia;
    }

    public void setTituloNoticia(String tituloNoticia) {
        this.tituloNoticia = tituloNoticia;
    }

    public String getResumoNoticia() {
        return resumoNoticia;
    }

    public void setResumoNoticia(String resumoNoticia) {
        this.resumoNoticia = resumoNoticia;
    }

    public String getLinkNoticia() {
        return linkNoticia;
    }

    public void setLinkNoticia(String linkNoticia) {
        this.linkNoticia = linkNoticia;
    }
}
