package com.quane.hail.yes.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The single access point for clients to communicate with the server.<br/>
 * All requests to add, remove, or query the 'Hail Yes!' data go through this
 * servlet.
 */
@WebServlet(name = "HailServiceServlet", description = "The single access point for clients to communicate with the server.", urlPatterns = { "/service" })
public class HailServiceServlet extends HttpServlet {

	private static final long serialVersionUID = 42L;

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

	}

	/**
	 * Handles PUT requests to this service.<br/>
	 * 
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO create or update a cabbie or hailer
	}

	/**
	 * Handles DELETE requests to this service<br/>
	 * 
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO remove a cabbie or hailer
	}

}
