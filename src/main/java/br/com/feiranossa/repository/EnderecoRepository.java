package br.com.feiranossa.repository;

import br.com.feiranossa.domain.model.EnderecoEntrega;
import br.com.feiranossa.persistence.CaminhoArquivos;
import br.com.feiranossa.persistence.CsvUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnderecoRepository {

    /** Persiste: assinaturaId;csv_endereco */
    public void salvar(int assinaturaId, EnderecoEntrega e) {
        try {
            // Remove registro anterior desta assinatura se existir
            List<String> linhas = CsvUtil.lerLinhas(CaminhoArquivos.ENDERECOS);
            List<String> filtradas = linhas.stream()
                    .filter(l -> !l.startsWith(assinaturaId + ";"))
                    .collect(Collectors.toList());
            CsvUtil.reescrever(CaminhoArquivos.ENDERECOS, filtradas);
            CsvUtil.acrescentarLinha(CaminhoArquivos.ENDERECOS, assinaturaId + ";" + e.toCsv());
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao salvar endereço: " + ex.getMessage(), ex);
        }
    }

    public Optional<EnderecoEntrega> buscarPorAssinatura(int assinaturaId) {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.ENDERECOS).stream()
                    .filter(l -> l.startsWith(assinaturaId + ";"))
                    .map(l -> EnderecoEntrega.fromCsv(l.substring(l.indexOf(";") + 1)))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar endereço: " + e.getMessage(), e);
        }
    }
}
