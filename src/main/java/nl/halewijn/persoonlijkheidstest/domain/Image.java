package nl.halewijn.persoonlijkheidstest.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Images")
public class Image {
	
	@Id
	@GeneratedValue
	private int imageId;
	
	private String imagePath;
	
	private String imageAlt;
	
	private String imageDescription;
	
	public int getImageId() {
		return this.imageId;
	}
	
	public Image() {
		
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageAlt() {
		return imageAlt;
	}

	public void setImageAlt(String imageAlt) {
		this.imageAlt = imageAlt;
	}

	public String getImageDescription() {
		return imageDescription;
	}

	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}	
}