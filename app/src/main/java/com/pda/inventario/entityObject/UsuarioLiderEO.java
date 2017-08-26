package com.pda.inventario.entityObject;

import java.io.Serializable;

public class UsuarioLiderEO implements Serializable {

	private String login;
	private String senha;

	public UsuarioLiderEO() {
	}

	public UsuarioLiderEO(String login, String senha) {
		this.login = login;
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
