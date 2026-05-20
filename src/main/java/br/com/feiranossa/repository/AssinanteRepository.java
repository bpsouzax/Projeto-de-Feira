package br.com.feiranossa.repository;

import br.com.feiranossa.domain.model.Assinante;
import br.com.feiranossa.persistence.CaminhoArquivos;
import br.com.feiranossa.persistence.CsvUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AssinanteRepository {

    public Assinante salvar(Assinante a) {
        try {
            if (a.getId() == 0) {
                a.setId(CsvUtil.proximoId(CaminhoArquivos.ASSINANTES));
            }
            // Atualiza se já existe, senão acrescenta
            List<String> linhas = CsvUtil.lerLinhas(CaminhoArquivos.ASSINANTES);
            boolean existe = linhas.stream().anyMatch(l -> l.startsWith(a.getId() + ";"));
            if (existe) {
                List<String> atualizadas = linhas.stream()
                        .map(l -> l.startsWith(a.getId() + ";") ? a.toCsv() : l)
                        .collect(Collectors.toList());
                CsvUtil.reescrever(CaminhoArquivos.ASSINANTES, atualizadas);
            } else {
                CsvUtil.acrescentarLinha(CaminhoArquivos.ASSINANTES, a.toCsv());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar assinante: " + e.getMessage(), e);
        }
        return a;
    }

    public Optional<Assinante> buscarPorCelular(String celular) {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.ASSINANTES).stream()
                    .map(Assinante::fromCsv)
                    .filter(a -> a.getCelular().equals(celular))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar assinante: " + e.getMessage(), e);
        }
    }

    public Optional<Assinante> buscarPorId(int id) {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.ASSINANTES).stream()
                    .map(Assinante::fromCsv)
                    .filter(a -> a.getId() == id)
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar assinante: " + e.getMessage(), e);
        }
    }

    public List<Assinante> listarTodos() {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.ASSINANTES).stream()
                    .map(Assinante::fromCsv)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao listar assinantes: " + e.getMessage(), e);
        }
    }
}
