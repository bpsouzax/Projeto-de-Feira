package br.com.feiranossa.repository;

import br.com.feiranossa.domain.model.PlanoAssinatura;
import br.com.feiranossa.persistence.CaminhoArquivos;
import br.com.feiranossa.persistence.CsvUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlanoRepository {

    public List<PlanoAssinatura> listarAtivos() {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.PLANOS).stream()
                    .map(PlanoAssinatura::fromCsv)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao listar planos: " + e.getMessage(), e);
        }
    }

    public Optional<PlanoAssinatura> buscarPorId(int id) {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.PLANOS).stream()
                    .map(PlanoAssinatura::fromCsv)
                    .filter(p -> p.getId() == id)
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar plano: " + e.getMessage(), e);
        }
    }

    public void salvar(PlanoAssinatura p) {
        try {
            CsvUtil.acrescentarLinha(CaminhoArquivos.PLANOS, p.toCsv());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar plano: " + e.getMessage(), e);
        }
    }
}
