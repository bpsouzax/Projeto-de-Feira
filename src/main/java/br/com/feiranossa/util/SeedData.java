package br.com.feiranossa.util;

import br.com.feiranossa.persistence.CaminhoArquivos;
import br.com.feiranossa.persistence.CsvUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Popula os arquivos CSV com dados iniciais (planos e catálogo de produtos).
 * Executa apenas uma vez; se os arquivos já tiverem conteúdo, não faz nada.
 */
public class SeedData {

    public static void inicializar() throws IOException {
        Files.createDirectories(Paths.get(CaminhoArquivos.BASE));
        seedPlanos();
        seedProdutos();
    }

    private static void seedPlanos() throws IOException {
        List<String> planos = CsvUtil.lerLinhas(CaminhoArquivos.PLANOS);
        if (!planos.isEmpty()) return;
        // id;nome;preco;qtdFrutas;qtdLegumes;qtdVerduras
        CsvUtil.acrescentarLinha(CaminhoArquivos.PLANOS, "1;Cesta Basica;59.0;3;3;2");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PLANOS, "2;Cesta Familia;99.0;5;5;4");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PLANOS, "3;Cesta Premium;149.0;7;7;6");
        System.out.println("[Seed] Planos carregados.");
    }

    private static void seedProdutos() throws IOException {
        List<String> produtos = CsvUtil.lerLinhas(CaminhoArquivos.PRODUTOS);
        if (!produtos.isEmpty()) return;
        // id;nome;unidade;preco;tipo
        // Frutas
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "1;Banana;cacho;4.50;FRUTA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "2;Laranja Pera;kg;3.90;FRUTA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "3;Limao Taiti;duzia;5.00;FRUTA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "4;Uva Italia;kg;12.90;FRUTA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "5;Pessego;kg;9.90;FRUTA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "6;Abacaxi;unidade;6.50;FRUTA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "7;Manga Tommy;unidade;4.00;FRUTA");
        // Legumes
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "8;Cenoura;kg;3.50;LEGUME");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "9;Batata Inglesa;kg;4.20;LEGUME");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "10;Milho Verde;unidade;2.50;LEGUME");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "11;Tomate Italiano;kg;5.80;LEGUME");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "12;Cebola;kg;3.00;LEGUME");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "13;Pimentao;unidade;2.80;LEGUME");
        // Verduras
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "14;Alface Crespa;pe;2.50;VERDURA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "15;Cheiro Verde;maco;3.00;VERDURA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "16;Brocolis;unidade;5.50;VERDURA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "17;Espinafre;maco;3.50;VERDURA");
        CsvUtil.acrescentarLinha(CaminhoArquivos.PRODUTOS, "18;Rucula;maco;4.00;VERDURA");
        System.out.println("[Seed] Produtos carregados.");
    }
}
