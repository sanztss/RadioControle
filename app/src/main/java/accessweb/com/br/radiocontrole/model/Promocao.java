package accessweb.com.br.radiocontrole.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class Promocao implements Serializable {
    private String imagemPromocao;
    private String tituloPromocao;
    private String dataEncerramentoPromocao;
    private String dataSorteioPromocao;
    private String linkRegulamentoPromocao;
    private String linkAudioPromocao;
    private String linkVideoPromocao;
    private Boolean participando;
    private Boolean vigente;
    private static List<Premio> premio;

    public Promocao() {
    }

    public Promocao(String imagemPromocao, String tituloPromocao, String dataEncerramentoPromocao, String dataSorteioPromocao, String linkRegulamentoPromocao, String linkAudioPromocao, String linkVideoPromocao, Boolean participando, Boolean vigente, List<Premio> premio) {
        this.imagemPromocao = imagemPromocao;
        this.tituloPromocao = tituloPromocao;
        this.dataEncerramentoPromocao = dataEncerramentoPromocao;
        this.dataSorteioPromocao = dataSorteioPromocao;
        this.linkRegulamentoPromocao = linkRegulamentoPromocao;
        this.linkAudioPromocao = linkAudioPromocao;
        this.linkVideoPromocao = linkVideoPromocao;
        this.participando = participando;
        this.vigente = vigente;
        this.premio = premio;
    }

    public String getImagemPromocao() {
        return imagemPromocao;
    }

    public void setImagemPromocao(String imagemPromocao) {
        this.imagemPromocao = imagemPromocao;
    }

    public String getTituloPromocao() {
        return tituloPromocao;
    }

    public void setTituloPromocao(String tituloPromocao) {
        this.tituloPromocao = tituloPromocao;
    }

    public String getDataEncerramentoPromocao() {
        return dataEncerramentoPromocao;
    }

    public void setDataEncerramentoPromocao(String dataEncerramentoPromocao) {
        this.dataEncerramentoPromocao = dataEncerramentoPromocao;
    }

    public String getDataSorteioPromocao() {
        return dataSorteioPromocao;
    }

    public void setDataSorteioPromocao(String dataSorteioPromocao) {
        this.dataSorteioPromocao = dataSorteioPromocao;
    }

    public String getLinkRegulamentoPromocao() {
        return linkRegulamentoPromocao;
    }

    public void setLinkRegulamentoPromocao(String linkRegulamentoPromocao) {
        this.linkRegulamentoPromocao = linkRegulamentoPromocao;
    }

    public String getLinkAudioPromocao() {
        return linkAudioPromocao;
    }

    public void setLinkAudioPromocao(String linkAudioPromocao) {
        this.linkAudioPromocao = linkAudioPromocao;
    }

    public String getLinkVideoPromocao() {
        return linkVideoPromocao;
    }

    public void setLinkVideoPromocao(String linkVideoPromocao) {
        this.linkVideoPromocao = linkVideoPromocao;
    }

    public Boolean getParticipando() {
        return participando;
    }

    public void setParticipando(Boolean participando) {
        this.participando = participando;
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public List<Premio> getPremio() {
        return premio;
    }

    public void setPremio(List<Premio> premio) {
        this.premio = premio;
    }
}
