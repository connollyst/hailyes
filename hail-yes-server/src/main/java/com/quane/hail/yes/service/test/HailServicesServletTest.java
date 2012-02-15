package com.quane.hail.yes.service.test;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.user.User;
import com.quane.hail.yes.user.UserDriver;

/**
 * Servlet implementation class HailServiceServletTest
 */
@WebServlet(description = "A live test suite for the service servlet.", urlPatterns = { "/services/test" })
public class HailServicesServletTest extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final double HOME_LATITUDE = -122.423723;
	private static final double HOME_LONGITUDE = 37.758103;

	private Logger logger = Logger.getLogger(getClass());

	private HttpClient client = new DefaultHttpClient();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HailServicesServletTest() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Writer writer = response.getWriter();
		try {
			writer.write("Running tests against /services now..\n");
			testAddNewRider(writer);
			writer.write("Testing complete.\n");
		} catch (Exception e) {
			logger.error("Failed while testing /services\n", e);
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	/**
	 * 
	 * @param writer
	 * @throws IOException
	 */
	private void testAddNewRider(Writer writer) throws Exception {
		writer.write("=== Testing: add a new rider\n");
		SimpleLocation homeLocation = new SimpleLocation(HOME_LATITUDE,
				HOME_LONGITUDE);
		User requestUser = new UserDriver();
		requestUser.setLocation(homeLocation);
		String jsonRider = gson.toJson(requestUser, User.class);
		writer.write("=== request: " + jsonRider + "\n");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("user", jsonRider));
		URI uri = URIUtils.createURI("http", "localhost", 8080,
				"/hailyes/services", URLEncodedUtils.format(qparams, "UTF-8"),
				null);
		HttpPut put = new HttpPut(uri);
		String response = client.execute(put, new BasicResponseHandler());
		writer.write("=== response: " + response + "\n");
		User responseUser = null;
		try {
			responseUser = gson.fromJson(response, User.class);
		} catch (Exception e) {
			writer.write("=== parsing error: " + e.getMessage() + "\n");
		}
		writer.write("=== parsed response: " + responseUser + "\n");

	}
}
