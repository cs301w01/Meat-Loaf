package com.cs301w01.meatload.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import android.content.Context;

import com.cs301w01.meatload.model.SQLiteDBManager;
import com.cs301w01.meatload.model.Picture;
import com.cs301w01.meatload.model.querygenerators.AlbumQueryGenerator;
import com.cs301w01.meatload.model.querygenerators.PictureQueryGenerator;
import com.cs301w01.meatload.model.querygenerators.TagQueryGenerator;

/**
 * Mediates between the GalleryActivity and the DBManager by creating HashMaps of pictures to be
 * displayed in the Gallery View.
 * <p>
 * Can be constructed using an album name as a String or a Collection of tags as Strings. 
 * @author Isaac Matichuk
 * @see SQLiteDBManager
 * @see GalleryActivity
 */
public class GalleryManager implements FController {

	private static final long serialVersionUID = 1L;
	private Context context;
	private String albumName = null;
	private Collection<String> tags;
	boolean isAlbum = false;
	
    public GalleryManager() {
    	//dbMan = new DBManager(context);
    	tags = new ArrayList<String>();
    	isAlbum = false;
    }
    
    /**
     * Constructs the GalleryManager with an Album of Pictures (or All Pictures) as opposed to
     * Pictures with a set of tags.
     * @param albumName String representation of album name to be associated with this object
     */
    public GalleryManager(String albumName) {
    	if (!albumName.equals("All Pictures")) {
    		this.albumName = albumName;
        	isAlbum = true;
    	}
    	tags = new ArrayList<String>();
    	//dbMan = new DBManager(context);
    }
    
    /**
     * Constructs the GalleryManager with all Pictures with a set of Tags as opposed to with an
     * Album.
     * @param tags Collection of tags as Strings
     */
    public GalleryManager(Collection<String> tags) {
    	this.tags = tags;
    	//dbMan = new DBManager(context);
    	isAlbum = false;
    }
    
    public void setContext(Context context){
    	this.context = context;
    }
    
    public void storePhoto(Picture picture) {
    	new PictureQueryGenerator(context).insertPicture(picture);
    }
    
    public Picture getPhoto(int pid) {
    	return new PictureQueryGenerator(context).selectPictureByID(pid);
    }
    
    /**
     * Invokes the DBManager to return a set of Picture objects based on whether GalleryManager
     * was contructed with an album name or a set of tags. 
     * @return ArrayList of HashMaps representing a set of Picture objects
     */
    public ArrayList<HashMap<String, String>> getPictureGallery() {
    	if (isAlbum)
    		return new PictureQueryGenerator(context).selectPicturesFromAlbum(albumName);
    	else if (tags.isEmpty())
    		return new PictureQueryGenerator(context).selectAllPictures();
    	else
    		return new PictureQueryGenerator(context).selectPicturesByTag(tags);
    }
    
    public void deletePicture(int pid) {
    	new PictureQueryGenerator(context).deletePictureByID(pid);
    }
    
    public void deleteAlbum(String name) {
    	new AlbumQueryGenerator(context).deleteAlbumByName(name);
    }
    
    public boolean isAlbum() {
    	return isAlbum;
    }

    public String getAlbumName() {
    	return albumName;
    }
    
    public String getTitle() {
    	
    	if (isAlbum)
    		return albumName;
    	
    	else if (tags.isEmpty())
    		return "All Pictures";
    	
    	else
    		return new TagQueryGenerator(context).stringJoin(tags, ", ");
    }
}
