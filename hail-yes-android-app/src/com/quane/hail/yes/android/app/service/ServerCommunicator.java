package com.quane.hail.yes.android.app.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
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

import android.util.Log;

import com.google.gson.Gson;
import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.android.app.ui.MainController;
import com.quane.hail.yes.resource.StandardsResource;
import com.quane.hail.yes.user.User;

public class ServerCommunicator {

	private static final String TAG = ServerCommunicator.class.getSimpleName();

	private final MainController mainController;

	private DefaultHttpClient httpClient;

	private Gson gson = new Gson();

	public ServerCommunicator(final MainController mainController) {
		this.mainController = mainController;
		httpClient = new DefaultHttpClient();
	}

	/**
	 * Fetch the current state from the server within
	 * 
	 * @param location
	 */
	public void getNeighbors(User me) {
		Log.v(TAG, "Status: contacting server");
		final SimpleLocation myLocation = me.getLocation();
		new Thread(new Runnable() {
			public void run() {
				try {
					// Prepare the parameters
					List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
					queryParams
							.add(new BasicNameValuePair(
									"location",
									"{latitude:"
											+ (myLocation.getLatitudeE6() / 1E6)
											+ ",longitude:"
											+ (myLocation.getLongitudeE6() / 1E6)
											+ "}"));
					queryParams
							.add(new BasicNameValuePair(
									StandardsResource.QUERY_PARAMETER_NAMES.COORDINATES_ARE_E6,
									StandardsResource.QUERY_PARAMETER_VALUES.COORDINATES_ARE_E6_FALSE));
					HttpGet get = new HttpGet(getURI(queryParams));
					final HttpParams params = new BasicHttpParams();
					HttpClientParams.setRedirecting(params, true);
					get.setParams(params);
					Log.v(TAG, "Status: calling url: " + get.getURI());
					String response = httpClient.execute(get,
							new BasicResponseHandler());
					Log.v(TAG, "Status: response=" + response);
					if (response != null) {
						User[] users = gson.fromJson(response, User[].class);
						mainController.redrawOverlay(Arrays.asList(users));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.v(TAG, "Status: done");
			}
		}).start();
	}

	/**
	 * 
	 * @param queryParams
	 * @return
	 * @throws URISyntaxException
	 */
	private URI getURI(List<NameValuePair> queryParams)
			throws URISyntaxException {
		return URIUtils.createURI("http", "www.thanksforhavingsexwithme.com",
				8080, "/hailyes/services",
				URLEncodedUtils.format(queryParams, "UTF-8"), null);
	}
}
