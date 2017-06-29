package accessweb.com.br.radiocontrole.model;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class Promocao {
    private String imagemPromocao;
    private String tituloPromocao;
    private String dataEncerramentoPromocao;
    private String dataSorteioPromocao;
    private String linkRegulamentoPromocao;
    private String linkAudioPromocao;
    private String linkVideoPromocao;
    private Premio premio;

    public Promocao() {
    }

    public Promocao(String imagemPromocao, String tituloPromocao, String dataEncerramentoPromocao, String dataSorteioPromocao, String linkRegulamentoPromocao, String linkAudioPromocao, String linkVideoPromocao, Premio premio) {
        this.imagemPromocao = imagemPromocao;
        this.tituloPromocao = tituloPromocao;
        this.dataEncerramentoPromocao = dataEncerramentoPromocao;
        this.dataSorteioPromocao = dataSorteioPromocao;
        this.linkRegulamentoPromocao = linkRegulamentoPromocao;
        this.linkAudioPromocao = linkAudioPromocao;
        this.linkVideoPromocao = linkVideoPromocao;
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

    public Premio getPremio() {
        return premio;
    }

    public void setPremio(Premio premio) {
        this.premio = premio;
    }
}
