package com.pda.inventario.entityObject;

import java.io.Serializable;

public class UsuarioEO implements Serializable {
	private int Codigo;
	private String Nome;
	private int CodigoPerfil;
	private String DescricaoPerfil;
	private int CodigoFilial;
	private String NomeFilial;
	private int CodigoCliente;
	private int Ativo;
	private int StatusSenha;
	
	public UsuarioEO(){}


	public UsuarioEO(int codigo, String nome, int codigoPerfil, String descricaoPerfil, int codigoFilial, String nomeFilial, int codigoCliente, int ativo, int statusSenha) {

		Codigo = codigo;
		Nome = nome;
		CodigoPerfil = codigoPerfil;
		DescricaoPerfil = descricaoPerfil;
		CodigoFilial = codigoFilial;
		NomeFilial = nomeFilial;
		CodigoCliente = codigoCliente;
		Ativo = ativo;
		StatusSenha = statusSenha;
	}

	public int getCodigo() {
		return Codigo;
	}
	public void setCodigo(int codigo) {
		Codigo = codigo;
	}

	public String getNome() {
		return Nome;
	}
	public void setNome(String nome) {
		Nome = nome;
	}

	public int getCodigoPerfil() {
		return CodigoPerfil;
	}
	public void setCodigoPerfil(int codigoPerfil) {
		CodigoPerfil = codigoPerfil;
	}

	public String getDescricaoPerfil() {
		return DescricaoPerfil;
	}
	public void setDescricaoPerfil(String descricaoPerfil) {
		DescricaoPerfil = descricaoPerfil;
	}

	public int getCodigoFilial() {
		return CodigoFilial;
	}
	public void setCodigoFilial(int codigoFilial) {
		CodigoFilial = codigoFilial;
	}

	public String getNomeFilial() {
		return NomeFilial;
	}
	public void setNomeFilial(String nomeFilial) {
		NomeFilial = nomeFilial;
	}

	public int getCodigoCliente() {
		return CodigoCliente;
	}
	public void setCodigoCliente(int codigoCliente) {
		CodigoCliente = codigoCliente;
	}

	public int getAtivo() {
		return Ativo;
	}
	public void setAtivo(int ativo) {
		Ativo = ativo;
	}

	public int getStatusSenha() {
		return StatusSenha;
	}
	public void setStatusSenha(int statusSenha) {
	}
}
