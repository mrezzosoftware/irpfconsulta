package com.mrezzosoftware.irpfconsulta;

import com.mrezzosoftware.irpfconsulta.pf.Declaracao;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class IRPFResultadoConsulta extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.irpfresultadoconsulta);
		apresentarDeclaracao();		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	private void apresentarDeclaracao() {
		TextView lblCpf = (TextView) findViewById(R.id.lblResultadoCPF);
		TextView lblNome = (TextView) findViewById(R.id.lblResultadoNome);
		TextView lblBanco = (TextView) findViewById(R.id.lblResultadoBanco);
		TextView lblAgencia = (TextView) findViewById(R.id.lblResultadoAgencia);
		TextView lblLote = (TextView) findViewById(R.id.lblResultadoLote);
		TextView lblDisponivel = (TextView) findViewById(R.id.lblResultadoDisponivel);
		TextView lblSituacao = (TextView) findViewById(R.id.lblResultadoSituacaoRestituicao);
		
		Declaracao declaracao = (Declaracao) getIntent().getSerializableExtra("declaracao");
		
		lblNome.setText(lblNome.getText() + declaracao.getNome());
		lblBanco.setText(lblBanco.getText() + declaracao.getBanco());
		lblAgencia.setText(lblAgencia.getText() + declaracao.getAgencia());
		lblLote.setText(lblLote.getText() + declaracao.getLote());
		lblDisponivel.setText(lblDisponivel.getText() + declaracao.getDataDisponivel());
		lblSituacao.setText(lblSituacao.getText() + declaracao.getSituacao());
	}
}
