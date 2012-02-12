package com.quane.hail.yes.android.app;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

public class HailCommunicator {

	private static final String SERVICE_URL = "http://10.0.1.4:8080/hailyes/service";

	private HttpClient httpClient;

	public HailCommunicator() {
		httpClient = new HttpClient();
	}

	public void registerAsHailer() {

	}

	public void registerAsCabbie() {

	}

	public void getCurrentState() {
		System.out.println("Status: contacting server");
		new Thread(new Runnable() {
			public void run() {
				try {
					GetMethod method = new GetMethod(SERVICE_URL);
					method.setFollowRedirects(true);
					method.setQueryString(new NameValuePair[] { new NameValuePair(
							"location", "{latitude:1234,longitude:4567}") });
					System.out.println("Status: calling url: "
							+ method.getURI());
					int status = httpClient.executeMethod(method);
					if (status == 200) {
						System.out.println("Status: bad status " + status);
					}
					String response = method.getResponseBodyAsString();
					System.out.println("Status: response=" + response);

				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Status: done");
			}
		}).start();
	}
}
