package com.pda.inventario.entityObject;

import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class InventarioColetorEO implements KvmSerializable {
	public int IdInventario;
	public int CodigoFilial;
	public String Filial;
	public String	Autorizacao;
	public int Status;	

	public InventarioColetorEO(){}


	public InventarioColetorEO(int idInventario, int codigoFilial, String filial, String autorizacao, int status) {

		IdInventario = idInventario;
		CodigoFilial = codigoFilial;
		Filial = filial;
		Autorizacao = autorizacao;
		Status = status;
	}

	public Object getProperty(int arg0) {

		switch(arg0)
		{
		case 0:
			return IdInventario;
		case 1:
			return CodigoFilial;
		case 2:
			return Filial;
		case 3:
			return Autorizacao;
		case 4:
			return Status;
		}

		return null;
	}

	public int getPropertyCount() {
		return 5;
	}

	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch(index)
		{
		case 0:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "IdInventario";
			break;
		case 1:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "CodigoFilial";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Filial";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Autorizacao";
			break;
		case 4:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Status";
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
			CodigoFilial = Integer.parseInt(value.toString());
			break;
		case 2:
			Filial = value.toString();
			break;
		case 3:
			Autorizacao = value.toString();
			break;
		case 4:
			Status = Integer.parseInt(value.toString());
			break;
		default:
			break;
		}
	}
}
