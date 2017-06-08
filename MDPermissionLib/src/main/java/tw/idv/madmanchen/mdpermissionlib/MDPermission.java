package tw.idv.madmanchen.mdpermissionlib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:      chenshaowei
 * Version      V1.0
 * Description:
 * Modification History:
 * Date         Author          version         Description
 * ---------------------------------------------------------------------
 * 2017/6/7      chenshaowei         V1.0.0          Create
 * Why & What is modified:
 */

public class MDPermission {
    private static final String TAG = "MDPermission";
    private static Activity mActivity;
    private static int requestPermissionCode = 1010;

    public static final int NEVER = -2;
    public static final int RATIONALE = -1;
    public static final int DENIED = 0;
    public static final int GRANTED = 1;

    private Map<String, Integer> requestPermissionsMap = new HashMap<>();

    private static PermissionCallbacks mCallbacks;
    private static AlertDialog.Builder msgBuilder;
    private static String msgBuilderBtnText = "I see";

    /**
     * 建構子
     *
     * @param activity Activity
     */
    public MDPermission(Activity activity) {
        mActivity = activity;
    }

    /**
     * 加入權限
     *
     * @param permissions {@link android.Manifest.permission}
     */
    public MDPermission addPermissions(String... permissions) {
        for (String permission : permissions) {
            // Check permission status
            int permissionStatus = ActivityCompat.checkSelfPermission(mActivity, permission);
            if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                int permissionCode = ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission) ? RATIONALE : DENIED;
                requestPermissionsMap.put(permission, permissionCode);
            }
        }
        return this;
    }

    /**
     * 設定請求碼
     *
     * @param requestCode 請求碼
     */
    public MDPermission setRequestCode(int requestCode) {
        requestPermissionCode = requestCode;
        return this;
    }

    /**
     * 設定訊息視窗
     *
     * @param msg     訊息
     * @param btnText 視窗按鈕文字
     */
    public MDPermission setMessageView(String msg, String btnText) {
        msgBuilder = new AlertDialog.Builder(mActivity);
        msgBuilder.setCancelable(false);
        msgBuilder.setMessage(msg);
        msgBuilderBtnText = btnText;
        return this;
    }

    /**
     * 顯示 app 詳細設定視窗
     *
     * @param context Context
     */
    public static void showDetailSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 開始請求權限
     *
     * @param callbacks 權限取得結果回傳
     */
    public void start(PermissionCallbacks callbacks) {
        mCallbacks = callbacks;
        final List<String> allList = new ArrayList<>();
        List<String> rationaleList = new ArrayList<>();

        Log.i(TAG, "start: " + requestPermissionsMap.toString());
        for (Map.Entry<String, Integer> entry : requestPermissionsMap.entrySet()) {
            allList.add(entry.getKey());
            if (entry.getValue() == RATIONALE) {
                rationaleList.add(entry.getKey());
            }
        }
        // 沒有要取得的權限 回傳取得權限成功
        if (allList.size() == 0) {
            mCallbacks.success();
            return;
        }

        // 判斷是否要顯示權限說明視窗
        if (msgBuilder != null && rationaleList.size() > 0) {
            msgBuilder.setPositiveButton(msgBuilderBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(mActivity, allList.toArray(new String[]{}), requestPermissionCode);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(mActivity, allList.toArray(new String[]{}), requestPermissionCode);
        }
    }

    /**
     * 權限取得回傳
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == requestPermissionCode) {
            List<String> deniedList = new ArrayList<>();
            List<String> alwaysDeniedList = new ArrayList<>();

            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    deniedList.add(permission);
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                        alwaysDeniedList.add(permission);
                    }
                }
            }
            // 判斷使用者是否勾選不再顯示
            if (alwaysDeniedList.size() > 0) {
                mCallbacks.alwaysDenied(alwaysDeniedList);
                return;
            }

            if (deniedList.size() == 0) {
                mCallbacks.success();
            } else {
                mCallbacks.fail(deniedList);
            }
        }
    }
}
