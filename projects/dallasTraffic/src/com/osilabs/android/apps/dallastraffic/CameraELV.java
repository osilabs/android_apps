package com.osilabs.android.apps.dallastraffic;


//import android.app.ExpandableListActivity;
//import android.os.Bundle;
//import android.widget.ExpandableListAdapter;
//import android.widget.SimpleExpandableListAdapter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
import android.app.ExpandableListActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

public class CameraELV extends ExpandableListActivity {
//    private static final String NAME = "NAME";
//    private static final String IS_EVEN = "IS_EVEN";
//
//    private ExpandableListAdapter mAdapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////    	String[] mainroads = getResources().getStringArray(R.array.campref_mainroads_values);
////    	String[] mainroads2= getResources().getXml(id)
////        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, countries));
//        
//        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
//        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
//        for (int i = 0; i < 20; i++) {
//            Map<String, String> curGroupMap = new HashMap<String, String>();
//            groupData.add(curGroupMap);
//            curGroupMap.put(NAME, "Group " + i);
//            curGroupMap.put(IS_EVEN, (i % 2 == 0) ? "This group is even" : "This group is odd");
//
//            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
//            for (int j = 0; j < 15; j++) {
//                Map<String, String> curChildMap = new HashMap<String, String>();
//                children.add(curChildMap);
//                curChildMap.put(NAME, "Child " + j);
//                curChildMap.put(IS_EVEN, (j % 2 == 0) ? "This child is even" : "This child is odd");
//            }
//            childData.add(children);
//        }
//
//        // Set up our adapter
//        mAdapter = new SimpleExpandableListAdapter(
//                this,
//                mainroads,
//                android.R.layout.simple_expandable_list_item_1,
//                new String[] { NAME, IS_EVEN },
//                new int[] { android.R.id.text1, android.R.id.text2 },
//                childData,
//                android.R.layout.simple_expandable_list_item_2,
//                new String[] { NAME, IS_EVEN },
//                new int[] { android.R.id.text1, android.R.id.text2 }
//                );
//        setListAdapter(mAdapter);

//        // Set up our adapter
//        mAdapter = new SimpleExpandableListAdapter(
//                this,
//                groupData,
//                android.R.layout.simple_expandable_list_item_1,
//                new String[] { NAME, IS_EVEN },
//                new int[] { android.R.id.text1, android.R.id.text2 },
//                childData,
//                android.R.layout.simple_expandable_list_item_2,
//                new String[] { NAME, IS_EVEN },
//                new int[] { android.R.id.text1, android.R.id.text2 }
//                );
//        setListAdapter(mAdapter);
//    }
	
	
    ExpandableListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up our adapter
        mAdapter = new MyExpandableListAdapter();
        setListAdapter(mAdapter);
        registerForContextMenu(getExpandableListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Sample menu");
        menu.add(0, 0, 0, R.string.expandable_list_sample_action);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

        String title = ((TextView) info.targetView).getText().toString();

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
            Toast.makeText(this, title + ": Child " + childPos + " clicked in group " + groupPos,
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            Toast.makeText(this, title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    /**
     * A simple adapter which maintains an ArrayList of photo resource Ids. 
     * Each photo is displayed as an image. This adapter supports clearing the
     * list of photos and adding a new photo.
     *
     */
    public class MyExpandableListAdapter extends BaseExpandableListAdapter {
        // Sample data set.  children[i] contains the children (String[]) for groups[i].
        //private String[] groups = { "People Names", "Dog Names", "Cat Names", "Fish Names" };
        private String[] groups = getResources().getStringArray(R.array.campref_mainroads_values);
//        private String[][] children = {
//                { "Arnold", "Barry", "Chuck", "David" },
//                { "Ace", "Bandit", "Cha-Cha", "Deuce" },
//                { "Fluffy", "Snuggles" },
//                { "Goldy", "Bubbles" }
//        };
        //private String[][] children = new String[1][100];
        
        // = getResources().getStringArray(R.array.campref_crossroads_US75);
        String[] x = getResources().getStringArray(R.array.campref_crossroads_US75);
        private String[][] children = {x};
        //children[0] = getResources().getStringArray(R.array.campref_crossroads_US75);
        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }

        public TextView getGenericView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 64);

            TextView textView = new TextView(CameraELV.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }

        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        public int getGroupCount() {
            return groups.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            TextView textView = getGenericView();
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
