package com.pda.inventario.businessComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.pda.inventario.entityObject.EnderecoColetorEO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;

public class EnderecoBC {
    private SQLiteDatabase bd;
    private DbOpenHelper openHelper;

    public EnderecoBC(Context context) {
        openHelper = new DbOpenHelper(context);
    }

    public void OpenConnection() {
        bd = openHelper.getWritableDatabase();
    }

    public void CloseConnection() {
        bd.close();
    }

    public void CreateEnderecoList(List<EnderecoColetorEO> objEnderecoList) {
        try {
            this.DeleteEndereco();
            this.OpenConnection();
            for (int i = 0; i < objEnderecoList.size(); i++) {

                ContentValues values = new ContentValues();
                values.put("ID_ENDERECO", objEnderecoList.get(i).IdEndereco);
                values.put("COD_ENDERECO", objEnderecoList.get(i).Endereco.trim());
                values.put("ID_SETOR", objEnderecoList.get(i).IdSetor);
                values.put("DESC_SETOR", objEnderecoList.get(i).Setor);
                values.put("ID_DEPATARMENTO", objEnderecoList.get(i).IdDepartamento);
                values.put("DESC_DEPARTAMENTO", objEnderecoList.get(i).Departamento);
                values.put("METODO_CONTAGEM", objEnderecoList.get(i).IdMetodoContagem);
                values.put("METODO_AUDITORIA", objEnderecoList.get(i).IdMetodoAuditoria);
                values.put("METODO_LEITURA", objEnderecoList.get(i).MetodoLeitura);
                values.put("ID_INVENTARIO", objEnderecoList.get(i).IdInventario);
                values.put("DESC_METODO_CONTAGEM", objEnderecoList.get(i).MetodoContagem);
                values.put("DESC_METODO_AUDITORIA", objEnderecoList.get(i).MetodoAuditoria);
                values.put("DESC_METODO_LEITURA", objEnderecoList.get(i).IdMetodoLeitura);
                values.put("QTDE_MAX_MULTIPLOS", objEnderecoList.get(i).Quantidade);
                values.put("FLAG_VERIFICA_ENDERECO_FECHADO", -1);

                bd.insert("PDA_TB_ENDERECO", null, values);
            }
        } finally {
            this.CloseConnection();
        }
    }

