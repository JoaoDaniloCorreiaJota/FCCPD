package br.com.pedro.mensagens.dominio;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Mensagem {

    private final String id;
    private final Usuario remetente;
    private final Usuario destinatario;
    private final String conteudo;
    private final LocalDateTime dataHoraEnvio;
    private final Set<String> usuariosQueLeram;

    public Mensagem(String id, Usuario remetente, Usuario destinatario, String conteudo) {
        this.id = id;
        this.remetente = remetente;
        this.destinatario = destinatario;

        if (conteudo == null || conteudo.isBlank()) {
            throw new IllegalArgumentException("A mensagem não pode ser vazia.");
        }

        if (conteudo.length() > 2000) {
            throw new IllegalArgumentException("A mensagem não pode ter mais de 2000 caracteres.");
        }

        this.conteudo = conteudo;
        this.dataHoraEnvio = LocalDateTime.now();
        this.usuariosQueLeram = ConcurrentHashMap.newKeySet();
        this.usuariosQueLeram.add(remetente.getId());
    }

    public String getId() {
        return id;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public LocalDateTime getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void marcarComoLida(Usuario usuario) {
        usuariosQueLeram.add(usuario.getId());
    }

    public boolean foiLidaPor(Usuario usuario) {
        return usuariosQueLeram.contains(usuario.getId());
    }

    public Set<String> getUsuariosQueLeram() {
        return Set.copyOf(usuariosQueLeram);
    }
}