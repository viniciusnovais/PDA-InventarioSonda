package com.pda.inventario.entityObject;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class DepartamentoColetorEO implements KvmSerializable{
    
	public int IdInventario;
    public int IdDepartamento;
    public int IdMetodoContagem;
    public int IdMetodoAuditoria;
    public int IdMetodoLeitura;
    public String Departamento;
    public String MetodoContagem;
    public String MetodoAuditoria;
    public int Quantidade;
	
    public DepartamentoColetorEO(){
		IdInventario = 0;
		IdDepartamento = 0;
		IdMetodoContagem = 0;
		IdMetodoAuditoria = 0;
		IdMetodoLeitura = 0;
		Departamento = "";
		MetodoContagem = "";
		MetodoAuditoria = "";
		Quantidade = 0;
	}
	
	public DepartamentoColetorEO(int codInventario, int codDepartamento, int codMetodoContagem, int codMetodoAuditoria, int codMetodoLeitura, String descDepartamento, String descMetodoContagem, String descMetodoAuditoria, int quantidade){
		IdInventario = codInventario;
		IdDepartamento = codDepartamento;
		IdMetodoContagem = codMetodoContagem;
		IdMetodoAuditoria = codMetodoAuditoria;
		IdMetodoLeitura = codMetodoLeitura;
		Departamento = descDepartamento;
		MetodoContagem = descMetodoContagem;
		MetodoAuditoria = descMetodoAuditoria;
		Quantidade = quantidade;
	}
	
	public Object getProperty(int arg0) {

		switch(arg0)
		{
		case 0:
			return IdInventario;
		case 1:
			return IdDepartamento;
		case 2:
			return IdMetodoContagem;
		case 3:
			return IdMetodoAuditoria;
		case 4:
			return IdMetodoLeitura;
		case 5:
			return Departamento;
		case 6:
			return MetodoContagem;
		case 7:
			return MetodoAuditoria;
		case 8:
			return Quantidade;
			
		}
		return null;
	}

	public int getPropertyCount() {
		return 9;
	}

	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch(index)
		{
		case 0:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "IdInventario";
			break;
		case 1:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "IdDepartamento";
			break;
		case 2:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "IdMetodoContagem";
			break;
		case 3:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "IdMetodoAuditoria";
			break;
		case 4:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "IdMetodoLeitura";
			break;
		case 5:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Departamento";
			break;
		case 6:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MetodoContagem";
			break;
		case 7:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MetodoAuditoria";
			break;
		case 8:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "Quantidade";
			break;
		default:break;
		}
	}

	public void setProperty(int index, Object value) {
		switch(index)
		{
		case 0:
			IdInventario = Integer.parseInt(value.toString());
			break;
		case 1:
			IdDepartamento = Integer.parseInt(value.toString());
			break;
		case 2:
			IdMetodoContagem = Integer.parseInt(value.toString());
			break;
		case 3:
			IdMetodoAuditoria = Integer.parseInt(value.toString());
			break;
		case 4:
			IdMetodoLeitura = Integer.parseInt(value.toString());
			break;
		case 5:
			Departamento = value.toString();
			break;
		case 6:
			MetodoContagem = value.toString();
			break;
		case 7:
			MetodoAuditoria = value.toString();
			break;
		case 8:
			Quantidade = Integer.parseInt(value.toString());
			break;
		default:
			break;
		}
	}
}
