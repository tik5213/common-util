package top.ftas.test.gauss;

import android.content.Intent;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import top.ftas.dunit.annotation.DUnit;
import top.ftas.util.gauss.GaussBackgroundUtil;
import top.ftas.dunit.core.AbstractDisplayUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-08-27 21:44
 */
@DUnit(group = GaussGroup.class)
public class TestGaussUnit extends AbstractDisplayUnit {
    @Override
    public void callUnit() {
        mActivity.runOnUiThread(() ->
                AndPermission.with(mActivity)
                        .runtime()
                        .permission(Permission.Group.STORAGE)
                        .onGranted(permissions -> {
                            String gaussBitmapPath = GaussBackgroundUtil.getGaussBitmapCacheFile(mActivity, true);
                            Intent intent = new Intent(mContext, TestGaussActivity.class);
                            intent.putExtra("gaussBitmapPath", gaussBitmapPath);
                            mActivity.startActivity(intent);
                        })
                        .onDenied(permissions -> {
                            // Storage permission are not allowed.
                        })
                        .start());

    }
}
