package br.com.pedro.mensagens;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.com.pedro.mensagens.dominio.Conversa;
import br.com.pedro.mensagens.dominio.Grupo;
import br.com.pedro.mensagens.dominio.Mensagem;
import br.com.pedro.mensagens.dominio.Usuario;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Usuario pedro = new Usuario("1", "Pedro");
        Usuario joao = new Usuario("2", "João");
        Usuario maria = new Usuario("3", "Maria");

        Grupo grupo = new Grupo("1", "Receitas", pedro);

        grupo.adicionarParticipante(pedro, joao, false);
        grupo.adicionarParticipante(pedro, maria, false);

        System.out.println("Participantes: " + grupo.getParticipantes().size());

        grupo.alterarNome(pedro, "Receitas Favoritas");

        System.out.println("Nome: " + grupo.getNome());

        grupo.removerParticipante(pedro, maria);

        System.out.println("Participantes após remoção: " + grupo.getParticipantes().size());

        try {
            grupo.removerParticipante(joao, pedro);
            System.out.println("ERRO: João conseguiu remover participante.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: usuário não administrador não conseguiu remover.");
        }

        try {
            grupo.alterarNome(joao, "Novo Nome");
            System.out.println("ERRO: João conseguiu alterar o grupo.");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: usuário não administrador não conseguiu alterar o grupo.");
        }
    }
}