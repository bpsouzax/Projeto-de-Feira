package br.com.feiranossa.domain.model;

public class CartaoCredito {

    private String numero;
    private String titular;
    private String validade;
    private String bandeira;

    public CartaoCredito() {}

    public CartaoCredito(String numero, String titular, String validade, String bandeira) {
        this.numero   = numero;
        this.titular  = titular;
        this.validade = validade;
        this.bandeira = bandeira;
    }

    public boolean validarDados() {
        if (numero == null || titular == null || validade == null) return false;
        String digits = numero.replaceAll("[^0-9]", "");
        return digits.length() >= 13 && digits.length() <= 19
                && validade.matches("\\d{2}/\\d{2}");
    }

    /** Retorna número mascarado: **** **** **** 4321 */
    public String mascararNumero() {
        String digits = numero.replaceAll("[^0-9]", "");
        if (digits.length() < 4) return "****";
        return "**** **** **** " + digits.substring(digits.length() - 4);
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public String getNumero()             { return numero; }
    public void   setNumero(String n)     { this.numero = n; }

    public String getTitular()            { return titular; }
    public void   setTitular(String t)    { this.titular = t; }

    public String getValidade()           { return validade; }
    public void   setValidade(String v)   { this.validade = v; }

    public String getBandeira()           { return bandeira; }
    public void   setBandeira(String b)   { this.bandeira = b; }

    @Override
    public String toString() {
        return String.format("Cartão %s | %s | Validade: %s", bandeira, mascararNumero(), validade);
    }
}
