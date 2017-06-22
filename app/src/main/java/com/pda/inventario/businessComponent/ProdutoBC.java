package com.pda.inventario.businessComponent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pda.inventario.entityObject.EnderecoColetorEO;
import com.pda.inventario.entityObject.ProdutoEO;
import com.pda.inventario.entityObject.UsuarioColetorEO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;

public class ProdutoBC {
    private SQLiteDatabase bd;
    private DbOpenHelper openHelper;

    public ProdutoBC(Context context) {
        openHelper = new DbOpenHelper(context);
    }

    public void OpenConnection() {
        bd = openHelper.getWritableDatabase();
    }

    public void CloseConnection() {
        bd.close();
    }

    public void InsertProduto(List<ProdutoEO> produtoList) {
        try {
            this.DeleteProduto();
            this.OpenConnection();

            String sql = "INSERT INTO PDA_TB_PRODUTO VALUES (?, ?, ?, ?, ?);";
            SQLiteStatement statement = bd.compileStatement(sql);
            bd.beginTransaction();
            Log.i("Insert", "Begin");
            for (int i = 0; i < produtoList.size(); i++) {
                statement.clearBindings();
                statement.bindString(2, produtoList.get(i).getCodSku());
                statement.bindString(3, produtoList
                        .get(i).getCodAutomacao());
                statement.bindString(4, produtoList.get(i).getDescSku());
                statement.bindDouble(5, produtoList.get(i).getPreco());
                statement.execute();
            }
            bd.setTransactionSuccessful();
            bd.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertProdutoFile(List<String> fileName) {
        String[] fileProd;
        //File sdcard = new File(Environment.getExternalStorageDirectory(), "/unzipped/");
        File sdcard = new File(Environment.getExternalStorageDirectory().toString());
        Log.i("ReadFile", "Begin");
        this.DeleteProduto();
        for (int i = 0; i < fileName.size(); i++) {
            // Get the text file
            File file = new File(sdcard, fileName.get(i));

            // Read text from file
            StringBuilder text = new StringBuilder();
            Log.i("ReadFile", file.getName());
            try {
                this.OpenConnection();
                String sql = "INSERT INTO PDA_TB_PRODUTO VALUES (?, ?, ?, ?, ?);";
                SQLiteStatement statement = bd.compileStatement(sql);
                bd.beginTransaction();

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    try {

                        fileProd = line.split(";");
                        statement.clearBindings();
                        statement.bindString(1, fileProd[0].toString());
                        statement.bindString(2, fileProd[2].toString());//DES
                        statement.bindString(3, fileProd[1].toString());
                        statement.bindString(4, fileProd[4].toString());
                        statement.bindDouble(5, Double.parseDouble(fileProd[3]));

                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public void InsertProduto(ProdutoEO produtoEO) {
        try {
            this.DeleteProduto();
            this.OpenConnection();

            String sql = "INSERT INTO PDA_TB_PRODUTO VALUES (?, ?, ?, ?, ?);";
            SQLiteStatement statement = bd.compileStatement(sql);
            bd.beginTransaction();
            Log.i("Insert", "Begin");

            statement.clearBindings();
            statement.bindString(2, produtoEO.getCodSku());
            statement.bindString(3, produtoEO.getCodAutomacao());
            statement.bindString(4, produtoEO.getDescSku());
            statement.bindDouble(5, produtoEO.getPreco());
            statement.execute();

            bd.setTransactionSuccessful();
            bd.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int DeleteProduto() {
        try {
            this.OpenConnection();
            return bd.delete("PDA_TB_PRODUTO", null, null);
        } finally {
            this.CloseConnection();
        }
    }

    public ProdutoEO GetProdByEAN(String codeEAN) {
        try {
            this.OpenConnection();

            ProdutoEO objProduto = new ProdutoEO();
            String[] args = {codeEAN};
            Cursor cursor = bd.query("PDA_TB_PRODUTO", null, "EAN = ?", args, null, null, null);
            while (cursor.moveToNext()) {
                objProduto.setIdProduto(cursor.getInt(0));
                objProduto.setCodSku(cursor.getString(0));
                objProduto.setCodAutomacao(cursor.getString(2));
                objProduto.setDescSku(cursor.getString(3));
                objProduto.setPreco(cursor.getInt(4));
            }
            cursor.close();

            return objProduto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public ProdutoEO GetProdBySKU(String codeSKU) {
        try {
            this.OpenConnection();

            ProdutoEO objProduto = new ProdutoEO();
            String[] args = {codeSKU};
            Cursor cursor = bd.query("PDA_TB_PRODUTO", null, "COD_PRODUTO = ?", args, null, null, null);
            while (cursor.moveToNext()) {
                //objProduto.setIdProduto(cursor.getInt(cursor.getColumnIndex("COD_PRODUTO")));
                objProduto.setCodSku(cursor.getString(cursor.getColumnIndex("COD_PRODUTO")));
                objProduto.setDescSku(cursor.getString(cursor.getColumnIndex("DESC_PRODUTO")));
                objProduto.setCodAutomacao(cursor.getString(cursor.getColumnIndex("EAN")));
                objProduto.setPreco(cursor.getInt(cursor.getColumnIndex("PRECO")));
                objProduto.setPPV(cursor.getString(cursor.getColumnIndex("PPV")));


            }
            cursor.close();

            return objProduto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }

    public List<ProdutoEO> GetProd() {
        try {
            this.OpenConnection();

            List<ProdutoEO> objProdutoList = new ArrayList<ProdutoEO>();
            ProdutoEO objProduto = new ProdutoEO();

            Cursor cursor = bd.query("PDA_TB_PRODUTO", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                objProduto = new ProdutoEO();
                objProduto.setIdProduto(cursor.getInt(0));
                objProduto.setCodSku(cursor.getString(1));
                objProduto.setCodAutomacao(cursor.getString(2));
                objProduto.setDescSku(cursor.getString(3));
                objProduto.setPreco(cursor.getInt(4));

                objProdutoList.add(objProduto);
            }
            cursor.close();

            return objProdutoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.CloseConnection();
        }
    }
}
