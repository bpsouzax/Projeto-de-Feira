package br.com.feiranossa.repository;

import br.com.feiranossa.domain.model.Pagamento;
import br.com.feiranossa.persistence.CaminhoArquivos;
import br.com.feiranossa.persistence.CsvUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PagamentoRepository {

    public Pagamento salvar(Pagamento p) {
        try {
            if (p.getId() == 0) {
                p.setId(CsvUtil.proximoId(CaminhoArquivos.PAGAMENTOS));
            }
            List<String> linhas = CsvUtil.lerLinhas(CaminhoArquivos.PAGAMENTOS);
            boolean existe = linhas.stream().anyMatch(l -> l.startsWith(p.getId() + ";"));
            if (existe) {
                List<String> atualizadas = linhas.stream()
                        .map(l -> l.startsWith(p.getId() + ";") ? p.toCsv() : l)
                        .collect(Collectors.toList());
                CsvUtil.reescrever(CaminhoArquivos.PAGAMENTOS, atualizadas);
            } else {
                CsvUtil.acrescentarLinha(CaminhoArquivos.PAGAMENTOS, p.toCsv());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar pagamento: " + e.getMessage(), e);
        }
        return p;
    }

    public Optional<Pagamento> buscarPorAssinatura(int assinaturaId) {
        try {
            return CsvUtil.lerLinhas(CaminhoArquivos.PAGAMENTOS).stream()
                    .filter(l -> {
                        String[] parts = l.split(";");
                        return parts.length > 1 && Integer.parseInt(parts[1]) == assinaturaId;
                    })
                    .findFirst()
                    .map(l -> {
                        String[] p = l.split(";", -1);
                        Pagamento pg = new Pagamento();
                        pg.setId(Integer.parseInt(p[0]));
                        pg.setAssinaturaId(Integer.parseInt(p[1]));
                        pg.setValor(Double.parseDouble(p[2]));
                        pg.setStatus(br.com.feiranossa.domain.enums.StatusPagamento.valueOf(p[3]));
                        return pg;
                    });
        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar pagamento: " + e.getMessage(), e);
        }
    }
}
