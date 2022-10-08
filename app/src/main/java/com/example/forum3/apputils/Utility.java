package com.example.forum3.apputils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class Utility {

    private static Utility appUtility = new Utility();
    public Utility() {
    }

    public static Utility getInstance() {
        return appUtility;
    }

    public static Bitmap decodeFile(String path, int swidth, int sheight) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bounds);
        if (bounds.outWidth == -1) {
            return null;
        } else {
            int width = bounds.outWidth;
            int height = bounds.outHeight;
            int sampleSize = 1;
            boolean withinBounds = width <= swidth && height <= sheight;
            if (!withinBounds) {
                float sampleSizeF = Math.max((float)width / (float)swidth, (float)height / (float)sheight);
                sampleSize = Math.round(sampleSizeF);
            }

            BitmapFactory.Options resample = new BitmapFactory.Options();
            resample.inSampleSize = sampleSize;
            return BitmapFactory.decodeFile(path, resample);
        }
    }

    public static void copyStream(InputStream is, OutputStream os) {
        boolean var2 = true;

        try {
            byte[] bytes = new byte[1024];

            while(true) {
                int count = is.read(bytes, 0, 1024);
                if (count == -1) {
                    break;
                }

                os.write(bytes, 0, count);
            }
        } catch (Exception var5) {
        }

    }

    public static String uniqueDeviceID(Context context) {
        @SuppressLint("WrongConstant") TelephonyManager tm = (TelephonyManager)context.getSystemService("phone");
        if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
            try {
                throw new Exception("Implement runtime permission before using this method");
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

        String tmDevice = "" + tm.getDeviceId();
        if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
            try {
                throw new Exception("Implement runtime permission before using this method");
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        String tmSerial = "" + tm.getSimSerialNumber();
        String androidId = "" + Settings.Secure.getString(context.getContentResolver(), "android_id");
        UUID deviceUuid = new UUID((long)androidId.hashCode(), (long)tmDevice.hashCode() << 32 | (long)tmSerial.hashCode());
        return deviceUuid.toString();
    }

    public static boolean checkConnection(Context c) {
        @SuppressLint("WrongConstant") ConnectivityManager cm = (ConnectivityManager)c.getSystemService("connectivity");
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == 1) {
            System.out.println("true wifi");
            return true;
        } else if (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == 0) {
            System.out.println("true edge");
            return true;
        } else if (networkInfo != null && networkInfo.isConnected()) {
            System.out.println("true net");
            return true;
        } else {
            System.out.println("false");
            return false;
        }
    }
    public RequestBody getRequestBody(String params) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params);
    }

    public static void show_snackbar(View view, String error_msg){
        if(view!=null){
            Snackbar snackbar = Snackbar.make(view, error_msg, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    /*public void getLoggedOut(final Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo task = tasks.get(0); // current task
        final ComponentName rootActivity = task.baseActivity;

        String currentPackageName = rootActivity.getPackageName();

        if (currentPackageName.equalsIgnoreCase("com.mysocietyhub.app")) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }*/


}
