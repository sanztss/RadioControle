package accessweb.com.br.radiocontrole.model;

import java.util.Date;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PodcastApp {
    private String tituloPodcast;
    private String linkPodcast;
    private Date dataPublicacaoPodcast;

    public PodcastApp() {
    }

    public PodcastApp(String tituloPodcast, String linkPodcast, Date dataPublicacaoPodcast) {
        this.tituloPodcast = tituloPodcast;
        this.linkPodcast = linkPodcast;
        this.dataPublicacaoPodcast = dataPublicacaoPodcast;
    }

    public String getTituloPodcast() {
        return tituloPodcast;
    }

    public void setTituloPodcast(String tituloPodcast) {
        this.tituloPodcast = tituloPodcast;
    }

    public String getLinkPodcast() {
        return linkPodcast;
    }

    public void setLinkPodcast(String linkPodcast) {
        this.linkPodcast = linkPodcast;
    }

    public Date getDataPublicacaoPodcast() {
        return dataPublicacaoPodcast;
    }

    public void setDataPublicacaoPodcast(Date dataPublicacaoPodcast) {
        this.dataPublicacaoPodcast = dataPublicacaoPodcast;
    }
}
