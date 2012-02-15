package com.quane.hail.yes.service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quane.hail.yes.HailLocation;
import com.quane.hail.yes.data.RandomPointDAO;
import com.quane.hail.yes.data.IDataAccessObject;
import com.quane.hail.yes.exception.HailYesException;
import com.quane.hail.yes.exception.MissingLocationException;
import com.quane.hail.yes.exception.MissingUserException;
import com.quane.hail.yes.user.BasicUser;

/**
 * The single access point for clients to communicate with the server.<br/>
 * All requests to add, remove, or query the 'Hail Yes!' data go through this
 * servlet.
 */
@WebServlet(name = "HailServiceServlet", description = "The single access point for clients to communicate with the server.", urlPatterns = { "/services" })
public class HailServiceServlet extends HttpServlet {

	private static final long serialVersionUID = 42L;

	private static final String REQUEST_PARAMETER_USER = "user";
	private static final String REQUEST_PARAMETER_LOCATION = "location";

	private Logger logger = Logger.getLogger(getClass());

	private IDataAccessObject dao = new RandomPointDAO();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Default constructor.
	 * 
	 * @see HttpServlet#HttpServlet()
	 */
	public HailServiceServlet() {
		super();
	}

	/**
	 * Handles GET requests to this service.<br/>
	 * GET requests mean 'tell me whats going on around me'. As such, we require
	 * the requester provide location information and in return we provide a
	 * list of locations mapping out where cabs and hailers are now.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("doGet called");
		try {
			logger.info("Getting location");
			HailLocation location = getLocationFromRequest(request);
			List<BasicUser> users = dao.getUsersNearLocation(location);
			String jsonUsers = gson.toJson(users);
			writeResponse(response, jsonUsers);
		} catch (HailYesException hye) {
			throw new ServletException(hye);
		}
		logger.info("doGet done");
	}

	/**
	 * Handles PUT requests to this service.<br/>
	 * 
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("doPut called");
		try {
			BasicUser user = getUserFromRequest(request);
			user = dao.saveUserLocation(user);
			String jsonUser = gson.toJson(user, BasicUser.class);
			writeResponse(response, jsonUser);
		} catch (HailYesException hye) {
			throw new ServletException(hye);
		}
		logger.info("doPut done");
	}

	/**
	 * Handles DELETE requests to this service<br/>
	 * 
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("doDelete called");
		try {
			BasicUser user = getUserFromRequest(request);
			dao.removeUserLocation(user);
			// no response necessary
		} catch (HailYesException hye) {
			throw new ServletException(hye);
		}
		logger.info("doDelete done");
	}

	/**
	 * Extracts a user object from the HTTP request.
	 * 
	 * @param request
	 * @return
	 */
	private BasicUser getUserFromRequest(HttpServletRequest request)
			throws MissingUserException {
		String jsonLocation = request.getParameter(REQUEST_PARAMETER_USER);
		if (jsonLocation == null) {
			throw new MissingUserException(
					"Request is missing parameter named '"
							+ REQUEST_PARAMETER_USER + "'!");
		}
		logger.info("request.user: " + jsonLocation);
		return gson.fromJson(jsonLocation, BasicUser.class);
	}

	/**
	 * Extracts a location object from the HTTP request.
	 * 
	 * @param request
	 * @return
	 */
	private HailLocation getLocationFromRequest(HttpServletRequest request)
			throws MissingLocationException {
		String jsonLocation = request.getParameter(REQUEST_PARAMETER_LOCATION);
		if (jsonLocation == null) {
			throw new MissingLocationException(
					"Request is missing parameter named '"
							+ REQUEST_PARAMETER_LOCATION + "'!");
		}
		logger.info("request.location: " + jsonLocation);
		return gson.fromJson(jsonLocation, HailLocation.class);
	}

	/**
	 * Writes the JSON string to the HTTP response, flushes, and closes.<br/>
	 * Note, no validation of the JSON is performed at this point.
	 * 
	 * @param response
	 *            the HTTP response object
	 * @param json
	 *            the JSON string to write
	 */
	private void writeResponse(HttpServletResponse response, String json) {
		logger.info("response: " + json);
		Writer writer = null;
		try {
			writer = response.getWriter();
			writer.write(json);
			writer.flush();
		} catch (IOException ioe1) {
			logger.error("", ioe1);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ioe2) {
					logger.error("", ioe2);
				}
			}
		}
	}
}
