package br.com.feiranossa.persistence;

/**
 * Centraliza os caminhos dos arquivos CSV usados como persistência.
 */
public final class CaminhoArquivos {

    private CaminhoArquivos() {}

    public static final String BASE          = "data/";
    public static final String ASSINANTES    = BASE + "assinantes.csv";
    public static final String PLANOS        = BASE + "planos.csv";
    public static final String PRODUTOS      = BASE + "produtos.csv";
    public static final String ASSINATURAS   = BASE + "assinaturas.csv";
    public static final String CESTAS        = BASE + "cestas.csv";
    public static final String ITENS_CESTA   = BASE + "itens_cesta.csv";
    public static final String ENDERECOS     = BASE + "enderecos.csv";
    public static final String PAGAMENTOS    = BASE + "pagamentos.csv";
    public static final String PROTOCOLOS    = BASE + "protocolos.csv";
}
