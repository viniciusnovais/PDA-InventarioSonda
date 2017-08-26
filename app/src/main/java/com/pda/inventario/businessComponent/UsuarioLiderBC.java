package com.pda.inventario.businessComponent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pda.inventario.entityObject.UsuarioColetorEO;
import com.pda.inventario.entityObject.UsuarioLiderEO;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

public class UsuarioLiderBC {

    private SQLiteDatabase bd;
    private DbOpenHelper openHelper;

    public UsuarioLiderBC(Context context) {
        openHelper = new DbOpenHelper(context);
    }

    public void OpenConnection() {
        bd = openHelper.getWritableDatabase();
    }

    public void CloseConnection() {
        bd.close();
    }

    public void CreateUsuario(List<UsuarioLiderEO> lista) {
        try {
            this.DeleteUsuario();
            this.OpenConnection();

            for (UsuarioLiderEO u : lista) {

                ContentValues values = new ContentValues();
                values.put("LOGIN", u.getLogin());
                values.put("PASSWORD", u.getSenha());

                bd.insert("PDA_TB_LOGIN_LIDER ", null, values);
            }
        } finally {
            this.CloseConnection();
        }
    }

    public int DeleteUsuario() {
        try {
            this.OpenConnection();
            return bd.delete("PDA_TB_LOGIN_LIDER", null, null);
        } finally {
            this.CloseConnection();
        }
    }

    public boolean existeUsuarioLider(String login, String senha) {

        try {
            String[] args = new String[]{login, senha};
            this.OpenConnection();
            Cursor cursor = bd.rawQuery("SELECT * FROM PDA_TB_LOGIN_LIDER WHERE LOGIN = ? and PASSWORD = ? ", args);
            while (cursor.moveToNext()) {
                return true;
            }

        } finally {
            this.CloseConnection();
        }
        return false;
    }
}
