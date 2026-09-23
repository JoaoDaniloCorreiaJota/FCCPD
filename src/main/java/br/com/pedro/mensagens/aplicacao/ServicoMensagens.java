package br.com.pedro.mensagens.aplicacao;

import br.com.pedro.mensagens.dominio.Conversa;
import br.com.pedro.mensagens.dominio.Mensagem;

public class ServicoMensagens {

    public void enviarMensagem(Conversa conversa, Mensagem mensagem) {
        conversa.adicionarMensagem(mensagem);
    }
}