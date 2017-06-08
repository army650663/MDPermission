package tw.idv.madmanchen.mdpermission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import tw.idv.madmanchen.mdpermissionlib.MDPermission;
import tw.idv.madmanchen.mdpermissionlib.PermissionCallbacks;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.CAMERA
        };

        new MDPermission(this)
                .addPermissions(permissions)
                .setRequestCode(1111)
                .setMessageView("需要權限才可以完整運行", "好吧")
                .start(new PermissionCallbacks() {
                    @Override
                    public void success() {
                        Log.i(TAG, "success: ");
                    }

                    @Override
                    public void fail(List<String> deniedList) {
                        Log.i(TAG, "fail: " + deniedList.toString());
                    }

                    @Override
                    public void alwaysDenied(List<String> alwaysDeniedList) {
                        Log.i(TAG, "alwaysDenied: " + alwaysDeniedList.toString());
                        MDPermission.showDetailSetting(MainActivity.this);
                    }
                });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MDPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
