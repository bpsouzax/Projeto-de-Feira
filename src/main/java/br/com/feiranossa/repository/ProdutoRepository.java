package br.com.feiranossa.repository;

import br.com.feiranossa.domain.enums.TipoProduto;
import br.com.feiranossa.domain.model.Produto;
import br.com.feiranossa.persistence.CaminhoArquivos;
import br.com.feiranossa.persistence.CsvUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProdutoRepository {

    public List<Produto> buscarPorTipo(TipoProduto tipo) {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.PRODUTOS).stream()
                    .map(Produto::fromCsv)
                    .filter(p -> p.getTipo() == tipo)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar produtos: " + e.getMessage(), e);
        }
    }

    public Optional<Produto> buscarPorId(int id) {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.PRODUTOS).stream()
                    .map(Produto::fromCsv)
                    .filter(p -> p.getId() == id)
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage(), e);
        }
    }

    public List<Produto> listarTodos() {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.PRODUTOS).stream()
                    .map(Produto::fromCsv)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
    }
}
