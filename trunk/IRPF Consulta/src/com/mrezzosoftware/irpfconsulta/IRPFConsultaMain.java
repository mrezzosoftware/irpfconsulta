package com.mrezzosoftware.irpfconsulta;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class IRPFConsultaMain extends Activity implements Runnable {
	
	private EditText txtCpf;
	private EditText txtCaptcha;
	private ImageButton btConsultar;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.irpfconsultamain);
        btConsultar = (ImageButton) findViewById(R.id.btnConsultar);
        btConsultar.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				txtCpf = (EditText) findViewById(R.id.txtCpf);
				txtCaptcha = (EditText) findViewById(R.id.txtCaptcha);

				new Thread(IRPFConsultaMain.this).start();
				
				Toast.makeText(IRPFConsultaMain.this,
						String.format("CPF: %s \nCaptcha: %s", txtCpf.getText().toString(), txtCaptcha.getText().toString()),
						Toast.LENGTH_SHORT).show();
			}
		});
    }
    
    private void connectTest() throws ClientProtocolException, IOException {
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("cache.fnde.gov.br", 80));
    	
    	//HttpGet httpget = new HttpGet("http://www.receita.fazenda.gov.br/Aplicacoes/Atrjo/ConsRest/Atual.app/index.asp");
    	HttpGet httpget = new HttpGet("http://www.receita.fazenda.gov.br/scripts/srf/intercepta/captcha.aspx?opt=image");
		
		Log.i("livro", "MEUREQUEST: " + httpget.getURI());
		
    	CookieStore cookieStore = new BasicCookieStore();
    	
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		HttpResponse response = httpclient.execute(httpget, localContext);
		//HttpResponse response = httpclient.execute(httpget);
		
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

	public void run() {
		// TODO Auto-generated method stub
		try {
			connectTest();
		} catch (ClientProtocolException e) {
			Log.e("livro", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("livro", e.getMessage(), e);
		}
	}
}