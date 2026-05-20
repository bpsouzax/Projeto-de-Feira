package br.com.feiranossa.domain.model;

import br.com.feiranossa.domain.enums.TipoProduto;

public class PlanoAssinatura {

    private int    id;
    private String nome;
    private double preco;
    private int    qtdFrutas;
    private int    qtdLegumes;
    private int    qtdVerduras;

    public PlanoAssinatura() {}

    public PlanoAssinatura(int id, String nome, double preco,
                           int qtdFrutas, int qtdLegumes, int qtdVerduras) {
        this.id          = id;
        this.nome        = nome;
        this.preco       = preco;
        this.qtdFrutas   = qtdFrutas;
        this.qtdLegumes  = qtdLegumes;
        this.qtdVerduras = qtdVerduras;
    }

    /** Retorna a quota para um dado tipo de produto. */
    public int getQuotaPorTipo(TipoProduto tipo) {
        return switch (tipo) {
            case FRUTA   -> qtdFrutas;
            case LEGUME  -> qtdLegumes;
            case VERDURA -> qtdVerduras;
        };
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public int    getId()           { return id; }
    public void   setId(int id)     { this.id = id; }

    public String getNome()              { return nome; }
    public void   setNome(String nome)   { this.nome = nome; }

    public double getPreco()             { return preco; }
    public void   setPreco(double preco) { this.preco = preco; }

    public int getQtdFrutas()                { return qtdFrutas; }
    public void setQtdFrutas(int qtdFrutas)  { this.qtdFrutas = qtdFrutas; }

    public int getQtdLegumes()               { return qtdLegumes; }
    public void setQtdLegumes(int v)         { this.qtdLegumes = v; }

    public int getQtdVerduras()              { return qtdVerduras; }
    public void setQtdVerduras(int v)        { this.qtdVerduras = v; }

    /** CSV: id;nome;preco;qtdFrutas;qtdLegumes;qtdVerduras */
    public String toCsv() {
        return id + ";" + nome + ";" + preco + ";" + qtdFrutas + ";" + qtdLegumes + ";" + qtdVerduras;
    }

    public static PlanoAssinatura fromCsv(String linha) {
        String[] p = linha.split(";", -1);
        return new PlanoAssinatura(Integer.parseInt(p[0]), p[1],
                Double.parseDouble(p[2]),
                Integer.parseInt(p[3]), Integer.parseInt(p[4]), Integer.parseInt(p[5]));
    }

    @Override
    public String toString() {
        return String.format("Plano #%d – %s | R$ %.2f/semana | Frutas:%d Legumes:%d Verduras:%d",
                id, nome, preco, qtdFrutas, qtdLegumes, qtdVerduras);
    }
}
