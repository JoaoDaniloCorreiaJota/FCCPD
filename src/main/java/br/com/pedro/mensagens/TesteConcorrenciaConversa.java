package br.com.pedro.mensagens;

import br.com.pedro.mensagens.aplicacao.ServicoMensagens;
import br.com.pedro.mensagens.dominio.Conversa;
import br.com.pedro.mensagens.dominio.Mensagem;
import br.com.pedro.mensagens.dominio.Usuario;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TesteConcorrenciaConversa {

    public static void main(String[] args) throws InterruptedException {

        Usuario pedro =
                new Usuario("1", "Pedro");

        Usuario joao =
                new Usuario("2", "João");

        Conversa conversa =
                new Conversa(
                        "1",
                        pedro,
                        joao
                );

        ServicoMensagens servico =
                new ServicoMensagens();

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        CountDownLatch inicio =
                new CountDownLatch(1);

        CountDownLatch primeirosOi =
                new CountDownLatch(2);

        CountDownLatch tudoBemEnviado =
                new CountDownLatch(1);

        System.out.println(
                "===== TESTE DE CONVERSA DIRETA ====="
        );

        executor.submit(() -> {

            try {

                inicio.await();

                servico.enviarMensagem(
                        "1",
                        pedro,
                        joao,
                        "Oi!",
                        conversa
                );

                System.out.println(
                        "Pedro: Oi!"
                );

                primeirosOi.countDown();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });

        executor.submit(() -> {

            try {

                inicio.await();

                servico.enviarMensagem(
                        "2",
                        joao,
                        pedro,
                        "Oi!",
                        conversa
                );

                System.out.println(
                        "João: Oi!"
                );

                primeirosOi.countDown();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });

        System.out.println(
                "Pedro e João estão prontos para enviar..."
        );

        inicio.countDown();

        primeirosOi.await();

        System.out.println();
        System.out.println(
                "Os dois enviaram a primeira mensagem."
        );

        executor.submit(() -> {

            servico.enviarMensagem(
                    "3",
                    pedro,
                    joao,
                    "Tudo bem?",
                    conversa
            );

            System.out.println(
                    "Pedro: Tudo bem?"
            );

            tudoBemEnviado.countDown();
        });

        executor.submit(() -> {

            try {

                tudoBemEnviado.await();

                servico.enviarMensagem(
                        "4",
                        joao,
                        pedro,
                        "Tudo ótimo!",
                        conversa
                );

                System.out.println(
                        "João: Tudo ótimo!"
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();

        executor.awaitTermination(
                1,
                TimeUnit.MINUTES
        );

        System.out.println();
        System.out.println(
                "===== HISTÓRICO DA CONVERSA ====="
        );

        for (Mensagem mensagem :
                conversa.getMensagens()) {

            System.out.println(
                    mensagem.getRemetente().getNome()
                            + ": "
                            + mensagem.getConteudo()
            );
        }

        System.out.println();
        System.out.println(
                "Mensagens esperadas: 4"
        );

        System.out.println(
                "Mensagens recebidas: "
                        + conversa.getMensagens().size()
        );

        if (conversa.getMensagens().size() == 4) {

            System.out.println();
            System.out.println(
                    "TESTE DE CONVERSA PASSOU!"
            );
        }
    }
}