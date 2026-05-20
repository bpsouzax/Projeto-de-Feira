package br.com.feiranossa.domain.model;

import br.com.feiranossa.domain.enums.StatusAssinatura;

import java.time.LocalDate;

public class Assinatura {

    private int              id;
    private int              assinanteId;
    private int              planoId;
    private LocalDate        dataInicio;
    private StatusAssinatura status;
    private double           valorTotal;

    // Objetos agregados (carregados em memória durante o fluxo)
    private CestaDaSemana   cesta;
    private EnderecoEntrega endereco;
    private Pagamento       pagamento;
    private Protocolo       protocolo;

    public Assinatura() {}

    public Assinatura(int id, int assinanteId, int planoId, double valorTotal) {
        this.id          = id;
        this.assinanteId = assinanteId;
        this.planoId     = planoId;
        this.valorTotal  = valorTotal;
        this.dataInicio  = LocalDate.now();
        this.status      = StatusAssinatura.AGUARDANDO;
    }

    public void ativar()   { this.status = StatusAssinatura.ATIVO; }
    public void aprovar()  { this.status = StatusAssinatura.APROVADO; }
    public void cancelar() { this.status = StatusAssinatura.CANCELADO; }

    // ── getters / setters ──────────────────────────────────────────────────
    public int              getId()              { return id; }
    public void             setId(int id)        { this.id = id; }

    public int              getAssinanteId()     { return assinanteId; }
    public void             setAssinanteId(int v){ this.assinanteId = v; }

    public int              getPlanoId()         { return planoId; }
    public void             setPlanoId(int v)    { this.planoId = v; }

    public LocalDate        getDataInicio()      { return dataInicio; }
    public void             setDataInicio(LocalDate d){ this.dataInicio = d; }

    public StatusAssinatura getStatus()          { return status; }
    public void             setStatus(StatusAssinatura s){ this.status = s; }

    public double           getValorTotal()      { return valorTotal; }
    public void             setValorTotal(double v){ this.valorTotal = v; }

    public CestaDaSemana    getCesta()            { return cesta; }
    public void             setCesta(CestaDaSemana c){ this.cesta = c; }

    public EnderecoEntrega  getEndereco()         { return endereco; }
    public void             setEndereco(EnderecoEntrega e){ this.endereco = e; }

    public Pagamento        getPagamento()        { return pagamento; }
    public void             setPagamento(Pagamento p){ this.pagamento = p; }

    public Protocolo        getProtocolo()        { return protocolo; }
    public void             setProtocolo(Protocolo p){ this.protocolo = p; }

    /** CSV: id;assinanteId;planoId;dataInicio;status;valorTotal */
    public String toCsv() {
        return id + ";" + assinanteId + ";" + planoId + ";" + dataInicio + ";" + status.name() + ";" + valorTotal;
    }

    public static Assinatura fromCsv(String linha) {
        String[] p = linha.split(";", -1);
        Assinatura a = new Assinatura();
        a.setId(Integer.parseInt(p[0]));
        a.setAssinanteId(Integer.parseInt(p[1]));
        a.setPlanoId(Integer.parseInt(p[2]));
        a.setDataInicio(LocalDate.parse(p[3]));
        a.setStatus(StatusAssinatura.valueOf(p[4]));
        a.setValorTotal(Double.parseDouble(p[5]));
        return a;
    }

    @Override
    public String toString() {
        return String.format("Assinatura #%d | Assinante:%d | Plano:%d | R$ %.2f | %s",
                id, assinanteId, planoId, valorTotal, status);
    }
}
