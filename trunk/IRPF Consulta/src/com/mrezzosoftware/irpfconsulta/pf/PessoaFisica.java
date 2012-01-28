package com.mrezzosoftware.irpfconsulta.pf;

public class PessoaFisica {
	
	private String cpf;
	private String ano;
	private String captcha;
	
	public PessoaFisica() {}
	
	public PessoaFisica(String cpf, String ano, String captcha) {
		super();
		this.cpf = cpf;
		this.ano = ano;
		this.captcha = captcha;
	}
	
	
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
