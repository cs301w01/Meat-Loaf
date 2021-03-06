package com.cs301w01.meatload.activities;

import android.content.Intent;
import android.widget.Gallery;
import com.cs301w01.meatload.R;
import com.cs301w01.meatload.adapters.HorizontalGalleryAdapter;
import com.cs301w01.meatload.controllers.GalleryManager;
import com.cs301w01.meatload.controllers.PictureCreator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.cs301w01.meatload.model.Album;
import com.cs301w01.meatload.model.Picture;
import com.cs301w01.meatload.model.PictureGenerator;
import com.cs301w01.meatload.model.gallery.AlbumGallery;

/**
 * Implements the logic for the TakePictureActivity, as well as the Take Picture
 * dialog.
 * 
 * @author Joel Burford
 */
public class TakePictureActivity extends Skindactivity {

	private Bitmap imgOnDisplay;

	private Album album;
	private HorizontalGalleryAdapter adapter;
	private Gallery gallery;
	private AlertDialog currentDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_picture);

		Bundle b = getIntent().getExtras();
		album = (Album) b.getSerializable("album");
		GalleryManager gMan = new GalleryManager(this, new AlbumGallery(album));
		gMan.setContext(this);

		// handle photo consistency gallery logic
		adapter = new HorizontalGalleryAdapter(this, gMan.getPictureGallery(),
				R.styleable.TakePictureActivity,
				R.styleable.TakePictureActivity_android_galleryItemBackground);

		gallery = (Gallery) findViewById(R.id.takePictureGallery);
		gallery.setAdapter(adapter);

		imgOnDisplay = new PictureGenerator().generatePicture();
		populateFields(imgOnDisplay);
		createListeners(imgOnDisplay);
	}
	
	@Override
	public void finish() {
		
		if (currentDialog != null) {
			if (currentDialog.isShowing()) {
				currentDialog.dismiss();
			}
		}

		super.finish();
	}

	/**
	 * Populates all dynamic fields on the screen.
	 * 
	 * @param Bitmap
	 *            - The picture to display
	 * @see <a href=
	 *      "http://stackoverflow.com/questions/6772024/how-to-update-or-change-images-of-imageview-dynamically-in-android"
	 *      >
	 *      http://stackoverflow.com/questions/6772024/how-to-update-or-change-images-of-imageview-dynamically-in-android </a>
	 */
	protected void populateFields(Bitmap pic) {
		ImageView image = (ImageView) findViewById(R.id.imgDisplay);
		image.setImageBitmap(pic);
	}

	protected void createListeners(Bitmap pic) {
		// Take Picture button listener
		final Button takePicButton = (Button) findViewById(R.id.takePic);
		takePicButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				takePicture();
			}
		});

		// Generate Picture button listener
		final Button genPicButton = (Button) findViewById(R.id.genPic);
		genPicButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				imgOnDisplay = new PictureGenerator().generatePicture();

				// used
				// http://stackoverflow.com/questions/6772024/how-to-update-or-change-images-of-imageview-dynamically-in-android
				populateFields(imgOnDisplay);
			}
		});
	}


	public void update(Object model) {

	}

	/**
	 * Opens a dialog asking the user if they want to keep the picture they have
	 * just taken.
	 * <p>
	 * If yes, saves the Picture in the Viewfinder (Or current randomly
	 * generated picture)
	 * 
	 * @see <a
	 *      href="http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog">
	 *      http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog</a>
	 */
	private void takePicture() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Confirm");
		alert.setMessage("Are you sure you want this picture?");

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int whichButton) {
				
				if (album == null) {
					dialog.dismiss();
					return;
				}
				
				Picture newPic = new PictureCreator(TakePictureActivity.this)
						.takePicture(getFilesDir(), imgOnDisplay, album.getName());

				Intent myIntent = new Intent();
				myIntent.setClassName("com.cs301w01.meatload",
						"com.cs301w01.meatload.activities.EditPictureActivity");
				myIntent.putExtra("picture", newPic);

				startActivity(myIntent);

				finish();
				
			}
			
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		currentDialog = alert.show();
	}
	
	public void performDialogClick(boolean button) {
		if (button == true) {
			currentDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
		} else {
			currentDialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();
		}
	}

}