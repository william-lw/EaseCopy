package com.william.androidsdk.rxjava;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxManager {
    private Context appContext;
    private Scheduler subscribeOn;
    private Scheduler observeOn;

    public RxManager(Context appContext, Scheduler subscribeOn, Scheduler observeOn) {
        this.appContext = appContext;
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
    }

    public Context getAppContext() {
        return appContext;
    }

    public <T extends RxRequest> void enqueue(T request, final RxCallback<T> callback) {
        create(request)
                .subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        callback.accept(disposable);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        callback.doFinally();
                    }
                })
                .subscribe(createCallBack(callback));
    }


    public <T extends RxRequest> Observable<T> create(final T request) {
        return Observable.create(request);
    }


    private <T extends RxRequest> Observer<T> createCallBack(final RxCallback<T> callback) {
        return new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                callback.onSubscribe(d);
            }

            @Override
            public void onNext(T t) {
                callback.onNext(t);
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        };
    }

    public static final class Builder {
        private Scheduler subscribeOn;
        private Scheduler observeOn;
        private static Context appContext;

        //need call in application, or create the constructor add Context create
        public static void initAppContext(Context context) {
            appContext = context;
        }

        public Builder subscribeOn(@NonNull Scheduler subscribeOn) {
            this.subscribeOn = subscribeOn;
            return this;
        }

        public Builder observeOn(@NonNull Scheduler observeOn) {
            this.observeOn = observeOn;
            return this;
        }

        public RxManager build() {
            if (subscribeOn == null) {
                throw new IllegalStateException("subscribeOn required.");
            }

            if (observeOn == null) {
                observeOn = AndroidSchedulers.mainThread();
            }

            return new RxManager(appContext, subscribeOn, observeOn);
        }

        public static RxManager newSingleThreadManager() {
            return new RxManager(appContext, Schedulers.newThread(), AndroidSchedulers.mainThread());
        }

        public static RxManager newIoThreadManager() {
            return new RxManager(appContext, Schedulers.io(), AndroidSchedulers.mainThread());
        }
    }
}
