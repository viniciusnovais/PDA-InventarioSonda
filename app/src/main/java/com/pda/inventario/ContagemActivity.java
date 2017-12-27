package com.pda.inventario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.pda_inventario.R;
import com.pda.inventario.businessComponent.ColetaBC;
import com.pda.inventario.businessComponent.EnderecoBC;
import com.pda.inventario.businessComponent.ProdutoBC;
import com.pda.inventario.businessComponent.UsuarioColetorBC;
import com.pda.inventario.businessComponent.UsuarioLiderBC;
import com.pda.inventario.entityObject.ColetaEO;
import com.pda.inventario.entityObject.ContagemColetorEO;
import com.pda.inventario.entityObject.EnderecoColetorEO;
import com.pda.inventario.entityObject.InventarioEO;
import com.pda.inventario.entityObject.ProdutoEO;
import com.pda.inventario.entityObject.UsuarioColetorEO;
import com.pda.inventario.entityObject.UsuarioEO;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ContagemActivity extends Activity {
    private AlertDialog alerta, dialog, dialogLogin;
    private String CURRENT_SELECTION, NEW_SELECTION;
    private int iCurrentSelection, countTentativa = 0, TIPO_ATIVIDADE = 0;
    private EditText etEndProd, etLoginLider, etPasswordLider, etQuantidade, etPassword, etUser;
    private TextView tvEndereco, tvDepartamento, tvSetor, tvProdDetail, tvTitulo;
    private Button btnFechar, btnLoginLider, btnOkMult, btnExcluir, btnZerar, btnEntrar;
    private ImageView btnProcurar;
    private ListView lvColeta;
    private Spinner spinMetodoContagem;
    private ArrayAdapter adp;
    private ContagemAdapter adapter;
    private EnderecoColetorEO objEndAberto = new EnderecoColetorEO();
    private List<ProdutoEO> objProdutoList = new ArrayList<ProdutoEO>();
    private UsuarioEO objUsuarioLogado = new UsuarioEO();
    private InventarioEO objInventarioCorrente = new InventarioEO();
    private EnderecoBC enderecoBC = new EnderecoBC(this);
    private String enderecoFlag = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contagem);

        Intent intent = getIntent();
        objUsuarioLogado = (UsuarioEO) intent.getSerializableExtra("UsuarioEO");
        objInventarioCorrente = (InventarioEO) intent.getSerializableExtra("InventarioEO");
        TIPO_ATIVIDADE = Integer.parseInt(intent.getSerializableExtra("TIPO_ATIVIDADE").toString());

        etEndProd = (EditText) findViewById(R.id.etEndProd);
        etEndProd.requestFocus();
        etEndProd.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(etEndProd, 0);
            }
        }, 200);
        etEndProd.setText("");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        tvEndereco = (TextView) findViewById(R.id.tvEndereco);
        tvDepartamento = (TextView) findViewById(R.id.tvDepto);
        tvSetor = (TextView) findViewById(R.id.tvSetor);
        tvProdDetail = (TextView) findViewById(R.id.tvProdDetail);
        tvTitulo = (TextView) findViewById(R.id.tvTituloTela);

        btnProcurar = (ImageView) findViewById(R.id.btnProcurar);
        btnFechar = (Button) findViewById(R.id.btnFechar);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);
        btnZerar = (Button) findViewById(R.id.btnZerar);


        lvColeta = (ListView) findViewById(R.id.lvColeta);
        lvColeta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ProdutoEO p = (ProdutoEO) parent.getItemAtPosition(position);
                Toast.makeText(ContagemActivity.this, "Produto selecionado: " + p.getDescSku(), Toast.LENGTH_SHORT).show();

                btnExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ContagemActivity.this);
                        builder.setTitle("Deseja excluir produto: " + p.getDescSku() + " ?")
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ColetaBC repository = new ColetaBC(ContagemActivity.this);
                                        repository.DeleteProdutoByCod(p.getIdProduto());
                                        tvProdDetail.setText(StringUtils.PROD);
                                        populaListViewColeta();
                                    }
                                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                });

            }
        });

        spinMetodoContagem = (Spinner) findViewById(R.id.spinMetodoContagem);

        this.setTituloTela();
        this.populaListViewColeta();
        this.populaSpinnerMetodoContagem();

        btnProcurar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!etEndProd.getText().toString().equals("")) {
                    validaColeta();

                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.BLANK_FIELD, Toast.LENGTH_SHORT).show();
                }
            }
        });

        etEndProd.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!etEndProd.getText().toString().equals("")) {
                        validaColeta();

                    } else {
                        Toast.makeText(ContagemActivity.this, StringUtils.BLANK_FIELD, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        btnFechar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!objProdutoList.isEmpty()) {
                    if (spinMetodoContagem.getSelectedItemPosition() == 0 || spinMetodoContagem.getSelectedItemPosition() == 1) {
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();
                    } else {
                        fecharVerificacao();
                    }
                    etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    etEndProd.requestFocus();
                    etEndProd.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager keyboard = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            keyboard.showSoftInput(etEndProd, 0);
                        }
                    }, 200);
                    etEndProd.setText("");
                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.COLET_ZERO, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContagemActivity.this);
                    builder.setTitle("Deseja fechar endereço zerado ?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EnderecoBC e = new EnderecoBC(ContagemActivity.this);
                            enderecoFlag = objEndAberto.Endereco;
                            objEndAberto.IdEndereco = "";
                            e.UpdateFlagFechaEndereco(enderecoFlag);
                            tvEndereco.setText(StringUtils.END);
                            tvDepartamento.setText(StringUtils.DPTO);
                            tvSetor.setText(StringUtils.SETOR);
                            etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                            etEndProd.requestFocus();
                            etEndProd.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager keyboard = (InputMethodManager)
                                            getSystemService(Context.INPUT_METHOD_SERVICE);
                                    keyboard.showSoftInput(etEndProd, 0);
                                }
                            }, 200);
                            etEndProd.setText("");
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (objEndAberto != null) {
                    if (objEndAberto.IdEndereco != "") {
                        ColetaBC repository = new ColetaBC(ContagemActivity.this);
                        EnderecoBC repositoryEnd = new EnderecoBC(ContagemActivity.this);

                        int ret = repository.getQtdeContagemByEndereco(String.valueOf(objEndAberto.IdEndereco));
                        if (ret == 0) {
                            repositoryEnd.DeleteEnderecoById(String.valueOf(objEndAberto.IdEndereco));
                        } else {
                            Toast.makeText(ContagemActivity.this, StringUtils.END_CONT, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ContagemActivity.this, StringUtils.END_SELECT, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.END_SELECT, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnZerar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (objEndAberto != null) {
                    if (objEndAberto.IdEndereco != "") {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ContagemActivity.this);
                        builder.setTitle("Deseja zerar endereço ?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ColetaBC repository = new ColetaBC(ContagemActivity.this);
                                repository.deleteColetaEnd(String.valueOf(objEndAberto.IdEndereco));
                                populaListViewColeta();
                                tvProdDetail.setText(StringUtils.PROD);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        }).create().show();


                    } else {
                        Toast.makeText(ContagemActivity.this, StringUtils.END_SELECT, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.END_SELECT, Toast.LENGTH_SHORT).show();
                }
            }
        });

        CURRENT_SELECTION = spinMetodoContagem.getSelectedItem().toString();
        iCurrentSelection = spinMetodoContagem.getSelectedItemPosition();
        spinMetodoContagem.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (objEndAberto != null) {
                    if (objEndAberto.IdEndereco != "") {
                        if (iCurrentSelection != position) {
                            NEW_SELECTION = spinMetodoContagem.getSelectedItem().toString();
                            spinMetodoContagem.setSelection(adp.getPosition(CURRENT_SELECTION));
                            autorizacaoLider();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private void setTituloTela() {
        try {
            switch (TIPO_ATIVIDADE) {
                case 2:
                    tvTitulo.setText("Contagem");
                    break;
                case 4:
                    tvTitulo.setText("Auditoria");
                    break;
                case 3:
                    tvTitulo.setText("Contagem Final");
                    break;
                case 6:
                    tvTitulo.setText("Divergência");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populaListViewColeta() {
        ProdutoBC repositoryProd = new ProdutoBC(this);
        ColetaBC repository = new ColetaBC(this);
        List<ColetaEO> objColetaList = new ArrayList<ColetaEO>();

        try {
            if (objEndAberto != null) {
                if (objEndAberto.IdEndereco != "") {

                    List<ProdutoEO> values = repository.getColetaListViewByEndereco(String.valueOf(objEndAberto.IdEndereco));

                    adapter = new ContagemAdapter(values, ContagemActivity.this);
                    lvColeta.setAdapter(adapter);

                    if (values != null) {

                        if (values.size() > 0) {
                            if (objProdutoList.isEmpty()) {
                                objColetaList = repository.GetColetaByEndereco(String.valueOf(objEndAberto.IdEndereco));

                                for (ColetaEO obj : objColetaList) {
                                    objProdutoList.add(repositoryProd.GetProdByEAN(obj.CodigoBarra));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populaSpinnerMetodoContagem() {
        try {

            String[] values = new String[]{
                    "Peça a peça",
                    "Múltiplos",
                    "Peça a peça com verificação",
                    "Múltiplos com verificação"
            };

            adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinMetodoContagem.setAdapter(adp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean verificaEndereco() {
        EnderecoBC repository = new EnderecoBC(this);
        try {
            if (objEndAberto != null) {
                if (objEndAberto.IdEndereco.equals("")) {
                    objEndAberto = repository.GetEnderecoByCode(this.etEndProd.getText().toString().toUpperCase());

                    if (objEndAberto.IdEndereco.equals("")) {
                        Toast.makeText(ContagemActivity.this, StringUtils.END_NOK, Toast.LENGTH_SHORT).show();
                        if (!enderecoFlag.equals("")) {
                            etEndProd.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        }
                        if (!enderecoFlag.equals("")) {
                            etEndProd.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        }

                        etEndProd.requestFocus();
                        etEndProd.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager keyboard = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                keyboard.showSoftInput(etEndProd, 0);
                            }
                        }, 200);
                        etEndProd.setText("");
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.END_OPEN, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ContagemActivity.this, StringUtils.FAIL_END, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean verificaProduto() {
        ProdutoBC repository = new ProdutoBC(this);
        ProdutoEO objProduto = new ProdutoEO();

        try {

            //ppv
            if (etEndProd.getText().toString().length() == 13 && etEndProd.getText().toString().substring(0, 1).equals("2")) {

                String ppv = etEndProd.getText().toString().substring(1, 5);
                objProduto = repository.GetProdByPPV(ppv);
                if (objProduto.getCodSku() != null) {
                    float valorEtiqueta = Float.parseFloat(etEndProd.getText().toString().substring(7, 10) + "." + etEndProd.getText().toString().substring(10, 13));
                    float quantidade = valorEtiqueta / objProduto.getPreco();

                    objProduto.setColeta(quantidade);
                    objProdutoList.add(objProduto);
                    this.insertColeta(objProduto);
                    spinMetodoContagem.setSelection(1);
                    iCurrentSelection = spinMetodoContagem.getSelectedItemPosition();
                    CURRENT_SELECTION = spinMetodoContagem.getSelectedItem().toString();
                } else {
                    Toast.makeText(ContagemActivity.this, StringUtils.PROD_NOK, Toast.LENGTH_SHORT).show();

                    if (!enderecoFlag.equals("")) {
                        etEndProd.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else {
                        etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    }

                    etEndProd.requestFocus();
                    etEndProd.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager keyboard = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            keyboard.showSoftInput(etEndProd, 0);
                        }
                    }, 200);
                    return false;
                }

            } else {

                objProduto = repository.GetProdByEAN(this.etEndProd.getText().toString().toUpperCase());

                if (objProduto.getCodSku() == null) {
                    objProduto = repository.GetProdBySKU(this.etEndProd.getText().toString().toUpperCase());

                    if (objProduto.getCodSku() == null) {
                        Toast.makeText(ContagemActivity.this, StringUtils.PROD_NOK, Toast.LENGTH_SHORT).show();

                        if (!enderecoFlag.equals("")) {
                            etEndProd.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        }

                        etEndProd.requestFocus();
                        etEndProd.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager keyboard = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                keyboard.showSoftInput(etEndProd, 0);
                            }
                        }, 200);
                        return false;
                    } else {
                        if (spinMetodoContagem.getSelectedItemPosition() == 0 || spinMetodoContagem.getSelectedItemPosition() == 2) {
                            objProduto.setColeta(1);
                            objProdutoList.add(objProduto);
                            this.insertColeta(objProduto);
                        } else if (spinMetodoContagem.getSelectedItemPosition() == 1 || spinMetodoContagem.getSelectedItemPosition() == 3) {
                            this.contagemMult(objProduto);
                        }
                    }
                } else {
                    if (spinMetodoContagem.getSelectedItemPosition() == 0 || spinMetodoContagem.getSelectedItemPosition() == 2) {
                        objProduto.setColeta(1);
                        objProdutoList.add(objProduto);
                        this.insertColeta(objProduto);
                    } else if (spinMetodoContagem.getSelectedItemPosition() == 1 || spinMetodoContagem.getSelectedItemPosition() == 3) {
                        this.contagemMult(objProduto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void insertColeta(ProdutoEO objProduto) {
        ColetaBC repository = new ColetaBC(this);
        ColetaEO objColeta;

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        sdf.format(new Date());

        try {
            objColeta = new ColetaEO(0,
                    objProduto.getCodAutomacao(),
                    objProduto.getColeta(),
                    objInventarioCorrente.getIdInventario(),
                    objEndAberto.IdEndereco,
                    sdf.toString(),
                    1,
                    TIPO_ATIVIDADE,
                    0,
                    objProduto.getCodSku(),
                    objProduto.getDescSku(),
                    1,
                    String.valueOf(new Random().nextInt() + 1),
                    0,
                    "",
                    0,
                    objUsuarioLogado.getCodigo(),
                    1);

            repository.CreateColeta(objColeta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validaColeta() {
        if (objEndAberto != null) {
            if (objEndAberto.IdEndereco.equals("")) {
                if (verificaEndereco()) {
                    tvEndereco.setText(StringUtils.END + objEndAberto.Endereco);
                    tvDepartamento.setText(StringUtils.DPTO + objEndAberto.Departamento);
                    tvSetor.setText(StringUtils.SETOR + objEndAberto.Setor);

                    enderecoFlag = this.etEndProd.getText().toString();
                    enderecoBC.UpdateFlagAbreEndereco(this.etEndProd.getText().toString());

                    this.populaListViewColeta();


                    spinMetodoContagem.setSelection(1);
                    iCurrentSelection = spinMetodoContagem.getSelectedItemPosition();
                    CURRENT_SELECTION = spinMetodoContagem.getSelectedItem().toString();

                    if (!enderecoFlag.equals("")) {
                        etEndProd.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else {
                        etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    }

                    etEndProd.requestFocus();
                    etEndProd.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager keyboard = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            keyboard.showSoftInput(etEndProd, 0);
                        }
                    }, 200);
                    etEndProd.setText("");
                }
            } else {
                if (verificaProduto()) {
                    if (spinMetodoContagem.getSelectedItemPosition() == 0 || spinMetodoContagem.getSelectedItemPosition() == 2) {
                        tvProdDetail.setText(StringUtils.PROD + objProdutoList.get(objProdutoList.size() - 1).getDescSku());

                        Toast.makeText(ContagemActivity.this, StringUtils.PROD_OK, Toast.LENGTH_SHORT).show();
                        this.populaListViewColeta();

                        if (!enderecoFlag.equals("")) {
                            etEndProd.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        }

                        etEndProd.requestFocus();
                        etEndProd.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager keyboard = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                keyboard.showSoftInput(etEndProd, 0);
                            }
                        }, 200);
                        etEndProd.setText("");
                    }
                } else {
                    popupAutorizaLider(etEndProd.getText().toString());
                    etEndProd.setText("");
                }
            }
        }
    }

    private class AsyncCallWS extends AsyncTask<Void, Integer, Void> {
        private final ProgressDialog dialog = new ProgressDialog(ContagemActivity.this);

        @Override
        protected void onPreExecute() {
            Log.i("Response", "onPreExecute");
            this.dialog.setMessage(StringUtils.CLOSING_END);
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("Response", "doInBackground");
            setContagem();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("Response", "onPostExecute");
            if (VerificaConexao.isNetworkConnected(ContagemActivity.this)) {
                Toast.makeText(getApplicationContext(), StringUtils.CLOSING_END_OK, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Itens pendentes, sem conexão de rede.", Toast.LENGTH_SHORT).show();
            }
            fechaEnderecoControls();
            this.dialog.dismiss();
        }
    }

    private void fechaEnderecoControls() {
        try {
            enderecoBC.UpdateFlagFechaEndereco(enderecoFlag);
            etEndProd.setText("");
            etEndProd.requestFocus();
            lvColeta.setAdapter(null);
            objEndAberto = new EnderecoColetorEO();
            objProdutoList = new ArrayList<ProdutoEO>();
            tvEndereco.setText(StringUtils.END);
            tvDepartamento.setText(StringUtils.DPTO);
            tvSetor.setText(StringUtils.SETOR);
            tvProdDetail.setText(StringUtils.PROD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        EnderecoBC e = new EnderecoBC(this);
        if (!enderecoFlag.equals("")) {
            if (e.VerificaEnderecoFechado(enderecoFlag).verificaEnderecoFechado == 1) {
                super.onBackPressed();
            } else {
                if (e.VerificaEnderecoFechado(objEndAberto.IdEndereco).verificaEnderecoFechado == 0) {
                    Toast.makeText(this, "Antes de voltar, feche o endereço", Toast.LENGTH_SHORT).show();
                } else {
                    super.onBackPressed();
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    //	public void setContagem() {
//
//		String SOAP_ACTION = "http://tempuri.org/SetColeta";
//		String METHOD_NAME = "SetColeta";
//		String NAMESPACE = "http://tempuri.org/";
//		//String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
//		String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsproduto.asmx";
//
//		try {
//			ColetaBC repository = new ColetaBC(this);
//			SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
//			List<ContagemColetorEO> obj = new ArrayList<ContagemColetorEO>();
//
//			obj = repository.GetContagemColetorByEndereco(String.valueOf(objEndAberto.IdEndereco));
//
//			SoapObject soapEntity = new SoapObject(NAMESPACE, "entity");
//
//			for (ContagemColetorEO i : obj){
//				soapEntity.addProperty("ContagemColetorEO", i);
//			}
//			Request.addSoapObject(soapEntity);
//
//			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//			soapEnvelope.implicitTypes = true;
//			soapEnvelope.dotNet = true;
//			soapEnvelope.setOutputSoapObject(Request);
//
//			MarshalDate md = new MarshalDate();
//			md.register(soapEnvelope);
//
//			soapEnvelope.addMapping(NAMESPACE, "ContagemColetorEO",new ContagemColetorEO().getClass());
//
//			HttpTransportSE transport = new HttpTransportSE(URL);
//
//			transport.call(SOAP_ACTION, soapEnvelope);
//
//			SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();
//
//			repository.UpdateColetaExport(String.valueOf(objEndAberto.IdEndereco));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    public void setContagem() {

        final String SOAP_ACTION = "http://tempuri.org/PutFile";
        final String METHOD_NAME = "PutFile";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsproduto.asmx";

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        // Create envelope
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        String data = sdf.format(new Date());
        request.addProperty("buffer", CriarArquivoContagem());
        request.addProperty("filename", data + ".txt");


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;

        // Set output SOAP object
        envelope.setOutputSoapObject(request);

        new MarshalBase64().register(envelope);

        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            ColetaBC repository = new ColetaBC(this);
            repository.UpdateColetaExportInventario(String.valueOf(objEndAberto.IdInventario));

        } catch (Exception e) {
            //Assign Error Status true in static variable 'errored'
            //LoginActivity.errored = true;
            e.printStackTrace();
        }
    }

    public byte[] CriarArquivoContagem() {
        String linha;
        File diretorio;
        String diretorioApp;
        String nomeDiretorio;
        FileInputStream inputStream;
        byte[] data = new byte[0];

        ColetaBC repository = new ColetaBC(this);
        List<ColetaEO> lista;

        lista = repository.GetColetaPendente(String.valueOf(objInventarioCorrente.getIdInventario()));

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmsss");
        sdf.format(new Date());

        nomeDiretorio = "arquivos";
        diretorioApp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/" + nomeDiretorio + "/";

        diretorio = new File(diretorioApp);
        diretorio.mkdirs();

        File fileExt = new File(diretorioApp, "coleta" + sdf + ".txt");

        fileExt.getParentFile().mkdirs();
        FileOutputStream fosExt = null;
        try {
            fosExt = new FileOutputStream(fileExt);

            for (ColetaEO c : lista) {
                linha =
                        c.getProperty(1) + ";" +
                                c.getProperty(9) + ";" +
                                c.getProperty(2) + ";" +
                                c.getProperty(3) + ";" +
                                c.getProperty(4) + ";" +
                                c.getProperty(7) + ";" +
                                c.getProperty(16) + ";" +
                                c.getProperty(12);
                linha = linha + "\n";
                fosExt.write(linha.getBytes());

            }
            fosExt.close();
            data = new byte[(int) fileExt.length()];

            inputStream = new FileInputStream(fileExt);
            inputStream.read(data);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private void validaAutorizacaoLider() {
        try {
            if (etLoginLider.getText().toString().equals("") || etPasswordLider.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD, Toast.LENGTH_SHORT).show();
            } else {
                AsyncCallWSAutenticacao task = new AsyncCallWSAutenticacao();
                task.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autorizacaoLider() {
        LayoutInflater li = getLayoutInflater();

        View view = li.inflate(R.layout.autorizacao_lider, null);
        etLoginLider = (EditText) view.findViewById(R.id.etUserLider);
        etPasswordLider = (EditText) view.findViewById(R.id.etPasswordLider);
        btnLoginLider = (Button) view.findViewById(R.id.btnOk);

        btnLoginLider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                validaAutorizacaoLider();
            }
        });

        etLoginLider.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    etPasswordLider.requestFocus();
                    return true;
                }
                return false;
            }
        });

        etPasswordLider.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    validaAutorizacaoLider();
                    return true;
                }
                return false;
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(StringUtils.ALERT_TITLE);
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }

    private void validaContMult(final ProdutoEO prod) {
        try {
            if (etQuantidade.getText().toString().equals("") || etQuantidade.getText().toString().equals("0")) {
                Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD_ZERO, Toast.LENGTH_SHORT).show();
            } else {
                if (Float.parseFloat(etQuantidade.getText().toString()) > (float) enderecoBC.qtdeMax(objEndAberto.Endereco)) {
                    popupAutorizaLiderValorMaior(prod);
                } else {
                    prod.setColeta(Float.parseFloat(etQuantidade.getText().toString()));
                    objProdutoList.add(prod);
                    insertColeta(prod);

                    tvProdDetail.setText(StringUtils.PROD + objProdutoList.get(objProdutoList.size() - 1).getDescSku());

                    Toast.makeText(ContagemActivity.this, StringUtils.PROD_OK, Toast.LENGTH_SHORT).show();
                    populaListViewColeta();

                    if (!enderecoFlag.equals("")) {
                        etEndProd.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else {
                        etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    }

                    etEndProd.requestFocus();
                    etEndProd.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager keyboard = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            keyboard.showSoftInput(etEndProd, 0);
                        }
                    }, 200);
                    etEndProd.setText("");

                    alerta.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void contagemMult(ProdutoEO objProduto) {
        try {
            LayoutInflater li = getLayoutInflater();

            View view = li.inflate(R.layout.multiplos, null);
            etQuantidade = (EditText) view.findViewById(R.id.etColetaMult);
            btnOkMult = (Button) view.findViewById(R.id.btnEntrarMult);
            final ProdutoEO prod = objProduto;

            etQuantidade.requestFocus();
            etQuantidade.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager keyboard = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(etQuantidade, 0);
                }
            }, 200);

            btnOkMult.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    validaContMult(prod);
                }
            });

            etQuantidade.setOnKeyListener(new EditText.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        validaContMult(prod);
                        return true;
                    }
                    return false;
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(StringUtils.ALERT_TITLE_MULT);
            builder.setView(view);
            alerta = builder.create();
            alerta.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AsyncCallWSAutenticacao extends AsyncTask<Void, Void, Void> {
        String resultString;
        private final ProgressDialog dialog = new ProgressDialog(ContagemActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(StringUtils.AUTHENTICATION);
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            resultString = getLogin(etLoginLider.getText().toString(), etPasswordLider.getText().toString());

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (resultString.compareTo(StringUtils.AUTHENTICATION_OK) == 0) {
                alerta.dismiss();
                CURRENT_SELECTION = NEW_SELECTION;
            }
            spinMetodoContagem.setSelection(adp.getPosition(CURRENT_SELECTION));
            iCurrentSelection = spinMetodoContagem.getSelectedItemPosition();

            showResult(resultString);
            this.dialog.dismiss();
        }

    }

    public void showResult(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    public String getLogin(String user, String password) {
        String SOAP_ACTION = "http://tempuri.org/GetAutenticacao";
        String METHOD_NAME = "GetAutenticacao";
        String NAMESPACE = "http://tempuri.org/";
        //String URL = "http://179.184.159.52/wsandroid/autenticacao.asmx";
        String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/autenticacao.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("login_", user);
            Request.addProperty("senha_", password);

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
                if (objSoap.getProperty("Lider").toString().compareTo("1") == 0) {
                    return StringUtils.AUTHENTICATION_OK;
                } else {
                    return StringUtils.PERMISSION_NOK;
                }
            } else {
                return objSoap.getProperty("mensagemErro").toString();
            }

        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    private void validaFecharEndMult(final ColetaBC repository) {
        try {
            int ret = 0;
            if (etQuantidade.getText().toString().equals("") || etQuantidade.getText().toString().equals("0")) {
                Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD_ZERO, Toast.LENGTH_SHORT).show();
            } else {
                ret = repository.getQtdeContagemByEndereco(String.valueOf(objEndAberto.IdEndereco));

                if (ret == Integer.parseInt(etQuantidade.getText().toString())) {
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                    alerta.dismiss();
                    countTentativa = 0;
                } else {
                    Toast.makeText(getApplicationContext(), StringUtils.WRONG_QUANTITY, Toast.LENGTH_SHORT).show();
                    countTentativa++;
                    etQuantidade.setText("");
                    etQuantidade.requestFocus();

                    if (countTentativa == 3) {
                        countTentativa = 0;
                        repository.deleteColetaEnd(String.valueOf(objEndAberto.IdEndereco));
                        alerta.dismiss();

                        if (!enderecoFlag.equals("")) {
                            etEndProd.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        }

                        etEndProd.requestFocus();
                        etEndProd.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager keyboard = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                keyboard.showSoftInput(etEndProd, 0);
                            }
                        }, 200);
                        etEndProd.setText("");
                        lvColeta.setAdapter(null);
                        objProdutoList = new ArrayList<ProdutoEO>();
                        tvProdDetail.setText(StringUtils.PROD);
                        Toast.makeText(getApplicationContext(), StringUtils.END_ZERO, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fecharVerificacao() {
        try {
            final ColetaBC repositoryColeta = new ColetaBC(this);

            LayoutInflater li = getLayoutInflater();

            View view = li.inflate(R.layout.multiplos, null);
            etQuantidade = (EditText) view.findViewById(R.id.etColetaMult);
            btnOkMult = (Button) view.findViewById(R.id.btnEntrarMult);

            etQuantidade.requestFocus();
            etQuantidade.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager keyboard = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(etQuantidade, 0);
                }
            }, 200);

            btnOkMult.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    validaFecharEndMult(repositoryColeta);
                }
            });


            etQuantidade.setOnKeyListener(new EditText.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        validaFecharEndMult(repositoryColeta);
                        return true;
                    }
                    return false;
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(StringUtils.ALERT_TITLE_VERIFY);
            builder.setView(view);
            alerta = builder.create();
            alerta.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void popupAutorizaLider(final String codProduto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = View.inflate(this, R.layout.view_login_autorizacao, null);
        etUser = (EditText) v.findViewById(R.id.etUser);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        btnEntrar = (Button) v.findViewById(R.id.btnEntrar);
        builder.setView(v);
        dialogLogin = builder.create();
        dialogLogin.show();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioLiderBC dao = new UsuarioLiderBC(ContagemActivity.this);

                if (!etUser.getText().toString().equals("") || !etPassword.getText().toString().equals("")) {
                    if (dao.existeUsuarioLider(etUser.getText().toString(), etPassword.getText().toString())) {
                        etUser.setText("");
                        etUser.setText("");

                        ProdutoEO p = new ProdutoEO();

                        p.setCodSku(codProduto);
                        p.setDescSku("Produto não cadastrado");

                        dialogLogin.dismiss();

                        contagemMult(p);

                    } else {
                        etUser.setText("");
                        etPassword.setText("");
                        Toast.makeText(ContagemActivity.this, "Usuário e senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContagemActivity.this, "Preencha os Campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void popupAutorizaLiderValorMaior(final ProdutoEO prod) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = View.inflate(this, R.layout.view_login_autorizacao, null);
        etUser = (EditText) v.findViewById(R.id.etUser);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        btnEntrar = (Button) v.findViewById(R.id.btnEntrar);
        builder.setView(v);
        dialogLogin = builder.create();
        dialogLogin.show();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioLiderBC dao = new UsuarioLiderBC(ContagemActivity.this);

                if (!etUser.getText().toString().equals("") || !etPassword.getText().toString().equals("")) {
                    if (dao.existeUsuarioLider(etUser.getText().toString(), etPassword.getText().toString())) {
                        etUser.setText("");
                        etUser.setText("");

                        prod.setColeta(Float.parseFloat(etQuantidade.getText().toString()));
                        objProdutoList.add(prod);
                        insertColeta(prod);

                        tvProdDetail.setText(StringUtils.PROD + objProdutoList.get(objProdutoList.size() - 1).getDescSku());
                        etQuantidade.setText("");

                        Toast.makeText(ContagemActivity.this, StringUtils.PROD_OK, Toast.LENGTH_SHORT).show();
                        populaListViewColeta();

                        if (!enderecoFlag.equals("")) {
                            etEndProd.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            etEndProd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        }

                        etEndProd.requestFocus();
                        etEndProd.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager keyboard = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                keyboard.showSoftInput(etEndProd, 0);
                            }
                        }, 200);
                        etEndProd.setText("");

                        alerta.dismiss();

                        dialogLogin.dismiss();

                    } else {
                        etUser.setText("");
                        etUser.setText("");
                        Toast.makeText(ContagemActivity.this, "Usuário e senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContagemActivity.this, "Preencha os Campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
