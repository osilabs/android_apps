package com.osilabs.android.apps;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;

public class CameraELV extends ExpandableListActivity implements OnChildClickListener {
    ExpandableListAdapter mAdapter;

    // ------------------------------------------------------------
    // ------------------------------------------------------------
    // --------------- CONFIGURE ARRAYS HERE ----------------------
    // ------------------------------------------------------------
    // ------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up our adapter
        mAdapter = new MyExpandableListAdapter();
        setListAdapter(mAdapter);
        registerForContextMenu(getExpandableListView());
    }

    @Override
    public void onPause() {
    	super.onPause();
    	//if(Config.DEBUG>0)Log.d("** CameraELV **", "onPause");
    }

    @Override
    public void onResume() {
    	super.onResume();
    	//if(Config.DEBUG>0)Log.d("** CameraELV **", "onResume");
    }
    
    
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, 
    	int childPosition, long id) { 
    	
    	if(Config.DEBUG>0)Log.d("** osilabs **", "CameraELV::onChildClick groupPos=" + groupPosition + ", childPos=" + childPosition);
    	
        // Get crossroad camera id
        int getRes = getResources().getIdentifier("campref_crossroads_" + Config.mainroadsValues[groupPosition] + "_values" , "array", getPackageName());
        String[] crossroads = getResources().getStringArray(getRes);

//    	Toast.makeText(this, "onChildClick."
//    			+ " id:" + Long.toString(id)
//    			+ " Mainroad:" + mainroads[groupPosition]
//    			+ " Crossroad:" + crossroads[childPosition]
//                ,Toast.LENGTH_SHORT).show();
    	
        Intent intent = new Intent();
        Bundle extras = new Bundle();

        // Set the id of the selected camera
        extras.putString("selected_camera", crossroads[childPosition] );
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
      
        // Return control back to main traffic app
      	finish();

		return false;    	
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	//
    	// Turn this back on to do something on long press.
    	//
    	
    	//Toast.makeText(this, "Tap the entry to select it",
        //        Toast.LENGTH_SHORT).show();
    	// menu.setHeaderTitle("Sample menu");
    	// menu.add(0, 0, 0, R.string.expandable_list_sample_action);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
        Toast.makeText(this, "onContextItemSelected",
                Toast.LENGTH_SHORT).show();
    	
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

        String title = ((TextView) info.targetView).getText().toString();

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
            //Toast.makeText(this, title + ": Child " + childPos + " clicked in group " + groupPos,
            //        Toast.LENGTH_SHORT).show();
            return true;
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            //Toast.makeText(this, title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    public class MyExpandableListAdapter extends BaseExpandableListAdapter {        
        //children[0] = getResources().getStringArray(R.array.campref_crossroads_US75);
        public Object getChild(int groupPosition, int childPosition) {
            return Config.crossroads[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return Config.crossroads[groupPosition].length;
        }

        // FIXME - Use XML to create the group and child groups, define a style there.
        public TextView getGenericView(boolean ismainroads) {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 64);
            TextView textView = new TextView(CameraELV.this);
            textView.setLayoutParams(lp);
            if( ismainroads ) {
            	textView.setTextSize(20);
            	textView.setTextAppearance(getApplicationContext(), R.style.expandableListGroup);
                textView.setBackgroundColor(0xFF6B7594);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                textView.setPadding(0, 0, 40, 0);
            } else {
                textView.setPadding(36, 0, 0, 0);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);            	
            }
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            TextView textView = getGenericView(false);
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }

        public Object getGroup(int groupPosition) {
            return Config.mainroads[groupPosition];
        }

        public int getGroupCount() {
            return Config.mainroads.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            TextView textView = getGenericView(true);
            textView.setText(getGroup(groupPosition).toString());
            return textView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }

    }
}
