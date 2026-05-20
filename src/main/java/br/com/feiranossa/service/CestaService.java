package br.com.feiranossa.service;

import br.com.feiranossa.domain.enums.TipoProduto;
import br.com.feiranossa.domain.model.CestaDaSemana;
import br.com.feiranossa.domain.model.ItemCesta;
import br.com.feiranossa.domain.model.Produto;
import br.com.feiranossa.repository.CestaRepository;
import br.com.feiranossa.repository.ProdutoRepository;

import java.time.LocalDate;
import java.util.List;

public class CestaService {

    private final CestaRepository    cestaRepo;
    private final ProdutoRepository  produtoRepo;

    public CestaService(CestaRepository cestaRepo, ProdutoRepository produtoRepo) {
        this.cestaRepo   = cestaRepo;
        this.produtoRepo = produtoRepo;
    }

    public CestaDaSemana criarCesta(int assinaturaId) {
        CestaDaSemana cesta = new CestaDaSemana(0, assinaturaId, LocalDate.now());
        return cestaRepo.salvar(cesta);
    }

    public List<Produto> buscarProdutosPorTipo(TipoProduto tipo) {
        return produtoRepo.buscarPorTipo(tipo);
    }

    /**
     * Adiciona itens de um tipo à cesta e persiste.
     */
    public void adicionarItens(CestaDaSemana cesta, List<ItemCesta> novosItens) {
        novosItens.forEach(cesta::adicionarItem);
        cestaRepo.salvar(cesta);
    }

    public void aguardarAprovacao(CestaDaSemana cesta) {
        cesta.aguardarAprovacao();
        cestaRepo.salvar(cesta);
    }

    public void aprovar(CestaDaSemana cesta) {
        cesta.aprovar();
        cestaRepo.salvar(cesta);
    }
}
