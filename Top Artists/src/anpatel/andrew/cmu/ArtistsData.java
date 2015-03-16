package anpatel.andrew.cmu;

/**
 * Class Name:  ArtistsData
 * Purpose: This is the container class to hold all data of the artists in a proper format.
 * 			The methods created here are pretty self-explanatory.
 * Author: Akash Patel
 **/

public class ArtistsData {
	
	private String name;
	private String imageUrl;
	private String artistUrl;
	private String listerns;
	
	// Parameterized constructor
	public ArtistsData(String name, String artistUrl, String imageUrl, String listerns) {
		super();
		this.name = name;
		this.imageUrl = imageUrl;
		this.artistUrl = artistUrl;
		this.listerns = listerns;
	}

	public String getName() {
		return name;
	}
	
	public String getListener(){
		return listerns;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getArtistUrl() {
		return artistUrl;
	}

	public void setArtistUrl(String artistUrl) {
		this.artistUrl = artistUrl;
	}

	public String getTrackUrl() {
		return listerns;
	}

	public void setTrackUrl(String listerns) {
		this.listerns = listerns;
	}
}