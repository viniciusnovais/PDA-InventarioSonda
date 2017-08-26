package com.pda.inventario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.*;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.pda_inventario.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pda.inventario.businessComponent.AutorizacaoBC;
import com.pda.inventario.businessComponent.ColetaBC;
import com.pda.inventario.businessComponent.ConfiguracaoBC;
import com.pda.inventario.businessComponent.DepartamentoBC;
import com.pda.inventario.businessComponent.EnderecoBC;
import com.pda.inventario.businessComponent.ProdutoBC;
import com.pda.inventario.businessComponent.SetorBC;
import com.pda.inventario.businessComponent.UsuarioColetorBC;
import com.pda.inventario.businessComponent.UsuarioLiderBC;
import com.pda.inventario.entityObject.ColetaEO;
import com.pda.inventario.entityObject.ControleArquivoEO;
import com.pda.inventario.entityObject.DepartamentoColetorEO;
import com.pda.inventario.entityObject.EnderecoColetorEO;
//import com.pda.inventario.entityObject.DepartamentoEO;
import com.pda.inventario.entityObject.InventarioColetorEO;
import com.pda.inventario.entityObject.InventarioEO;
import com.pda.inventario.entityObject.ProdutoEO;
import com.pda.inventario.entityObject.SetorColetorEO;
import com.pda.inventario.entityObject.UsuarioColetorEO;
import com.pda.inventario.entityObject.UsuarioEO;
import com.pda.inventario.entityObject.UsuarioLiderEO;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AutorizacaoActivity extends AbsRuntimePermission {
    private final AutorizacaoBC repository = new AutorizacaoBC(this);
    private ProgressDialog dialogLimpar;
    private List<String> fileNameList = new ArrayList<String>();

    UsuarioEO objUsuarioLogado = new UsuarioEO();
    InventarioEO objInventario = new InventarioEO();

    Button btnOK, btnLimpar, btnConf;
    Dialog dialog;
    private EditText etAutorizacao;
    TextView tvUsuarioLogado;

    int downloadProgress = 0, downloadedSize = 0, totalSize = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorizacao);

        Intent intent = getIntent();
        objUsuarioLogado = (UsuarioEO) intent.getSerializableExtra("UsuarioEO");
