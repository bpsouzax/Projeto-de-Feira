package br.com.feiranossa.domain.model;

import br.com.feiranossa.domain.enums.TipoProduto;

public class Produto {

    private int         id;
    private String      nome;
    private String      unidade;
    private double      preco;
    private TipoProduto tipo;

    public Produto() {}

    public Produto(int id, String nome, String unidade, double preco, TipoProduto tipo) {
        this.id      = id;
        this.nome    = nome;
        this.unidade = unidade;
        this.preco   = preco;
        this.tipo    = tipo;
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public int         getId()              { return id; }
    public void        setId(int id)        { this.id = id; }

    public String      getNome()            { return nome; }
    public void        setNome(String n)    { this.nome = n; }

    public String      getUnidade()         { return unidade; }
    public void        setUnidade(String u) { this.unidade = u; }

    public double      getPreco()           { return preco; }
    public void        setPreco(double p)   { this.preco = p; }

    public TipoProduto getTipo()            { return tipo; }
    public void        setTipo(TipoProduto t){ this.tipo = t; }

    /** CSV: id;nome;unidade;preco;tipo */
    public String toCsv() {
        return id + ";" + nome + ";" + unidade + ";" + preco + ";" + tipo.name();
    }

    public static Produto fromCsv(String linha) {
        String[] p = linha.split(";", -1);
        return new Produto(Integer.parseInt(p[0]), p[1], p[2],
                Double.parseDouble(p[3]), TipoProduto.valueOf(p[4]));
    }

    @Override
    public String toString() {
        return String.format("#%d %-20s (%-7s) R$ %.2f/%s", id, nome, tipo, preco, unidade);
    }
}
