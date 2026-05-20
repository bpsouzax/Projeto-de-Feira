package br.com.feiranossa.service;

import br.com.feiranossa.domain.model.*;
import br.com.feiranossa.repository.*;

import java.util.List;

public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepo;
    private final PlanoRepository      planoRepo;
    private final EnderecoRepository   enderecoRepo;
    private final ProtocoloRepository  protocoloRepo;

    public AssinaturaService(AssinaturaRepository assinaturaRepo,
                             PlanoRepository planoRepo,
                             EnderecoRepository enderecoRepo,
                             ProtocoloRepository protocoloRepo) {
        this.assinaturaRepo = assinaturaRepo;
        this.planoRepo      = planoRepo;
        this.enderecoRepo   = enderecoRepo;
        this.protocoloRepo  = protocoloRepo;
    }

    public List<PlanoAssinatura> buscarTodosPlanos() {
        return planoRepo.listarAtivos();
    }

    public Assinatura criarAssinatura(int assinanteId, int planoId) {
        PlanoAssinatura plano = planoRepo.buscarPorId(planoId)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado: " + planoId));

        Assinatura a = new Assinatura(0, assinanteId, planoId, plano.getPreco());
        return assinaturaRepo.salvar(a);
    }

    public void salvarEndereco(int assinaturaId, EnderecoEntrega endereco) {
        if (!endereco.validarCEP()) {
            throw new IllegalArgumentException("CEP inválido: " + endereco.getCep());
        }
        enderecoRepo.salvar(assinaturaId, endereco);
    }

    public Protocolo finalizarAssinatura(Assinatura assinatura) {
        assinatura.aprovar();
        assinaturaRepo.salvar(assinatura);

        Protocolo protocolo = new Protocolo(assinatura.getId());
        protocoloRepo.salvar(protocolo);
        assinatura.setProtocolo(protocolo);
        return protocolo;
    }

    public Assinatura buscarPorId(int id) {
        return assinaturaRepo.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Assinatura não encontrada: " + id));
    }
}
