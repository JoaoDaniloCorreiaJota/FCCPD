package br.com.pedro.mensagens;

import br.com.pedro.mensagens.aplicacao.ServicoGrupos;
import br.com.pedro.mensagens.aplicacao.ServicoMensagens;
import br.com.pedro.mensagens.dominio.Grupo;
import br.com.pedro.mensagens.dominio.Mensagem;
import br.com.pedro.mensagens.dominio.Usuario;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TesteConcorrenciaGrupo {

    public static void main(String[] args) throws InterruptedException {

        Usuario pedro =
                new Usuario("1", "Pedro");

        Usuario joao =
                new Usuario("2", "João");

        Usuario maria =
                new Usuario("3", "Maria");

        ServicoGrupos servicoGrupos =
                new ServicoGrupos();

        ServicoMensagens servicoMensagens =
                new ServicoMensagens();

        Grupo grupo =
                servicoGrupos.criarGrupo(
                        "1",
                        "Receitas",
                        pedro
                );

        servicoGrupos.adicionarParticipante(
                grupo,
                pedro,
                joao,
                false
        );

        servicoGrupos.adicionarParticipante(
                grupo,
                pedro,
                maria,
                false
        );

        ExecutorService executor =
                Executors.newFixedThreadPool(6);

        System.out.println(
                "===== TESTE DE CONCORRÊNCIA EM GRUPO ====="
        );

        System.out.println();
        System.out.println(
                "Grupo criado com Pedro, João e Maria."
        );

        CountDownLatch inicio =
                new CountDownLatch(1);

        executor.submit(() -> {

            try {

                inicio.await();

                servicoGrupos.alterarNome(
                        grupo,
                        pedro,
                        "Receitas Favoritas"
                );

                System.out.println(
                        "Pedro alterou o nome do grupo."
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });

        executor.submit(() -> {

            try {

                inicio.await();

                servicoMensagens.enviarMensagemGrupo(
                        "1",
                        joao,
                        "Oi!",
                        grupo
                );

                System.out.println(
                        "João: Oi!"
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });

        executor.submit(() -> {

            try {

                inicio.await();

                servicoMensagens.enviarMensagemGrupo(
                        "2",
                        maria,
                        "Oi!",
                        grupo
                );

                System.out.println(
                        "Maria: Oi!"
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });

        System.out.println();
        System.out.println(
                "Pedro, João e Maria estão iniciando operações simultaneamente..."
        );

        inicio.countDown();

        Thread.sleep(500);

        System.out.println();
        System.out.println(
                "Nome atual do grupo: "
                        + grupo.getNome()
        );

        System.out.println();
        System.out.println(
                "===== INÍCIO DA CONVERSA ====="
        );

        CountDownLatch joaoLePedro =
                new CountDownLatch(1);

        CountDownLatch mariaLePedro =
                new CountDownLatch(1);

        executor.submit(() -> {

            servicoMensagens.enviarMensagemGrupo(
                    "3",
                    pedro,
                    "Alguém tem uma receita de pizza?",
                    grupo
            );

            System.out.println(
                    "Pedro: Alguém tem uma receita de pizza?"
            );
        });

        Mensagem mensagemPedro =
                esperarUltimaMensagem(
                        grupo,
                        "3"
                );

        executor.submit(() -> {

            servicoMensagens.marcarComoLida(
                    joao,
                    mensagemPedro,
                    grupo
            );

            System.out.println(
                    "João leu a mensagem do Pedro."
            );

            joaoLePedro.countDown();
        });

        executor.submit(() -> {

            servicoMensagens.marcarComoLida(
                    maria,
                    mensagemPedro,
                    grupo
            );

            System.out.println(
                    "Maria leu a mensagem do Pedro."
            );

            mariaLePedro.countDown();
        });

        joaoLePedro.await();
        mariaLePedro.await();

        CountDownLatch pedroLeJoao =
                new CountDownLatch(1);

        CountDownLatch mariaLeJoao =
                new CountDownLatch(1);

        executor.submit(() -> {

            servicoMensagens.enviarMensagemGrupo(
                    "4",
                    joao,
                    "Eu tenho uma receita!",
                    grupo
            );

            System.out.println(
                    "João: Eu tenho uma receita!"
            );
        });

        Mensagem mensagemJoao =
                esperarUltimaMensagem(
                        grupo,
                        "4"
                );

        executor.submit(() -> {

            servicoMensagens.marcarComoLida(
                    pedro,
                    mensagemJoao,
                    grupo
            );

            System.out.println(
                    "Pedro leu a mensagem do João."
            );

            pedroLeJoao.countDown();
        });

        executor.submit(() -> {

            servicoMensagens.marcarComoLida(
                    maria,
                    mensagemJoao,
                    grupo
            );

            System.out.println(
                    "Maria leu a mensagem do João."
            );

            mariaLeJoao.countDown();
        });

        pedroLeJoao.await();
        mariaLeJoao.await();

        CountDownLatch pedroLeMaria =
                new CountDownLatch(1);

        CountDownLatch joaoLeMaria =
                new CountDownLatch(1);

        executor.submit(() -> {

            servicoMensagens.enviarMensagemGrupo(
                    "5",
                    maria,
                    "Manda aqui no grupo!",
                    grupo
            );

            System.out.println(
                    "Maria: Manda aqui no grupo!"
            );
        });

        Mensagem mensagemMaria =
                esperarUltimaMensagem(
                        grupo,
                        "5"
                );

        executor.submit(() -> {

            servicoMensagens.marcarComoLida(
                    pedro,
                    mensagemMaria,
                    grupo
            );

            System.out.println(
                    "Pedro leu a mensagem da Maria."
            );

            pedroLeMaria.countDown();
        });

        executor.submit(() -> {

            servicoMensagens.marcarComoLida(
                    joao,
                    mensagemMaria,
                    grupo
            );

            System.out.println(
                    "João leu a mensagem da Maria."
            );

            joaoLeMaria.countDown();
        });

        pedroLeMaria.await();
        joaoLeMaria.await();

        CountDownLatch joaoLeResposta =
                new CountDownLatch(1);

        CountDownLatch mariaLeResposta =
                new CountDownLatch(1);

        executor.submit(() -> {

            servicoMensagens.enviarMensagemGrupo(
                    "6",
                    pedro,
                    "Valeu!",
                    grupo
            );

            System.out.println(
                    "Pedro: Valeu!"
            );
        });

        Mensagem mensagemResposta =
                esperarUltimaMensagem(
                        grupo,
                        "6"
                );

        executor.submit(() -> {

            servicoMensagens.marcarComoLida(
                    joao,
                    mensagemResposta,
                    grupo
            );

            System.out.println(
                    "João leu a mensagem do Pedro."
            );

            joaoLeResposta.countDown();
        });

        executor.submit(() -> {

            servicoMensagens.marcarComoLida(
                    maria,
                    mensagemResposta,
                    grupo
            );

            System.out.println(
                    "Maria leu a mensagem do Pedro."
            );

            mariaLeResposta.countDown();
        });

        joaoLeResposta.await();
        mariaLeResposta.await();

        executor.shutdown();

        executor.awaitTermination(
                1,
                TimeUnit.MINUTES
        );

        System.out.println();
        System.out.println(
                "===== HISTÓRICO DO GRUPO ====="
        );

        for (Mensagem mensagem :
                grupo.getMensagens()) {

            System.out.println(
                    mensagem.getRemetente().getNome()
                            + ": "
                            + mensagem.getConteudo()
            );
        }

        System.out.println();
        System.out.println(
                "Participantes esperados: 3"
        );

        System.out.println(
                "Participantes encontrados: "
                        + grupo.getParticipantes().size()
        );

        System.out.println();
        System.out.println(
                "Mensagens esperadas: 6"
        );

        System.out.println(
                "Mensagens recebidas: "
                        + grupo.getMensagens().size()
        );

        System.out.println();
        System.out.println(
                "Última mensagem lida por todos: "
                        + servicoMensagens.foiLidaPorTodos(
                        mensagemResposta,
                        grupo
                )
        );

        if (
                grupo.getParticipantes().size() == 3
                        && grupo.getMensagens().size() == 6
                        && servicoMensagens.foiLidaPorTodos(
                        mensagemResposta,
                        grupo
                )
        ) {

            System.out.println();
            System.out.println(
                    "TESTE DE GRUPO PASSOU!"
            );
        }
    }

    private static Mensagem esperarUltimaMensagem(
            Grupo grupo,
            String idMensagem
    ) throws InterruptedException {

        while (true) {

            for (Mensagem mensagem :
                    grupo.getMensagens()) {

                if (mensagem.getId().equals(idMensagem)) {
                    return mensagem;
                }
            }

            Thread.sleep(10);
        }
    }
}