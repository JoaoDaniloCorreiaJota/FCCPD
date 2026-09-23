package br.com.pedro.mensagens;

import br.com.pedro.mensagens.aplicacao.ServicoGrupos;
import br.com.pedro.mensagens.aplicacao.ServicoMensagens;
import br.com.pedro.mensagens.dominio.Grupo;
import br.com.pedro.mensagens.dominio.Mensagem;
import br.com.pedro.mensagens.dominio.ParticipanteGrupo;
import br.com.pedro.mensagens.dominio.Conversa;
import br.com.pedro.mensagens.dominio.Usuario;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        ServicoMensagens servicoMensagens =
                new ServicoMensagens();

        ServicoGrupos servicoGrupos =
                new ServicoGrupos();

        ExecutorService executor =
                Executors.newFixedThreadPool(10);

        Usuario pedro =
                new Usuario("1", "Pedro");

        Usuario joao =
                new Usuario("2", "João");

        Usuario maria =
                new Usuario("3", "Maria");

        Conversa conversa =
                new Conversa(
                        "1",
                        pedro,
                        joao
                );

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

        Scanner scanner =
                new Scanner(System.in);

        boolean executando = true;

        while (executando) {

            System.out.println();
            System.out.println("===== SISTEMA DE MENSAGENS =====");
            System.out.println("1 - Enviar mensagem direta");
            System.out.println("2 - Listar mensagens diretas");
            System.out.println("3 - Enviar mensagem no grupo");
            System.out.println("4 - Listar mensagens do grupo");
            System.out.println("5 - Adicionar participante");
            System.out.println("6 - Remover participante");
            System.out.println("7 - Alterar nome do grupo");
            System.out.println("8 - Listar participantes");
            System.out.println("9 - Marcar última mensagem do grupo como lida");
            System.out.println("10 - Verificar se última mensagem foi lida por todos");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            String opcao =
                    scanner.nextLine();

            try {

                switch (opcao) {

                    case "1":

                        System.out.print("Remetente (1=Pedro, 2=João): ");
                        String idRemetente =
                                scanner.nextLine();

                        Usuario remetente =
                                encontrarUsuario(
                                        idRemetente,
                                        pedro,
                                        joao,
                                        maria
                                );

                        System.out.print("Mensagem: ");
                        String conteudo =
                                scanner.nextLine();

                        executor.submit(() -> {

                            servicoMensagens.enviarMensagem(
                                    String.valueOf(
                                            System.nanoTime()
                                    ),
                                    remetente,
                                    remetente.getId().equals("1")
                                            ? joao
                                            : pedro,
                                    conteudo,
                                    conversa
                            );

                            System.out.println(
                                    "[Thread] "
                                            + remetente.getNome()
                                            + " enviou uma mensagem direta."
                            );
                        });

                        System.out.println(
                                "Mensagem enviada para processamento."
                        );

                        break;

                    case "2":

                        List<Mensagem> mensagensConversa =
                                servicoMensagens.listarMensagens(
                                        conversa
                                );

                        System.out.println();
                        System.out.println(
                                "===== CONVERSA ====="
                        );

                        for (Mensagem mensagem :
                                mensagensConversa) {

                            System.out.println(
                                    mensagem.getRemetente().getNome()
                                            + " -> "
                                            + mensagem.getDestinatario().getNome()
                                            + ": "
                                            + mensagem.getConteudo()
                            );
                        }

                        break;

                    case "3":

                        System.out.print(
                                "Remetente (1=Pedro, 2=João, 3=Maria): "
                        );

                        String idGrupoRemetente =
                                scanner.nextLine();

                        Usuario remetenteGrupo =
                                encontrarUsuario(
                                        idGrupoRemetente,
                                        pedro,
                                        joao,
                                        maria
                                );

                        System.out.print("Mensagem: ");

                        String mensagemGrupo =
                                scanner.nextLine();

                        executor.submit(() -> {

                            servicoMensagens.enviarMensagemGrupo(
                                    String.valueOf(
                                            System.nanoTime()
                                    ),
                                    remetenteGrupo,
                                    mensagemGrupo,
                                    grupo
                            );

                            System.out.println(
                                    "[Thread] "
                                            + remetenteGrupo.getNome()
                                            + " enviou mensagem no grupo."
                            );
                        });

                        System.out.println(
                                "Mensagem enviada para processamento."
                        );

                        break;

                    case "4":

                        List<Mensagem> mensagensGrupo =
                                servicoMensagens.listarMensagens(
                                        grupo
                                );

                        System.out.println();
                        System.out.println(
                                "===== GRUPO "
                                        + grupo.getNome()
                                        + " ====="
                        );

                        for (Mensagem mensagem :
                                mensagensGrupo) {

                            System.out.println(
                                    mensagem.getRemetente().getNome()
                                            + ": "
                                            + mensagem.getConteudo()
                            );
                        }

                        break;

                    case "5":

                        System.out.print("ID do novo participante: ");
                        String idNovo =
                                scanner.nextLine();

                        System.out.print("Nome do novo participante: ");
                        String nomeNovo =
                                scanner.nextLine();

                        Usuario novoUsuario =
                                new Usuario(
                                        idNovo,
                                        nomeNovo
                                );

                        executor.submit(() -> {

                            servicoGrupos.adicionarParticipante(
                                    grupo,
                                    pedro,
                                    novoUsuario,
                                    false
                            );

                            System.out.println(
                                    "[Thread] Participante adicionado."
                            );
                        });

                        System.out.println(
                                "Operação enviada para processamento."
                        );

                        break;

                    case "6":

                        System.out.print(
                                "ID do participante a remover: "
                        );

                        String idRemover =
                                scanner.nextLine();

                        Usuario usuarioRemover =
                                encontrarUsuarioNoGrupo(
                                        idRemover,
                                        grupo
                                );

                        if (usuarioRemover == null) {

                            System.out.println(
                                    "Participante não encontrado."
                            );

                            break;
                        }

                        executor.submit(() -> {

                            servicoGrupos.removerParticipante(
                                    grupo,
                                    pedro,
                                    usuarioRemover
                            );

                            System.out.println(
                                    "[Thread] Participante removido."
                            );
                        });

                        System.out.println(
                                "Operação enviada para processamento."
                        );

                        break;

                    case "7":

                        System.out.print(
                                "Novo nome do grupo: "
                        );

                        String novoNome =
                                scanner.nextLine();

                        executor.submit(() -> {

                            servicoGrupos.alterarNome(
                                    grupo,
                                    pedro,
                                    novoNome
                            );

                            System.out.println(
                                    "[Thread] Nome do grupo alterado."
                            );
                        });

                        System.out.println(
                                "Operação enviada para processamento."
                        );

                        break;

                    case "8":

                        System.out.println();
                        System.out.println(
                                "===== PARTICIPANTES ====="
                        );

                        for (ParticipanteGrupo participante :
                                servicoGrupos.listarParticipantes(grupo)) {

                            System.out.println(
                                    participante.getUsuario().getNome()
                                            + " - "
                                            + (
                                            participante.isAdministrador()
                                                    ? "Administrador"
                                                    : "Participante"
                                    )
                            );
                        }

                        break;

                    case "9":

                        List<Mensagem> mensagensParaLer =
                                grupo.getMensagens();

                        if (mensagensParaLer.isEmpty()) {

                            System.out.println(
                                    "Não há mensagens no grupo."
                            );

                            break;
                        }

                        Mensagem ultimaMensagem =
                                mensagensParaLer.get(
                                        mensagensParaLer.size() - 1
                                );

                        executor.submit(() -> {

                            servicoMensagens.marcarComoLida(
                                    joao,
                                    ultimaMensagem,
                                    grupo
                            );

                            System.out.println(
                                    "[Thread] João marcou a mensagem como lida."
                            );
                        });

                        System.out.println(
                                "Leitura enviada para processamento."
                        );

                        break;

                    case "10":

                        List<Mensagem> mensagensParaVerificar =
                                grupo.getMensagens();

                        if (mensagensParaVerificar.isEmpty()) {

                            System.out.println(
                                    "Não há mensagens no grupo."
                            );

                            break;
                        }

                        Mensagem ultima =
                                mensagensParaVerificar.get(
                                        mensagensParaVerificar.size() - 1
                                );

                        System.out.println(
                                servicoMensagens.foiLidaPorTodos(
                                        ultima,
                                        grupo
                                )
                                        ? "Mensagem lida por todos."
                                        : "Mensagem ainda não foi lida por todos."
                        );

                        break;

                    case "0":

                        executando = false;

                        break;

                    default:

                        System.out.println(
                                "Opção inválida."
                        );
                }

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Erro: "
                                + e.getMessage()
                );
            }
        }

        executor.shutdown();

        try {

            executor.awaitTermination(
                    1,
                    TimeUnit.MINUTES
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        scanner.close();

        System.out.println(
                "Sistema encerrado."
        );
    }

    private static Usuario encontrarUsuario(
            String id,
            Usuario... usuarios
    ) {

        for (Usuario usuario : usuarios) {

            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }

        throw new IllegalArgumentException(
                "Usuário não encontrado."
        );
    }

    private static Usuario encontrarUsuarioNoGrupo(
            String id,
            Grupo grupo
    ) {

        for (ParticipanteGrupo participante :
                grupo.getParticipantes()) {

            if (participante.getUsuario().getId().equals(id)) {
                return participante.getUsuario();
            }
        }

        return null;
    }
}