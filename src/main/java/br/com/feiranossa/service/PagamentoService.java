package br.com.feiranossa.service;

import br.com.feiranossa.domain.model.CartaoCredito;
import br.com.feiranossa.domain.model.Pagamento;
import br.com.feiranossa.repository.PagamentoRepository;

import java.util.UUID;

/**
 * Processa o pagamento e simula a comunicação com a operadora de cartão.
 */
public class PagamentoService {

    private final PagamentoRepository pagamentoRepo;

    public PagamentoService(PagamentoRepository pagamentoRepo) {
        this.pagamentoRepo = pagamentoRepo;
    }

    /**
     * Processa o pagamento. Simula a validação com a operadora:
     * - Cartões terminados em 0000 são recusados (teste de falha).
     * - Todos os demais são aprovados.
     */
    public Pagamento processarPagamento(int assinaturaId, double valor, CartaoCredito cartao) {
        if (!cartao.validarDados()) {
            throw new IllegalArgumentException("Dados do cartão inválidos.");
        }

        Pagamento pagamento = new Pagamento(0, assinaturaId, valor, cartao);
        pagamento = pagamentoRepo.salvar(pagamento);

        boolean aprovado = validarComOperadora(cartao, valor);

        if (aprovado) {
            String codigoTransacao = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            pagamento.confirmar(codigoTransacao);
            System.out.println("[OPERADORA] Transação aprovada. Código: " + codigoTransacao);
        } else {
            pagamento.recusar();
            System.out.println("[OPERADORA] Transação recusada.");
        }

        return pagamentoRepo.salvar(pagamento);
    }

    /** Simulação da operadora: recusa cartões terminados em 0000. */
    private boolean validarComOperadora(CartaoCredito cartao, double valor) {
        String digits = cartao.getNumero().replaceAll("[^0-9]", "");
        return !digits.endsWith("0000");
    }
}
