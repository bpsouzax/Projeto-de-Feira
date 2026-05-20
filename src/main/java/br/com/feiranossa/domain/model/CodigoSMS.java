package br.com.feiranossa.domain.model;

import java.time.LocalDateTime;

public class CodigoSMS {

    private String        celular;
    private String        codigo;
    private LocalDateTime expiracao;
    private boolean       validado;

    public CodigoSMS() {}

    public CodigoSMS(String celular, String codigo) {
        this.celular   = celular;
        this.codigo    = codigo;
        this.expiracao = LocalDateTime.now().plusMinutes(5);
        this.validado  = false;
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(expiracao);
    }

    public boolean validar(String codigoInformado) {
        if (isExpirado()) return false;
        if (this.codigo.equals(codigoInformado)) {
            this.validado = true;
            return true;
        }
        return false;
    }

    // ── getters / setters ──────────────────────────────────────────────────
    public String        getCelular()           { return celular; }
    public void          setCelular(String c)   { this.celular = c; }

    public String        getCodigo()            { return codigo; }
    public void          setCodigo(String c)    { this.codigo = c; }

    public LocalDateTime getExpiracao()         { return expiracao; }
    public void          setExpiracao(LocalDateTime e){ this.expiracao = e; }

    public boolean       isValidado()           { return validado; }
    public void          setValidado(boolean v) { this.validado = v; }
}
