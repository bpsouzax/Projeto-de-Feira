package br.com.feiranossa.domain.model;

public class EnderecoEntrega {

    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String diaSemana;
    private String turno;

    public EnderecoEntrega() {}

    public EnderecoEntrega(String cep, String rua, String numero,
                           String complemento, String bairro,
                           String diaSemana, String turno) {
        this.cep          = cep;
        this.rua          = rua;
        this.numero       = numero;
        this.complemento  = complemento;
        this.bairro       = bairro;
        this.diaSemana    = diaSemana;
        this.turno        = turno;
    }

    public boolean validarCEP() {
        return cep != null && cep.replaceAll("[^0-9]", "").length() == 8;
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public String getCep()               { return cep; }
    public void   setCep(String cep)     { this.cep = cep; }

    public String getRua()               { return rua; }
    public void   setRua(String rua)     { this.rua = rua; }

    public String getNumero()            { return numero; }
    public void   setNumero(String n)    { this.numero = n; }

    public String getComplemento()           { return complemento; }
    public void   setComplemento(String c)   { this.complemento = c; }

    public String getBairro()            { return bairro; }
    public void   setBairro(String b)    { this.bairro = b; }

    public String getDiaSemana()         { return diaSemana; }
    public void   setDiaSemana(String d) { this.diaSemana = d; }

    public String getTurno()             { return turno; }
    public void   setTurno(String t)     { this.turno = t; }

    /** CSV: cep;rua;numero;complemento;bairro;diaSemana;turno */
    public String toCsv() {
        return cep + ";" + rua + ";" + numero + ";" + complemento + ";" + bairro + ";" + diaSemana + ";" + turno;
    }

    public static EnderecoEntrega fromCsv(String linha) {
        String[] p = linha.split(";", -1);
        return new EnderecoEntrega(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
    }

    @Override
    public String toString() {
        return String.format("%s, %s %s – %s | Entrega: %s (%s)", rua, numero, complemento, bairro, diaSemana, turno);
    }
}
