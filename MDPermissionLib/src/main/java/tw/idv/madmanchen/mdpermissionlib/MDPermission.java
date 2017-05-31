package tw.idv.madmanchen.mdpermissionlib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author:      chenshaowei
 * Version      V1.0
 * Description:
 * Modification History:
 * Date         Author          version         Description
 * ---------------------------------------------------------------------
 * 2017/5/12      chenshaowei         V1.0.0          Create
 * What is modified:
 */

public class MDPermission {
    public static final String TAG = "MDPermission";
    private static int mRequestCode = 111;
    private Activity mActivity;
    private AlertDialog.Builder mBuilder;
    private String[] mPermissions;
    private String mRationaleBtnText = "OK";
    private Map<String, String> mRationaleMap;
    private DialogInterface.OnClickListener mRationaleOnClickListener;
    private static PermissionCallbacks mCallbacks;

    public MDPermission(Activity activity) {
        mActivity = activity;
        mBuilder = new AlertDialog.Builder(mActivity);
        mBuilder.setCancelable(false);
    }

    public MDPermission(Fragment fragment) {
        mActivity = fragment.getActivity();
        mBuilder = new AlertDialog.Builder(mActivity);
        mBuilder.setCancelable(false);
    }

    /**
     * 設定回傳 Code
     *
     * @param requestCode 回傳
     */
    public MDPermission setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    /**
     * 加入要求權限
     *
     * @param permissions 權限字串或陣列
     */
    public MDPermission addPermissions(String... permissions) {
        mPermissions = permissions;
        return this;
    }

    /**
     * 加入權限說明
     *
     * @param rationaleMap 權限對照 key = 權限 : value = 說明
     */
    public MDPermission addRationale(Map<String, String> rationaleMap) {
        mRationaleMap = rationaleMap;
        return this;
    }

    /**
     * 設定說明視窗按鈕文字
     *
     * @param btnText 按鈕文字
     */
    public MDPermission setRationaleButtonText(String btnText) {
        mRationaleBtnText = btnText;
        return this;
    }

    /**
     * 設定說明視窗按鈕文字監聽器
     *
     * @param onClickListener 監聽器
     */
    public MDPermission setRationaleOnClickListener(DialogInterface.OnClickListener onClickListener) {
        mRationaleOnClickListener = onClickListener;
        return this;
    }

    /**
     * 開始請求權限
     *
     * @param callbacks 要求權限結果回傳
     */
    public void start(PermissionCallbacks callbacks) {
        mCallbacks = callbacks;
        List<String> permissionList = new ArrayList<>(mPermissions.length);

        // 檢查是否需要要求權限
        for (final String permission : mPermissions) {
            if (!havePermission(permission)) {
                permissionList.add(permission);
            }
        }

        if (permissionList.size() > 0) {
            for (final String permission : permissionList) {
                // 檢查是否要顯示權限說明
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission) && mRationaleMap != null) {
                    String rationale = mRationaleMap.get(permission);
                    if (rationale != null) {
                        mBuilder.setMessage(rationale);
                        if (mRationaleOnClickListener != null) {
                            mBuilder.setNegativeButton(mRationaleBtnText, mRationaleOnClickListener);
                        } else {
                            mBuilder.setNegativeButton(mRationaleBtnText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(mActivity, new String[]{permission}, mRequestCode);
                                }
                            });
                        }
                        mBuilder.show();
                    } else {
                        ActivityCompat.requestPermissions(mActivity, new String[]{permission}, mRequestCode);
                    }
                } else { // 無權限說明 直接要求權限
                    ActivityCompat.requestPermissions(mActivity, mPermissions, mRequestCode);
                }

            }
        } else {
            callbacks.success(null);
        }
    }

    /**
     * 檢查是否有權限
     *
     * @param permission 權限
     */
    private boolean havePermission(String permission) {
        int isGrant = ContextCompat.checkSelfPermission(mActivity, permission);
        return isGrant == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 要求權限結果回傳
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mRequestCode) {
            List<String> successList = new ArrayList<>();
            List<String> failList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                boolean isGrant = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                if (isGrant) {
                    successList.add(permissions[i]);
                } else {
                    failList.add(permissions[i]);
                }
            }

            if (failList.size() > 0) {
                mCallbacks.fail(failList);
            } else {
                mCallbacks.success(successList);
            }
        }
    }
}
