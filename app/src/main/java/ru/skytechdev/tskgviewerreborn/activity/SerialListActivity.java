package ru.skytechdev.tskgviewerreborn.activity;

import ru.skytechdev.tskgviewerreborn.R;
import ru.skytechdev.tskgviewerreborn.engine.TsEngine;
import ru.skytechdev.tskgviewerreborn.serial.SerialsList;
import ru.skytechdev.tskgviewerreborn.structs.TsSerialItem;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SerialListActivity extends Activity implements OnItemClickListener {
	private ProgressDialog ProgressBar;
	private final String LogTag = "SerialListActivity::LOG";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seriallist);
		ListView serialsItems = (ListView)findViewById(R.id.listView1);
		serialsItems.setOnItemClickListener(this);
		
		
		SerialsList serials = TsEngine.getInstance().getSerialList();
		
		int serialsCount = serials.getSerialsCount();
		
		if (serialsCount == 0) {
			Log.d(LogTag,"ERR::serialsCount == 0");
			return;
		}
		
		String serialsElements[];
		serialsElements = new String[serialsCount];
		for (int i = 0; i < serialsCount; i++) {
			serialsElements[i] = serials.getSerialById(i).value;
		}
		
		serialsItems.setAdapter((ListAdapter) new ArrayAdapter<String>(SerialListActivity.this,
				   android.R.layout.simple_list_item_1, serialsElements));		
				
	}

	@Override
	protected void onStart() {
		super.onStart();			
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> notUse1, View notUse2, int itemId, long notUse3) {
		ProgressBar = ProgressDialog.show(SerialListActivity.this, "Загрузка...",
				  "Пожалуйста ждите.... ", true, false);		
		new SerialOpenTask().execute(itemId);
	}

    class SerialOpenTask extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... itemId) {
			boolean result = false;
			
			SerialsList serials = TsEngine.getInstance().getSerialList();
			
			TsSerialItem serial = serials.getSerialById(itemId[0]);
			
			result = TsEngine.getInstance().loadSerialInfo(serial.url);
			
			return result;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar.dismiss();
			if (!result) {
				Toast.makeText(getBaseContext(),
						"Невозможно загрузить сериал",
						Toast.LENGTH_LONG).show();
			}
			else {
				Intent intent = new Intent(SerialListActivity.this, SerialActivity.class);
				startActivity(intent);				
			}
		}
    }	

}
