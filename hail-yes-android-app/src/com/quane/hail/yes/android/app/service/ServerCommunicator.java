package com.quane.hail.yes.android.app.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.google.android.maps.GeoPoint;
import com.google.gson.Gson;
import com.quane.hail.yes.HailLocations;
import com.quane.hail.yes.StandardsResource;
import com.quane.hail.yes.android.app.ui.MainController;
import com.quane.hail.yes.user.BasicUser;

public class ServerCommunicator {

	private final MainController mainController;

	private DefaultHttpClient httpClient;
	private HailLocations locations;

	private Gson gson = new Gson();

	public ServerCommunicator(final MainController mainController) {
		this.mainController = mainController;
		httpClient = new DefaultHttpClient();
	}

	public void registerAsDriver() {
		// TODO
	}

	public void registerAsRider() {
		// TODO
	}

	/**
	 * Fetch the current state from the server.
	 * 
	 * @param location
	 */
	public void getCurrentState(final GeoPoint location) {
		System.out.println("Status: contacting server");
		new Thread(new Runnable() {
			public void run() {
				try {
					// Prepare the parameters
					List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
					queryParams.add(new BasicNameValuePair("location",
							"{latitude:" + (location.getLatitudeE6() / 1E6)
									+ ",longitude:"
									+ (location.getLongitudeE6() / 1E6) + "}"));
					queryParams
							.add(new BasicNameValuePair(
									StandardsResource.QUERY_PARAMETER_NAMES.COORDINATES_ARE_E6,
									StandardsResource.QUERY_PARAMETER_VALUES.COORDINATES_ARE_E6_FALSE));
					HttpGet get = new HttpGet(getURI(queryParams));
					final HttpParams params = new BasicHttpParams();
					HttpClientParams.setRedirecting(params, true);
					get.setParams(params);
					System.out.println("Status: calling url: " + get.getURI());
					String response = httpClient.execute(get,
							new BasicResponseHandler());
					System.out.println("Status: response=" + response);
					if (response != null) {
						locations = new HailLocations();
						BasicUser[] users = gson.fromJson(response,
								BasicUser[].class);
						for (BasicUser user : users) {
							locations.getLocations().add(user.getLocation());
						}
						mainController.redrawOverlay(locations.getLocations());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Status: done");
			}
		}).start();
	}

	private URI getURI(List<NameValuePair> queryParams)
			throws URISyntaxException {
		return URIUtils.createURI("http", "www.thanksforhavingsexwithme.com",
				8080, "/hailyes/services",
				URLEncodedUtils.format(queryParams, "UTF-8"), null);
	}
}