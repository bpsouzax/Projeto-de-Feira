package br.com.feiranossa.repository;

import br.com.feiranossa.domain.model.CestaDaSemana;
import br.com.feiranossa.domain.model.ItemCesta;
import br.com.feiranossa.persistence.CaminhoArquivos;
import br.com.feiranossa.persistence.CsvUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CestaRepository {

    /** Persiste o cabeçalho da cesta: id;assinaturaId;semana;status */
    public CestaDaSemana salvar(CestaDaSemana c) {
        try {
            if (c.getId() == 0) {
                c.setId(CsvUtil.proximoId(CaminhoArquivos.CESTAS));
            }
            String linha = c.getId() + ";" + c.getAssinaturaId() + ";"
                    + c.getSemanaReferencia() + ";" + c.getStatus().name();

            List<String> linhas = CsvUtil.lerLinhas(CaminhoArquivos.CESTAS);
            boolean existe = linhas.stream().anyMatch(l -> l.startsWith(c.getId() + ";"));
            if (existe) {
                final String l2 = linha;
                List<String> atualizadas = linhas.stream()
                        .map(l -> l.startsWith(c.getId() + ";") ? l2 : l)
                        .collect(Collectors.toList());
                CsvUtil.reescrever(CaminhoArquivos.CESTAS, atualizadas);
            } else {
                CsvUtil.acrescentarLinha(CaminhoArquivos.CESTAS, linha);
            }

            // Persiste os itens
            salvarItens(c);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar cesta: " + e.getMessage(), e);
        }
        return c;
    }

    private void salvarItens(CestaDaSemana c) throws IOException {
        // Remove itens antigos desta cesta e regrava
        List<String> existentes = CsvUtil.lerLinhas(CaminhoArquivos.ITENS_CESTA);
        List<String> outros = existentes.stream()
                .filter(l -> !l.startsWith(c.getId() + ";"))
                .collect(Collectors.toList());
        CsvUtil.reescrever(CaminhoArquivos.ITENS_CESTA, outros);
        for (ItemCesta item : c.getItens()) {
            CsvUtil.acrescentarLinha(CaminhoArquivos.ITENS_CESTA,
                    c.getId() + ";" + item.toCsv());
        }
    }

    public Optional<CestaDaSemana> buscarPorAssinatura(int assinaturaId) {
        try {
            List<String> linhas = CsvUtil.lerLinhas(CaminhoArquivos.CESTAS);
            Optional<String> linhaOpt = linhas.stream()
                    .filter(l -> {
                        String[] p = l.split(";");
                        return p.length > 1 && Integer.parseInt(p[1]) == assinaturaId;
                    })
                    .findFirst();

            if (linhaOpt.isEmpty()) return Optional.empty();

            String[] p = linhaOpt.get().split(";");
            CestaDaSemana c = new CestaDaSemana();
            c.setId(Integer.parseInt(p[0]));
            c.setAssinaturaId(Integer.parseInt(p[1]));
            c.setSemanaReferencia(LocalDate.parse(p[2]));
            c.setStatus(br.com.feiranossa.domain.enums.StatusCesta.valueOf(p[3]));
            c.setItens(carregarItens(c.getId()));
            return Optional.of(c);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar cesta: " + e.getMessage(), e);
        }
    }

    private List<ItemCesta> carregarItens(int cestaId) throws IOException {
        List<ItemCesta> itens = new ArrayList<>();
        for (String linha : CsvUtil.lerLinhas(CaminhoArquivos.ITENS_CESTA)) {
            String[] p = linha.split(";", 2);
            if (Integer.parseInt(p[0]) == cestaId) {
                itens.add(ItemCesta.fromCsv(p[1]));
            }
        }
        return itens;
    }
}
