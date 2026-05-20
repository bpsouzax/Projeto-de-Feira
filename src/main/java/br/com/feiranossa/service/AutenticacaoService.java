package br.com.feiranossa.service;

import br.com.feiranossa.domain.model.Assinante;
import br.com.feiranossa.domain.model.CodigoSMS;
import br.com.feiranossa.repository.AssinanteRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Gerencia autenticação via código SMS.
 * Em produção, delegaria a um gateway real de SMS.
 * Aqui o "envio" é simulado via console.
 */
public class AutenticacaoService {

    private final AssinanteRepository assinanteRepo;
    // Mapa em memória: celular → CodigoSMS (válido enquanto o processo roda)
    private final Map<String, CodigoSMS> codigosAtivos = new HashMap<>();

    public AutenticacaoService(AssinanteRepository assinanteRepo) {
        this.assinanteRepo = assinanteRepo;
    }

    /**
     * Gera e "envia" o código SMS para o celular informado.
     * Retorna o código gerado (para ser exibido no console em modo simulação).
     */
    public String enviarCodigoSMS(String celular) {
        String codigo = String.format("%06d", new Random().nextInt(999999));
        CodigoSMS sms = new CodigoSMS(celular, codigo);
        codigosAtivos.put(celular, sms);
        // Simulação: em produção chamaria ServicoSMSGateway.enviar(celular, codigo)
        System.out.println("[SMS SIMULADO] Código enviado para " + celular + ": " + codigo);
        return codigo;
    }

    /**
     * Valida o código informado pelo assinante.
     */
    public boolean validarCodigo(String celular, String codigoInformado) {
        CodigoSMS sms = codigosAtivos.get(celular);
        if (sms == null) return false;
        boolean valido = sms.validar(codigoInformado);
        if (valido) codigosAtivos.remove(celular);
        return valido;
    }

    /**
     * Busca ou cria assinante pelo celular.
     */
    public Assinante buscarOuCriarAssinante(String celular, String nome, String email) {
        Optional<Assinante> existente = assinanteRepo.buscarPorCelular(celular);
        if (existente.isPresent()) return existente.get();

        Assinante novo = new Assinante(0, nome, email, celular);
        return assinanteRepo.salvar(novo);
    }
}
