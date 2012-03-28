package com.cs301w01.meatload.activities;

import android.content.Intent;
import android.widget.Gallery;
import com.cs301w01.meatload.R;
import com.cs301w01.meatload.adapters.HorizontalGalleryAdapter;
import com.cs301w01.meatload.controllers.GalleryManager;
import com.cs301w01.meatload.controllers.PictureManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.cs301w01.meatload.model.Album;
import com.cs301w01.meatload.model.AlbumGallery;
import com.cs301w01.meatload.model.Picture;

/**
* Implements the logic for the TakePictureActivity, as well as the Take Picture dialog.
* @author Joel Burford
*/
public class TakePictureActivity extends Skindactivity {

//TODO: can we move imgOnDisplay into the methods? It would be nice
//if it weren't a global variable
private Bitmap imgOnDisplay;

private PictureManager pictureManager;
private GalleryManager galleryManager;
private Album album;
    private HorizontalGalleryAdapter adapter;
    private Gallery gallery;
    
public void onCreate(Bundle savedInstanceState) {
Bitmap img;////
super.onCreate(savedInstanceState);
setContentView(R.layout.take_picture);

Bundle b = getIntent().getExtras();
        AlbumGallery aGal = (AlbumGallery) b.getSerializable("gallery");
        album = aGal.getAlbum();

        //handle photo consistency gallery logic
        adapter = new HorizontalGalleryAdapter(this, aGal.getPictureGallery(this));

        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(adapter);

        //handle photo consistency gallery logic
        adapter = new HorizontalGalleryAdapter(this, aGal.getPictureGallery(this));

        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(adapter);

        pictureManager = new PictureManager(this, album.getName());
        pictureManager.setContext(this);
imgOnDisplay = pictureManager.generatePicture();
populateFields(imgOnDisplay);
createListeners(imgOnDisplay);
}

protected void populateFields(Bitmap pic){
     ImageView image = (ImageView) findViewById(R.id.imgDisplay);
     image.setImageBitmap(pic);
}

protected void createListeners(Bitmap pic){
//Take Picture button listener
        final Button takePicButton = (Button) findViewById(R.id.takePic);
        takePicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
             takePicture();
            }
        });
        
//Generate Picture button listener
        final Button genPicButton = (Button) findViewById(R.id.genPic);
        genPicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
             imgOnDisplay = pictureManager.generatePicture();
            
             //used http://stackoverflow.com/questions/6772024/how-to-update-or-change-images-of-imageview-dynamically-in-android
             populateFields(imgOnDisplay);
            }
        });
}

    //@Override
    public void update(Object model) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
    /**
* Opens a dialog asking the user if they want to keep the picture they have just taken.
* <p>
* If yes, saves the Picture in the Viewfinder (Or current randomly generated picture)
* @see <a href="http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog">
http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog</a>
*/
    private void takePicture() {

        //TODO: Add album association, and null album checking

AlertDialog.Builder alert = new AlertDialog.Builder(this);

alert.setTitle("Confirm");
alert.setMessage("Are you sure you want this picture?");

alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int whichButton) {

                Picture newPic = pictureManager.takePicture(getFilesDir());

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

alert.show();
    }
    
}