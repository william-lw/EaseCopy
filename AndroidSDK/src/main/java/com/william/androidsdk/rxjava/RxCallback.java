package com.william.androidsdk.rxjava;


import io.reactivex.disposables.Disposable;

public abstract class RxCallback<T extends RxRequest> {


    public abstract void onNext(T t);

    public abstract void onError(Throwable e);

    public void onComplete() {
    }

    public void onSubscribe(Disposable d) {
    }

    public void doFinally() {
    }

    public void accept(Disposable disposable) {
    }

}
