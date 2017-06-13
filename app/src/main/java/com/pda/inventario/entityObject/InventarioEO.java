package com.pda.inventario.entityObject;

import java.io.Serializable;

public class InventarioEO implements Serializable {
	private int IdInventario;
	private int CodigoFilial;
	private String Filial;
	private String	Autorizacao;
	private int Status;	

	public InventarioEO(){}

	public InventarioEO(int idInventario, int codigoFilial, String filial, String autorizacao, int status) {

		IdInventario = idInventario;
		CodigoFilial = codigoFilial;
		Filial = filial;
		Autorizacao = autorizacao;
		Status = status;
	}

	public int getIdInventario() {
		return IdInventario;
	}

	public void setIdInventario(int idInventario) {
		IdInventario = idInventario;
	}

	public int getCodigoFilial() {
		return CodigoFilial;
	}

	public void setCodigoFilial(int codigoFilial) {
		CodigoFilial = codigoFilial;
	}

	public String getFilial() {
		return Filial;
	}

	public void setFilial(String filial) {
		Filial = filial;
	}

	public String getAutorizacao() {
		return Autorizacao;
	}

	public void setAutorizacao(String autorizacao) {
		Autorizacao = autorizacao;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}
}
