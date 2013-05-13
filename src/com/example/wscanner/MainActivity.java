package com.example.wscanner;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

public class MainActivity extends Activity implements ScanditSDKListener{
	
	private ScanditSDK mBarcodePicker;
	public static final String sScanditSdkAppKey = "WmtCErlDEeKDcwJF0KbeWeL4inazTtKQNG62ntllh5A";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		initScanner();
	}
	
	public void initScanner(){
		System.out.println("init scanner");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(this, 
				sScanditSdkAppKey, 
				ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);
		
		this.setContentView(picker);
		mBarcodePicker = picker;

		// Register listener, in order to be notified about relevant events 
		// (e.g. a successfully scanned bar code).
		mBarcodePicker.getOverlayView().addListener(this);

		// show search bar in scan user interface
		mBarcodePicker.getOverlayView().showSearchBar(true);

		// To activate recognition of 2d codes
		mBarcodePicker.setQrEnabled(true);
		mBarcodePicker.setDataMatrixEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void didCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didManualSearch(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "User entered: " + arg0, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void didScanBarcode(String barcode, String symbology) {
		// TODO Auto-generated method stub
		System.out.println(barcode);
		System.out.println(symbology);
		
		Log.i("Scanner", barcode);
		Log.i("Scanner", symbology);

		String cleanedBarcode = "";
		for (int i = 0 ; i < barcode.length(); i++) {
			if (barcode.charAt(i) > 30) {
				cleanedBarcode += barcode.charAt(i);
			}
		}

		Toast.makeText(this, symbology + ": " + cleanedBarcode, Toast.LENGTH_LONG).show();
		mBarcodePicker.stopScanning();

	}
	
	@Override
	protected void onPause() {
		// When the activity is in the background immediately stop the 
		// scanning to save resources and free the camera.
		if (mBarcodePicker != null) {
			mBarcodePicker.stopScanning();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// Once the activity is in the foreground again, restart scanning.
		if (mBarcodePicker != null) {
			mBarcodePicker.startScanning();
		}
		super.onResume();
	}
	
	@Override
	public void onBackPressed() {
		mBarcodePicker.stopScanning();
		finish();
	}

}
