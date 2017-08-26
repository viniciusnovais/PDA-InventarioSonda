package com.pda.inventario;

import com.example.pda_inventario.R;
import com.pda.inventario.businessComponent.ConfiguracaoBC;
import com.pda.inventario.entityObject.ConfiguracaoEO;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfiguracaoActivity extends Activity {
    Button btnOK;

    EditText etServidor, etDir, etFilial;

    long ret = 0;

    ConfiguracaoBC repository = new ConfiguracaoBC(this);
    ConfiguracaoEO objConfig = new ConfiguracaoEO();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.configuracao);

        etServidor = (EditText) findViewById(R.id.etServer);
        etDir = (EditText) findViewById(R.id.etDiretorio);
        etFilial = (EditText) findViewById(R.id.etFilial);

        SharedPreferences preferences = getSharedPreferences("configuracao", MODE_PRIVATE);

        etServidor.setText(preferences.getString("servidor",""));
        etDir.setText(preferences.getString("diretorio",""));
        etFilial.setText(preferences.getString("filial","-1"));

        StringUtils.SERVIDOR = preferences.getString("servidor","");
        StringUtils.DIRETORIO_VIRTUAL = preferences.getString("diretorio","");
        StringUtils.FILIAL = preferences.getString("filial","-1");

        etServidor.requestFocus();

        btnOK = (Button) findViewById(R.id.btnOkConfig);

        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (etServidor.getText().toString().equals("")
                        || etFilial.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), StringUtils.BLANK_FIELD, Toast.LENGTH_SHORT).show();
                } else {
                    objConfig = new ConfiguracaoEO();
                    objConfig.setServidor(etServidor.getText().toString());
                    objConfig.setDiretorioVirtual(etDir.getText().toString());
                    objConfig.setFilial(etFilial.getText().toString());

                    if (repository.VerificaConfig()) {
                        ret = repository.updateConfiguracao(objConfig);
                    } else {
                        ret = repository.createConfiguracao(objConfig);
                    }

                    if (ret > 0) {
                        Toast.makeText(getApplicationContext(), StringUtils.CONFIG_OK, Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = getSharedPreferences("configuracao", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("servidor", etServidor.getText().toString());
                        editor.putString("diretorio", etDir.getText().toString());
                        editor.putString("filial", etFilial.getText().toString());
                        editor.commit();

                        StringUtils.SERVIDOR = preferences.getString("servidor","");
                        StringUtils.DIRETORIO_VIRTUAL = preferences.getString("diretorio","");
                        StringUtils.FILIAL = preferences.getString("filial","-1");
                    } else {
                        Toast.makeText(getApplicationContext(), StringUtils.CONFIG_NOK, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
