package com.technext.savings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AndroidStorage extends CordovaPlugin{
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private final String TAG = "AndroidStorage";
	private final String ADD_VIDEO_ID = "add_video_id";
	private final String GET_VIDEO_ID = "get_video_id";
	private final String REMOVE_VIDEO_ID = "remove_video_id";
	private final String QUEUE_SONG_NAME = "queue_song_name";
	private final String SONG_PERCENTAGE = "song_percentage";
	private final String SAVE_SONG_PERCENTAGE = "save_song_percentage";
	private final String GET_SONG_PERCENTAGE = "get_song_percentage";
	private final String ADD_PLAYLIST_ITEMS = "add_playlist_item";
	private final String GET_PLAYLIST_ITEMS = "get_playlist_items";
	private final String DELETE_PLAYLIST_ITEM = "delete_playlist_item";
	private final String PLAYLIST = "playlist";
//	private final String DOWNLOADED = "downloaded";
	
	public AndroidStorage(){
		
	}
	
	public void initialize(){
		prefs = PreferenceManager.getDefaultSharedPreferences(this.cordova.getActivity());
		editor = prefs.edit();
		editor.commit();
	}
	
	public void initializePlaylist(){
		prefs = this.cordova.getActivity().getSharedPreferences(PLAYLIST, Context.MODE_PRIVATE);
		editor = prefs.edit();
		editor.commit();
	}
	
	public void addVideoId(String video_info){
		String video_id, video_name;
		video_id = video_info.split(",")[0].replaceAll("\"", "");
		video_name = video_info.split(",")[1].replaceAll("\"", "");
		Log.e(TAG, "adding: "+video_id);
		editor.putString(video_id, video_name);
		editor.commit();
	}
	
	public String getVideoId(){
		Set<String> keySet = prefs.getAll().keySet();
		if(keySet.isEmpty()){
			Log.e(TAG, "returning: "+"null");
			return "null"; // not found
		}else{
			String vid_id = keySet.iterator().next();
			Log.e(TAG, "returning: "+vid_id);
			return vid_id;
		}
	}
	
	public ArrayList<String> getQueueSongName(){
		Set<String> keySet = prefs.getAll().keySet();
		ArrayList<String> song_name = new ArrayList<String>();
		if(!keySet.isEmpty()){
			Log.e(TAG, "in getSongNmae");
			int i = 0;
			Iterator<String> iterator = keySet.iterator();
			while(iterator.hasNext()){
				song_name.add(prefs.getString(iterator.next(), "Unknown"));
				Log.e(TAG, "items: "+song_name.get(i++));
			}
			Log.e(TAG, "in getPlaysitItems");			
		}
		Log.e(TAG, "in getPlaysitItems");
		return song_name;
	}
	
	public void removeVideoId(String video_id){
		video_id = video_id.replaceAll("\"", "");
		Log.e(TAG, "removing: "+video_id);
		editor.remove(video_id);
		editor.commit();
	}
	
	public void saveSongPercentage(int percentage){
		editor.putInt(SONG_PERCENTAGE, percentage);
		editor.commit();
	}
	
	public int getSongPercentage(){
		try{
			int percentae = prefs.getInt(SONG_PERCENTAGE, 0);
			return percentae;
		}catch(Exception e){
			return 0;
		}
	}
	
	public void addToPlaylist(String playlist_item){
		Log.e(TAG, "adding: "+playlist_item);
		editor.putBoolean(playlist_item, true);
		editor.commit();
	}
	
	public ArrayList<String> getPlaysitItems(){
		Set<String> keySet = prefs.getAll().keySet();
		ArrayList<String> playlist_items = new ArrayList<String>();
		if(!keySet.isEmpty()){
			Log.e(TAG, "in getPlaysitItems");
			int i = 0;
			Iterator<String> iterator = keySet.iterator();
			while(iterator.hasNext()){
				playlist_items.add(iterator.next());
				Log.e(TAG, "items: "+playlist_items.get(i++));
			}
			Log.e(TAG, "in getPlaysitItems");			
		}
		Log.e(TAG, "in getPlaysitItems");
		return playlist_items;
	}
	
	public void removeFromPlaylist(String playlist_item){
		Log.e(TAG, "removing: "+playlist_item);
		editor.remove(playlist_item);
		editor.commit();
	}
	
	@Override
	public boolean execute(String action, String rawArgs,
			CallbackContext callbackContext) throws JSONException {
		initialize();
		if(action.equalsIgnoreCase(ADD_VIDEO_ID)){
			addVideoId(rawArgs);
			callbackContext.success();
		    return true;
		}else if(action.equalsIgnoreCase(GET_VIDEO_ID)){
			callbackContext.success(getVideoId());
		    return true;
		}else if(action.equalsIgnoreCase(REMOVE_VIDEO_ID)){
			removeVideoId(rawArgs);
			callbackContext.success();
		    return true;
		}else if(action.equalsIgnoreCase(SAVE_SONG_PERCENTAGE)){
			saveSongPercentage(Integer.parseInt(rawArgs));
			callbackContext.success();
		    return true;
		}else if(action.equalsIgnoreCase(GET_SONG_PERCENTAGE)){
			callbackContext.success(getSongPercentage());
		    return true;
		}else if(action.equalsIgnoreCase(ADD_PLAYLIST_ITEMS)){
			initializePlaylist();
			addToPlaylist(rawArgs);
			callbackContext.success();
			return true;
		}else if(action.equalsIgnoreCase(DELETE_PLAYLIST_ITEM)){
			initializePlaylist();
			removeFromPlaylist(rawArgs);
			callbackContext.success();
			return true;
		}else if(action.equalsIgnoreCase(GET_PLAYLIST_ITEMS)){
			initializePlaylist();
			ArrayList<String> playlist_items = getPlaysitItems();
			if(playlist_items.isEmpty()){
				callbackContext.success(new JSONArray("[\"null\"]"));
				return true;
			}else{
				callbackContext.success(new JSONArray(playlist_items));
				return true;
			}
		}else if(action.equalsIgnoreCase(QUEUE_SONG_NAME)){
			ArrayList<String> song_names = getQueueSongName();
			if(song_names.isEmpty()){
				callbackContext.success(new JSONArray("[\"null\"]"));
				return true;
			}else{
				callbackContext.success(new JSONArray(song_names));
				return true;
			}
		}
		callbackContext.error("");
		return false;
	}
}
