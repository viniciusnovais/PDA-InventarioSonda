package com.pda.inventario.businessComponent;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pda.inventario.entityObject.ColetaEO;
import com.pda.inventario.entityObject.UsuarioColetorEO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsuarioColetorBC {

	private SQLiteDatabase bd;
	private DbOpenHelper openHelper;

	public UsuarioColetorBC(Context context){
		openHelper = new DbOpenHelper(context);
	}

	public void OpenConnection(){
		bd = openHelper.getWritableDatabase();
	}

	public void CloseConnection() {
		bd.close();
	}

	public void CreateUsuario(UsuarioColetorEO objUsuario){
		try{
			this.DeleteUsuario();
			this.OpenConnection();
			ContentValues values = new ContentValues();
			values.put("NOME", objUsuario.getLogin());
			values.put("TIPO", objUsuario.getLider());
			values.put("LOGIN", objUsuario.getLogin());
			values.put("PASSWORD", objUsuario.getSenha());
			values.put("DATA_ACESSO", new Date().toString());
			
			bd.insert("PDA_TB_USUARIO", null, values);
		}
		finally{
			this.CloseConnection();
		}		
	}
	
	public void CreateUsuarioList(List<UsuarioColetorEO> objUsuarioList){
		try{
			this.DeleteUsuario();
			this.OpenConnection();
			Date data = new Date();
			for (int i = 0; i < objUsuarioList.size(); i++) {				
				
				ContentValues values = new ContentValues();
				values.put("ID_USUARIO", objUsuarioList.get(i).getCodigo());
				values.put("NOME", objUsuarioList.get(i).getLogin());
				values.put("TIPO", objUsuarioList.get(i).getLider());
				values.put("LOGIN", objUsuarioList.get(i).getLogin());
				values.put("PASSWORD", objUsuarioList.get(i).getSenha());
				values.put("DATA_ACESSO", data.toString());				
				bd.insert("PDA_TB_USUARIO", null, values);	
			}			
		}
		finally{
			this.CloseConnection();
		}		
	}

	public int DeleteUsuario(){
		try{
			this.OpenConnection();
			return bd.delete("PDA_TB_USUARIO", null, null);	
		}
		finally{
			this.CloseConnection();
		}			
	}
	
	public UsuarioColetorEO GetUsuario(String idUser){
		try{
			this.OpenConnection();
			
			UsuarioColetorEO objUser = new UsuarioColetorEO();
			String[] argsWhere = {idUser};
			Cursor cursor = bd.query("PDA_TB_USUARIO", null, "ID_USUARIO = ?" ,argsWhere, null, null, null);
			while(cursor.moveToNext()){
				objUser.setCodigo(cursor.getInt(0));
				objUser.setLogin(cursor.getString(3));
				objUser.setSenha(cursor.getBlob(4));
				objUser.setLider(cursor.getInt(2));												
			}
			cursor.close(); 

			return objUser;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			this.CloseConnection();
		}
	}
}
