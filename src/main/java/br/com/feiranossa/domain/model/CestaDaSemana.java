package br.com.feiranossa.domain.model;

import br.com.feiranossa.domain.enums.StatusCesta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CestaDaSemana {

    private int         id;
    private int         assinaturaId;
    private LocalDate   semanaReferencia;
    private StatusCesta status;
    private List<ItemCesta> itens = new ArrayList<>();

    public CestaDaSemana() {}

    public CestaDaSemana(int id, int assinaturaId, LocalDate semanaReferencia) {
        this.id               = id;
        this.assinaturaId     = assinaturaId;
        this.semanaReferencia = semanaReferencia;
        this.status           = StatusCesta.EM_SELECAO;
    }

    public void adicionarItem(ItemCesta item) {
        itens.add(item);
    }

    public double calcularTotal() {
        return itens.stream().mapToDouble(ItemCesta::calcularSubtotal).sum();
    }

    public void aprovar() {
        this.status = StatusCesta.APROVADA;
    }

    public void aguardarAprovacao() {
        this.status = StatusCesta.AGUARD_APROVACAO;
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public int          getId()                   { return id; }
    public void         setId(int id)             { this.id = id; }

    public int          getAssinaturaId()          { return assinaturaId; }
    public void         setAssinaturaId(int v)     { this.assinaturaId = v; }

    public LocalDate    getSemanaReferencia()       { return semanaReferencia; }
    public void         setSemanaReferencia(LocalDate d){ this.semanaReferencia = d; }

    public StatusCesta  getStatus()                { return status; }
    public void         setStatus(StatusCesta s)   { this.status = s; }

    public List<ItemCesta> getItens()              { return itens; }
    public void            setItens(List<ItemCesta> itens){ this.itens = itens; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Cesta #%d | Semana: %s | Status: %s%n", id, semanaReferencia, status));
        itens.forEach(i -> sb.append(i).append("\n"));
        sb.append(String.format("  TOTAL: R$ %.2f%n", calcularTotal()));
        return sb.toString();
    }
}
