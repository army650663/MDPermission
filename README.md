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
    compile 'com.github.army650663:MDPermission:v1.0.4'
}
```

#### 範例
##### 取得權限
* setRequestCode(`int requestCode`) : 可選項
* setMessageView(`String msg`, `String btnText`) : 可選項，使用者第一次拒絕權限後於第二次要求權限示顯示

 ``` java
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
    
 ``` 

#### 重要
* ##### 必須接入 onRequestPermissionsResult
 
 ``` java
     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         MDPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
     }
 ```


