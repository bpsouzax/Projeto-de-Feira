# FeiraNossa – Wiki do Projeto

Bem-vindo à Wiki do projeto **FeiraNossa**. Esta documentação apresenta todos os artefatos de análise e design que fundamentam a implementação Java.

---

## 📑 Índice

| Página | Conteúdo |
|--------|----------|
| [Caso de Uso](Caso-de-Uso) | Descrição completa do cenário |
| [Protótipo de Telas](Prototipo) | Sequência de 10 telas do fluxo |
| [Classes Candidatas](Classes-Candidatas) | 14 classes identificadas no protótipo |
| [Diagrama de Sequência](Diagrama-Sequencia) | Fluxo dinâmico do caso de uso em PlantUML |
| [Diagrama de Classes de Projeto](Diagrama-Classes-Projeto) | Arquitetura em 5 camadas em PlantUML |
| [Implementação Java](Implementacao) | Guia de correspondência UML ↔ Código |
| [Como Executar](Como-Executar) | Passo a passo para rodar o sistema |
| [Vídeo Demo](Video) | Link para o vídeo de demonstração |

---

## 🏛️ Visão Geral da Arquitetura

```
┌─────────────────────────────────────────────────────────┐
│                   AssinaturaUI (Boundary)                │
│          Interface console – 10 telas do protótipo       │
└──────────────────────┬──────────────────────────────────┘
                       │ chama
┌──────────────────────▼──────────────────────────────────┐
│             AssinaturaController (Controller)            │
│     Orquestra os 25 passos do caso de uso               │
└──┬───────────────┬────────────────┬──────────────────────┘
   │               │                │
   ▼               ▼                ▼
Autenticacao   Assinatura      CestaService    PagamentoService
Service        Service         (frutas,        (cartão +
(SMS)          (plano,         legumes,        operadora)
               endereço,       verduras)
               protocolo)
   │               │                │                │
   └───────────────┴────────────────┴────────────────┘
                       │ persiste via
              ┌────────▼────────┐
              │  Repositories   │
              │  (CSV files)    │
              └─────────────────┘
```

---

## 🔁 Rastreabilidade: Caso de Uso → Código

| Passo | Tela | Método |
|-------|------|--------|
| 1–2 | Tela 1 | `AssinaturaUI.telaIdentificacao()` → `controller.iniciarIdentificacao()` → `AutenticacaoService.enviarCodigoSMS()` |
| 3–4 | Tela 2 | `AssinaturaUI.telaValidacaoSMS()` → `controller.validarCodigoSMS()` → `AutenticacaoService.validarCodigo()` |
| 5–6 | Tela 3 | `AssinaturaUI.telaSelecaoPlano()` → `controller.selecionarPlano()` → `AssinaturaService.criarAssinatura()` |
| 7–9 | Tela 4 | `AssinaturaUI.telaSelecaoProdutos(FRUTA)` → `controller.confirmarItensCesta()` → `CestaService.adicionarItens()` |
| 10–12 | Tela 5 | `telaSelecaoProdutos(LEGUME)` → `CestaService.adicionarItens()` |
| 13–15 | Tela 6 | `telaSelecaoProdutos(VERDURA)` → `CestaService.adicionarItens()` |
| 16–18 | Tela 7 | `telaEndereco()` → `controller.informarEnderecoEntrega()` → `CestaService.aguardarAprovacao()` |
| 19–22 | Tela 8+9 | `telaDadosCartao()` → `controller.processarPagamento()` → `PagamentoService` → `OperadoraCartao (simulada)` |
| 23–25 | Tela 10 | `AssinaturaService.finalizarAssinatura()` → `ProtocoloRepository.salvar()` → `telaConfirmacao()` |