//        objUsuarioLogado.setNome(objUsuarioLogado.getNome());
//        objUsuarioLogado.setCodigo(objUsuarioLogado.getCodigo());
//        objUsuarioLogado.setCodigoPerfil(objUsuarioLogado.getCodigoPerfil());


        tvUsuarioLogado = (TextView) findViewById(R.id.tvUsuarioLogado);
        tvUsuarioLogado.setText(objUsuarioLogado.getNome());

        btnOK = (Button) findViewById(R.id.btnOk);
        btnLimpar = (Button) findViewById(R.id.btnLimpar);
        btnConf = (Button) findViewById(R.id.btnConfig);

        etAutorizacao = (EditText) findViewById(R.id.etAutorizacao);
        //etAutorizacao.setText("72826018");

        dialogLimpar = new ProgressDialog(AutorizacaoActivity.this);

        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (etAutorizacao.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD, Toast.LENGTH_LONG).show();
                } else {
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogLimpar.setMessage(StringUtils.DATA_BASE_CLEAR);
                dialogLimpar.show();
                repository.limpaBase();
                Toast.makeText(getApplicationContext(), StringUtils.DATA_BASE_CLEAR_OK, Toast.LENGTH_LONG).show();
                dialogLimpar.dismiss();
            }
        });

        btnConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AutorizacaoActivity.this, ConfiguracaoActivity.class);
                startActivity(i);
            }
        });

        if (!new ConfiguracaoBC(this).VerificaConfig()) {
            Intent i = new Intent(AutorizacaoActivity.this, ConfiguracaoActivity.class);
            startActivity(i);
//			Toast.makeText(getApplicationContext(), StringUtils.FIRST_CONFIG, Toast.LENGTH_SHORT).show();
        } else {
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        requestAppPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string.action_settings, 10);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private boolean verificaImportacao() {
        try {
            UsuarioColetorBC repository = new UsuarioColetorBC(this);
            UsuarioColetorEO objUsuario = new UsuarioColetorEO();

            objUsuario = repository.GetUsuario(String.valueOf(objUsuarioLogado.getCodigo()));

            if (objUsuario.getCodigo() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Autorizacao Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class AsyncCallWS extends AsyncTask<Void, Integer, Void> {
        private final ProgressDialog dialog = new ProgressDialog(AutorizacaoActivity.this);
        String resultCall;

        @Override
        protected void onPreExecute() {
            Log.i("Response", "onPreExecute");
            this.dialog.setMessage(StringUtils.IMPORT);
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("Response", "doInBackground");

            resultCall = getInventory(etAutorizacao.getText().toString());

//            for (int i = 0; i < count; i++) {
//                publishProgress(i);
//            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //dialog.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("Response", "onPostExecute");
            if (resultCall.equals(StringUtils.INVENT_OK)) {
                Intent intent = new Intent(AutorizacaoActivity.this, PrincipalActivity.class);
                intent.putExtra("UsuarioEO", objUsuarioLogado);
                getIntent().getSerializableExtra("UsuarioEO");
                intent.putExtra("InventarioEO", objInventario);
                getIntent().getSerializableExtra("InventarioEO");

                startActivity(intent);
            }
            Toast.makeText(getApplicationContext(), resultCall, Toast.LENGTH_LONG).show();
            this.dialog.dismiss();
        }
    }

    public String getInventory(String authorization) {

        final String SOAP_ACTION = "http://tempuri.org/ObterInventario";
        final String METHOD_NAME = "ObterInventario";
        final String NAMESPACE = "http://tempuri.org/";
        //final String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
        final String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsproduto.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("_autorizacao", authorization);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoap = (SoapObject) soapEnvelope.getResponse();

            if (objSoap != null) {
                if (objSoap.getProperty("Status").equals("0")) {
                    return StringUtils.INVENT_CLOSED;
                } else {
                    objInventario = new InventarioEO();
                    objInventario.setIdInventario(Integer.parseInt(objSoap.getProperty("IDInventario").toString()));
                    objInventario.setCodigoFilial(Integer.parseInt(objSoap.getProperty("CodigoEmpresa").toString()));
                    objInventario.setFilial(objSoap.getProperty("NomeEmpresa").toString());
                    objInventario.setAutorizacao(objSoap.getProperty("CodigoAutorizacao").toString());
                    objInventario.setStatus(Integer.parseInt(objSoap.getProperty("Status").toString()));

                    //if (verificaImportacao()) {
                    //this.getUsers();
                    //this.getDepto();
                    //this.getSetor();
                    this.DownloadZipFile(GetProduto());
                    this.GetUsuario();
                    this.GetEndereco();
                    //this.DownloadZipFile(GetEnderecoFile());
                    //this.getProdutoList(objInventario.getIdInventario());

                    // }
                    return StringUtils.INVENT_OK;
                }
            } else
                return StringUtils.INVENT_INVALID;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public void getUsers() {

        final String SOAP_ACTION = "http://tempuri.org/GetUsuarioColetor";
        final String METHOD_NAME = "GetUsuarioColetor";
        final String NAMESPACE = "http://tempuri.org/";
        //final String URL = "http://179.184.159.52/wsandroid/autenticacao.asmx";
        final String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/autenticacao.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            InventarioColetorEO obj = new InventarioColetorEO();

            PropertyInfo pi = new PropertyInfo();
            pi.setName("obj");
            pi.setValue(obj);
            pi.setType(obj.getClass());

            Request.addProperty(pi);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            soapEnvelope.addMapping(NAMESPACE, "InventarioColetorEO", new InventarioColetorEO().getClass());

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

            if (objSoapList != null) {
                List<UsuarioColetorEO> objUsuarioList = new ArrayList<UsuarioColetorEO>();
                for (int i = 0; i < objSoapList.getPropertyCount(); i++) {
                    SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
                    UsuarioColetorEO objUsuario = new UsuarioColetorEO();

                    objUsuario.setCodigo(Integer.parseInt(objSoap.getPropertyAsString("Codigo")));
                    objUsuario.setLogin(objSoap.getPropertyAsString("Login"));
                    objUsuario.setLider(Integer.parseInt(objSoap.getPropertyAsString("Lider")));
                    objUsuario.setSenha(objSoap.getPropertyAsString("Senha").getBytes());
                    objUsuarioList.add(objUsuario);
                }

                if (!objUsuarioList.isEmpty()) {
                    UsuarioColetorBC repository = new UsuarioColetorBC(this);
                    repository.CreateUsuarioList(objUsuarioList);
                }
            } else
                Log.i("WS Call", "Return null");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DownloadFileAsync extends AsyncTask<String, Integer, String> {
        List<ControleArquivoEO> objControleArquivoList = new ArrayList<ControleArquivoEO>();
        int myProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                DownloadZipFile(GetProduto());

                while (myProgress < 100) {
                    myProgress++;
                    downloadProgress++;
                    publishProgress(myProgress);
                    SystemClock.sleep(100);
                }
                downloadProgress = 0;

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {

        }

    }

    private List<ProdutoEO> ReadFile(List<String> fileName) {
        String[] fileProd;
        List<ProdutoEO> produtoEOList = new ArrayList<ProdutoEO>();
        ProdutoBC repository = new ProdutoBC(this);
        File sdcard = new File(Environment.getExternalStorageDirectory(), "/unzipped/");
        Log.i("ReadFile", "Begin");
        for (int i = 0; i < fileName.size(); i++) {
            // Get the text file
            File file = new File(sdcard, fileName.get(i));

            // Read text from file
            StringBuilder text = new StringBuilder();
            Log.i("ReadFile", file.getName());
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    ProdutoEO produtoEO = new ProdutoEO();
                    fileProd = line.split(";");

                    produtoEO.setCodSku(fileProd[0]);
                    produtoEO.setCodAutomacao(fileProd[1]);
                    produtoEO.setDescSku(fileProd[2]);
                    produtoEO.setPreco(0);
                    repository.InsertProduto(produtoEO);
                    //produtoEOList.add(produtoEO);
                }
                br.close();
                //repository.InsertProduto(produtoEOList);
                //produtoEOList = new ArrayList<ProdutoEO>();
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return produtoEOList;
    }

    public void getDepto() {

        final String SOAP_ACTION = "http://tempuri.org/GetDepartamento";
        final String METHOD_NAME = "GetDepartamento";
        final String NAMESPACE = "http://tempuri.org/";
        //final String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
        final String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/inventario.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            DepartamentoColetorEO obj = new DepartamentoColetorEO();
            obj.IdInventario = objInventario.getIdInventario();

            PropertyInfo pi = new PropertyInfo();
            pi.setName("entity");
            pi.setValue(obj);
            pi.setType(obj.getClass());

            Request.addProperty(pi);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            soapEnvelope.addMapping(NAMESPACE, "DepartamentoColetorEO", new DepartamentoColetorEO().getClass());

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

            if (objSoapList != null) {
                List<DepartamentoColetorEO> objDepartamentoList = new ArrayList<DepartamentoColetorEO>();
                for (int i = 0; i < objSoapList.getPropertyCount(); i++) {
                    SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
                    DepartamentoColetorEO objDepartamento = new DepartamentoColetorEO();

                    objDepartamento.setProperty(0, Integer.parseInt(objSoap.getPropertyAsString("IdInventario")));
                    objDepartamento.setProperty(1, Integer.parseInt(objSoap.getPropertyAsString("IdDepartamento")));
                    objDepartamento.setProperty(2, Integer.parseInt(objSoap.getPropertyAsString("IdMetodoContagem")));
                    objDepartamento.setProperty(3, Integer.parseInt(objSoap.getPropertyAsString("IdMetodoAuditoria")));
                    objDepartamento.setProperty(4, Integer.parseInt(objSoap.getPropertyAsString("IdMetodoLeitura")));
                    objDepartamento.setProperty(5, objSoap.getPropertyAsString("Departamento"));
                    objDepartamento.setProperty(6, objSoap.getPropertyAsString("MetodoContagem"));
                    objDepartamento.setProperty(7, objSoap.getPropertyAsString("MetodoAuditoria"));
                    objDepartamento.setProperty(8, Integer.parseInt(objSoap.getPropertyAsString("Quantidade")));

                    objDepartamentoList.add(objDepartamento);
                }

                if (!objDepartamentoList.isEmpty()) {
                    DepartamentoBC repository = new DepartamentoBC(this);
                    repository.CreateDepartamentoColetorList(objDepartamentoList);
                }
            } else
                Log.i("WS Call", "Return null");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSetor() {

        final String SOAP_ACTION = "http://tempuri.org/GetSetor";
        final String METHOD_NAME = "GetSetor";
        final String NAMESPACE = "http://tempuri.org/";
        //final String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
        final String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsinventario.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            SetorColetorEO obj = new SetorColetorEO();
            obj.IdInventario = objInventario.getIdInventario();

            PropertyInfo pi = new PropertyInfo();
            pi.setName("entity");
            pi.setValue(obj);
            pi.setType(obj.getClass());

            Request.addProperty(pi);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            soapEnvelope.addMapping(NAMESPACE, "SetorColetorEO", new SetorColetorEO().getClass());

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

            if (objSoapList != null) {
                List<SetorColetorEO> objSetorList = new ArrayList<SetorColetorEO>();
                for (int i = 0; i < objSoapList.getPropertyCount(); i++) {
                    SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
                    SetorColetorEO objSetor = new SetorColetorEO();

                    objSetor.IdInventario = Integer.parseInt(objSoap.getPropertyAsString("IdInventario").toString());
                    objSetor.IdDepartamento = Integer.parseInt(objSoap.getPropertyAsString("IdDepartamento").toString());
                    objSetor.IdSetor = Integer.parseInt(objSoap.getPropertyAsString("IdSetor").toString());
                    objSetor.IdMetodoContagem = Integer.parseInt(objSoap.getPropertyAsString("IdMetodoContagem").toString());
                    objSetor.IdMetodoAuditoria = Integer.parseInt(objSoap.getPropertyAsString("IdMetodoAuditoria").toString());
                    objSetor.IdMetodoLeitura = Integer.parseInt(objSoap.getPropertyAsString("IdMetodoLeitura").toString());
                    objSetor.Setor = objSoap.getPropertyAsString("Setor").toString();
                    objSetor.MetodoContagem = objSoap.getPropertyAsString("MetodoContagem").toString();
                    objSetor.MetodoAuditoria = objSoap.getPropertyAsString("MetodoAuditoria").toString();
                    objSetor.Quantidade = Integer.parseInt(objSoap.getPropertyAsString("Quantidade").toString());

                    objSetorList.add(objSetor);
                }

                if (!objSetorList.isEmpty()) {
                    SetorBC repository = new SetorBC(this);
                    repository.CreateSetorColetorList(objSetorList);
                }
            } else
                Log.i("WS Call", "Return null");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetEndereco() {

        final String SOAP_ACTION = "http://tempuri.org/ObterListaEnderecos";
        final String METHOD_NAME = "ObterListaEnderecos";
        final String NAMESPACE = "http://tempuri.org/";
        //final String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
        final String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsproduto.asmx";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("_codEmpresa", objInventario.getCodigoFilial());

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

//            SoapObject objSoap = (SoapObject) soapEnvelope.getResponse();

//            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
//            EnderecoColetorEO obj = new EnderecoColetorEO();
//            obj.IdInventario = objInventario.getIdInventario();

//            PropertyInfo pi = new PropertyInfo();
//            pi.setName("entity");
//            pi.setValue(obj);
//            pi.setType(obj.getClass());

//            Request.addProperty(pi);

//            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            soapEnvelope.implicitTypes = true;
//            soapEnvelope.dotNet = true;
//            soapEnvelope.setOutputSoapObject(Request);
//
//            MarshalDate md = new MarshalDate();
//            md.register(soapEnvelope);
//
//            soapEnvelope.addMapping(NAMESPACE, "EnderecoColetorEO", new EnderecoColetorEO().getClass());
//
//            HttpTransportSE transport = new HttpTransportSE(URL);

            //transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

            if (objSoapList != null) {
                List<EnderecoColetorEO> objEnderecoList = new ArrayList<EnderecoColetorEO>();
//                count = objSoapList.getPropertyCount();
                for (int i = 0; i < objSoapList.getPropertyCount(); i++) {
                    SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
                    EnderecoColetorEO objEndereco = new EnderecoColetorEO();

                    objEndereco.IdInventario = objInventario.getIdInventario();
                    objEndereco.IdEndereco = (objSoap.getPropertyAsString("ENDERECO").toString());
                    objEndereco.IdDepartamento = 1;
                    objEndereco.IdSetor = 1;
                    objEndereco.IdMetodoContagem = 4;
                    objEndereco.IdMetodoAuditoria = 4;
                    objEndereco.IdMetodoLeitura = 1;
                    objEndereco.MetodoLeitura = "1";
                    objEndereco.Endereco = objSoap.getPropertyAsString("ENDERECO").toString();
                    objEndereco.Departamento = objSoap.getPropertyAsString("NM_LOCAL").toString();
                    objEndereco.Setor = objSoap.getPropertyAsString("NM_MODULO").toString();
                    objEndereco.MetodoContagem = "Multiplo";
                    objEndereco.MetodoAuditoria = "Multiplo";
                    objEndereco.Quantidade = Integer.parseInt(objSoap.getPropertyAsString("_x005F_x003A_B1").toString());

//                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                    try {
//                        objEndereco.DataHora = format.parse(objSoap.getPropertyAsString("DataHora").toString());
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

                    objEnderecoList.add(objEndereco);
                }

                if (!objEnderecoList.isEmpty()) {
                    EnderecoBC repository = new EnderecoBC(this);
                    repository.CreateEnderecoList(objEnderecoList);
                }
            } else
                Log.i("WS Call", "Return null");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<ControleArquivoEO> GetProduto() {

        final String SOAP_ACTION = "http://tempuri.org/ObterListaNomeArquivos";
        final String METHOD_NAME = "ObterListaNomeArquivos";
        final String NAMESPACE = "http://tempuri.org/";
        //final String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
        final String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsproduto.asmx";

        List<ControleArquivoEO> objControleArquivoList = new ArrayList<ControleArquivoEO>();
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("_empresa", objInventario.getCodigoFilial());
            Request.addProperty("idInventario", objInventario.getIdInventario());
            Request.addProperty("_autorizacao", objInventario.getAutorizacao());

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

            for (int i = 0; i < objSoapList.getPropertyCount(); i++) {
                SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
                ControleArquivoEO objControleArquivo = new ControleArquivoEO();

                objControleArquivo.CodigoInventario = objInventario.getIdInventario();
                objControleArquivo.NomeArquivo = objSoap.getPropertyAsString("Arquivo").toString();
                objControleArquivoList.add(objControleArquivo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return objControleArquivoList;
    }

    public List<ControleArquivoEO> GetEnderecoFile() {

        final String SOAP_ACTION = "http://tempuri.org/GetEndereco";
        final String METHOD_NAME = "GetEndereco";
        final String NAMESPACE = "http://tempuri.org/";
        //final String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
        final String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/inventario.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("idInventario", objInventario.getIdInventario());

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();
            List<ControleArquivoEO> objControleArquivoList = new ArrayList<ControleArquivoEO>();

            for (int i = 0; i < objSoapList.getPropertyCount(); i++) {
                SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
                ControleArquivoEO objControleArquivo = new ControleArquivoEO();

                objControleArquivo.CodigoInventario = Integer.parseInt(objSoap.getPropertyAsString("CodigoInventario").toString());
                objControleArquivo.NomeArquivo = objSoap.getPropertyAsString("NomeArquivo").toString();
                objControleArquivoList.add(objControleArquivo);
            }
            return objControleArquivoList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void DownloadZipFile(List<ControleArquivoEO> objControleArquivoList) {
        String fileName;
        int count;
        List<String> fileNameList = new ArrayList<String>();
        List<ProdutoEO> produtoList = new ArrayList<ProdutoEO>();
        ProdutoBC repository = new ProdutoBC(this);
        EnderecoBC repositoryEnd = new EnderecoBC(this);
        try {
            for (int i = 0; i < objControleArquivoList.size(); i++) {
                //URL url = new URL("http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/Arquivos/" + objControleArquivoList.get(i).NomeArquivo.replace(".ZIP", ".TXT"));
                URL url = new URL("http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/Arquivos/" + objControleArquivoList.get(i).NomeArquivo);
                //				URL url = new URL("http://179.184.159.52/wsandroid/Arquivos/SKU_189-9.ZIP");
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(
                        //Environment.getExternalStorageDirectory().getPath() + "/" + objControleArquivoList.get(i).NomeArquivo.replace(".ZIP", ".TXT"));
                        Environment.getExternalStorageDirectory().getPath() + "/" + objControleArquivoList.get(i).NomeArquivo);
                //						Environment.getExternalStorageDirectory().getPath() + "/SKU_189-9.ZIP");

                byte data[] = new byte[1024];


                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

//                fileNameList.add(objControleArquivoList.get(i).NomeArquivo.replace(".ZIP", ".TXT"));
                fileNameList.add(objControleArquivoList.get(i).NomeArquivo);
            }

            //fileNameList = unzip(objControleArquivoList, Environment.getExternalStorageDirectory() + "/unzipped/");

            if (!fileNameList.isEmpty()) {
                //produtoList = ReadFile(fileNameList);
                if (fileNameList.get(0).contains("END"))
                    repositoryEnd.insertEnderecoFile(fileNameList);
                else
                    repository.insertProdutoFile(fileNameList);
            }
//			if (!produtoList.isEmpty()){
//				repository.InsertProduto(produtoList);
//				//repository.importProduto(produtoList);
//			}else{
//
//			}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> unzip(List<ControleArquivoEO> objControleArquivoList, String location) {
        String zipFile;
        List<String> fileNameList = new ArrayList<String>();
        File file;
        FileInputStream fin;
        ZipInputStream zin;
        ZipEntry ze;
        FileOutputStream fout;
        try {
            for (int i = 0; i < objControleArquivoList.size(); i++) {
                zipFile = Environment.getExternalStorageDirectory().getPath() + "/" + objControleArquivoList.get(i).NomeArquivo;
                file = new File(zipFile);
                //int BUFFER_SIZE = 1024;
                //int size;
                //byte[] buffer = new byte[BUFFER_SIZE];

                //ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));

                fin = new FileInputStream(zipFile);
                zin = new ZipInputStream(fin);
                ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    Log.v("Decompress", "Unzipping " + ze.getName());
                    if (ze.isDirectory()) {
                        _dirChecker(ze.getName(), location);
                    } else {
                        fout = new FileOutputStream(location + ze.getName());
                        for (int c = zin.read(); c != -1; c = zin.read()) {
                            fout.write(c);
                        }
                        zin.closeEntry();
                        fout.close();
                    }
                    fileNameList.add(ze.getName());
                }
                zin.close();
                file.delete();
            }
            return fileNameList;
        } catch (Exception e) {
            Log.e("Decompress", "unzip", e);
            return null;
        } finally {
            zipFile = null;
            fileNameList = null;
            file = null;
            fin = null;
            zin = null;
            ze = null;
            fout = null;
        }
    }

    private void _dirChecker(String dir, String location) {
        File f = new File(location + dir);

        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public void getProdutoList(int idInventario) {
        String SOAP_ACTION = "http://tempuri.org/GetProdutoMobile";
        String METHOD_NAME = "GetProdutoMobile";
        String NAMESPACE = "http://tempuri.org/";
        //String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
        String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/inventario.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("idInventario", idInventario);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            MarshalDate md = new MarshalDate();
            md.register(soapEnvelope);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

            if (objSoapList != null) {
                List<ProdutoEO> objProdutoList = new ArrayList<ProdutoEO>();
                for (int i = 0; i < objSoapList.getPropertyCount(); i++) {
                    SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
                    ProdutoEO objProduto = new ProdutoEO();


                    objProduto.setCodSku(objSoap.getPropertyAsString("CodigoProduto").toString());
                    objProduto.setCodAutomacao(objSoap.getPropertyAsString("CodigoBarra").toString());
                    objProduto.setDescSku(objSoap.getPropertyAsString("Descricao").toString());
                    objProduto.setPreco(0);

                    objProdutoList.add(objProduto);
                }

                if (!objProdutoList.isEmpty()) {
                    ProdutoBC repository = new ProdutoBC(this);
                    repository.InsertProduto(objProdutoList);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void GetUsuario() {

        final String SOAP_ACTION = "http://tempuri.org/ListaLider ";
        final String METHOD_NAME = "ListaLider ";
        final String NAMESPACE = "http://tempuri.org/";
        //final String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
        final String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsautenticacao.asmx";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();
            SoapObject item;
            List<UsuarioLiderEO> lista = new ArrayList<>();
            UsuarioLiderBC usuarioLiderBC = new UsuarioLiderBC(AutorizacaoActivity.this);
            for (int i = 0; i < objSoapList.getPropertyCount(); i++) {
                item = (SoapObject) objSoapList.getProperty(i);
                UsuarioLiderEO usuarioLiderEO = new UsuarioLiderEO();

                usuarioLiderEO.setLogin(item.getProperty("Login").toString());
                usuarioLiderEO.setSenha(item.getProperty("Senha").toString());

                lista.add(usuarioLiderEO);

            }

            usuarioLiderBC.CreateUsuario(lista);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
