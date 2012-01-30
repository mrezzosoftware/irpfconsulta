package com.mrezzosoftware.irpfconsulta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mrezzosoftware.irpfconsulta.connection.RFConnection;
import com.mrezzosoftware.irpfconsulta.pf.PessoaFisica;

public class IRPFConsultaMain extends Activity {

	private static final String LOGCAT_TAG = "irpf";
	private EditText txtCpf;
	private AlertDialog.Builder dialogAnos;
	private String[] anosDisponiveis;
	private Button btnAnos;
	private EditText txtCaptcha;
	private Bitmap captcha;
	private ImageView imgRecarregar;
	// Futuro robô para ler o captcha.
	//private ImageView imgCaptcha;
	private ImageButton btConsultar;
	private RFConnection rfConnection = new RFConnection();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.irpfconsultamain);

		txtCpf = (EditText) findViewById(R.id.txtCpf);
		
		btnAnos = (Button) findViewById(R.id.btnAno);
		
		carregarAnos();
		
		btnAnos.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialogAnos.create().show();
			}
		});

		txtCaptcha = (EditText) findViewById(R.id.txtCaptcha);
		carregarCaptcha();
		
		// Futuro robô para ler o captcha.
		//imgCaptcha = (ImageView) findViewById(R.id.imgCaptcha);

		imgRecarregar = (ImageView) findViewById(R.id.imgRecarregar);
		imgRecarregar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				carregarCaptcha();
			}
		});

		btConsultar = (ImageButton) findViewById(R.id.btnConsultar);
		btConsultar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				consultarDados();
				carregarCaptcha();
			}
		});

	}
	
	// Carrega a combo com os anos disponíveis no site da receita federal.
	private void carregarAnos() {
		new Handler().post(new Runnable() {
			public void run() {
				anosDisponiveis = rfConnection.getAnosDisponiveisConsulta();
				
				dialogAnos = new AlertDialog.Builder(IRPFConsultaMain.this)
				.setTitle("Selecione o ano")
				.setItems(anosDisponiveis, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, final int which) {
		            	//final int id = which;
		            	new Handler().post(new Runnable() {
							public void run() {
								btnAnos.setText(anosDisponiveis[which]);
				            	btnAnos.invalidate();
							}
						});
		            }
		        });
			}
		});
	}

	// Carrega o captcha do site da receita federal.
	private void carregarCaptcha() {
		new Handler().post(new Runnable() {
			public void run() {

				//RFConnection rfConnection = new RFConnection();
				captcha = rfConnection
						.getImage(RFConnection.URL_CAPTCHA_RECEITA);

				if (captcha != null) {
					ImageView imgView = (ImageView) findViewById(R.id.imgCaptcha);
					imgView.setImageBitmap(captcha);
				} else {
					Toast.makeText(
							IRPFConsultaMain.this,
							"Falha ao obter captcha do site da Receita Federal.\nTente novamente em alguns segundos.",
							Toast.LENGTH_SHORT);
				}

			}
		});
	}

	protected void consultarDados() {
		new Handler().post(new Runnable() {
			
			public void run() {
				PessoaFisica pessoa = new PessoaFisica(txtCpf.getText().toString(),
						btnAnos.getText().toString(), txtCaptcha.getText()
								.toString());
				//RFConnection rfConnection = new RFConnection();
				rfConnection.consultarDadosRF(IRPFConsultaMain.this, pessoa);
			}
		});
	}

	public static void logError(Throwable error) {
		Log.e(IRPFConsultaMain.LOGCAT_TAG, error.getMessage(), error);
	}

	public static void logInfo(String message) {
		Log.i(IRPFConsultaMain.LOGCAT_TAG, message);
	}
}