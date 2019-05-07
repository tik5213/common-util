package top.ftas.test.okhttp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import top.ftas.util.ToastUtil;
import top.ftas.util.okhttp.OkHttpClientNoVerifyUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-09-17 11:09
 */
@DUnit
public class TestOkHttpClientNoVerifyUtilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            //参数url化
            String city = java.net.URLEncoder.encode("北京", "utf-8");

            //拼地址
            String apiUrl = String.format("https://www.sojson.com/open/api/weather/json.shtml?city=%s", city);

            //获取不进行ssl校验的okhttp
            OkHttpClient okHttpClient = OkHttpClientNoVerifyUtil.createOkHttpClientIsNoVerify(System.currentTimeMillis() % 2 == 0);

            //请求天气
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .get()      //默认就是get请求
                    .build();
            okHttpClient.newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            ToastUtil.toast("异常：" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ToastUtil.toast("当前天气 ： " + response.body().string());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
