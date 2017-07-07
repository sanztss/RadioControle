package accessweb.com.br.radiocontrole.model;


import java.util.Date;

/**
 * Created by Des. Android on 14/06/2017.
 */

public class Mural{

    private String fotoUsuario;
    private String nomeUsuario;
    private Date tempoPublicacao;
    private String textoPublicacao;
    private String imagemPublicacao;
    private String audioPublicacao;
    private String modeloPublicacao;

    public Mural() {

    }

    public Mural(String fotoUsuario, String nomeUsuario, Date tempoPublicacao, String textoPublicacao, String imagemPublicacao, String audioPublicacao, String modeloPublicacao) {
        this.fotoUsuario = fotoUsuario;
        this.nomeUsuario = nomeUsuario;
        this.tempoPublicacao = tempoPublicacao;
        this.textoPublicacao = textoPublicacao;
        this.imagemPublicacao = imagemPublicacao;
        this.audioPublicacao = audioPublicacao;
        this.modeloPublicacao = modeloPublicacao;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public Date getTempoPublicacao() {
        return tempoPublicacao;
    }

    public void setTempoPublicacao(Date tempoPublicacao) {
        this.tempoPublicacao = tempoPublicacao;
    }

    public String getTextoPublicacao() {
        return textoPublicacao;
    }

    public void setTextoPublicacao(String textoPublicacao) {
        this.textoPublicacao = textoPublicacao;
    }

    public String getImagemPublicacao() {
        return imagemPublicacao;
    }

    public void setImagemPublicacao(String imagemPublicacao) {
        this.imagemPublicacao = imagemPublicacao;
    }

    public String getAudioPublicacao() {
        return audioPublicacao;
    }

    public void setAudioPublicacao(String audioPublicacao) {
        this.audioPublicacao = audioPublicacao;
    }

    public String getModeloPublicacao() {
        return modeloPublicacao;
    }

    public void setModeloPublicacao(String modeloPublicacao) {
        this.modeloPublicacao = modeloPublicacao;
    }
}
