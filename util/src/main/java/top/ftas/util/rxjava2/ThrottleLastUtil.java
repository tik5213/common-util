package top.ftas.util.rxjava2;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/*
        ThrottleLastUtil
                .throttleLastFast(ANIMATION_DURATION + 50)
                .throttleLastSlow(350)
                .delaySlow(100)
                .subscribe(new ThrottleLastUtil.OnSubscribeListener()
 */

/**
 * 将两个一快一慢的事件传入节流器，合并到一个事件流中，去掉 N 秒内的抖动，发送最终事件到调用者。
 * 一般用来监听 Scroller 的滚动，当监听到滚动时发送一个 fast 事件，滚动停止发送一个 slow 事件。
 * 最终，能在 subscribeFast 中监听到滚动进行中事件，subscribeSlow 监听到滚动停止事件。
 * <p>
 *
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-14 15:31
 */
public class ThrottleLastUtil {

    public interface OnSubscribeListener {
        void subscribeFast();

        void subscribeSlow();

        void onSubscribe(ThrottleLastHolder holder);
    }

    public static class ThrottleLastHolder implements Disposable {
        ObservableEmitter<Boolean> mFastEmitter;
        ObservableEmitter<Boolean> mSlowEmitter;

        Disposable mFastDisposable;
        Disposable mSlowDisposable;

        long mThrottleLastFast;
        long mThrottleLastSlow;
        long mDelayFast;
        long mDelaySlow;

        Consumer<Throwable> mThrowableConsumer;

        public ThrottleLastHolder throttleLastFast(long milliseconds) {
            mThrottleLastFast = milliseconds;
            return this;
        }

        public ThrottleLastHolder throttleLastSlow(long milliseconds) {
            mThrottleLastSlow = milliseconds;
            return this;
        }

        public ThrottleLastHolder onError(Consumer<Throwable> onError) {
            mThrowableConsumer = onError;
            return this;
        }

        public ThrottleLastHolder subscribe(final OnSubscribeListener listener) {
            Observable<Boolean> fastObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                    mFastEmitter = emitter;
                }
            })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .throttleLast(mThrottleLastFast, TimeUnit.MILLISECONDS);
            if (mDelayFast > 0) {
                fastObservable = fastObservable.delay(mDelayFast, TimeUnit.MILLISECONDS);
            }

            //经过 delay 后，需要再次调用 observeOn ，否则线程不对
            fastObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mFastDisposable = d;
                        }

                        @Override
                        public void onNext(Boolean isFast) {
                            if (isFast) {
                                listener.subscribeFast();
                            } else {
                                listener.subscribeSlow();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mThrowableConsumer != null) {
                                try {
                                    mThrowableConsumer.accept(e);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });


            Observable<Boolean> slowObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                    mSlowEmitter = emitter;
                }
            })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .throttleLast(mThrottleLastSlow, TimeUnit.MILLISECONDS);
            if (mDelaySlow > 0) {
                slowObservable = slowObservable
                        .delay(mDelaySlow, TimeUnit.MILLISECONDS);
            }
            //经过 delay 后，需要再次调用 observerOn ，否则线程不对。
            slowObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mSlowDisposable = d;
                        }

                        @Override
                        public void onNext(Boolean isSlow) {
                            if (isSlow) {
                                mFastEmitter.onNext(false);
                            } else {
                                mFastEmitter.onNext(true);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mThrowableConsumer != null) {
                                try {
                                    mThrowableConsumer.accept(e);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

            listener.onSubscribe(this);

            return this;
        }

        public ThrottleLastHolder delayFast(long milliseconds) {
            mDelayFast = milliseconds;
            return this;
        }

        public ThrottleLastHolder delaySlow(long milliseconds) {
            mDelaySlow = milliseconds;
            return this;
        }

        public ThrottleLastHolder sendFastEvent() {
            if (mFastEmitter == null) return this;
            mFastEmitter.onNext(true);
            return this;
        }

        public ThrottleLastHolder sendSlowEvent() {
            if (mSlowEmitter == null) return this;
            mSlowEmitter.onNext(true);
            return this;
        }

        @Override
        public void dispose() {
            try {
                if (mFastDisposable != null && !mFastDisposable.isDisposed()) {
                    mFastDisposable.dispose();
                }
                if (mSlowDisposable != null && !mSlowDisposable.isDisposed()) {
                    mSlowDisposable.dispose();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean isDisposed() {
            return mFastDisposable != null && mSlowDisposable != null && mFastDisposable.isDisposed() && mSlowDisposable.isDisposed();
        }
    }

    public static ThrottleLastHolder throttleLastFast(long milliseconds) {
        return new ThrottleLastHolder().throttleLastFast(milliseconds);
    }
}
