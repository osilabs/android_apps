package com.osilabs.android.apps.dallastraffic;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ListActivity; 
import android.content.Intent;
 
public class CameraPicker extends ListActivity 
{
    @Override  
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.camera_picker);
        
        Intent i = getIntent();
        //String mainroad = i.getExtras().getString("mainroad");
        int mainroad_pos= i.getExtras().getInt("mainroad_pos");
        
    	String[] mainroads = getResources().getStringArray(R.array.campref_mainroads_values);
        
        // Get array of crossroads for the current mainroad
        int getRes = getResources().getIdentifier("campref_crossroads_" + mainroads[ mainroad_pos ] , "array", getPackageName());

        try {
			String[] cameras = getResources().getStringArray(getRes);
			setListAdapter(new ArrayAdapter<String>(this, R.layout.camera_list_item, cameras));
		} catch (Exception e) {
	        Toast.makeText(this, 
	                "Oops. Cameras not found. " + e.getMessage(),
	                Toast.LENGTH_SHORT).show();
		}
    }    
 
    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) 
    {   
    	
        Intent i = getIntent();
//        //String mainroad = i.getExtras().getString("mainroad");
        int mainroad_pos = i.getExtras().getInt("mainroad_pos");
//        10
    	String[] mainroads = getResources().getStringArray(R.array.campref_mainroads_values);
//        
//        // Get array of crossroads for the current mainroad
        int mrGetRes = getResources().getIdentifier("campref_crossroads_" + mainroads[ mainroad_pos ] + "_values" , "array", getPackageName());
//    	
    	String[] cameras = getResources().getStringArray(mrGetRes);

        
        Intent intent = new Intent();
        Bundle extras = new Bundle();

        extras.putInt("selected_camera", Integer.parseInt(cameras[position]) );
        extras.putLong("selected_camera_position", position);
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        
        // Return control back to main traffic app
        finish();
    }  
}