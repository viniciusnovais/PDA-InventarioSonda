package com.pda.inventario.entityObject;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class DepartamentoEO implements KvmSerializable{
    
	private long IdInventario;
    private long IdDepartamento;
    private long IdMetodoContagem;
    private long IdMetodoAuditoria;
    private long IdMetodoLeitura;
    private String Departamento;
    private String MetodoContagem;
    private String MetodoAuditoria;
    private Double Quantidade;
	
	public DepartamentoEO(){}
	
	public DepartamentoEO(long IdInventario, long IdDepartamento, long IdMetodoContagem, long IdMetodoAuditoria, long IdMetodoLeitura, String Departamento, String MetodoContagem, String MetodoAuditoria, Double quantidade){
		IdInventario = IdInventario;
		IdDepartamento = IdDepartamento;
		IdMetodoContagem = IdMetodoContagem;
		IdMetodoAuditoria = IdMetodoAuditoria;
		IdMetodoLeitura = IdMetodoLeitura;
		Departamento = Departamento;
		MetodoContagem = MetodoContagem;
		MetodoAuditoria = MetodoAuditoria;
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
			Quantidade = Double.parseDouble(value.toString());
			break;
		default:
			break;
		}
	}
}
