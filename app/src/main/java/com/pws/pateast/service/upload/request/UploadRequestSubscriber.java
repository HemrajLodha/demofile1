package com.pws.pateast.service.upload.request;

import com.pws.pateast.api.service.APIService;
import com.pws.pateast.download.ItemDownloadCallback;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by intel on 10-Aug-17.
 */

public class UploadRequestSubscriber<T>
{
    private Disposable disposable;
    private UploadRequestCallback<T> callback;
    private static UploadRequestSubscriber subscriber;

    private UploadRequestSubscriber(UploadRequestCallback<T> callback) {
        this.callback = callback;
    }

    public static UploadRequestSubscriber getInstance(UploadRequestCallback callback)
    {
        if(subscriber == null)
            subscriber = new UploadRequestSubscriber<>(callback);

        return subscriber;
    }

    public void emitNextItem(T t) {
        callback.getUploadAbleItem(t)
                .subscribeWith(getObserver());
    }

    private Observer<T> getObserver() {
        return new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(T o) {
                callback.onUploadCompleted(o);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
                performCleanUp();
            }

            @Override
            public void onComplete() {
                callback.onUploadComplete();
                performCleanUp();
            }
        };
    }

    public void performCleanUp() {
        if (disposable != null) {
            disposable.dispose();
            subscriber = null;
        }
    }

}
