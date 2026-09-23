package br.com.pedro.mensagens.aplicacao;

import br.com.pedro.mensagens.dominio.Conversa;
import br.com.pedro.mensagens.dominio.Grupo;
import br.com.pedro.mensagens.dominio.Mensagem;
import br.com.pedro.mensagens.dominio.Usuario;

import java.util.List;

public class ServicoMensagens {

    public Mensagem enviarMensagem(
            String id,
            Usuario remetente,
            Usuario destinatario,
            String conteudo,
            Conversa conversa
    ) {

        Mensagem mensagem = new Mensagem(
                id,
                remetente,
                destinatario,
                conteudo
        );

        conversa.adicionarMensagem(mensagem);

        return mensagem;
    }

    public Mensagem enviarMensagemGrupo(
            String id,
            Usuario remetente,
            String conteudo,
            Grupo grupo
    ) {

        if (!grupo.ehParticipante(remetente)) {
            throw new IllegalArgumentException(
                    "Apenas participantes podem enviar mensagens no grupo."
            );
        }

        Mensagem mensagem = new Mensagem(
                id,
                remetente,
                null,
                conteudo
        );

        grupo.adicionarMensagem(mensagem);

        return mensagem;
    }

    public void marcarComoLida(
            Usuario usuario,
            Mensagem mensagem,
            Grupo grupo
    ) {

        grupo.marcarMensagemComoLida(
                usuario,
                mensagem
        );
    }

    public boolean foiLidaPorTodos(
            Mensagem mensagem,
            Grupo grupo
    ) {

        return grupo.mensagemFoiLidaPorTodos(mensagem);
    }

    public List<Mensagem> listarMensagens(Conversa conversa) {
        return conversa.getMensagens();
    }

    public List<Mensagem> listarMensagens(Grupo grupo) {
        return grupo.getMensagens();
    }
}