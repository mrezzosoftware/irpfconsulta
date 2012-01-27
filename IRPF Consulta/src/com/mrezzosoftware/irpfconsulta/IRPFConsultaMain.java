package com.mrezzosoftware.irpfconsulta;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrezzosoftware.irpfconsulta.connection.RFConnection;

public class IRPFConsultaMain extends Activity {

	private EditText txtCpf;
	private Spinner cboAno;
	private EditText txtCaptcha;
	private Bitmap captcha;
	private ImageView imgRecarregar;
	private ImageButton btConsultar;
	private static final String LOGCAT_TAG = "irpf";
	private HttpClient httpclient;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.irpfconsultamain);
		
		cboAno = (Spinner) findViewById(R.id.cboAno);
		//cboAno.setAdapter(new Array)
		
		
		btConsultar = (ImageButton) findViewById(R.id.btnConsultar);
		btConsultar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				txtCpf = (EditText) findViewById(R.id.txtCpf);
				txtCaptcha = (EditText) findViewById(R.id.txtCaptcha);

				Toast.makeText(
						IRPFConsultaMain.this,
						String.format("CPF: %s \nCaptcha: %s", txtCpf.getText()
								.toString(), txtCaptcha.getText().toString()),
						Toast.LENGTH_SHORT).show();
			}
		});
		
		imgRecarregar = (ImageView) findViewById(R.id.imgRecarregar);
		imgRecarregar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				carregarCaptcha();
			}
		});

		carregarCaptcha();
	}

	// Carrega o captcha do site da receita federal.
	private void carregarCaptcha() {
		new Handler().post(new Runnable() {
			public void run() {

				RFConnection rfconnection = new RFConnection();
				captcha = rfconnection
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
	
	
	
	// Gerenciamento de cookies, caso necessário. Não apagar ainda.
	private void connectTest() throws ClientProtocolException, IOException {

		HttpClient httpclient = new DefaultHttpClient();
		// httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
		// new HttpHost("cache.fnde.gov.br", 80));

		// HttpGet httpget = new
		// HttpGet("http://www.receita.fazenda.gov.br/Aplicacoes/Atrjo/ConsRest/Atual.app/index.asp");
		HttpGet httpget = new HttpGet(
				"http://www.receita.fazenda.gov.br/scripts/srf/intercepta/captcha.aspx?opt=image");

		Log.i("livro", "MEUREQUEST: " + httpget.getURI());

		CookieStore cookieStore = new BasicCookieStore();

		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		HttpResponse response = httpclient.execute(httpget, localContext);
		// HttpResponse response = httpclient.execute(httpget);

		for (Header h : response.getAllHeaders()) {
			Log.i("livro", "HName: " + h.getName());
			Log.i("livro", "HValue: " + h.getValue());

		}

		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie c : cookies) {
			Log.i("livro", "CNome:" + c.getName());
			Log.i("livro", "CValor:" + c.getValue());
		}
	}

	public static void logError(Throwable error) {
		Log.e(IRPFConsultaMain.LOGCAT_TAG, error.getMessage(), error);
	}

	public static void logInfo(String message) {
		Log.i(IRPFConsultaMain.LOGCAT_TAG, message);
	}
}