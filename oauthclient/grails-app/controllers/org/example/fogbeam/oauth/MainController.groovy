package org.example.fogbeam.oauth

import grails.converters.JSON

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.codehaus.groovy.grails.web.json.JSONObject

class MainController 
{

	String authEndpoint = "https://www.dropbox.com/1/oauth2/authorize";
	String tokenEndpoint = "https://api.dropbox.com/1/oauth2/token";
	String accountInfoEndpoint = "https://api.dropbox.com/1/account/info";

	String clientId = "zu1tk4xu8lqq8m6";
	String clientSecret = "rwdn8kgzo3e7pww";
	String redirectURI = "https://localhost:8443/oauthclient/main/callback";
	
	def index = 
	{
		
		[];	
	}
	
	def login =
	{
		String authRequest = authEndpoint.concat("?response_type=code")
		.concat("&client_id=").concat(clientId)
		.concat("&redirect_uri=").concat(redirectURI)
		.concat("&state=1234");
		
		
		redirect( uri:authRequest );
	}
	
	def callback =
	{
		String code = params.code;
		String state = params.state;
		
		String tokenRequestString =
		tokenEndpoint
		.concat("?grant_type=authorization_code")
		.concat("&client_id=").concat(clientId)
		.concat("&client_secret=").concat(clientSecret)
		.concat("&redirect_uri=")
		.concat(URLEncoder.encode(redirectURI, "UTF-8"))
		.concat("&code=").concat(code);
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost tokenRequest = new HttpPost( tokenRequestString );
		tokenRequest.setHeader("Accept", "application/json");
		
		HttpResponse response = client.execute(tokenRequest);
		
		System.out.println("Response Code : "
			+ response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		JSONObject userJson = JSON.parse(result.toString())
		String token = userJson.access_token;
		session.token = token;
		
		[code:code, state:state, token:token];	
	}
	
	def getAccountInfo =
	{
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getAccountInfoRequest = new HttpGet(accountInfoEndpoint);
		getAccountInfoRequest.addHeader("Accept", "application/json");
		println( "making call with token: " + session.token );
		getAccountInfoRequest.addHeader("Authorization", "Bearer " + session.token );

		HttpResponse response = client.execute(getAccountInfoRequest);
		
		System.out.println("Response Code : "
			+ response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

	
		JSONObject userJson = JSON.parse(result.toString())
		
		[jsonResponse:userJson];
	}	
}