package br.com.feiranossa.domain.model;

import br.com.feiranossa.domain.enums.StatusPagamento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pagamento {

    private int              id;
    private int              assinaturaId;
    private double           valor;
    private StatusPagamento  status;
    private LocalDateTime    dataPagamento;
    private String           codigoTransacao;
    private CartaoCredito    cartao;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Pagamento() {}

    public Pagamento(int id, int assinaturaId, double valor, CartaoCredito cartao) {
        this.id           = id;
        this.assinaturaId = assinaturaId;
        this.valor        = valor;
        this.cartao       = cartao;
        this.status       = StatusPagamento.PENDENTE;
    }

    public void confirmar(String codigoTransacao) {
        this.status          = StatusPagamento.APROVADO;
        this.codigoTransacao = codigoTransacao;
        this.dataPagamento   = LocalDateTime.now();
    }

    public void recusar() {
        this.status        = StatusPagamento.RECUSADO;
        this.dataPagamento = LocalDateTime.now();
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public int             getId()                   { return id; }
    public void            setId(int id)             { this.id = id; }

    public int             getAssinaturaId()          { return assinaturaId; }
    public void            setAssinaturaId(int v)     { this.assinaturaId = v; }

    public double          getValor()                 { return valor; }
    public void            setValor(double v)         { this.valor = v; }

    public StatusPagamento getStatus()                { return status; }
    public void            setStatus(StatusPagamento s){ this.status = s; }

    public LocalDateTime   getDataPagamento()          { return dataPagamento; }
    public void            setDataPagamento(LocalDateTime d){ this.dataPagamento = d; }

    public String          getCodigoTransacao()        { return codigoTransacao; }
    public void            setCodigoTransacao(String c){ this.codigoTransacao = c; }

    public CartaoCredito   getCartao()                 { return cartao; }
    public void            setCartao(CartaoCredito c)  { this.cartao = c; }

    /** CSV: id;assinaturaId;valor;status;dataPagamento;codigoTransacao;cartaoMascarado */
    public String toCsv() {
        String data = dataPagamento != null ? dataPagamento.format(FMT) : "";
        String cod  = codigoTransacao != null ? codigoTransacao : "";
        String cartaoStr = cartao != null ? cartao.mascararNumero() : "";
        return id + ";" + assinaturaId + ";" + valor + ";" + status.name() + ";" + data + ";" + cod + ";" + cartaoStr;
    }

    @Override
    public String toString() {
        return String.format("Pagamento #%d | R$ %.2f | %s | %s", id, valor, status,
                dataPagamento != null ? dataPagamento.format(FMT) : "pendente");
    }
}
