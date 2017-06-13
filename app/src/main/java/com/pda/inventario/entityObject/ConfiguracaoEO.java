package com.pda.inventario.entityObject;

public class ConfiguracaoEO {
	private String Servidor;
	private String DiretorioVirtual;
	private String Filial;
	
	public ConfiguracaoEO(){
		Servidor = "";
		DiretorioVirtual = "";
		Filial = "";
	}

	public ConfiguracaoEO(String servidor, String diretorioVirtual, String filial) {

		Servidor = servidor;
		DiretorioVirtual = diretorioVirtual;
		Filial = filial;
	}

	public String getServidor() {
		return Servidor;
	}

	public void setServidor(String servidor) {
		Servidor = servidor;
	}

	public String getDiretorioVirtual() {
		return DiretorioVirtual;
	}

	public void setDiretorioVirtual(String diretorioVirtual) {
		DiretorioVirtual = diretorioVirtual;
	}

	public String getFilial() {
		return Filial;
	}

	public void setFilial(String filial) {
		Filial = filial;
	}
}
