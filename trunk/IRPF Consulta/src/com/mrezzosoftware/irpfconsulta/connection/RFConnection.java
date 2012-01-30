package com.mrezzosoftware.irpfconsulta.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.sax.StartElementListener;
import android.text.Html;
import android.widget.Toast;

import com.mrezzosoftware.irpfconsulta.IRPFConsultaMain;
import com.mrezzosoftware.irpfconsulta.pf.PessoaFisica;

public class RFConnection extends DefaultHttpClient {

	private HttpResponse httpResponse;
	public static final String URL_CAPTCHA_RECEITA = "http://www.receita.fazenda.gov.br/scripts/srf/intercepta/captcha.aspx?opt=image";
	private static final String URL_TELA_COMBO_ANOS_RECEITA = "http://www.receita.fazenda.gov.br/Aplicacoes/Atrjo/ConsRest/Atual.app/index.asp";
	private static final String URL_TELA_RESTITUICAO = "http://www.receita.fazenda.gov.br/Aplicacoes/Atrjo/ConsRest/Atual.app/RESTITUICAO.ASP";

	public RFConnection() {
		// Defini��o de Proxy. Comentar se estiver fora do FNDE.
		//getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("172.21.128.10", 80));
	}

	public String[] getAnosDisponiveisConsulta() {

		try {

			String xPathQuery = "//select[@name='exercicio']/option";

			HttpGet httpGet = new HttpGet(URL_TELA_COMBO_ANOS_RECEITA);

			httpResponse = execute(httpGet);

			HtmlCleaner cleaner = new HtmlCleaner();
			CleanerProperties props = cleaner.getProperties();
			props.setAllowHtmlInsideAttributes(true);
			props.setAllowMultiWordAttributes(true);
			props.setRecognizeUnicodeChars(true);
			props.setOmitComments(true);

			TagNode respostaXml = cleaner.clean(new InputStreamReader(
					httpResponse.getEntity().getContent()));

			Object[] noOptionAnos = respostaXml.evaluateXPath(xPathQuery);

			String[] anos;
			if (noOptionAnos.length > 0) {
				anos = new String[noOptionAnos.length];
				for (int i = 0; i < noOptionAnos.length; i++) {
					TagNode ano = (TagNode) noOptionAnos[i];
					anos[i] = ano.getText().toString();
				}

				return anos;
			}

		} catch (Exception e) {
			IRPFConsultaMain.logError(e);
		}
		return null;
	}

	public Bitmap getImage(String url) {
		try {

			HttpGet httpGet = new HttpGet(url);

			httpResponse = execute(httpGet);

			HttpEntity httpEntity = httpResponse.getEntity();
			BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
					httpEntity);

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

	public void consultarDadosRF(final Context ctx, final PessoaFisica pessoa) {
		try {

			HttpPost httpPost = new HttpPost(URL_TELA_RESTITUICAO);

			definirAtributosPost(httpPost, pessoa);
			definirCabecalhosPost(httpPost);

			httpResponse = execute(httpPost);
			
			//pesquisarxPathHttpResponse("//font[@class='txtNomeContribuinte']", 0);
			pesquisarxPathHttpResponse("//td[@colspan='2']/font[@class='txtDadosContribuinte']", 1);
			
/*			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));

			String line = "";
			while ((line = bReader.readLine()) != null) {
				IRPFConsultaMain.logInfo("Linha: " + line);
			}*/

		} catch (ClientProtocolException e) {
			IRPFConsultaMain.logError(e);
		} catch (IOException e) {
			IRPFConsultaMain.logError(e);
		}
	}

	private void definirAtributosPost(HttpPost post, PessoaFisica pessoa)
			throws UnsupportedEncodingException {

		List<NameValuePair> parametros = new ArrayList<NameValuePair>(5);
		parametros.add(new BasicNameValuePair("CPF", pessoa.getCpf()));
		parametros.add(new BasicNameValuePair("exercicio", pessoa.getAno()));
		parametros.add(new BasicNameValuePair("senha", pessoa.getCaptcha()));
		parametros.add(new BasicNameValuePair("idSom", null));
		parametros.add(new BasicNameValuePair("btnConsultar", "Consultar"));

		post.setEntity(new UrlEncodedFormEntity(parametros));

	}
	private void definirCabecalhosPost(HttpPost post) {

		post.setHeader("Host", "www.receita.fazenda.gov.br");
		post.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");
		post.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		post.setHeader("Accept-Language", "pt-br,pt;q=0.8,en-us;q=0.5,en;q=0.3");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		post.setHeader("Connection", "keep-alive");
		post.setHeader(
				"Referer",
				"http://www.receita.fazenda.gov.br/Aplicacoes/Atrjo/ConsRest/Atual.app/index.ASP");
		// post.setHeader("Cookie",
		// "ASPSESSIONIDQQDCADDC=HOOFJOMBOKJLOPPPKIEOGIBE; cookieCaptcha=BWP6pWB8SoVeT/PdiBSAWs5taWeojZH5g0rWwh0XWPA=");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		// post.setHeader("Content-Length", "71");

	}

	
	public void pesquisarxPathHttpResponse(String xPath, int indiceNo) {

		try {

			String xPathQuery = xPath;

			HtmlCleaner cleaner = new HtmlCleaner();
			CleanerProperties props = cleaner.getProperties();
			props.setAllowHtmlInsideAttributes(true);
			props.setAllowMultiWordAttributes(true);
			props.setRecognizeUnicodeChars(true);
			props.setOmitComments(true);

			TagNode respostaXml = cleaner.clean(new InputStreamReader(
					httpResponse.getEntity().getContent()));

			Object[] no = respostaXml.evaluateXPath(xPathQuery);
			TagNode nome = (TagNode) no[indiceNo];
			String resposta = nome.getText().toString().trim();
			resposta = resposta.contains("Creditado") ? "Creditado" : "";
			IRPFConsultaMain.logInfo("Nome do CPF: " + resposta);

/*			String[] anos;
			if (noOptionAnos.length > 0) {
				anos = new String[noOptionAnos.length];
				for (int i = 0; i < noOptionAnos.length; i++) {
					TagNode ano = (TagNode) noOptionAnos[i];
					anos[i] = ano.getText().toString();
				}
			}*/

		} catch (Exception e) {
			IRPFConsultaMain.logError(e);
		}
	}
	
}
