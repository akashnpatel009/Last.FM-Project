package anpatel.andrew.cmu;

/**
 * Class Name:  ArtistsActivity
 * Purpose: This class will be the main activity to handle the UI.
 * 			It will initialize the UI elements on main.xml.
 * 			Moreover, it will call the search method of GetArtistsTask and process the JSON that came back.
 * Author: Akash Patel
 **/

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ArtistsActivity extends Activity {

	static TextView foundView;
	static TextView notFoundView;
	static String searchTerm;
	ListView lv;
	ArrayList<ArtistsData> artistData;
	ArrayList<String> aName;

	/*
	 * Override some of the methods of the Activity class to accommodate
	 * the customization. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final ArtistsActivity ip = this;

		Button submitButton = (Button)findViewById(R.id.artist_button);

		lv = (ListView) findViewById(R.id.artists_list); 

		// Set up the list view for onclick event
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// selected item 
				String artistName = ((TextView) view).getText().toString();
				String listeners = artistData.get(position).getListener();
				String artistURL = artistData.get(position).getArtistUrl();
				String imageUrl = artistData.get(position).getImageUrl();

				// Launching new Activity on selecting single List Item
				Intent intent = new Intent(getApplicationContext(), ArtistItemActivity.class);

				// Sending data to new activity
				intent.putExtra("artist", artistName);
				intent.putExtra("listeners", listeners);
				intent.putExtra("artistURL", artistURL);
				intent.putExtra("imageUrl", imageUrl);
				startActivity(intent);	             
			}
		});

		// Add a listener to the send button
		submitButton.setOnClickListener(new OnClickListener(){
			public void onClick(View viewParam) {
				Spinner metroSpinner = (Spinner) findViewById(R.id.metro);
				TextView txtView  =  (TextView)metroSpinner.getSelectedView();
				searchTerm = txtView.getText().toString();

				GetArtistsTask gp = new GetArtistsTask();
				gp.search(searchTerm, ip);
			}
		});
	}

	/* 
	 * This method is called by the GetArtistsTask object when the list is ready.  
	 * It allows for passing back the JSON data for updating the ListView
	 */
	public void extractArtists(String result) {
		artistData = new ArrayList<ArtistsData>();
		aName = new ArrayList<String>();

		if (result.length() == 0) {
			return;
		}

		// Logic to extract data from JSON
		try {
			JSONObject respObj = new JSONObject(result);
			JSONObject topArtistsObj = respObj.getJSONObject("topartists");
			JSONArray artists = topArtistsObj.getJSONArray("artist");
			for(int i=0; i<artists.length(); i++) {
				//Get the Artists one by one
				JSONObject artist = artists.getJSONObject(i);	
				String artistName = artist.getString("name");
				String listeners = artist.getString("listeners");
				String artistURL = artist.getString("url");

				String imageUrl;
				try {
					JSONArray imageUrls = artist.getJSONArray("image");
					imageUrl = null;
					for(int j=0; j<imageUrls.length(); j++) {
						JSONObject imageObj = imageUrls.getJSONObject(j);
						imageUrl = imageObj.getString("#text");
						if(imageObj.getString("size").equals("extralarge")) {
							break;
						}
					}
				} catch (Exception e) {
					imageUrl = null;
				}
				aName.add(artistName);
				artistData.add(new ArtistsData(artistName, artistURL, imageUrl, listeners));				
			}

			// Setup the adapter to put artists data on listview
			ArrayAdapter<String> artistAdapter = new ArrayAdapter<String>(ArtistsActivity.this, android.R.layout.simple_list_item_1, aName);
			lv.setAdapter(artistAdapter);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
