package com.osilabs.android.apps.seattletraffic;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
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
	private String[] mainroads = {"US75", "SH183", "IH35E", "IH635", "US67", "IH20", "Spur 408", "High Five S.E. 1", "High Five S.E. 2", "IH30", "Loop 12", "Spur 366", "IH345", "High Five N.E. 1", "High Five N.E. 2", "High Five S.W. 1", "High Five S.W. 2", "IH30/IH35E Mixmaster", "SH114", "IH35", "IH35W"};
	 
	private String[] mainroadsValues = {"US75", "SH183", "IH35E", "IH635", "US67", "IH20", "Spur_408", "High_Five_S_E__1", "High_Five_S_E__2", "IH30", "Loop_12", "Spur_366", "IH345", "High_Five_N_E__1", "High_Five_N_E__2", "High_Five_S_W__1", "High_Five_S_W__2", "IH30_IH35E_Mixmaster", "SH114", "IH35", "IH35W"};
	 
	private String[][] crossroads = {{"Coit Flyover", "Forest", "Royal", "Meadow North", "Meadow South", "Royal North", "SMU Blvd.", "University", "Walnut Hill", "Northpark", "Caruth Haven", "Lovers Lane", "Mockingbird", "McCommas", "Monticello", "Knox North", "Knox South", "Haskell", "Lemmon", "Hall", "Fitzhugh", "Park Lane", "IH635 North", "Midpark", "Spring Valley", "Belt Line", "Arapaho", "Campbell", "Galatyn Pkwy.", "Dallas/Collin County Line", "SH190 S.W. #1", "SH190 S.W. #2", "SH190 N.E. #1", "SH190 N.E. #2", "15th St.", "Park Blvd.", "Parker South", "Parker North", "Spring Creek Pkwy.", "Legacy", "Ridgemont", "Bethany", "McDermott", "Allen Dr.", "Exchange Pkwy."}
	, {"Esters", "Valley View", "Belt Line", "Story", "MacArthur", "Carl Rd.", "SH114", "Grauwyler", "Profit", "Loop 12", "Wingren"}
	, {"Royal", "Walnut Hill", "Loop 12", "SH183", "Riverfront Blvd.", "Colorado", "East 8th St.", "Louisiana", "Beckley", "Clarendon", "Saner", "US67", "Marsalis", "Danieldale", "Wintergreen", "Commerce", "Spur 366", "Hi Line", "Tollway", "Market Center", "Wycliff", "Medical District Dr.", "Inwood", "Commonwealth", "Mockingbird", "Empire Central", "Regal Row", "Northwest Hwy", "Raceway / Harry Hines", "Spur 482", "Pleasant Run", "Belt Line South", "Reunion", "IH635 South", "Chambers Creek", "SH34", "IH635 North", "Valley View", "Valwood Pkwy.", "Crosby", "Belt Line North", "Luna", "Sandy Lake", "SH190 South", "Parkerville", "US77 North", "FM387", "US287", "Brookside", "US77 South", "Grainery Rd.", "Kiest", "Ann Arbor", "Loop 12", "Laureland", "Camp Wisdom", "Wheatland", "Dallas/Ellis County Line", "SH342", "FM664 (Ovilla Rd.)", "Red Oak Rd.", "SH190 North", "SH121 Bypass", "Business 121", "FM1171 (Main St.)", "Garden Ridge", "Denton Rd.", "Corinth Pkwy.", "Mayhill", "Dallas Dr. (US77)", "US377", "IH35W Split", "Johnston Rd.", "Ellis County Rest Area", "FM329", "Bill Lewis Rd."}
	, {"Rosser", "Ratcliff Pedestrian Bridge", "Josey", "Preston", "Montfort", "Welch", "Harry Hines", "IH30 South", "Towne Centre", "Park Central", "Coit East", "Coit", "TI Blvd West", "TI Blvd East", "Greenville", "Abrams", "Forest", "Skillman", "Miller", "Plano Rd.", "Kingsley", "Jupiter", "Garland Rd.", "Northwest Hwy.", "Centerville", "La Prada", "Oates", "Galloway", "IH30 N.W. 1", "IH30 N.W. 2", "IH35E West", "Town East"}
	, {"Red Bird", "Kiest", "Polk", "Loop 12", "Swansee", "Hampton", "Camp Wisdom", "IH20 North"}
	, {"Kirnwood", "Wheatland", "Hampton", "South Polk", "Willoughby", "Spur 408 East", "Dallas/Tarrant County Line", "Robinson Rd.", "Carrier Pkwy.", "Fish Creek", "Belt Line", "Mountain Creek EB", "Mountain Creek Pkwy.", "Cedar Ridge", "Duncanville Rd.", "Oriole", "Camp Wisdom", "Westmoreland", "Spur 408 EB", "Spur 408 WB", "Spur 408", "IH35E S.W. #1", "IH35E S.W. #2", "IH35E S.W. #3"}
	, {"Loop 12", "Illinois", "Kiest North", "Kiest", "Kiest South", "Grady Niblo", "Artesian Creek NB", "Artesian Creek SB", "IH20 North"}
	, {"High Five S.E. 1"}
	, {"High Five S.E. 2"}
	, {"Hotel St.", "Lamar", "Cadiz", "Akard", "St. Paul", "Good Latimer EB", "Haskell", "Carroll", "East Grand", "St. Francis", "Jim Miller", "Samuell", "Dolphin", "Hunnicut", "IH45", "Good Latimer WB", "2nd Ave.", "US80", "Big Town", "Motley", "Gus Thomasson", "Galloway", "Houston St.", "NW 19th St.", "NW 7th St.", "Belt Line", "MacArthur West", "MacArthur East", "Gifford Hill RR Spur", "Loop 12", "Westmoreland", "Hampton", "Sylvan", "Cesar Chavez Blvd.", "Duncan Perry"}
	, {"Trinity River (Elm Fork)", "SH183 East", "SH183 South", "Grauwyler", "Union Bower", "Irving Blvd.", "Shady Grove", "Trinity River (West Fork)", "Singleton", "IH30 North", "Anderson", "Jefferson", "Keeneland Pkwy."}
	, {"US75", "IH35E North", "IH35E South", "IH35E East", "US75 North", "US75 South", "US75 West"}
	, {"Ross", "Pacific", "Taylor"}
	, {"High Five N.E. 1"}
	, {"High Five N.E. 2"}
	, {"High Five S.W. 1"}
	, {"High Five S.W. 2"}
	, {"IH30/IH35E Mixmaster"}
	, {"Dallas/Tarrant County Line", "Freeport Pkwy.", "Esters", "Belt Line West", "Belt Line East", "Longhorn Dr.", "SH161", "MacArthur", "Hidden Ridge", "OConnor", "Rochelle West", "Rochelle East"}
	, {"Bear Creek"}
	, {"FM2449", "Crawford Rd.", "FM407", "FM1171", "Earnhardt Way", "SH114", "Eagle Pkwy."}
	};
	 
	private String[][] crossroadsValues = {{"29", "31", "43", "52", "53", "93", "112", "113", "283", "284", "285", "286", "287", "288", "289", "290", "291", "292", "293", "294", "314", "315", "521", "522", "523", "524", "527", "530", "532", "575", "544", "545", "542", "543", "525", "537", "538", "539", "546", "534", "541", "529", "535", "526", "531"}
	, {"105", "106", "107", "139", "141", "143", "145", "146", "147", "153", "162"}
	, {"123", "124", "125", "154", "168", "169", "170", "171", "173", "174", "176", "177", "232", "244", "245", "273", "274", "275", "276", "277", "278", "279", "280", "281", "324", "325", "326", "327", "328", "329", "246", "247", "167", "122", "463", "464", "488", "487", "486", "485", "484", "483", "482", "481", "248", "581", "582", "583", "584", "585", "586", "548", "549", "550", "551", "552", "553", "577", "580", "578", "579", "567", "566", "565", "564", "563", "598", "561", "560", "559", "558", "557", "602", "603", "604", "605"}
	, {"127", "128", "129", "134", "135", "136", "137", "330", "331", "412", "185", "413", "444", "445", "446", "447", "448", "449", "450", "451", "452", "453", "454", "455", "456", "457", "458", "459", "460", "461", "229", "600"}
	, {"175", "178", "179", "180", "181", "182", "183", "184"}
	, {"249", "250", "251", "252", "253", "263", "414", "415", "416", "417", "418", "420", "419", "424", "425", "426", "428", "427", "422", "423", "429", "554", "555", "556"}
	, {"254", "255", "256", "257", "258", "259", "260", "261", "262"}
	, {"316"}
	, {"317"}
	, {"359", "360", "361", "362", "363", "366", "369", "370", "371", "372", "373", "377", "378", "374", "367", "365", "368", "383", "437", "438", "439", "462", "357", "588", "589", "590", "591", "592", "593", "594", "595", "596", "597", "606", "587"}
	, {"156", "501", "502", "509", "520", "511", "517", "519", "518", "510", "508", "514", "516"}
	, {"431", "478", "479", "468", "469", "470", "471"}
	, {"432", "433", "434"}
	, {"442"}
	, {"443"}
	, {"440"}
	, {"441"}
	, {"358"}
	, {"489", "490", "491", "492", "493", "494", "495", "496", "497", "498", "499", "500"}
	, {"576"}
	, {"568", "569", "570", "571", "572", "573", "574"}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up our adapter
        mAdapter = new MyExpandableListAdapter();
        setListAdapter(mAdapter);
        registerForContextMenu(getExpandableListView());
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, 
    	int childPosition, long id) { 
    	
    	// Get mainroad
        String[] mainroads = getResources().getStringArray(R.array.campref_mainroads_values);

        // Get crossroad camera id
        int getRes = getResources().getIdentifier("campref_crossroads_" + mainroads[groupPosition] + "_values" , "array", getPackageName());
        String[] crossroads = getResources().getStringArray(getRes);

//    	Toast.makeText(this, "onChildClick."
//    			+ " id:" + Long.toString(id)
//    			+ " Mainroad:" + mainroads[groupPosition]
//    			+ " Crossroad:" + crossroads[childPosition]
//                ,Toast.LENGTH_SHORT).show();
    	
        Intent intent = new Intent();
        Bundle extras = new Bundle();

        // Set the id of the selected camera
        extras.putInt("selected_camera", Integer.parseInt(crossroads[childPosition]) );
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

    public class MyExpandableListAdapter extends BaseExpandableListAdapter {        
        //children[0] = getResources().getStringArray(R.array.campref_crossroads_US75);
        public Object getChild(int groupPosition, int childPosition) {
            return crossroads[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return crossroads[groupPosition].length;
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
            return mainroads[groupPosition];
        }

        public int getGroupCount() {
            return mainroads.length;
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
