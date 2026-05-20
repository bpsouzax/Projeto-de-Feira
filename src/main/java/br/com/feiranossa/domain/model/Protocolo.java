package br.com.feiranossa.domain.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Protocolo {

    private String        numero;
    private int           assinaturaId;
    private LocalDateTime dataEmissao;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Protocolo() {}

    public Protocolo(int assinaturaId) {
        this.assinaturaId = assinaturaId;
        this.dataEmissao  = LocalDateTime.now();
        this.numero       = gerar(assinaturaId);
    }

    /** Gera número de protocolo no formato FN-ANO-IDPADDED */
    public String gerar(int assinaturaId) {
        int ano = LocalDateTime.now().getYear();
        return String.format("FN-%d-%05d", ano, assinaturaId);
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public String        getNumero()             { return numero; }
    public void          setNumero(String n)     { this.numero = n; }

    public int           getAssinaturaId()       { return assinaturaId; }
    public void          setAssinaturaId(int v)  { this.assinaturaId = v; }

    public LocalDateTime getDataEmissao()        { return dataEmissao; }
    public void          setDataEmissao(LocalDateTime d){ this.dataEmissao = d; }

    /** CSV: numero;assinaturaId;dataEmissao */
    public String toCsv() {
        return numero + ";" + assinaturaId + ";" + dataEmissao.format(FMT);
    }

    @Override
    public String toString() {
        return String.format("Protocolo: %s | Emitido em: %s", numero, dataEmissao.format(FMT));
    }
}
