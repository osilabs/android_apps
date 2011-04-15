package com.osilabs.android.apps.cba.test;

import com.osilabs.android.apps.cba.ColorDrop;
import com.osilabs.android.apps.cba.ColorblindAssist;
import com.osilabs.android.apps.cba.ImageProcessing;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class cbaTest extends ActivityInstrumentationTestCase2<ColorblindAssist> {
    
	private ColorblindAssist mActivity;
    private TextView mView;
    private String resourceString;
    private ColorDrop d;

	public cbaTest() {
		super("com.osilabs.android.apps.cba", ColorblindAssist.class);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
    	d = new ColorDrop();
        
        mActivity = this.getActivity();
        mView = (TextView) mActivity.findViewById(com.osilabs.android.apps.cba.R.id.color_value_display);
        resourceString = mActivity.getString(com.osilabs.android.apps.cba.R.string.app_name);
    }

    public void testPreconditions() {
        assertNotNull(mView);
    }

    public void testText() {
        //assertEquals(resourceString, (String)mView.getText());
        assertEquals(resourceString, "ColorBlindAssist");
    }
    
    public void testMinimums() {
    	// Test minimum threasholds. i.e. 0.42.0 is minimum for green
    	for (int i=42; i<=255; i++) {
    		d.R=i; d.G=0; d.B=0; 
    		this.red( ImageProcessing.getColorNameFromRGB(d), d.R );

    		d.R=0; d.G=i; d.B=0; 
    		this.green( ImageProcessing.getColorNameFromRGB(d), d.G );

    		d.R=0; d.G=0; d.B=i; 
    		this.blue( ImageProcessing.getColorNameFromRGB(d), d.B );
    	}
    }
    public void red(String color, int value) {
    	assertEquals("Red Value:" + Integer.toString(value), "Red", color);    	
    }
    public void green(String color, int value) {
    	assertEquals("Green Value:" + Integer.toString(value), "Green", color);    	
    }
    public void blue(String color, int value) {
    	assertEquals("Blue Value:" + Integer.toString(value), "Blue", color);    	
    }

}
