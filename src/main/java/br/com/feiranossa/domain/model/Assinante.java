package br.com.feiranossa.domain.model;

public class Assinante {

    private int    id;
    private String nome;
    private String email;
    private String celular;

    public Assinante() {}

    public Assinante(int id, String nome, String email, String celular) {
        this.id      = id;
        this.nome    = nome;
        this.email   = email;
        this.celular = celular;
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public int    getId()      { return id; }
    public void   setId(int id){ this.id = id; }

    public String getNome()              { return nome; }
    public void   setNome(String nome)   { this.nome = nome; }

    public String getEmail()             { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getCelular()               { return celular; }
    public void   setCelular(String celular) { this.celular = celular; }

    /** Validação simples: celular deve ter pelo menos 10 dígitos numéricos. */
    public boolean validarCelular() {
        if (celular == null) return false;
        String digits = celular.replaceAll("[^0-9]", "");
        return digits.length() >= 10;
    }

    /** Serializa para linha CSV:  id;nome;email;celular  */
    public String toCsv() {
        return id + ";" + nome + ";" + email + ";" + celular;
    }

    /** Reconstrói a partir de uma linha CSV. */
    public static Assinante fromCsv(String linha) {
        String[] p = linha.split(";", -1);
        return new Assinante(Integer.parseInt(p[0]), p[1], p[2], p[3]);
    }

    @Override
    public String toString() {
        return "Assinante{id=" + id + ", nome='" + nome + "', celular='" + celular + "'}";
    }
}
