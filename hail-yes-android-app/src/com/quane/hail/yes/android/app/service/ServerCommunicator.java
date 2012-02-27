package com.quane.hail.yes.android.app.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

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

	private DefaultHttpClient httpClient = new DefaultHttpClient();

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * @param mainController
	 */
	public ServerCommunicator(final MainController mainController) {
		this.mainController = mainController;
	}

	/**
	 * Fetch the list of neighbors from the server within range of the current
	 * user.
	 * 
	 * @param me
	 *            the current user
	 */
	public void getNeighbors(final User me) {
		Log.v(TAG, "Fetching a list of folks around me.");
		sendMessage(ACTION.GET, me);
	}

	/**
	 * Let the server know that the current user is now looking for a cab.
	 * 
	 * @param me
	 *            the current user
	 */
	public void registerMyself(final User me) {
		Log.v(TAG, "Registering myself as searching.");
		me.setIsSearching(true);
		sendMessage(ACTION.PUT, me);
	}

	/**
	 * Let the server know that the current user is no longer looking for a cab
	 * or fare.
	 * 
	 * @param me
	 *            the current user
	 */
	public void unregisterMyself(final User me) {
		Log.v(TAG, "Unregistering myself as searching.");
		me.setIsSearching(false);
		sendMessage(ACTION.DELETE, me);
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
	private void sendMessage(final ACTION action, final User me) {
		new Thread(new Runnable() {
			public void run() {
				try {
					// Prepare the parameters
					List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
					queryParams.add(new BasicNameValuePair("user", mapper
							.writeValueAsString(me)));
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
						throw new Exception("The method '" + action
								+ "' is not supported!");
					}
					// Execute the request and return the response
					try {
						Log.v(TAG, "Calling url: " + request.getURI());
						String response = httpClient.execute(request,
								new BasicResponseHandler());
						Log.v(TAG, "Response=" + response);
						User[] users = mapper.readValue(response, User[].class);
						mainController.redrawOverlay(Arrays.asList(users));
					} catch (HttpResponseException re) {
						switch (re.getStatusCode()) {
						case 500:
							throw new Exception(re.getStatusCode()
									+ ": The server couldn't interpret"
									+ " the request!", re);
						default:
							throw new Exception(re.getStatusCode() + ": "
									+ re.getLocalizedMessage(), re);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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
