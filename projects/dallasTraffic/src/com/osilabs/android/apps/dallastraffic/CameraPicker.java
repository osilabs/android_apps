package com.osilabs.android.apps.dallastraffic;

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
        String mainroad = i.getExtras().getString("mainroad");
        Toast.makeText(this, 
                "Mainroad is " + mainroad, 
                Toast.LENGTH_SHORT).show();
                
        String[] cameras = getResources().getStringArray(R.array.campref_crossroads_US75);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.camera_list_item, cameras));
    }    
 
    public void onListItemClick(
    ListView parent, View v,
    int position, long id) 
    {   
    	String[] cameras = getResources().getStringArray(R.array.campref_crossroads_US75_values);
        Toast.makeText(this, 
            "You have selected " + cameras[position], 
            Toast.LENGTH_SHORT).show();
        
        Bundle info = new Bundle();
        info.putInt("selected_camera", Integer.parseInt(cameras[position]) );
        info.putLong("selected_camera_position", position);
        Intent intent = new Intent();
        intent.putExtras(info);
        setResult(RESULT_OK, intent);
        
        // Return control back to main traffic app
        finish();
    }  
}