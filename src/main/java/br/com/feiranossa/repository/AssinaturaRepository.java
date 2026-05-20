package br.com.feiranossa.repository;

import br.com.feiranossa.domain.model.Assinatura;
import br.com.feiranossa.persistence.CaminhoArquivos;
import br.com.feiranossa.persistence.CsvUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AssinaturaRepository {

    public Assinatura salvar(Assinatura a) {
        try {
            if (a.getId() == 0) {
                a.setId(CsvUtil.proximoId(CaminhoArquivos.ASSINATURAS));
            }
            List<String> linhas = CsvUtil.lerLinhas(CaminhoArquivos.ASSINATURAS);
            boolean existe = linhas.stream().anyMatch(l -> l.startsWith(a.getId() + ";"));
            if (existe) {
                List<String> atualizadas = linhas.stream()
                        .map(l -> l.startsWith(a.getId() + ";") ? a.toCsv() : l)
                        .collect(Collectors.toList());
                CsvUtil.reescrever(CaminhoArquivos.ASSINATURAS, atualizadas);
            } else {
                CsvUtil.acrescentarLinha(CaminhoArquivos.ASSINATURAS, a.toCsv());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar assinatura: " + e.getMessage(), e);
        }
        return a;
    }

    public Optional<Assinatura> buscarPorId(int id) {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.ASSINATURAS).stream()
                    .map(Assinatura::fromCsv)
                    .filter(a -> a.getId() == id)
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar assinatura: " + e.getMessage(), e);
        }
    }

    public List<Assinatura> buscarPorAssinante(int assinanteId) {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.ASSINATURAS).stream()
                    .map(Assinatura::fromCsv)
                    .filter(a -> a.getAssinanteId() == assinanteId)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar assinaturas: " + e.getMessage(), e);
        }
    }
}
