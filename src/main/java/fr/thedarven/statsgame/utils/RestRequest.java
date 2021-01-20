package fr.thedarven.statsgame.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import fr.thedarven.statsgame.RestGame;

public class RestRequest {
	
	public static String sendPostRequest(String accessToken, String content) throws IOException {
		URL urlForGetRequest = new URL(RestGame.REQUEST_ADDRESS);
		String readLine = null;
		
		HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("access_token", accessToken);
		connection.setDoOutput(true);
		
		OutputStream os = connection.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
		osw.write(content);
		osw.flush();
		osw.close();
		os.close();
		
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			while (Objects.nonNull(readLine = in.readLine())) {
				response.append(readLine);
			}
			in.close();
			return response.toString();
		} else {
			throw new IOException();
		}
	}
	
}
