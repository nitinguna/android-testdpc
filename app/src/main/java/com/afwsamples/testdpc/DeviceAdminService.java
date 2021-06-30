package com.afwsamples.testdpc;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION_CODES;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * To allow DPC process to be persistent and foreground.
 *
 * @see {@link android.app.admin.DeviceAdminService}
 */
@RequiresApi(api = VERSION_CODES.O)
public class DeviceAdminService extends android.app.admin.DeviceAdminService {

    private final String TAG = "TestDPC-WakeLock";

    private BroadcastReceiver mPackageChangedReceiver;

    private BroadcastReceiver mApkInstallerReceiver;

    private PowerManager.WakeLock wakeLock;
   // private AlarmManager mAlarmManager ;
public Boolean getWakeLockState(){
   return   wakeLock.isHeld();
}

private void acquireWakeLock()
{
    PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
            "MyApp::MyWakelockTag");
    Log.e(TAG,"Acquiring WakeLock");
    wakeLock.acquire();
}



    @Override
    public void onCreate() {
        super.onCreate();
        acquireWakeLock();
        registerPackageChangesReceiver();
        registerApkInstallerReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"Releasing WakeLock");
        wakeLock.release();

        unregisterPackageChangesReceiver();
        unregisterApkInstallerReceiver();
    }

    private void registerPackageChangesReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        mPackageChangedReceiver = new PackageMonitorReceiver();
        getApplicationContext().registerReceiver(mPackageChangedReceiver, intentFilter);
    }

    private void registerApkInstallerReceiver() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("com.afwsamples.testdpc.intent.action.INSTALL_APK");

        mApkInstallerReceiver = new ApkInstaller();
        getApplicationContext().registerReceiver(mApkInstallerReceiver, intentFilter);
        //mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //AlarmUtils.getInstance().setAlarm(mAlarmManager, this, true, true, true, 20 * 1000);


    }

    private void unregisterPackageChangesReceiver() {
        if (mPackageChangedReceiver != null) {
            getApplicationContext().unregisterReceiver(mPackageChangedReceiver);
            mPackageChangedReceiver = null;
        }
    }

    private void unregisterApkInstallerReceiver() {
        if (mApkInstallerReceiver != null) {
            getApplicationContext().unregisterReceiver(mApkInstallerReceiver);
            mApkInstallerReceiver = null;
        }
    }
}
