package com.mrezzosoftware.irpfconsulta;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

				//new Thread(IRPFConsultaMain.this).start();
				try {
					verCaptcha();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Toast.makeText(IRPFConsultaMain.this,
						String.format("CPF: %s \nCaptcha: %s", txtCpf.getText().toString(), txtCaptcha.getText().toString()),
						Toast.LENGTH_SHORT).show();
			}
		});
    }
    
	public void run() {
		// TODO Auto-generated method stub
		try {
			//connectTest();
			verCaptcha();
		} catch (ClientProtocolException e) {
			Log.e("livro", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("livro", e.getMessage(), e);
		}
	}
	
	private void verCaptcha() throws ClientProtocolException, IOException {
		
		new Handler().post(new Runnable() {
			
			public void run() {
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet("http://www.receita.fazenda.gov.br/scripts/srf/intercepta/captcha.aspx?opt=image");
				HttpResponse response = null;
				
				try {
					response = httpclient.execute(httpget);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				HttpEntity httpEntity = response.getEntity();
				BufferedHttpEntity bufferedHttpEntity = null;
				
				try {
					bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final long tamanhoImagem = bufferedHttpEntity.getContentLength();
				
				if (tamanhoImagem > 0) {
					InputStream is = null;
					
					try {
						is = bufferedHttpEntity.getContent();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				    Bitmap bitmap = BitmapFactory.decodeStream(is);
					ImageView imgCaptcha = (ImageView) findViewById(R.id.imgCaptcha);
				    imgCaptcha.setImageBitmap(bitmap);
				    imgCaptcha.invalidate();
				}
				
				

			}
		});
		
		
		

	}
    
    private void connectTest() throws ClientProtocolException, IOException {
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	//httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("cache.fnde.gov.br", 80));
    	
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

}