package ru.skytechdev.tskgviewerreborn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LastSeen {
		
	private ArrayList<TSItem> seenList = new ArrayList<TSItem>(); 
	private final int max_list_size = 10;
	private Context context;
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public void loadFromSettings() {	
		File seenFile = new File(context.getDir("data",Context.MODE_PRIVATE),"lastSeen");
		
		try {
			ObjectInputStream seenStream = new ObjectInputStream(new FileInputStream(seenFile));
			
			ArrayList<TSItem> readObject = (ArrayList<TSItem>)seenStream.readObject();
			if (readObject == null) {
				seenList = new ArrayList<TSItem>();
			}
			else {
				seenList = readObject;
			}
			seenStream.close();

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} 		
		catch (ClassNotFoundException e) {

		} 
		/*
		seenList.clear();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
	    int itemCount = sp.getInt("ls_ItemCount", 0);
	    for (int i = 0; i < itemCount; i++) {
	    	TSItem item = new TSItem();//seenList.get(i);
	    	item.value = sp.getString("ls_Caption_"+i, "");
	    	item.url = sp.getString("ls_Url_"+i, "");
	    	item.imgurl = sp.getString("ls_Img_"+i, "");
	    	
	    	seenList.add(item);
	    }
	    */
		
	}
	
	public void saveToSettings() {				
		File seenFile = new File(context.getDir("data",Context.MODE_PRIVATE),"lastSeen");
		
		try {
			ObjectOutputStream seenStream = new ObjectOutputStream(new FileOutputStream(seenFile));
			seenStream.writeObject(seenList);			
			seenStream.flush();
			seenStream.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		/*
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = sp.edit();
	    int itemCount = seenList.size();
	    editor.putInt("ls_ItemCount", itemCount);
	    for (int i = 0; i < itemCount; i++) {
	    	TSItem item = seenList.get(i);
	    	editor.remove("ls_Caption_"+i);
	    	editor.remove("ls_Url_"+i);
	    	editor.remove("ls_Img_"+i);
	    	editor.putString("ls_Caption_"+i, item.value);
	    	editor.putString("ls_Url_"+i, item.url);
	    	editor.putString("ls_Img_"+i, item.imgurl);
	    }
	    editor.commit();
	    */	    
	}
	
	public int getCount() {
		return seenList.size();		
	}
	
	public TSItem get(int id) {
		if (id < 0 || id >= getCount() || getCount() == 0) {
			return new TSItem();
		}
		return seenList.get(id);
	}
	
	public String getCaption(int id) {
		if (id < 0 || id >= getCount() || getCount() == 0) {
			return "";
		}
		return seenList.get(id).value;
	}
	
	public String getUrl(int id) {
		if (id < 0 || id >= getCount() || getCount() == 0) {
			return "";
		}
		return seenList.get(id).url;
	} 
	
	public void add(TSItem val) {
		if (seenList.size() == 0) {
			seenList.add(val);
			return;
		}
		int len = seenList.size();
		if (len+1 > max_list_size) {
			len = max_list_size;
		}
		ArrayList<TSItem> tempList = new ArrayList<TSItem>();
		tempList.add(val);
		for (int i = 0; i < len; i++) {
			if (seenList.get(i).equals(val)) {
				continue;
			}
			tempList.add(seenList.get(i));
		}
		seenList = tempList;
	}
	
}
