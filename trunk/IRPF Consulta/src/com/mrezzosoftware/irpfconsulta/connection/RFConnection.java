package com.mrezzosoftware.irpfconsulta.connection;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mrezzosoftware.irpfconsulta.IRPFConsultaMain;

public class RFConnection extends DefaultHttpClient {
	
	private HttpEntity httpEntity;
	public static final String URL_CAPTCHA_RECEITA
	= "http://www.receita.fazenda.gov.br/scripts/srf/intercepta/captcha.aspx?opt=image";
	public static final String URL_TELA_COMBO_ANOS_RECEITA
	= "http://www.receita.fazenda.gov.br/Aplicacoes/Atrjo/ConsRest/Atual.app/index.asp";
	
	
	public RFConnection() {
		getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
	}
	
	public String[] getAnosDisponiveisConsulta() {
		
		return null;
	}
	
	public Bitmap getImage(String url) {
		try {
			// Definição de Proxy. Comentar se estiver fora do FNDE.
			getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,	new HttpHost("cache.fnde.gov.br", 80));
			
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
			
			final long tamanhoImagem = bufferedHttpEntity.getContentLength();
			
			if (tamanhoImagem > 0) {
				InputStream is = bufferedHttpEntity.getContent();
				return BitmapFactory.decodeStream(is);
			} else {
				return null;
			}

		} catch (ClientProtocolException e) {
			IRPFConsultaMain.logError(e);
		} catch (IOException e) {
			IRPFConsultaMain.logError(e);
		}
		
		return null;
	}

	
}
