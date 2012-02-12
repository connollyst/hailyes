package com.quane.hail.yes.android.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.maps.GeoPoint;
import com.google.gson.Gson;

public class HailCommunicator {

	private static final String SERVICE_URL = "http://10.0.1.4:8080/hailyes/service";

	private HttpClient httpClient;

	public HailCommunicator() {
		httpClient = new DefaultHttpClient();
	}

	public void registerAsHailer() {

	}

	public void registerAsCabbie() {

	}

	public void getCurrentState(final HailActivity hailActivity,
			final GeoPoint location) {
		System.out.println("Status: contacting server");
		new Thread(new Runnable() {
			public void run() {
				try {
					List<NameValuePair> qparams = new ArrayList<NameValuePair>();
					qparams.add(new BasicNameValuePair("location", "{latitude:"
							+ location.getLatitudeE6() + ",longitude:"
							+ location.getLongitudeE6() + "}"));
					HttpGet get = new HttpGet(SERVICE_URL);
					get.setURI(URIUtils.createURI("http", "10.0.1.4", 8080,
							"/hailyes/service",
							URLEncodedUtils.format(qparams, "UTF-8"), null));
					System.out.println("Status: calling url: " + get.getURI());
					String response = httpClient.execute(get,
							new BasicResponseHandler());
					System.out.println("Status: response=" + response);
					if (response != null) {
						Gson gson = new Gson();
						HailLocation[] locations = gson.fromJson(response,
								HailLocation[].class);
						hailActivity.setLocations(locations);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Status: done");
			}
		}).start();
	}
}
