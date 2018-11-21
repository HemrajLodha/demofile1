package com.pws.pateast.download;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by anshul on 7/2/17.
 */

public class DownloadRequestsSubscriber<T> {
    public static final int MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS = 1;
    public static final int INVALID_ID = -1;

    private Subscription downloadRequestsSubscription;
    private FlowableEmitter downloadsFlowableEmitter;
    private ItemDownloadCallback itemDownloadCallback;


    public DownloadRequestsSubscriber(ItemDownloadCallback itemDownloadCallback) {
        this.itemDownloadCallback = itemDownloadCallback;
        FlowableOnSubscribe flowableOnSubscribe = new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter e) throws Exception {
                downloadsFlowableEmitter = e;
            }
        };

        final Flowable flowable = Flowable.create(flowableOnSubscribe, BackpressureStrategy.BUFFER);
        final Subscriber subscriber = getSubscriber();
        flowable.subscribeWith(subscriber);
    }

    public void requestDownload(int number) {
        downloadRequestsSubscription.request(MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS - number);
    }

    public void emitNextItem(T downloadableItem) {
        downloadsFlowableEmitter.onNext(downloadableItem);
    }

    private Subscriber<T> getSubscriber() {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                downloadRequestsSubscription = s;
                downloadRequestsSubscription.request(MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS);
            }

            @Override
            public void onNext(T o) {
                itemDownloadCallback.onDownloadStarted(o);
            }

            @Override
            public void onError(Throwable t) {
                itemDownloadCallback.onError();
            }

            @Override
            public void onComplete() {
                //itemDownloadCallback.onDownloadComplete();
            }
        };
    }

    public void performCleanUp() {
        if (downloadRequestsSubscription != null) {
            downloadRequestsSubscription.cancel();
        }
    }
}
