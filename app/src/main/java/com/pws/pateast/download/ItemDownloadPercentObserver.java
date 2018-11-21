package com.pws.pateast.download;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by anshul on 7/2/17.
 */

public class ItemDownloadPercentObserver<T>
{

  private ObservableEmitter percentageObservableEmitter;
  private Disposable downloadPercentDisposable;
  private final ItemPercentCallback callback;

  public ItemDownloadPercentObserver(ItemPercentCallback callback) {
    this.callback=callback;
    ObservableOnSubscribe observableOnSubscribe = new ObservableOnSubscribe() {
      @Override
      public void subscribe(ObservableEmitter e) throws Exception {
        percentageObservableEmitter = e;
      }
    };

    final Observable observable = Observable.create(observableOnSubscribe);

    final Observer subscriber = getObserver();
    observable.subscribeWith(subscriber);
  }

  public ObservableEmitter getPercentageObservableEmitter() {
    return percentageObservableEmitter;
  }

  private Observer<T> getObserver()
  {
    return new Observer<T>() {
      @Override
      public void onSubscribe(Disposable d) {
        downloadPercentDisposable = d;
      }

      @Override
      public void onNext(T value) {
        callback.updateDownloadableItem(value);
      }

      @Override
      public void onError(Throwable e) {
        if (downloadPercentDisposable != null) {
          downloadPercentDisposable.dispose();
        }
      }

      @Override
      public void onComplete() {
        if (downloadPercentDisposable != null) {
          downloadPercentDisposable.dispose();
        }
      }
    };
  }

  public void performCleanUp() {
    if (downloadPercentDisposable != null) {
      downloadPercentDisposable.dispose();
    }
  }
}
