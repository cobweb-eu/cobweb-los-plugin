package ie.ucd.cobweb.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import android.os.Build;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.hardware.*;

import java.util.*;

public class COBWEBSensorPlugin extends CordovaPlugin implements
		SensorEventListener {

	private float[] accel, orient;
	final private static int NMAT = 9;

	// Actions
	final private static String LOS = "lineOfSight";
	final private static String DEVI = "deviceInfo";

	// Device meta data
	final private static String MKMOD = "Make and Model";
	final private static String OSVER = "OS Version";
	final private static char SP = ' ';

	// Compass attributes
	private static final String AZIMUTH = "Azimuth";
	private static final String PITCH = "Pitch";
	private static final String ROLL = "Roll";

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		SensorManager mSensorManager = (SensorManager) this.cordova
				.getActivity().getSystemService(Context.SENSOR_SERVICE);
		Sensor mCompass = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mSensorManager.registerListener(this, mCompass,
				SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
		Sensor mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
	}

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (action.equals(LOS)) {
			if (orient == null)
				return false;
			JSONObject compass = new JSONObject();
			
			// Convert radians to degrees
			double degO[]=new double[3];
			degO[0]=Math.toDegrees(orient[0]);
			degO[1]=Math.toDegrees(orient[1]);
			degO[2]=Math.toDegrees(orient[2]);
			
			compass.put(AZIMUTH, degO[0]);
			compass.put(PITCH, degO[1]);
			compass.put(ROLL, degO[2]);
			callbackContext.success(compass);
			return true;

		} else if (action.equals(DEVI)) {
			JSONObject info = new JSONObject();
			String mkM = Build.MODEL.toUpperCase();
			if (!mkM.startsWith(Build.MANUFACTURER.toUpperCase()))
				mkM = Build.MANUFACTURER.toUpperCase() + SP + mkM;
			info.put(MKMOD, mkM);
			info.put(OSVER, Build.VERSION.RELEASE);
			callbackContext.success(info);
			return true;
		}
		return false;

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		int type = event.sensor.getType();
		if (type == Sensor.TYPE_ACCELEROMETER) {
			accel = event.values;
		} else {
			float[] magF = event.values;
			if (accel != null) {
				float[] rMat = new float[NMAT];
				if (SensorManager.getRotationMatrix(rMat, null, accel, magF)) {
					orient = new float[3];		
					SensorManager.getOrientation(rMat, orient);
				}
			}
		}
	}

}