    public EnderecoColetorEO GetEnderecoByCode(String codeEndereco) {
        try {
            this.OpenConnection();

            EnderecoColetorEO objEndereco = new EnderecoColetorEO();
            String[] args = {codeEndereco};
            Cursor cursor = bd.query("PDA_TB_ENDERECO", null, "COD_ENDERECO = ?", args, null, null, null);
            while (cursor.moveToNext()) {
                objEndereco.IdEndereco = cursor.getString(cursor.getColumnIndex("ID_ENDERECO"));
                objEndereco.Endereco = cursor.getString(cursor.getColumnIndex("COD_ENDERECO"));
                objEndereco.IdSetor = cursor.getInt(1);
                objEndereco.Setor = cursor.getString(cursor.getColumnIndex("DESC_SETOR"));
                objEndereco.IdMetodoContagem = cursor.getInt(4);
                objEndereco.MetodoContagem = cursor.getString(cursor.getColumnIndex("METODO_CONTAGEM"));
                objEndereco.IdMetodoAuditoria = cursor.getInt(4);
                objEndereco.MetodoAuditoria = cursor.getString(cursor.getColumnIndex("METODO_AUDITORIA"));
                objEndereco.IdDepartamento = cursor.getInt(1);
                objEndereco.Departamento = cursor.getString(cursor.getColumnIndex("DESC_DEPARTAMENTO"));
                objEndereco.Quantidade = cursor.getInt(cursor.getColumnIndex("QTDE_MAX_MULTIPLOS"));
                objEndereco.IdInventario = cursor.getInt(cursor.getColumnIndex("ID_INVENTARIO"));
                objEndereco.IdMetodoLeitura = cursor.getInt(1);
                objEndereco.MetodoLeitura = cursor.getString(cursor.getColumnIndex("METODO_LEITURA"));
            }
            cursor.close();

            return objEndereco;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public EnderecoColetorEO VerificaEnderecoFechado(String codeEndereco) {
        try {
            this.OpenConnection();

            EnderecoColetorEO objEndereco = new EnderecoColetorEO();
            String[] args = {codeEndereco}, coluna = {"FLAG_VERIFICA_ENDERECO_FECHADO"};
            Cursor cursor = bd.query("PDA_TB_ENDERECO",coluna, "COD_ENDERECO = ?", args, null, null, null);
            while (cursor.moveToNext()) {

                objEndereco.verificaEnderecoFechado = cursor.getInt(cursor.getColumnIndex("FLAG_VERIFICA_ENDERECO_FECHADO"));
            }
            cursor.close();

            return objEndereco;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public int DeleteEndereco() {
        try {
            this.OpenConnection();
            return bd.delete("PDA_TB_ENDERECO", null, null);
        } finally {
            this.CloseConnection();
        }
    }

    public long UpdateFlagFechaEndereco(String codeEndereco) {
        try {
            this.OpenConnection();
            String[] args = {codeEndereco};
            ContentValues values = new ContentValues();
            values.put("FLAG_VERIFICA_ENDERECO_FECHADO", 1);
            return bd.update("PDA_TB_ENDERECO", values, "COD_ENDERECO = ?", args);
        } finally {
            this.CloseConnection();
        }
    }

    public long UpdateFlagAbreEndereco(String codeEndereco) {
        try {
            this.OpenConnection();
            String[] args = {codeEndereco};
            ContentValues values = new ContentValues();
            values.put("FLAG_VERIFICA_ENDERECO_FECHADO", 0);
            return bd.update("PDA_TB_ENDERECO", values, "COD_ENDERECO = ?", args);
        } finally {
            this.CloseConnection();
        }
    }

    public int DeleteEnderecoById(String idEndereco) {
        try {
            String[] argsWhere = {idEndereco};

            this.OpenConnection();
            return bd.delete("PDA_TB_ENDERECO", "ID_ENDERECO = ?", argsWhere);
        } finally {
            this.CloseConnection();
        }
    }

    public void insertEnderecoFile(List<String> fileName) {
        String[] fileEnd;
        //File sdcard = new File(Environment.getExternalStorageDirectory(), "/unzipped/");
        File sdcard = new File(Environment.getExternalStorageDirectory().toString());
        Log.i("ReadFile", "Begin");

        for (int i = 0; i < fileName.size(); i++) {
            // Get the text file
            File file = new File(sdcard, fileName.get(i));

            // Read text from file
            StringBuilder text = new StringBuilder();
            Log.i("ReadFile", file.getName());
            try {
                this.OpenConnection();
                //String sql = "INSERT INTO PDA_TB_PRODUTO VALUES (?, ?, ?, ?, ?);";
                //SQLiteStatement statement = bd.compileStatement(sql);
                bd.beginTransaction();

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

//				while ((line = br.readLine()) != null) {
//					//ProdutoEO produtoEO = new ProdutoEO();
//					fileEnd = line.split(";");
//					statement.clearBindings();
//					statement.bindString(2, fileEnd[0]);
//					statement.bindString(3, fileEnd[1]);
//					statement.bindString(4, fileEnd[2]);
//					statement.bindDouble(5, 0);
//					statement.execute();
//				}

                while ((line = br.readLine()) != null) {
                    fileEnd = line.split(";");
                    ContentValues values = new ContentValues();
                    values.put("ID_ENDERECO", fileEnd[0]);
                    values.put("COD_ENDERECO", fileEnd[1]);
                    values.put("ID_SETOR", Integer.parseInt(fileEnd[2]));
                    values.put("DESC_SETOR", fileEnd[3]);
                    values.put("METODO_CONTAGEM", Integer.parseInt(fileEnd[4]));
                    values.put("DESC_METODO_CONTAGEM", fileEnd[5]);
                    values.put("METODO_AUDITORIA", fileEnd[6]);
                    values.put("DESC_METODO_AUDITORIA", fileEnd[7]);
                    values.put("ID_DEPATARMENTO", Integer.parseInt(fileEnd[8]));
                    values.put("DESC_DEPARTAMENTO", fileEnd[9]);
                    values.put("QTDE_MAX_MULTIPLOS", Integer.parseInt(fileEnd[10]));
                    values.put("ID_INVENTARIO", Integer.parseInt(fileEnd[11]));
                    values.put("METODO_LEITURA", Integer.parseInt(fileEnd[12]));
                    values.put("DESC_METODO_LEITURA", Integer.parseInt(fileEnd[12]));
                    bd.insert("PDA_TB_ENDERECO", null, values);
                }


                br.close();
                bd.setTransactionSuccessful();
                bd.endTransaction();
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}