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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class IRPFConsultaMain extends Activity {
	
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
				connectTest();
				
				Toast.makeText(IRPFConsultaMain.this,
						String.format("CPF: %s \nCaptcha: %s", txtCpf.getText().toString(), txtCaptcha.getText().toString()),
						Toast.LENGTH_SHORT).show();
			}
		});
    }
    
    private void connectTest() {
    	
    	HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.receita.fazenda.gov.br");
		
    	CookieStore cookieStore = new BasicCookieStore();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (Header h : response.getAllHeaders()) {
			Log.i("livro", "HName: " + h.getName());
			Log.i("livro", "HValue: " + h.getValue());
			
		}
		
		
		try {
			response = httpclient.execute(httpget, localContext);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Cookie> cookies = cookieStore.getCookies();
		
		for (Cookie c : cookies) {
			Log.i("livro", "Nome:" + c.getName());
			Log.i("livro", "Valor:" + c.getValue());
		}
    }
}