package br.com.pedro.mensagens.dominio;

import java.util.ArrayList;
import java.util.List;

public class Conversa {

    private final String id;
    private final Usuario usuario1;
    private final Usuario usuario2;
    private final List<Mensagem> mensagens;

    public Conversa(String id, Usuario usuario1, Usuario usuario2) {
        this.id = id;
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.mensagens = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public Usuario getUsuario2() {
        return usuario2;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public synchronized void adicionarMensagem(Mensagem mensagem) {
        mensagens.add(mensagem);
    }
}