package br.com.feiranossa.persistence;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário genérico para leitura e escrita de arquivos CSV.
 * Cada linha representa um registro; os campos são separados por ";".
 */
public class CsvUtil {

    /** Garante que o arquivo existe; cria-o vazio se necessário. */
    public static void garantirArquivo(String caminho) throws IOException {
        Path path = Paths.get(caminho);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
    }

    /** Lê todas as linhas não-vazias do arquivo. */
    public static List<String> lerLinhas(String caminho) throws IOException {
        garantirArquivo(caminho);
        List<String> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.isBlank()) linhas.add(linha.trim());
            }
        }
        return linhas;
    }

    /** Acrescenta uma linha ao arquivo (append). */
    public static void acrescentarLinha(String caminho, String linha) throws IOException {
        garantirArquivo(caminho);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho, true))) {
            bw.write(linha);
            bw.newLine();
        }
    }

    /** Reescreve o arquivo inteiro com as linhas fornecidas. */
    public static void reescrever(String caminho, List<String> linhas) throws IOException {
        garantirArquivo(caminho);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminho, false))) {
            for (String l : linhas) {
                bw.write(l);
                bw.newLine();
            }
        }
    }

    /**
     * Retorna o próximo ID disponível: lê a última linha, extrai o primeiro
     * campo (inteiro) e soma 1. Retorna 1 se o arquivo estiver vazio.
     */
    public static int proximoId(String caminho) throws IOException {
        List<String> linhas = lerLinhas(caminho);
        if (linhas.isEmpty()) return 1;
        String ultima = linhas.get(linhas.size() - 1);
        try {
            return Integer.parseInt(ultima.split(";")[0]) + 1;
        } catch (NumberFormatException e) {
            return linhas.size() + 1;
        }
    }
}
