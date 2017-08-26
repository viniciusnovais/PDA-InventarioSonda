package com.pda.inventario;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pda_inventario.R;
import com.pda.inventario.entityObject.ProdutoEO;

import java.util.List;

/**
 * Created by PDA on 13/07/2017.
 */

public class ContagemAdapter extends BaseAdapter {

    List<ProdutoEO> lista;
    Context context;

    public ContagemAdapter(List<ProdutoEO> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view  = View.inflate(context, R.layout.item_contagem_lista,null);

        TextView tvNomeProduto,tvCodProduto,tvQtde;

        tvNomeProduto = (TextView) view.findViewById(R.id.nomeProduto);
        tvCodProduto = (TextView) view.findViewById(R.id.cod);
        tvQtde = (TextView) view.findViewById(R.id.qtde);

        tvCodProduto.setText(lista.get(position).getCodSku());
        tvNomeProduto.setText(lista.get(position).getDescSku());
        tvQtde.setText(lista.get(position).getQuantidade()+"");

        return view;
    }
}
