package com.pda.inventario.businessComponent;

import java.util.List;

import com.pda.inventario.entityObject.SetorColetorEO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SetorBC {
	private SQLiteDatabase bd;
	private DbOpenHelper openHelper;

	public SetorBC(Context context){
		openHelper = new DbOpenHelper(context);
	}

	public void OpenConnection(){
		bd = openHelper.getWritableDatabase();
	}

	public void CloseConnection() {
		bd.close();
	}
	
	public void CreateSetorColetorList(List<SetorColetorEO> objSetorList){
		try{
			this.DeleteSetor();
			this.OpenConnection();
			for (int i = 0; i < objSetorList.size(); i++) {				
				
				ContentValues values = new ContentValues();
				values.put("DESCRICAO", objSetorList.get(i).Setor);
				values.put("METODO_CONTAGEM", objSetorList.get(i).IdMetodoContagem);
				values.put("DESC_METODO_CONTAGEM", objSetorList.get(i).MetodoContagem);
				values.put("METODO_AUDITORIA", objSetorList.get(i).IdMetodoAuditoria);
				values.put("DESC_METODO_AUDITORIA", objSetorList.get(i).MetodoAuditoria);
				values.put("QTDE_MAX_MULTIPLOS", objSetorList.get(i).Quantidade);
				values.put("ID_INVENTARIO", objSetorList.get(i).IdInventario);
				values.put("METODO_LEITURA", objSetorList.get(i).IdMetodoLeitura);		
				values.put("DESC_METODO_LEITURA", objSetorList.get(i).MetodoLeitura);		
				bd.insert("PDA_TB_SETOR", null, values);	
			}			
		}
		finally{
			this.CloseConnection();
		}		
	}
	
	public int DeleteSetor(){
		try{
			this.OpenConnection();
			return bd.delete("PDA_TB_SETOR", null, null);	
		}
		finally{
			this.CloseConnection();
		}			
	}
}