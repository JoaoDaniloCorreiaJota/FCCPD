package br.com.pedro.mensagens.dominio;

import java.util.ArrayList;
import java.util.List;

public class Grupo {

    private final String id;
    private String nome;
    private final List<ParticipanteGrupo> participantes;

    public Grupo(String id, String nome, Usuario criador) {
        this.id = id;
        this.nome = nome;
        this.participantes = new ArrayList<>();

        participantes.add(
                new ParticipanteGrupo(criador, true)
        );
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public synchronized void alterarNome(Usuario solicitante, String novoNome) {

        if (!ehAdministrador(solicitante)) {
            throw new IllegalArgumentException(
                    "Apenas administradores podem alterar o nome do grupo."
            );
        }

        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome do grupo não pode ser vazio."
            );
        }

        this.nome = novoNome;
    }

    public List<ParticipanteGrupo> getParticipantes() {
        return List.copyOf(participantes);
    }

    public synchronized void adicionarParticipante(Usuario solicitante, Usuario usuario, boolean administrador) {
        if (!ehAdministrador(solicitante)) {
            throw new IllegalArgumentException(
                    "Apenas administradores podem adicionar participantes."
            );
        }

        if (participantes.size() >= 256) {
            throw new IllegalArgumentException(
                    "O grupo não pode ter mais de 256 participantes."
            );
        }

        participantes.add(
                new ParticipanteGrupo(usuario, administrador)
        );
    }

    public boolean ehAdministrador(Usuario usuario) {

        for (ParticipanteGrupo participante : participantes) {
            if (participante.getUsuario().getId().equals(usuario.getId())) {
                return participante.isAdministrador();
            }
        }

        return false;
    }

    public synchronized void removerParticipante(Usuario solicitante, Usuario usuario) {
        if (!ehAdministrador(solicitante)) {
            throw new IllegalArgumentException(
                    "Apenas administradores podem remover participantes."
            );
        }

        participantes.removeIf(
                participante -> participante.getUsuario().getId().equals(usuario.getId())
        );
    }
}