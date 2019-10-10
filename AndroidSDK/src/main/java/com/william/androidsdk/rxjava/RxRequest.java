package com.william.androidsdk.rxjava;


import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public abstract class RxRequest implements ObservableOnSubscribe {
    private ObservableEmitter emitter;
    public int errorCode = -1;
    public String errorMessage = "";

    @Override
    public void subscribe(ObservableEmitter emitter) {
        this.emitter = emitter;
        try {
            execute();
        } catch (Exception e) {
            onError(e);
            e.printStackTrace();
        }
        emitter.onNext(this);
    }

    public RxRequest() {
        super();
    }

    protected abstract void execute() throws Exception;

    public void onError(Throwable throwable) {
        emitter.onError(throwable);
    }

    public void onError(String errorMessage) {
        emitter.onError(new Throwable(errorMessage));
    }

    public void onComplete() {
        emitter.onComplete();
    }

}
