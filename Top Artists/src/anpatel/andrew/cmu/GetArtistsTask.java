package anpatel.andrew.cmu;

/**
 * Class Name:  GetArtistsTask
 * Purpose: This class will perform the communication with the cloud based web-service 
 * 			and retrieve the list of top artist for the country in JSON format
 * Author: Akash Patel
 **/


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class GetArtistsTask {
	
	ArtistsActivity ip = null;
	private ProgressDialog progDialog;
	private Context context;

	/*
	 * search is the public GetArtistsTask method.  
	 * Its arguments are the search term, and the ArtistsActivity object that called it.
	 */
	public void search(String searchTerm, ArtistsActivity ip) {
		this.ip = ip;
		this.context = this.ip.getApplicationContext();
		new AsyncArtistSearch().execute(searchTerm); // Call the Async method with parameter
	}

	/*
	 * AsyncTask provides a simple way to use a thread separate from the UI thread 
	 * in which to do network operations. 
	 * doInBackground would call the search method with country name(s) and get the JSON response
	 * onPostExecute is run in the UI thread, allowing for safe UI updates.
	 */
	private class AsyncArtistSearch extends AsyncTask<String, Void, String> {

		// This inline method 
		protected String doInBackground(String... urls) {
			String searchResult = "";
			try {
				searchResult = search(urls[0]);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return searchResult;
		}

		// Setup the progressbar
		protected void onPreExecute() {
			super.onPreExecute(); 
			progDialog = ProgressDialog.show(ip, "Search", "Looking for Top Artists..." , 
					true, false);
		}

		// Get the result and dismiss the progressbar
		protected void onPostExecute(String result) {
			ip.extractArtists(result);
			progDialog.dismiss();
		}

		/* 
		 * Search the appengine web service for the searchTerm argument
		 * And return a JSON that can be put in an ListView
		 */
		private String search(String searchTerm) throws UnsupportedEncodingException {
			searchTerm = searchTerm.replace(" ", "+");

			String url = "http://topartistsonlastfm.appspot.com/top_artists_web_project_2?country=" 
			+ searchTerm;

			// Make HTTP Request to appengine
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);

			int readCount = 0;
			byte[] buff = new byte[1024];
			String retval = "";

			try {
				// Execute the request
				HttpResponse response = client.execute(request);

				// Process the content. 
				HttpEntity entity = response.getEntity();
				InputStream ist = entity.getContent();
				ByteArrayOutputStream content = new ByteArrayOutputStream();

				while ((readCount = ist.read(buff)) != -1) {
					content.write(buff, 0, readCount);
				}
				retval = new String (content.toByteArray());

			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				return retval;
			}
			
			//Comment the part above and un-comment this part to remove dependency on appengine.

//			String url = "http://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&country=" + searchTerm + "&api_key=659a090b6df3ba15ea9b8eaef959c854&format=json";
//
//			HttpClient client = new DefaultHttpClient();
//			HttpGet request = new HttpGet(url);
//			int readCount = 0;
//			byte[] buff = new byte[1024];
//			String retval = "";
//
//			try {
//				// execute the request
//				HttpResponse response = client.execute(request);
//				StatusLine status = response.getStatusLine();
//
//				if (status.getStatusCode() == 200) {
//					// process the content. 
//					HttpEntity entity = response.getEntity();
//					InputStream ist = entity.getContent();
//					ByteArrayOutputStream content = new ByteArrayOutputStream();
//
//					while ((readCount = ist.read(buff)) != -1) {
//						content.write(buff, 0, readCount);
//					}
//					retval = new String (content.toByteArray());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			finally{
//				return retval;
//			}
		}
	}
}