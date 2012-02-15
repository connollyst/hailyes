package com.quane.hail.yes.android.app.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.google.gson.Gson;
import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.android.app.ui.MainController;
import com.quane.hail.yes.resource.StandardsResource;
import com.quane.hail.yes.user.User;

/**
 * 
 * @author Sean Connolly
 */
public class ServerCommunicator {

	private enum ACTION {
		GET, PUT, DELETE
	}

	private static final String TAG = ServerCommunicator.class.getSimpleName();

	private final MainController mainController;

	private DefaultHttpClient httpClient;

	private Gson gson = new Gson();

	/**
	 * 
	 * @param mainController
	 */
	public ServerCommunicator(final MainController mainController) {
		this.mainController = mainController;
		httpClient = new DefaultHttpClient();
	}

	/**
	 * Let the server know that the current user is now looking for a cab.
	 * 
	 * @param me
	 *            the current user
	 */
	public void registerMyself(final User me) {
		new Thread(new Runnable() {
			public void run() {
				try {
					String response = sendMessage(ACTION.PUT, me);
					if (response != null) {
						User[] users = gson.fromJson(response, User[].class);
						mainController.redrawOverlay(Arrays.asList(users));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Let the server know that the current user is no longer looking for a cab
	 * or fare.
	 * 
	 * @param me
	 *            the current user
	 */
	public void unregisterMyself(final User me) {
		new Thread(new Runnable() {
			public void run() {
				try {
					String response = sendMessage(ACTION.DELETE, me);
					if (response != null) {
						User[] users = gson.fromJson(response, User[].class);
						mainController.redrawOverlay(Arrays.asList(users));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Fetch the list of neighbors from the server within range of the current
	 * user.
	 * 
	 * @param me
	 *            the current user
	 */
	public void getNeighbors(final User me) {
		new Thread(new Runnable() {
			public void run() {
				try {
					String response = sendMessage(ACTION.GET, me);
					if (response != null) {
						User[] users = gson.fromJson(response, User[].class);
						mainController.redrawOverlay(Arrays.asList(users));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param action
	 * @param me
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws Exception
	 */
	private String sendMessage(ACTION action, User me)
			throws URISyntaxException, ClientProtocolException, IOException,
			Exception {
		// Prepare the parameters
		SimpleLocation myLocation = me.getLocation();
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("location", "{latitude:"
				+ (myLocation.getLatitudeE6() / 1E6) + ",longitude:"
				+ (myLocation.getLongitudeE6() / 1E6) + "}"));
		queryParams
				.add(new BasicNameValuePair(
						StandardsResource.QUERY_PARAMETER_NAMES.COORDINATES_ARE_E6,
						StandardsResource.QUERY_PARAMETER_VALUES.COORDINATES_ARE_E6_FALSE));
		URI uri = getURI(queryParams);
		// Prepare the request
		HttpRequestBase request = null;
		switch (action) {
		case GET:
			request = new HttpGet(uri);
			break;
		case PUT:
			request = new HttpPut(uri);
			break;
		case DELETE:
			request = new HttpDelete(uri);
			break;
		default:
			throw new Exception("The method '" + action + "' is not supported!");
		}
		// Execute the request and return the response
		Log.v(TAG, "Calling url: " + request.getURI());
		String response = httpClient.execute(request,
				new BasicResponseHandler());
		Log.v(TAG, "Response=" + response);
		return response;
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
