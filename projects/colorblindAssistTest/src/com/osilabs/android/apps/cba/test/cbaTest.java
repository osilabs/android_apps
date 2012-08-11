package com.osilabs.android.apps.cba.test;

import com.osilabs.android.apps.cba.ColorDrop;
import com.osilabs.android.apps.cba.ColorblindAssist;
import com.osilabs.android.apps.cba.ImageProcessing;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class cbaTest extends ActivityInstrumentationTestCase2<ColorblindAssist> {
	public static int BEHAVIOR_FREE = 0;
	public static int BEHAVIOR_PRO  = 1;
    
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
    public void testReds() {
    	this.rgbCheck("187,74,39", "Red");
    	this.rgbCheck("104,51,34", "Red");
    	this.rgbCheck("210,134,110", "Red");
    	this.rgbCheck("219,31,64", "Red");
    	this.rgbCheck("137,43,60", "Red");
    }
    public void testGreens() {
    	this.rgbCheck("93,107,47", "Green");
    	this.rgbCheck("47,107,86", "Green");
    	this.rgbCheck("170,223,162", "Green");
    	this.rgbCheck("36,78,30", "Green");
    }
    public void testBlues() {
    	this.rgbCheck("30,37,78", "Blue");
    	this.rgbCheck("30,60,78", "Blue");
    }
    public void testPurples() {
    	this.rgbCheck("173,100,215", "Purple");
    }
    public void rgbCheck(String rgb, String expected) {
    	d.setRGB(rgb);assertEquals("Color: " + rgb, expected, ImageProcessing.getColorNameFromRGB(d, this.BEHAVIOR_PRO));
    }
    public void testMinimums() {
        // Test minimum threasholds. i.e. 0.42.0 is minimum for green
        for (int i=42; i<=255; i++) {
            d.R=i; d.G=0; d.B=0;
            this.red( ImageProcessing.getColorNameFromRGB(d, this.BEHAVIOR_PRO), d.R );

            d.R=0; d.G=i; d.B=0;
            this.green( ImageProcessing.getColorNameFromRGB(d, this.BEHAVIOR_PRO), d.G );

            d.R=0; d.G=0; d.B=i;
            this.blue( ImageProcessing.getColorNameFromRGB(d, this.BEHAVIOR_PRO), d.B );
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
