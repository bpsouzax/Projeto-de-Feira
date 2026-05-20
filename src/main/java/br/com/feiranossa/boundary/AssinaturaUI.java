package br.com.feiranossa.boundary;

import br.com.feiranossa.controller.AssinaturaController;
import br.com.feiranossa.domain.enums.TipoProduto;
import br.com.feiranossa.domain.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Objeto Fronteira (Boundary) — interface de usuário em modo console.
 * Corresponde à camada de apresentação do diagrama de sequência.
 * Toda interação com o usuário passa por esta classe.
 */
public class AssinaturaUI {

    private final AssinaturaController controller;
    private final Scanner scanner = new Scanner(System.in);

    public AssinaturaUI(AssinaturaController controller) {
        this.controller = controller;
    }

    public void iniciar() {
        exibirCabecalho();

        // ── Tela 1: Identificação ─────────────────────────────────────────
        String[] dadosPessoais = telaIdentificacao();
        String celular = dadosPessoais[0];
        String nome    = dadosPessoais[1];
        String email   = dadosPessoais[2];

        try {
            controller.iniciarIdentificacao(celular, nome, email);
        } catch (IllegalArgumentException e) {
            erro(e.getMessage());
            return;
        }

        // ── Tela 2: Validação SMS ─────────────────────────────────────────
        Assinante assinante = telaValidacaoSMS(celular, nome, email);
        if (assinante == null) return;

        // ── Tela 3: Seleção de Plano ──────────────────────────────────────
        List<PlanoAssinatura> planos = controller.listarPlanos();
        int planoId = telaSelecaoPlano(planos);

        Assinatura assinatura;
        try {
            assinatura = controller.selecionarPlano(planoId);
        } catch (Exception e) {
            erro("Erro ao selecionar plano: " + e.getMessage());
            return;
        }

        PlanoAssinatura planoSelecionado = planos.stream()
                .filter(p -> p.getId() == planoId).findFirst().orElseThrow();

        // ── Tela 4: Frutas ────────────────────────────────────────────────
        List<ItemCesta> frutas = telaSelecaoProdutos(TipoProduto.FRUTA,
                planoSelecionado.getQtdFrutas());
        controller.confirmarItensCesta(frutas);

        // ── Tela 5: Legumes ───────────────────────────────────────────────
        List<ItemCesta> legumes = telaSelecaoProdutos(TipoProduto.LEGUME,
                planoSelecionado.getQtdLegumes());
        controller.confirmarItensCesta(legumes);

        // ── Tela 6: Verduras ──────────────────────────────────────────────
        List<ItemCesta> verduras = telaSelecaoProdutos(TipoProduto.VERDURA,
                planoSelecionado.getQtdVerduras());
        controller.confirmarItensCesta(verduras);

        // ── Tela 7: Revisão + Endereço ────────────────────────────────────
        exibirResumoCesta(controller.getCestaAtual());
        EnderecoEntrega endereco = telaEndereco();
        try {
            controller.informarEnderecoEntrega(endereco);
        } catch (IllegalArgumentException e) {
            erro(e.getMessage());
            return;
        }

        // ── Tela 8: Pagamento ─────────────────────────────────────────────
        exibirTotalPagamento(assinatura.getValorTotal());
        CartaoCredito cartao = telaDadosCartao();

        // ── Tela 9: Processamento ─────────────────────────────────────────
        System.out.println("\n  ⏳ Processando pagamento...");
        Protocolo protocolo;
        try {
            protocolo = controller.processarPagamento(cartao);
        } catch (RuntimeException e) {
            erro("Pagamento não aprovado: " + e.getMessage());
            return;
        }

        // ── Tela 10: Confirmação ──────────────────────────────────────────
        telaConfirmacao(protocolo, endereco, assinatura);
    }

    // ─────────────────────────────────────────────────────────────────────
    // TELAS
    // ─────────────────────────────────────────────────────────────────────

    private void exibirCabecalho() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         🌿  FEIRANOSSA  🌿               ║");
        System.out.println("║   Assinatura de Cesta de Feira Semanal   ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();
    }

    private String[] telaIdentificacao() {
        titulo("TELA 1 — Identificação");
        System.out.print("  Celular (com DDD): ");
        String celular = scanner.nextLine().trim();
        System.out.print("  Nome completo    : ");
        String nome = scanner.nextLine().trim();
        System.out.print("  E-mail           : ");
        String email = scanner.nextLine().trim();
        return new String[]{celular, nome, email};
    }

    private Assinante telaValidacaoSMS(String celular, String nome, String email) {
        titulo("TELA 2 — Validação SMS");
        System.out.println("  Um código foi enviado para: " + celular);
        for (int tentativas = 3; tentativas > 0; tentativas--) {
            System.out.print("  Digite o código recebido: ");
            String codigo = scanner.nextLine().trim();
            try {
                return controller.validarCodigoSMS(celular, codigo, nome, email);
            } catch (IllegalArgumentException e) {
                erro("Código inválido. Tentativas restantes: " + (tentativas - 1));
            }
        }
        erro("Número máximo de tentativas atingido.");
        return null;
    }

