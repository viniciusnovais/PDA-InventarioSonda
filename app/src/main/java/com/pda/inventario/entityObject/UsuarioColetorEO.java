package com.pda.inventario.entityObject;

public class UsuarioColetorEO {
	private int Codigo;
	private String Login;
	private int Lider;
	private byte[] Senha;
	
	public UsuarioColetorEO(){}


	public UsuarioColetorEO(int codigo, String login, int lider, byte[] senha) {

		Codigo = codigo;
		Login = login;
		Lider = lider;
		Senha = senha;
	}

	public int getCodigo() {
		return Codigo;
	}
	public void setCodigo(int codigo) {
		Codigo = codigo;
	}

	public String getLogin() {
		return Login;
	}
	public void setLogin(String login) {
		Login = login;
	}

	public int getLider() {
		return Lider;
	}
	public void setLider(int lider) {
		Lider = lider;
	}

	public byte[] getSenha() {
		return Senha;
	}
	public void setSenha(byte[] senha) {
		Senha = senha;
	}
}
