package com.pda.inventario.entityObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.text.format.DateFormat;

public class EnderecoColetorEO implements KvmSerializable {

    public int IdInventario;
    public String IdEndereco;
    public int IdDepartamento;
    public int IdSetor;
    public int IdMetodoContagem;
    public int IdMetodoAuditoria;
    public int IdMetodoLeitura;
    public String Endereco;
    public String Departamento;
    public String Setor;
    public String MetodoContagem;
    public String MetodoAuditoria;
    public int Quantidade;
    public Date DataHora;
    public String MetodoLeitura;
    public int verificaEnderecoFechado;

    public EnderecoColetorEO() {
        IdInventario = 0;
        IdEndereco = "";
        IdDepartamento = 0;
        IdSetor = 0;
        IdMetodoContagem = 0;
        IdMetodoAuditoria = 0;
        IdMetodoLeitura = 0;
        Endereco = "";
        Departamento = "";
        Setor = "";
        MetodoContagem = "";
        MetodoAuditoria = "";
        Quantidade = 0;
        DataHora = new Date();
        MetodoLeitura = "";
    }

    public EnderecoColetorEO(int idInventario, String idEndereco, int idDepartamento, int idSetor, int idMetodoContagem, int idMetodoAuditoria, int idMetodoLeitura, String endereco, String departamento, String setor, String metodoContagem, String metodoAuditoria, int quantidade, Date dataHora, String metodoLeitura, int verificaEnderecoFechado) {
        IdInventario = idInventario;
        IdEndereco = idEndereco;
        IdDepartamento = idDepartamento;
        IdSetor = idSetor;
        IdMetodoContagem = idMetodoContagem;
        IdMetodoAuditoria = idMetodoAuditoria;
        IdMetodoLeitura = idMetodoLeitura;
        Endereco = endereco;
        Departamento = departamento;
        Setor = setor;
        MetodoContagem = metodoContagem;
        MetodoAuditoria = metodoAuditoria;
        Quantidade = quantidade;
        DataHora = dataHora;
        MetodoLeitura = metodoLeitura;
        verificaEnderecoFechado = verificaEnderecoFechado;
    }

    public Object getProperty(int arg0) {

        switch (arg0) {
            case 0:
                return IdInventario;
            case 1:
                return IdEndereco;
            case 2:
                return IdDepartamento;
            case 3:
                return IdSetor;
            case 4:
                return IdMetodoContagem;
            case 5:
                return IdMetodoAuditoria;
            case 6:
                return IdMetodoLeitura;
            case 7:
                return Endereco;
            case 8:
                return Departamento;
            case 9:
                return Setor;
            case 10:
                return MetodoContagem;
            case 11:
                return MetodoAuditoria;
            case 12:
                return Quantidade;
            case 13:
                return DataHora;
            case 14:
                return MetodoLeitura;
            case 15:
                return verificaEnderecoFechado;
        }
        return null;
    }

    public int getPropertyCount() {
        return 16;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch (index) {
            case 0:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdInventario";
                break;
            case 1:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdEndereco";
                break;
            case 2:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdDepartamento";
                break;
            case 3:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdSetor";
                break;
            case 4:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdMetodoContagem";
                break;
            case 5:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdMetodoAuditoria";
                break;
            case 6:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "IdMetodoLeitura";
                break;
            case 7:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Endereco";
                break;
            case 8:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Departamento";
                break;
            case 9:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Setor";
                break;
            case 10:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MetodoContagem";
                break;
            case 11:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MetodoAuditoria";
                break;
            case 12:
                info.type = PropertyInfo.LONG_CLASS;
                info.name = "Quantidade";
                break;
            case 13:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "DataHora";
                break;
            case 14:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "MetodoLeitura";
                break;
            default:
                break;
        }
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case 0:
                IdInventario = Integer.parseInt(value.toString());
                break;
            case 1:
                IdEndereco = value.toString();
                break;
            case 2:
                IdDepartamento = Integer.parseInt(value.toString());
                break;
            case 3:
                IdSetor = Integer.parseInt(value.toString());
                break;
            case 4:
                IdMetodoContagem = Integer.parseInt(value.toString());
                break;
            case 5:
                IdMetodoAuditoria = Integer.parseInt(value.toString());
                break;
            case 6:
                IdMetodoLeitura = Integer.parseInt(value.toString());
                break;
            case 7:
                Endereco = value.toString();
                break;
            case 8:
                Departamento = value.toString();
                break;
            case 9:
                Setor = value.toString();
                break;
            case 10:
                MetodoContagem = value.toString();
                break;
            case 11:
                MetodoAuditoria = value.toString();
                break;
            case 12:
                Quantidade = Integer.parseInt(value.toString());
                break;
            case 13:
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    DataHora = format.parse(value.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 14:
                MetodoLeitura = value.toString();
                break;
            default:
                break;
        }
    }

}
