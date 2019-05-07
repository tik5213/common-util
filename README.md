# 所有常用工具类

注意 compileOnly ，根据需要，在目标项目中添加

```sh
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    compileOnly 'com.android.support:appcompat-v7:28.0.0'
    compileOnly'com.android.support.constraint:constraint-layout:1.1.3'

    compileOnly 'com.android.support:recyclerview-v7:28.0.0'

    //高斯模糊
    compileOnly 'com.github.pinguo-zhouwei:EasyBlur:v1.0.0'
    //    app 的build.gradle添加：
    //    使用renderscript 兼容包
    //    renderscriptTargetApi 25
    //    renderscriptSupportModeEnabled true


    compileOnly 'com.squareup.okhttp3:okhttp:3.12.1'
    compileOnly 'com.squareup.okio:okio:1.15.0'
```
