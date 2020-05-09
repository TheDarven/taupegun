package fr.thedarven.statsgame.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.thedarven.statsgame.RestGame;

public class RestRequest {
	
	public static String sendPostRequest(String accessToken, String content) throws IOException {
		URL urlForGetRequest = new URL(RestGame.REQUEST_ADDRESS);
		String readLine = null;
		
		HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
		conection.setRequestMethod("POST");
		conection.setRequestProperty("Content-Type", "application/json");
		conection.setRequestProperty("access_token", accessToken);
		conection.setDoOutput(true);
		
		OutputStream os = conection.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
		osw.write(content);
		osw.flush();
		osw.close();
		os.close();
		
		int responseCode = conection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in .readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			return response.toString();
		} else {
			throw new IOException();
		}
	}
	
}
