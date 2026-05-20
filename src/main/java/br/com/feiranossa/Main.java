package br.com.feiranossa;

import br.com.feiranossa.boundary.AssinaturaUI;
import br.com.feiranossa.controller.AssinaturaController;
import br.com.feiranossa.repository.*;
import br.com.feiranossa.service.*;
import br.com.feiranossa.util.SeedData;

/**
 * Ponto de entrada da aplicação FeiraNossa.
 *
 * Responsabilidades:
 *  1. Inicializa os dados de seed (planos e catálogo de produtos)
 *  2. Instancia os repositórios (acesso a dados CSV)
 *  3. Instancia os serviços (regras de negócio)
 *  4. Instancia o controller (orquestração do fluxo)
 *  5. Instancia e inicia a UI (objeto fronteira / boundary)
 *
 * Corresponde ao ponto de partida do Diagrama de Sequência:
 *   Assinante → [Sistema] iniciar fluxo
 */
public class Main {

    public static void main(String[] args) {
        try {
            // ── 1. Seed dos dados iniciais ────────────────────────────────
            SeedData.inicializar();

            // ── 2. Repositórios ───────────────────────────────────────────
            AssinanteRepository  assinanteRepo  = new AssinanteRepository();
            AssinaturaRepository assinaturaRepo = new AssinaturaRepository();
            PlanoRepository      planoRepo      = new PlanoRepository();
            ProdutoRepository    produtoRepo    = new ProdutoRepository();
            CestaRepository      cestaRepo      = new CestaRepository();
            EnderecoRepository   enderecoRepo   = new EnderecoRepository();
            PagamentoRepository  pagamentoRepo  = new PagamentoRepository();
            ProtocoloRepository  protocoloRepo  = new ProtocoloRepository();

            // ── 3. Serviços ───────────────────────────────────────────────
            AutenticacaoService autenticacaoService = new AutenticacaoService(assinanteRepo);
            CestaService        cestaService        = new CestaService(cestaRepo, produtoRepo);
            PagamentoService    pagamentoService    = new PagamentoService(pagamentoRepo);
            AssinaturaService   assinaturaService   = new AssinaturaService(
                    assinaturaRepo, planoRepo, enderecoRepo, protocoloRepo);

            // ── 4. Controller ─────────────────────────────────────────────
            AssinaturaController controller = new AssinaturaController(
                    autenticacaoService, assinaturaService, cestaService, pagamentoService);

            // ── 5. Boundary (UI) ──────────────────────────────────────────
            AssinaturaUI ui = new AssinaturaUI(controller);
            ui.iniciar();

        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
