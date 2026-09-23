package br.com.pedro.mensagens.dominio;

public class ParticipanteGrupo {

    private final Usuario usuario;
    private boolean administrador;

    public ParticipanteGrupo(Usuario usuario, boolean administrador) {
        this.usuario = usuario;
        this.administrador = administrador;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }
}