    private int telaSelecaoPlano(List<PlanoAssinatura> planos) {
        titulo("TELA 3 — Planos de Assinatura");
        planos.forEach(p -> System.out.println("  " + p));
        System.out.print("  Informe o número do plano desejado: ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private List<ItemCesta> telaSelecaoProdutos(TipoProduto tipo, int quota) {
        titulo("TELA " + tipoToTela(tipo) + " — Escolha de " + tipo.name().charAt(0)
                + tipo.name().substring(1).toLowerCase() + "s");

        List<Produto> produtos = controller.listarProdutosPorTipo(tipo);
        if (produtos.isEmpty()) {
            System.out.println("  (Nenhum produto disponível para " + tipo + ")");
            return List.of();
        }

        System.out.println("  Disponíveis (quota: até " + quota + " itens):");
        produtos.forEach(p -> System.out.println("  " + p));

        List<ItemCesta> selecionados = new ArrayList<>();
        int totalSelecionado = 0;

        System.out.println("  Informe o ID e quantidade (ex: 1 2). Digite 0 para encerrar.");
        while (totalSelecionado < quota) {
            System.out.printf("  [%d/%d] ID Produto (0 = encerrar): ", totalSelecionado, quota);
            String linha = scanner.nextLine().trim();
            if (linha.equals("0")) break;

            String[] partes = linha.split("\\s+");
            int produtoId   = Integer.parseInt(partes[0]);
            int quantidade  = partes.length > 1 ? Integer.parseInt(partes[1]) : 1;

            int restante = quota - totalSelecionado;
            if (quantidade > restante) {
                System.out.println("  ⚠ Quota excedida. Máximo restante: " + restante);
                quantidade = restante;
            }

            Produto prod = produtos.stream()
                    .filter(p -> p.getId() == produtoId)
                    .findFirst().orElse(null);

            if (prod == null) { System.out.println("  ⚠ Produto não encontrado."); continue; }

            final int qtd = quantidade;
            selecionados.stream()
                    .filter(i -> i.getProdutoId() == produtoId)
                    .findFirst()
                    .ifPresentOrElse(
                            i -> i.setQuantidade(i.getQuantidade() + qtd),
                            () -> selecionados.add(new ItemCesta(
                                    prod.getId(), prod.getNome(),
                                    qtd, prod.getUnidade(), prod.getPreco()))
                    );

            totalSelecionado += quantidade;
            System.out.println("  ✔ Adicionado: " + prod.getNome() + " x" + quantidade);
        }

        System.out.println("  Itens confirmados ✔");
        return selecionados;
    }

    private void exibirResumoCesta(CestaDaSemana cesta) {
        titulo("TELA 7 — Revisão da Cesta");
        System.out.println(cesta);
    }

    private EnderecoEntrega telaEndereco() {
        titulo("Endereço de Entrega");
        System.out.print("  CEP          : "); String cep  = scanner.nextLine().trim();
        System.out.print("  Rua/Avenida  : "); String rua  = scanner.nextLine().trim();
        System.out.print("  Número       : "); String num  = scanner.nextLine().trim();
        System.out.print("  Complemento  : "); String comp = scanner.nextLine().trim();
        System.out.print("  Bairro       : "); String bair = scanner.nextLine().trim();
        System.out.println("  Dias disponíveis: Segunda / Quarta / Sabado / Domingo");
        System.out.print("  Dia de entrega   : "); String dia  = scanner.nextLine().trim();
        System.out.println("  Turnos: Manha / Tarde");
        System.out.print("  Turno de entrega : "); String turno = scanner.nextLine().trim();
        return new EnderecoEntrega(cep, rua, num, comp, bair, dia, turno);
    }

    private void exibirTotalPagamento(double valor) {
        titulo("TELA 8 — Pagamento");
        System.out.printf("  Total da assinatura: R$ %.2f/semana%n%n", valor);
    }

    private CartaoCredito telaDadosCartao() {
        System.out.print("  Número do cartão : "); String num  = scanner.nextLine().trim();
        System.out.print("  Titular          : "); String tit  = scanner.nextLine().trim();
        System.out.print("  Validade (MM/AA) : "); String val  = scanner.nextLine().trim();
        System.out.print("  Bandeira         : "); String band = scanner.nextLine().trim();
        return new CartaoCredito(num, tit, val, band);
    }

    private void telaConfirmacao(Protocolo protocolo, EnderecoEntrega endereco, Assinatura assinatura) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║        ✅  ASSINATURA CONFIRMADA!         ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("  " + protocolo);
        System.out.printf("  Plano    : #%d | R$ %.2f/semana%n",
                assinatura.getPlanoId(), assinatura.getValorTotal());
        System.out.println("  Entrega  : " + endereco);
        System.out.println("  Status   : " + assinatura.getStatus());
        System.out.println();
        System.out.println("  Um SMS e e-mail de confirmação foram enviados.");
        System.out.println("  Obrigado por assinar a FeiraNossa! 🌿");
    }

    // ─────────────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────────────

    private void titulo(String t) {
        System.out.println("\n── " + t + " " + "─".repeat(Math.max(0, 42 - t.length())));
    }

    private void erro(String msg) {
        System.out.println("\n  ❌ " + msg + "\n");
    }

    private int tipoToTela(TipoProduto tipo) {
        return switch (tipo) {
            case FRUTA   -> 4;
            case LEGUME  -> 5;
            case VERDURA -> 6;
        };
    }
}
