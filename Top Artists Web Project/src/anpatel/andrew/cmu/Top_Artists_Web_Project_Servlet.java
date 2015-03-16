package anpatel.andrew.cmu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.*;


/**
 * Class: Top_Artists_Web_Project_2Servlet
 * Purpose: This servlet would work as a webserver hosted on GAE.
 * 			It will accept request from android device and query Last FM API
 * 			In return, it will send the response back to the device with data.
 * Author: Akash Patel
 **/

@SuppressWarnings("serial")
public class Top_Artists_Web_Project_Servlet extends HttpServlet {

	//This would be a web service to handle HTTP Request and Response
	HttpURLConnection connection = null;  
	PrintWriter outWriter = null;  
	BufferedReader serverResponse = null;  
	
	//doGet method of the Servlet
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//Get the country name from the request
		StringBuffer buff = new StringBuffer(); 
		String country = req.getParameter("country");
		country = country.replace(" ", "+");
		
		//Prepare URL to Last FM API
		String url = "http://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&country=" 
		+ country + "&api_key=659a090b6df3ba15ea9b8eaef959c854&format=json";

		//Create connection
		connection = ( HttpURLConnection ) new URL(url).openConnection();
		//SET REQUEST INFO  
		connection.setRequestMethod( "GET" );  
		connection.setDoOutput( true );  

		outWriter = new PrintWriter( connection.getOutputStream() );

		//Get Response from API
		serverResponse = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );  

		String line;  
		
		// Write back the JSON data as the response to the Android
		while ( (line = serverResponse.readLine() ) != null )   
		{  
			buff.append( line );  
		}
		resp.setContentType("text/plain");
		resp.getWriter().println(buff.toString());
	}
}
