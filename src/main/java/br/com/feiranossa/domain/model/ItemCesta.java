package br.com.feiranossa.domain.model;

public class ItemCesta {

    private int     produtoId;
    private String  nomeProduto;
    private int     quantidade;
    private String  unidade;
    private double  precoUnitario;

    public ItemCesta() {}

    public ItemCesta(int produtoId, String nomeProduto,
                     int quantidade, String unidade, double precoUnitario) {
        this.produtoId     = produtoId;
        this.nomeProduto   = nomeProduto;
        this.quantidade    = quantidade;
        this.unidade       = unidade;
        this.precoUnitario = precoUnitario;
    }

    public double calcularSubtotal() {
        return quantidade * precoUnitario;
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public int    getProdutoId()              { return produtoId; }
    public void   setProdutoId(int produtoId) { this.produtoId = produtoId; }

    public String getNomeProduto()              { return nomeProduto; }
    public void   setNomeProduto(String n)      { this.nomeProduto = n; }

    public int    getQuantidade()               { return quantidade; }
    public void   setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getUnidade()              { return unidade; }
    public void   setUnidade(String u)      { this.unidade = u; }

    public double getPrecoUnitario()             { return precoUnitario; }
    public void   setPrecoUnitario(double p)     { this.precoUnitario = p; }

    /** CSV: produtoId;nomeProduto;quantidade;unidade;precoUnitario */
    public String toCsv() {
        return produtoId + ";" + nomeProduto + ";" + quantidade + ";" + unidade + ";" + precoUnitario;
    }

    public static ItemCesta fromCsv(String linha) {
        String[] p = linha.split(";", -1);
        return new ItemCesta(Integer.parseInt(p[0]), p[1],
                Integer.parseInt(p[2]), p[3], Double.parseDouble(p[4]));
    }

    @Override
    public String toString() {
        return String.format("  %-20s x%d %-8s = R$ %.2f", nomeProduto, quantidade, unidade, calcularSubtotal());
    }
}
