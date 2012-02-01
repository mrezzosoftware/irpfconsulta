package com.mrezzosoftware.irpfconsulta.pf;

import java.io.Serializable;

public class Declaracao implements Serializable{
	
	private String nome;
	private String banco;
	private String agencia;
	private String lote;
	private String dataDisponivel;
	private String situacao;
	private int codigoRetorno;

	public static final int ERRO_HABILITACAO = -7;
	public static final int OK = 0;
	public static final int CPF_INVALIDO = 1;
	public static final int CAPTCHA_INVALIDO = 2;
	public static final int SEM_SALDO = 3;
	
	public Declaracao(String nome, String banco, String agencia, String lote,
			String dataDisponivel, String situacao, int codigoRetorno) {
		
		this.nome = nome;
		this.banco = banco;
		this.agencia = agencia;
		this.lote = lote;
		this.dataDisponivel = dataDisponivel;
		this.situacao = situacao;
		this.codigoRetorno = codigoRetorno;
	}
	
	public Declaracao() {
		this("","","","","","",0);
	}

	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getDataDisponivel() {
		return dataDisponivel;
	}

	public void setDataDisponivel(String dataDisponivel) {
		this.dataDisponivel = dataDisponivel;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public int getCodigoRetorno() {
		return codigoRetorno;
	}

	public void setCodigoRetorno(int codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}
}
