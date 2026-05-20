# 🌿 FeiraNossa – Sistema de Assinatura de Cesta de Feira

Implementação Java do caso de uso **"Assinar Serviço de Feira"**, com correspondência direta entre os modelos UML (Diagrama de Sequência e Diagrama de Classes de Projeto) e o código-fonte.

---

## 📐 Estrutura do Projeto

```
feiranossa/
├── data/                          ← Arquivos CSV (persistência)
│   ├── assinantes.csv
│   ├── planos.csv
│   ├── produtos.csv
│   ├── assinaturas.csv
│   ├── cestas.csv
│   ├── itens_cesta.csv
│   ├── enderecos.csv
│   ├── pagamentos.csv
│   └── protocolos.csv
│
└── src/main/java/br/com/feiranossa/
    ├── Main.java                  ← Ponto de entrada / injeção de dependências
    ├── boundary/
    │   └── AssinaturaUI.java      ← Objeto Fronteira (Boundary) – UI console
    ├── controller/
    │   └── AssinaturaController.java  ← Orquestra o fluxo do caso de uso
    ├── service/
    │   ├── AssinaturaService.java
    │   ├── AutenticacaoService.java
    │   ├── CestaService.java
    │   └── PagamentoService.java
    ├── domain/
    │   ├── model/
    │   │   ├── Assinante.java
    │   │   ├── Assinatura.java
    │   │   ├── PlanoAssinatura.java
    │   │   ├── CestaDaSemana.java
    │   │   ├── ItemCesta.java
    │   │   ├── Produto.java
    │   │   ├── EnderecoEntrega.java
    │   │   ├── CartaoCredito.java
    │   │   ├── Pagamento.java
    │   │   ├── Protocolo.java
    │   │   └── CodigoSMS.java
    │   └── enums/
    │       ├── TipoProduto.java
    │       ├── StatusAssinatura.java
    │       ├── StatusCesta.java
    │       └── StatusPagamento.java
    ├── repository/
    │   ├── AssinanteRepository.java
    │   ├── AssinaturaRepository.java
    │   ├── PlanoRepository.java
    │   ├── ProdutoRepository.java
    │   ├── CestaRepository.java
    │   ├── EnderecoRepository.java
    │   ├── PagamentoRepository.java
    │   └── ProtocoloRepository.java
    ├── persistence/
    │   ├── CsvUtil.java           ← Leitura/escrita genérica de CSV
    │   └── CaminhoArquivos.java   ← Constantes de caminhos
    └── util/
        └── SeedData.java          ← Popula dados iniciais (planos e produtos)
```

---

## 🏛️ Arquitetura em Camadas

```
[ AssinaturaUI ]  ←→  Boundary (Fronteira)
       ↓
[ AssinaturaController ]  ←→  Controller
       ↓
[ Services: Assinatura / Autenticacao / Cesta / Pagamento ]  ←→  Serviço
       ↓
[ Domain: Assinante, Assinatura, CestaDaSemana, Pagamento... ]  ←→  Entidade
       ↓
[ Repositories → CsvUtil → data/*.csv ]  ←→  Persistência
```

---

## 🔁 Correspondência com o Diagrama de Sequência

| Passo do Caso de Uso                | Método no Controller / Service                         |
|-------------------------------------|-------------------------------------------------------|
| 1–2. Informar celular + envio SMS   | `controller.iniciarIdentificacao()`                   |
| 3–4. Validar código SMS             | `controller.validarCodigoSMS()`                       |
| 5–6. Buscar e selecionar plano      | `controller.listarPlanos()` + `selecionarPlano()`     |
| 7–9. Escolher frutas                | `controller.listarProdutosPorTipo(FRUTA)` + `confirmarItensCesta()` |
| 10–12. Escolher legumes             | `controller.listarProdutosPorTipo(LEGUME)` + `confirmarItensCesta()` |
| 13–15. Escolher verduras            | `controller.listarProdutosPorTipo(VERDURA)` + `confirmarItensCesta()` |
| 16–18. Endereço + status AGUARDANDO | `controller.informarEnderecoEntrega()`                |
| 19–22. Pagamento + aprovação        | `controller.processarPagamento()`                     |
| 23–25. Protocolo + confirmação      | `assinaturaService.finalizarAssinatura()`             |

---

## ▶️ Como executar

**Pré-requisito:** Java 17+

```bash
# Compilar
find src -name "*.java" > fontes.txt
javac -d out @fontes.txt

# Executar
java -cp out br.com.feiranossa.Main
```

Ou via IDE (IntelliJ / Eclipse / VS Code): execute a classe `Main.java`.

---

## 💳 Simulação de Pagamento

- Qualquer cartão com dados válidos é **aprovado** pela operadora simulada.
- Para testar **rejeição**: informe um número de cartão terminado em `0000` (ex: `1234 5678 9012 0000`).

---

## 📁 Persistência CSV

Todos os dados são persistidos em arquivos `.csv` na pasta `data/`. Cada campo é separado por `;`. Os arquivos são criados automaticamente na primeira execução.

---

## 🗂️ Wiki e Diagramas UML

Consulte a [Wiki do projeto](../../wiki) para:
- Diagrama de Sequência completo
- Diagrama de Classes de Projeto
- Diagrama de Classes Candidatas
- Protótipo de telas
- Vídeo de demonstração
