package com.pda.inventario.businessComponent;

import com.pda.inventario.StringUtils;
import com.pda.inventario.entityObject.ConfiguracaoEO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ConfiguracaoBC {
	private SQLiteDatabase bd;
	private DbOpenHelper openHelper;

	public ConfiguracaoBC(Context context){
		openHelper = new DbOpenHelper(context);
	}

	public void openConnection(){
		bd = openHelper.getWritableDatabase();
	}

	public void closeConnection() {
		bd.close();
	}

	public long createConfiguracao(ConfiguracaoEO objConfig){
		try{
			ContentValues values = new ContentValues();
			values.put("SERVIDOR", objConfig.getServidor());
			values.put("DIRETORIO_VIRTUAL", objConfig.getDiretorioVirtual());
			values.put("FILIAL", objConfig.getFilial());
			
			this.openConnection();	
			return bd.insert("PDA_TB_CONFIGURACAO", null, values);
		}
		finally{
			this.closeConnection();
		}		
	}

	public int updateConfiguracao(ConfiguracaoEO objConfig){
		try{
			ContentValues values = new ContentValues();
			values.put("SERVIDOR", objConfig.getServidor());
			values.put("DIRETORIO_VIRTUAL", objConfig.getDiretorioVirtual());
			values.put("FILIAL", objConfig.getFilial());

			this.openConnection();
			return bd.update("PDA_TB_CONFIGURACAO", values, null, null);	
		}
		finally{
			this.closeConnection();
		}			
	}

	private ConfiguracaoEO getConfiguracao(){
		try{
			this.openConnection();

			ConfiguracaoEO objConfig = new ConfiguracaoEO();
			Cursor cursor = bd.query("PDA_TB_CONFIGURACAO", null, null ,null, null, null, null);
			while(cursor.moveToNext()){				
				objConfig.setServidor(cursor.getString(0));
				objConfig.setDiretorioVirtual(cursor.getString(1));
				objConfig.setFilial(cursor.getString(2));
			}
			cursor.close(); 

			return objConfig;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			this.closeConnection();
		}
	}

	public boolean VerificaConfig(){
		try {
			ConfiguracaoEO objConfig;
			objConfig = this.getConfiguracao();

			if (objConfig != null){
				if (objConfig.getServidor().equals("")){
					return false;
				}else{
					StringUtils.SERVIDOR = objConfig.getServidor();
					StringUtils.DIRETORIO_VIRTUAL = objConfig.getDiretorioVirtual();
					StringUtils.FILIAL = objConfig.getFilial();
					return true;
				}
			}else{
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			this.closeConnection();
		}

	}
}
