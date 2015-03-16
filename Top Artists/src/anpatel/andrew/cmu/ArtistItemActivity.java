package anpatel.andrew.cmu;

/**
 * Class Name:  ArtistItemActivity
 * Purpose: This class will be the second activity to handle the UI.
 * 			It will initialize the UI elements on artist_page.xml.
 * 			Moreover, it will call the load the image on page and set the button to open the web-browser.
 * Author: Akash Patel
 **/

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import anpatel.andrew.cmu.R;

public class ArtistItemActivity extends Activity{

	ArtistItemActivity ia = this;
	
	/*
	 * Override some of the methods of the Activity class to accommodate
	 * the customization. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.artist_page);

		// Creating a new Intent
		Intent i = getIntent();

		// Getting attached intent data
		String artist = i.getStringExtra("artist");
		String listeners = i.getStringExtra("listeners");
		final String artistURL = i.getStringExtra("artistURL");
		String imageUrl = i.getStringExtra("imageUrl");

		// Start loading the image
		new LoadImage().execute(imageUrl);
				
		// Set UI elements
		TextView txtArtistName = (TextView) findViewById(R.id.artist_name);
		TextView txtArtistListners = (TextView) findViewById(R.id.artist_listers);
		Button button = (Button) findViewById(R.id.artist_button);

		// Button Event
		button.setOnClickListener((new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				//Open web broswer on click
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(artistURL));
				startActivity(browserIntent);
			}
		}));

		// Displaying selected artist name
		txtArtistName.setText(artist);
		txtArtistListners.setText("Total listenrs to " + artist + " are:\n" + listeners);
	}

	
	// This class will handle downloading the image and returning it back
	private class LoadImage extends AsyncTask<String, String, Bitmap> {
		Bitmap bitmap;
		private ProgressDialog progDialog;
		ImageView pictureView = (ImageView)findViewById(R.id.artistPicture);
		
		// Set progressbar
		protected void onPreExecute() {
			super.onPreExecute();
			progDialog = ProgressDialog.show(ia, "Search", "Looking for Top Artists..." , true, false);
		}
		
		// Decode the URL and download image in background
		protected Bitmap doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		
		// Dismiss the progessbar at the end and show the image
		protected void onPostExecute(Bitmap image) {
			progDialog.dismiss();
			if(image != null){
				pictureView.setImageBitmap(image);
			}
		}
	}

}
