# MDPermission
## 使用
**1. Gradle dependency** (recommended)

  -  Add the following to your project level `build.gradle`:
 
``` gradle
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```
  -  Add this to your app `build.gradle`:
 
``` gradle
dependencies {
	compile 'com.github.army650663:MDPermission:v1.0.0'
}
```

#### 範例
##### 取得權限
 
 ``` java
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
         
 ``` 
 
 #### 重要
 ##### 必須接入 onRequestPermissionsResult
 
 ``` java
     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         MDPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
     }
 ```

