package com.pda.inventario.entityObject;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class SetorColetorEO implements KvmSerializable{
    public int IdInventario ;
    public int IdDepartamento ;
    public int IdSetor ;
    public int IdMetodoContagem ;
    public int IdMetodoAuditoria ;
    public int IdMetodoLeitura ;
    public String Setor ;
    public String MetodoContagem ;
    public String MetodoAuditoria ;
    public String MetodoLeitura ;
    public int Quantidade ;
    
    public SetorColetorEO(){
    	IdInventario = 0;
        IdDepartamento = 0;
        IdSetor = 0;
        IdMetodoContagem = 0;
        IdMetodoAuditoria = 0;
        IdMetodoLeitura = 0;
        Setor = "";
        MetodoContagem = "";
        MetodoAuditoria = "";
        MetodoLeitura = "";
        Quantidade = 0;
    }
    
    public SetorColetorEO(int idInventario, int idDepartamento, int idSetor, int idMetodoContagem, int idMetodoAuditoria, int idMetodoLeitura, String setor, String metodoContagem, String metodoAuditoria, String metodoLeitura, int quantidade){
    	IdInventario = idInventario;
        IdDepartamento = idDepartamento;
        IdSetor = idSetor;
        IdMetodoContagem = idMetodoContagem;
        IdMetodoAuditoria = idMetodoAuditoria;
        IdMetodoLeitura = idMetodoLeitura;
        Setor = setor;
        MetodoContagem = metodoContagem;
        MetodoAuditoria = metodoAuditoria;
        MetodoLeitura = metodoLeitura;
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
			return IdSetor;
		case 3:
			return IdMetodoContagem;
		case 4:
			return IdMetodoAuditoria;
		case 5:
			return IdMetodoLeitura;
		case 6:
			return Setor;
		case 7:
			return MetodoContagem;
		case 8:
			return MetodoAuditoria;
		case 9:
			return MetodoLeitura;
		case 10:
			return Quantidade;
			
		}
		return null;
	}

	public int getPropertyCount() {
		return 11;
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
			info.name = "IdSetor";
			break;
		case 3:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "IdMetodoContagem";
			break;
		case 4:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "IdMetodoAuditoria";
			break;
		case 5:
			info.type = PropertyInfo.LONG_CLASS;
			info.name = "IdMetodoLeitura";
			break;
		case 6:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Setor";
			break;
		case 7:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MetodoContagem";
			break;
		case 8:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MetodoAuditoria";
			break;
		case 9:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "MetodoLeitura";
			break;
		case 10:
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
			IdSetor = Integer.parseInt(value.toString());
			break;
		case 3:
			IdMetodoContagem = Integer.parseInt(value.toString());
			break;
		case 4:
			IdMetodoAuditoria = Integer.parseInt(value.toString());
			break;
		case 5:
			IdMetodoLeitura = Integer.parseInt(value.toString());
			break;
		case 6:
			Setor = value.toString();
			break;
		case 7:
			MetodoContagem = value.toString();
			break;
		case 8:
			MetodoAuditoria = value.toString();
			break;
		case 9:
			MetodoLeitura = value.toString();
			break;
		case 10:
			Quantidade = Integer.parseInt(value.toString());
			break;
		default:
			break;
		}
	}
}
