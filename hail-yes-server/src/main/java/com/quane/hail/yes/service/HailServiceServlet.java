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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.data.IDataAccessObject;
import com.quane.hail.yes.data.SimulationDAO;
import com.quane.hail.yes.exception.HailYesException;
import com.quane.hail.yes.exception.MissingUserException;
import com.quane.hail.yes.user.User;

/**
 * The single access point for clients to communicate with the server.<br/>
 * All requests to add, remove, or query the 'Hail Yes!' data go through this
 * servlet.
 */
@WebServlet(name = "HailServiceServlet", description = "The single access point for clients to communicate with the server.", urlPatterns = { "/services" })
public class HailServiceServlet extends HttpServlet {

	private static final long serialVersionUID = 42L;

	private static final String REQUEST_PARAMETER_USER = "user";

	private Logger logger = Logger.getLogger(getClass());

	private IDataAccessObject dao = new SimulationDAO();
	private ObjectMapper mapper = new ObjectMapper();

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
			User user = getUserFromRequest(request);
			SimpleLocation location = user.getLocation();
			List<User> users = dao.getUsersNearLocation(location);
			writeResponse(response, users);
		} catch (HailYesException hye) {
			throw new ServletException(hye);
		}
		logger.info("doGet done");
	}

	/**
	 * Handles PUT requests to this service.<br/>
	 * Adds the user as looking for a ride/passenger.
	 * 
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("doPut called");
		try {
			User user = getUserFromRequest(request);
			user = dao.saveUserLocation(user);
			List<User> users = dao.getUsersNearLocation(user.getLocation());
			writeResponse(response, users);
		} catch (HailYesException hye) {
			throw new ServletException(hye);
		}
		logger.info("doPut done");
	}

	/**
	 * Handles DELETE requests to this service<br/>
	 * Removes the user as looking for a ride/passenger.
	 * 
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("doDelete called");
		try {
			User user = getUserFromRequest(request);
			dao.removeUserLocation(user);
			List<User> users = dao.getUsersNearLocation(user.getLocation());
			writeResponse(response, users);
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
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private User getUserFromRequest(HttpServletRequest request)
			throws MissingUserException, JsonParseException,
			JsonMappingException, IOException {
		String jsonLocation = request.getParameter(REQUEST_PARAMETER_USER);
		if (jsonLocation == null) {
			throw new MissingUserException(
					"Request is missing parameter named '"
							+ REQUEST_PARAMETER_USER + "'!");
		}
		logger.info("request.user: " + jsonLocation);
		return mapper.readValue(jsonLocation, User.class);
	}

	/**
	 * Writes the JSON string to the HTTP response, flushes, and closes.<br/>
	 * Note, no validation of the JSON is performed at this point.
	 * 
	 * @param response
	 *            the HTTP response object
	 * @param json
	 *            the JSON string to write
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	private void writeResponse(HttpServletResponse response, List<User> users)
			throws JsonGenerationException, JsonMappingException, IOException {
		String json = new ObjectMapper().writerWithType(
				TypeFactory.collectionType(List.class, User.class))
				.writeValueAsString(users);
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
