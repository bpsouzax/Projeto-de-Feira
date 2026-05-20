package br.com.feiranossa.repository;

import br.com.feiranossa.domain.model.Protocolo;
import br.com.feiranossa.persistence.CaminhoArquivos;
import br.com.feiranossa.persistence.CsvUtil;

import java.io.IOException;

public class ProtocoloRepository {

    public Protocolo salvar(Protocolo p) {
        try {
            CsvUtil.acrescentarLinha(CaminhoArquivos.PROTOCOLOS, p.toCsv());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar protocolo: " + e.getMessage(), e);
        }
        return p;
    }
}
