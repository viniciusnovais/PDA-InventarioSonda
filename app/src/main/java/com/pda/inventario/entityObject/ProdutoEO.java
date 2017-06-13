package com.pda.inventario.entityObject;

public class ProdutoEO {
	private int IdProduto;
	private String CodAutomacao;
    private String CodSku;
    private String DescSku;
    private float Preco;
    private float Quantidade;
    private int Coleta;
    
    public ProdutoEO(){}
    
    public ProdutoEO(int idProduto, String codAutomacao, String codSku, String descSku, float preco, float quantidade, int coleta){
    	IdProduto = idProduto;
    	CodAutomacao = codAutomacao;
        CodSku = codSku;
        DescSku = descSku;
        Preco = preco;
        Quantidade = quantidade;
        Coleta = coleta;
    }
    
    public int getIdProduto() {
		return IdProduto;
	}
	public void setIdProduto(int idProduto) {
		IdProduto = idProduto;
	}

	public String getCodAutomacao() {
		return CodAutomacao;
	}
	public void setCodAutomacao(String codAutomacao) {
		CodAutomacao = codAutomacao;
	}

	public String getCodSku() {
		return CodSku;
	}
	public void setCodSku(String codSku) {
		CodSku = codSku;
	}

	public String getDescSku() {
		return DescSku;
	}
	public void setDescSku(String descSku) {
		DescSku = descSku;
	}

	public float getPreco() {
		return Preco;
	}
	public void setPreco(float preco) {
		Preco = preco;
	}

	public float getQuantidade() {
		return Quantidade;
	}
	public void setQuantidade(float quantidade) {
		Quantidade = quantidade;
	}

	public int getColeta() {
		return Coleta;
	}

	public void setColeta(int coleta) {
		Coleta = coleta;
	}
    
}
