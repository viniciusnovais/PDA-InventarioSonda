package com.pda.inventario.businessComponent;

import java.util.Date;
import java.util.List;

import com.pda.inventario.entityObject.DepartamentoColetorEO;
import com.pda.inventario.entityObject.DepartamentoEO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DepartamentoBC {
	private SQLiteDatabase bd;
	private DbOpenHelper openHelper;

	public DepartamentoBC(Context context){
		openHelper = new DbOpenHelper(context);
	}

	public void OpenConnection(){
		bd = openHelper.getWritableDatabase();
	}

	public void CloseConnection() {
		bd.close();
	}
	
	public void CreateDepartamentoList(List<DepartamentoEO> objDepartamentoList){
		try{
			this.DeleteDepartamento();
			this.OpenConnection();
			for (int i = 0; i < objDepartamentoList.size(); i++) {				
				
				ContentValues values = new ContentValues();
				values.put("DESCRICAO", objDepartamentoList.get(i).getProperty(5).toString());
				values.put("METODO_CONTAGEM", objDepartamentoList.get(i).getProperty(2).toString());
				values.put("DESC_METODO_CONTAGEM", objDepartamentoList.get(i).getProperty(6).toString());
				values.put("METODO_AUDITORIA", objDepartamentoList.get(i).getProperty(3).toString());
				values.put("DESC_METODO_AUDITORIA", objDepartamentoList.get(i).getProperty(7).toString());
				values.put("QTDE_MAX_MULTIPLOS", objDepartamentoList.get(i).getProperty(8).toString());
				values.put("ID_INVENTARIO", objDepartamentoList.get(i).getProperty(0).toString());
				values.put("METODO_LEITURA", objDepartamentoList.get(i).getProperty(4).toString());		
				values.put("DESC_METODO_LEITURA", objDepartamentoList.get(i).getProperty(4).toString());		
				bd.insert("PDA_TB_DEPARTAMENTO", null, values);	
			}			
		}
		finally{
			this.CloseConnection();
		}		
	}
	
	public void CreateDepartamentoColetorList(List<DepartamentoColetorEO> objDepartamentoList){
		try{
			this.DeleteDepartamento();
			this.OpenConnection();
			for (int i = 0; i < objDepartamentoList.size(); i++) {				
				
				ContentValues values = new ContentValues();
				values.put("DESCRICAO", objDepartamentoList.get(i).getProperty(5).toString());
				values.put("METODO_CONTAGEM", objDepartamentoList.get(i).getProperty(2).toString());
				values.put("DESC_METODO_CONTAGEM", objDepartamentoList.get(i).getProperty(6).toString());
				values.put("METODO_AUDITORIA", objDepartamentoList.get(i).getProperty(3).toString());
				values.put("DESC_METODO_AUDITORIA", objDepartamentoList.get(i).getProperty(7).toString());
				values.put("QTDE_MAX_MULTIPLOS", objDepartamentoList.get(i).getProperty(8).toString());
				values.put("ID_INVENTARIO", objDepartamentoList.get(i).getProperty(0).toString());
				values.put("METODO_LEITURA", objDepartamentoList.get(i).getProperty(4).toString());		
				values.put("DESC_METODO_LEITURA", objDepartamentoList.get(i).getProperty(4).toString());		
				bd.insert("PDA_TB_DEPARTAMENTO", null, values);	
			}			
		}
		finally{
			this.CloseConnection();
		}		
	}
	
	public int DeleteDepartamento(){
		try{
			this.OpenConnection();
			return bd.delete("PDA_TB_DEPARTAMENTO", null, null);	
		}
		finally{
			this.CloseConnection();
		}			
	}
}