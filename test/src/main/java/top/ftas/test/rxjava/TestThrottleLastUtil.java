package top.ftas.test.rxjava;

import top.ftas.dunit.annotation.DUnit;
import top.ftas.dunit.core.AbstractDisplayUnit;
import top.ftas.util.rxjava2.ThrottleLastUtil;


/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-14 17:03
 */
@DUnit(group = RxJavaGroup.class)
public class TestThrottleLastUtil extends AbstractDisplayUnit {
    private final static int ANIMATION_DURATION = 200;

    @Override
    public void callUnit() {
        ThrottleLastUtil.ThrottleLastHolder holder =
                ThrottleLastUtil
                .throttleLastFast(ANIMATION_DURATION + 50)
                .throttleLastSlow(350)
                .delaySlow(100)
                .subscribe(new ThrottleLastUtil.OnSubscribeListener() {
                    @Override
                    public void subscribeFast() {
                        mMessageHelper.appendLine("subscribeFast " + System.currentTimeMillis());
                    }

                    @Override
                    public void subscribeSlow() {
                        mMessageHelper.appendLine("subscribeSlow " + System.currentTimeMillis());
                    }

                    @Override
                    public void onSubscribe(ThrottleLastUtil.ThrottleLastHolder holder) {
                    }
                });

        holder.sendFastEvent();
        holder.sendFastEvent();
        holder.sendFastEvent();
        holder.sendFastEvent();
        holder.sendSlowEvent();
        holder.sendSlowEvent();
        holder.sendSlowEvent();

    }
}
