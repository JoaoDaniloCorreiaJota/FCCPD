package br.com.pedro.mensagens.aplicacao;

import br.com.pedro.mensagens.dominio.Grupo;
import br.com.pedro.mensagens.dominio.ParticipanteGrupo;
import br.com.pedro.mensagens.dominio.Usuario;

import java.util.List;

public class ServicoGrupos {

    public Grupo criarGrupo(
            String id,
            String nome,
            Usuario criador
    ) {

        return new Grupo(
                id,
                nome,
                criador
        );
    }

    public void adicionarParticipante(
            Grupo grupo,
            Usuario administrador,
            Usuario usuario,
            boolean novoAdministrador
    ) {

        grupo.adicionarParticipante(
                administrador,
                usuario,
                novoAdministrador
        );
    }

    public void removerParticipante(
            Grupo grupo,
            Usuario administrador,
            Usuario usuario
    ) {

        grupo.removerParticipante(
                administrador,
                usuario
        );
    }

    public void alterarNome(
            Grupo grupo,
            Usuario administrador,
            String novoNome
    ) {

        grupo.alterarNome(
                administrador,
                novoNome
        );
    }

    public List<ParticipanteGrupo> listarParticipantes(
            Grupo grupo
    ) {

        return grupo.getParticipantes();
    }

    public boolean ehAdministrador(
            Grupo grupo,
            Usuario usuario
    ) {

        return grupo.ehAdministrador(usuario);
    }

    public boolean ehParticipante(
            Grupo grupo,
            Usuario usuario
    ) {

        return grupo.ehParticipante(usuario);
    }
}