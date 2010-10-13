package com.osilabs.android.apps.lifedropper;

import android.os.AsyncTask;
//import android.widget.TextView;

class HandleFrameTask extends AsyncTask<byte[], Void, Void> {

	@Override
	protected Void doInBackground(byte[]... data) {
		//for(int iByte = 0; iByte < data.length; iByte++)
		//	(char)data[iByte];

		//TextView tv = (TextView) findViewById(R.id.preview_text);
		//tv.setText ((char) data[0]);
		
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    }
}