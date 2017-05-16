package tw.idv.madmanchen.mdpermission;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import tw.idv.madmanchen.mdpermissionlib.MDPermission;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HashMap<String, String> map = new HashMap<>();
        map.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "This permission use update");
        map.put(Manifest.permission.READ_SMS, "This permission send sms");
        new MDPermission(this)
                .addPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS)
                .addRationale(map)
                .setRationaleButtonText("好Der")
                .start(new MDPermission.PermissionCallbacks() {
                    @Override
                    public void success(List<String> perms) {
                        Log.i(TAG, "success: " + perms.toString());
                    }

                    @Override
                    public void fail(List<String> perms) {
                        Log.i(TAG, "fail: " + perms.toString());
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MDPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
