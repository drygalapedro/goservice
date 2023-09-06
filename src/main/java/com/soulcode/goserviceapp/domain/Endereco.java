package com.soulcode.goserviceapp.domain;

import jakarta.persistence.*;

@Entity
@Table (name= "enderecos")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usuarios")
    private Usuario usuario;


    private String logradouro;
    private String numero;
    private String cidade;
    private String uf;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Endereco(Long id, String logradouro, String numero, String cidade, String uf ) {
        this.id= id;
        this.logradouro= logradouro;
        this.numero= numero;
        this.cidade= cidade;
        this.uf= uf;

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

}
