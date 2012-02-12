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
import com.quane.hail.yes.HailLocation;
import com.quane.hail.yes.data.HailDAO;
import com.quane.hail.yes.user.AbstractUser;

/**
 * The single access point for clients to communicate with the server.<br/>
 * All requests to add, remove, or query the 'Hail Yes!' data go through this
 * servlet.
 */
@WebServlet(name = "HailServiceServlet", description = "The single access point for clients to communicate with the server.", urlPatterns = { "/services" })
public class HailServiceServlet extends HttpServlet {

	private static final long serialVersionUID = 42L;

	private Logger logger = Logger.getLogger(getClass());

	private Gson gson = new Gson();

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
			List<AbstractUser> users = HailDAO.getUsersNearLocation(location);
			String jsonUsers = gson.toJson(users);
			logger.info("response: " + jsonUsers);
			Writer writer = response.getWriter();
			writer.write(jsonUsers);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error("Something went wrong.", e);
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
		AbstractUser user = getUserFromRequest(request);
		user = HailDAO.saveUserLocation(user);
	}

	/**
	 * Handles DELETE requests to this service<br/>
	 * 
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("doDelete called");
		AbstractUser user = getUserFromRequest(request);
		HailDAO.removeUserLocation(user);
	}

	/**
	 * Extracts a user object from the HTTP request.
	 * 
	 * @param request
	 * @return
	 */
	private AbstractUser getUserFromRequest(HttpServletRequest request) {
		String jsonLocation = request.getParameter("user");
		logger.info("request.location: " + jsonLocation);
		return gson.fromJson(jsonLocation, AbstractUser.class);
	}

	/**
	 * Extracts a location object from the HTTP request.
	 * 
	 * @param request
	 * @return
	 */
	private HailLocation getLocationFromRequest(HttpServletRequest request) {
		String jsonLocation = request.getParameter("location");
		logger.info("request.location: " + jsonLocation);
		return gson.fromJson(jsonLocation, HailLocation.class);
	}
}
