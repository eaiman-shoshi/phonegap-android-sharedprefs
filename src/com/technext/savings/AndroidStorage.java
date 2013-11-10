package com.technext.savings;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AndroidStorage extends CordovaPlugin{
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private final String SONG_PERCENTAGE = "song_percentage";
	private final String TAG = "AndroidStorage";
	private final String ADD_VIDEO_ID = "add_video_id";
	private final String GET_VIDEO_ID = "get_video_id";
	private final String REMOVE_VIDEO_ID = "remove_video_id";
	private final String SAVE_SONG_PERCENTAGE = "save_song_percentage";
	private final String GET_SONG_PERCENTAGE = "get_song_percentage";
	
	public AndroidStorage(){
		
	}
	
	public void initialize(){
		prefs = PreferenceManager.getDefaultSharedPreferences(this.cordova.getActivity());
		editor = prefs.edit();
		editor.commit();
	}
	
	public void addVideoId(String video_id){
		Log.e(TAG, "adding: "+video_id);
		editor.putBoolean(video_id, true);
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
	
	public void removeVideoId(String video_id){
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
		}
		callbackContext.error("");
		return false;
	}
}
