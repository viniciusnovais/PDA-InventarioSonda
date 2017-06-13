package com.pda.inventario.entityObject;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ControleArquivoEO implements KvmSerializable{
    public int CodigoInventario;
    public String NomeArquivo;
    
    public ControleArquivoEO(){
    	CodigoInventario = 0;
    	NomeArquivo = "";
	}
	
	public ControleArquivoEO(int codigoInventario, String nomeArquivo){
    	CodigoInventario = codigoInventario;
    	NomeArquivo = nomeArquivo;
	}
	
	public Object getProperty(int arg0) {

		switch(arg0)
		{
		case 0:
			return CodigoInventario;
		case 1:
			return NomeArquivo;			
		}
		return null;
	}

	public int getPropertyCount() {
		return 2;
	}

	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch(index)
		{
		case 0:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "CodigoInventario";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "NomeArquivo";
			break;
		default:break;
		}
	}

	public void setProperty(int index, Object value) {
		switch(index)
		{
		case 0:
			CodigoInventario = Integer.parseInt(value.toString());
			break;
		case 1:
			NomeArquivo = value.toString();
			break;
		default:
			break;
		}
	}
}
