package com.pda.inventario;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.pda_inventario.R;
import com.pda.inventario.businessComponent.UsuarioLiderBC;
import com.pda.inventario.entityObject.UsuarioEO;
import com.pda.inventario.entityObject.UsuarioLiderEO;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.textservice.SpellCheckerInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    final UsuarioEO objUsuaurio = new UsuarioEO();

    EditText etLogin;
    EditText etPassword;
    Button btnLogin;
    Button btnCancel;
    String TAG = "Response";
    SoapPrimitive resultString;
    String login, senha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        SharedPreferences preferences = getSharedPreferences("configuracao", MODE_PRIVATE);
        StringUtils.SERVIDOR = preferences.getString("servidor", "");
        StringUtils.DIRETORIO_VIRTUAL = preferences.getString("diretorio", "");
        StringUtils.FILIAL = preferences.getString("filial", "-1");

        etLogin = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnEntrar);

//        etLogin.setText("21007339");
//        etPassword.setText("694609");
        // SharedPreferences preferences = getSharedPreferences("PDA-INVENTARIO", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();

//        if (preferences != null) {
//            preferences.getString("login", "");
//            preferences.getString("senha", "");
//        } else {
//            Toast.makeText(this, "Preferences Null", Toast.LENGTH_SHORT).show();
//        }
//        editor.putString("login", etLogin.getText().toString());
//        editor.putString("senha", etPassword.getText().toString());
//        editor.commit();

//        AsyncCallWS task = new AsyncCallWS();
//        task.execute();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            public void onClick(View v) {

                if (etLogin.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD, Toast.LENGTH_SHORT).show();
                } else {
                    if (etLogin.getText().toString().equals("12345678") && etPassword.getText().toString().equals("12345678")) {
                        etLogin.setText("");
                        etPassword.setText("");
                        Intent intent = new Intent(LoginActivity.this, ConfiguracaoActivity.class);
                        startActivity(intent);
                    } else {
                        login = etLogin.getText().toString();
                        senha = etPassword.getText().toString();
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
//        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        String resultString;
        private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            this.dialog.setMessage(StringUtils.AUTHENTICATION);
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            resultString = getLogin(login, senha);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            Toast.makeText(LoginActivity.this, "result" + resultString, Toast.LENGTH_SHORT).show();
            if (resultString.compareTo(StringUtils.AUTHENTICATION_OK) == 0) {

                Intent intent = new Intent(LoginActivity.this, AutorizacaoActivity.class);
                intent.putExtra("UsuarioEO", objUsuaurio);
                startActivity(intent);
                finish();
            }
            this.dialog.dismiss();
        }
    }

    public void showResult(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    public String getLogin(String user, String password) {

        //SoapObject soap = new SoapObject("http://10.0.2.2/WsPDA/", "GetAutenticacao");

        String SOAP_ACTION = "http://tempuri.org/Login";
        String METHOD_NAME = "Login";
        String NAMESPACE = "http://tempuri.org/";
        //String URL = "http://179.184.159.52/wsandroid/autenticacao.asmx";
        String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsautenticacao.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("_login", user);
            Request.addProperty("_senha", password);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoap = (SoapObject) soapEnvelope.getResponse();

            if (!objSoap.hasProperty("mensagemErro")) {
                objUsuaurio.setCodigo(Integer.parseInt(objSoap.getProperty("Codigo").toString()));
                objUsuaurio.setNome(objSoap.getProperty("Nome").toString());
                objUsuaurio.setCodigoPerfil(Integer.parseInt(objSoap.getProperty("CodigoPerfil").toString()));
                objUsuaurio.setDescricaoPerfil(objSoap.getProperty("DescricaoPefil").toString());
                objUsuaurio.setCodigoFilial(Integer.parseInt(objSoap.getProperty("CodigoFilial").toString()));
                objUsuaurio.setNomeFilial(objSoap.getProperty("NomeFilial").toString());
                // objUsuaurio.setCodigoCliente(Integer.parseInt(objSoap.getProperty("CodigoCliente").toString()));
                objUsuaurio.setAtivo(Integer.parseInt(objSoap.getProperty("Ativo").toString()));
                objUsuaurio.setStatusSenha(Integer.parseInt(objSoap.getProperty("StatusSenha").toString()));

                return StringUtils.AUTHENTICATION_OK;
            } else {
                return objSoap.getProperty("mensagemErro").toString();
            }

        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

}
