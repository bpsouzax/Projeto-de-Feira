package br.com.feiranossa.controller;

import br.com.feiranossa.domain.enums.TipoProduto;
import br.com.feiranossa.domain.model.*;
import br.com.feiranossa.service.*;

import java.util.List;

/**
 * Camada de controle: orquestra o fluxo do caso de uso "Assinar Serviço de Feira".
 * Recebe chamadas da camada de fronteira (UI) e delega às services.
 */
public class AssinaturaController {

    private final AutenticacaoService autenticacaoService;
    private final AssinaturaService   assinaturaService;
    private final CestaService        cestaService;
    private final PagamentoService    pagamentoService;

    // Estado intermediário do fluxo (contexto da sessão)
    private Assinante     assinanteAtual;
    private Assinatura    assinaturaAtual;
    private CestaDaSemana cestaAtual;

    public AssinaturaController(AutenticacaoService autenticacaoService,
                                AssinaturaService assinaturaService,
                                CestaService cestaService,
                                PagamentoService pagamentoService) {
        this.autenticacaoService = autenticacaoService;
        this.assinaturaService   = assinaturaService;
        this.cestaService        = cestaService;
        this.pagamentoService    = pagamentoService;
    }

    // ── Passo 1–2: Identificação ──────────────────────────────────────────

    public void iniciarIdentificacao(String celular, String nome, String email) {
        Assinante a = new Assinante(0, nome, email, celular);
        if (!a.validarCelular()) {
            throw new IllegalArgumentException("Número de celular inválido.");
        }
        autenticacaoService.enviarCodigoSMS(celular);
    }

    // ── Passo 3–4: Validação SMS ──────────────────────────────────────────

    public Assinante validarCodigoSMS(String celular, String codigo, String nome, String email) {
        boolean valido = autenticacaoService.validarCodigo(celular, codigo);
        if (!valido) throw new IllegalArgumentException("Código SMS inválido ou expirado.");
        assinanteAtual = autenticacaoService.buscarOuCriarAssinante(celular, nome, email);
        return assinanteAtual;
    }

    // ── Passo 5: Listar planos ────────────────────────────────────────────

    public List<PlanoAssinatura> listarPlanos() {
        return assinaturaService.buscarTodosPlanos();
    }

    // ── Passo 6: Selecionar plano ─────────────────────────────────────────

    public Assinatura selecionarPlano(int planoId) {
        assinaturaAtual = assinaturaService.criarAssinatura(assinanteAtual.getId(), planoId);
        cestaAtual      = cestaService.criarCesta(assinaturaAtual.getId());
        assinaturaAtual.setCesta(cestaAtual);
        return assinaturaAtual;
    }

    // ── Passos 7–15: Produtos por tipo ────────────────────────────────────

    public List<Produto> listarProdutosPorTipo(TipoProduto tipo) {
        return cestaService.buscarProdutosPorTipo(tipo);
    }

    public void confirmarItensCesta(List<ItemCesta> itens) {
        cestaService.adicionarItens(cestaAtual, itens);
    }

    // ── Passos 16–18: Endereço de entrega ────────────────────────────────

    public void informarEnderecoEntrega(EnderecoEntrega endereco) {
        assinaturaService.salvarEndereco(assinaturaAtual.getId(), endereco);
        assinaturaAtual.setEndereco(endereco);
        cestaService.aguardarAprovacao(cestaAtual);
    }

    // ── Passos 19–22: Pagamento ───────────────────────────────────────────

    public Protocolo processarPagamento(CartaoCredito cartao) {
        Pagamento pagamento = pagamentoService.processarPagamento(
                assinaturaAtual.getId(),
                assinaturaAtual.getValorTotal(),
                cartao);

        assinaturaAtual.setPagamento(pagamento);

        if (pagamento.getStatus() != br.com.feiranossa.domain.enums.StatusPagamento.APROVADO) {
            throw new RuntimeException("Pagamento recusado pela operadora.");
        }

        cestaService.aprovar(cestaAtual);
        Protocolo protocolo = assinaturaService.finalizarAssinatura(assinaturaAtual);
        assinaturaAtual.setProtocolo(protocolo);
        return protocolo;
    }

    // ── Getters de contexto ───────────────────────────────────────────────

    public Assinante     getAssinanteAtual()  { return assinanteAtual; }
    public Assinatura    getAssinaturaAtual() { return assinaturaAtual; }
    public CestaDaSemana getCestaAtual()      { return cestaAtual; }
}
