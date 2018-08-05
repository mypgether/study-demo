package com.gether.bigdata.rxjava;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class RxJavaTest {

  @Test
  public void testRx() {
    Observable<String> myObservable = Observable.create(
        new Observable.OnSubscribe<String>() {
          @Override
          public void call(Subscriber<? super String> sub) {
            sub.onNext("Hello, world!");
            sub.onCompleted();
          }
        }
    );
    Subscriber<String> mySubscriber = new Subscriber<String>() {
      @Override
      public void onNext(String s) {
        System.out.println(s);
      }

      @Override
      public void onCompleted() {
        System.out.println("onCompleted");
      }

      @Override
      public void onError(Throwable e) {
        System.out.println("onError" + e.getCause());
      }
    };
    myObservable.subscribe(mySubscriber);
  }

  @Test
  public void testLam() {
    Observable.just("hello ", " where ", " are ", " you ", "from", "?")
        // 事件消费的线程
        //.observeOn(Schedulers.io())
        .map((s) -> s.hashCode())
        .filter((hash) -> hash > 0)
        //事件发生的线程
        .subscribeOn(Schedulers.io())
        .subscribe((s) -> System.out.println(s));
  }
}