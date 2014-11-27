//package com.getgigradio.gigradio.util;
//
//import com.colintmiller.simplenosql.NoSQL;
//import com.colintmiller.simplenosql.NoSQLEntity;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import rx.Observable;
//import rx.Subscriber;
//import rx.schedulers.Schedulers;
//import timber.log.Timber;
//
///**
// * @author dsvoronin
// */
//public final class RxSimpleNoSQL {
//  private final NoSQL noSQL;
//
//  public RxSimpleNoSQL(NoSQL noSQL) {
//    this.noSQL = noSQL;
//  }
//
//  public <T extends Persistable> Observable<List<T>> observeReplace(String bucket, Class<T> type,
//      List<T> data) {
//    return Observable.create((Subscriber<? super List<T>> subscriber) -> {
//      try {
//        List<NoSQLEntity<T>> entityList = new ArrayList<>();
//        for (T t : data) {
//          entityList.add(new NoSQLEntity<>(bucket, t.getId(), t));
//        }
//        noSQL.using(type).save(entityList);
//        subscriber.onNext(data);
//        subscriber.onCompleted();
//      } catch (Throwable e) {
//        subscriber.onError(e);
//      }
//    })
//        .subscribeOn(Schedulers.io())
//        .doOnError(
//            throwable -> Timber.e(throwable, "Error while replace(bucket=%s ; type=%s ; data=%s)",
//                bucket, type.getName(), data));
//  }
//
//  public <T extends Persistable> Observable<List<T>> observeReplace(Class<T> type, List<T> data) {
//    return observeReplace(type.getSimpleName(), type, data);
//  }
//
//  public <T extends Persistable> T replace(String bucket, Class<T> type, T data) {
//    CountDownLatch latch = new CountDownLatch(1);
//    noSQL.using(type).save(new NoSQLEntity<>(bucket, data.getId(), data));
//    latch.countDown();
//    try {
//      latch.await();
//      return data;
//    } catch (InterruptedException e) {
//      Timber.e(e, "interrupt");
//      return null;
//    }
//  }
//
//  public <T extends Persistable> T replace(Class<T> type, T data) {
//    CountDownLatch latch = new CountDownLatch(1);
//    noSQL.using(type).save(new NoSQLEntity<>(type.getSimpleName(), data.getId(), data));
//    latch.countDown();
//    try {
//      latch.await();
//      return data;
//    } catch (InterruptedException e) {
//      Timber.e(e, "interrupt");
//      return null;
//    }
//  }
//
//  public <T extends Persistable> Observable<T> observeReplace(Class<T> type, T data) {
//    return Observable.create(new Observable.OnSubscribe<T>() {
//      @Override
//      public void call(Subscriber<? super T> subscriber) {
//        try {
//          CountDownLatch latch = new CountDownLatch(1);
//          noSQL.using(type).save(new NoSQLEntity<>(type.getSimpleName(), data.getId(), data));
//          latch.countDown();
//          latch.await();
//          subscriber.onNext(data);
//          subscriber.onCompleted();
//        } catch (Throwable e) {
//          subscriber.onError(e);
//        }
//      }
//    })
//        .subscribeOn(Schedulers.io())
//        .doOnError(throwable -> Timber.e(throwable, "Error while replace(type=%s ; data=%s)",
//            type.getName(), data));
//  }
//
//  public <T extends Persistable> List<T> replaceAll(String bucket, Class<T> type, List<T> data) {
//    CountDownLatch latch = new CountDownLatch(1);
//    List<NoSQLEntity<T>> entityList = new ArrayList<>();
//    for (T t : data) {
//      entityList.add(new NoSQLEntity<>(bucket, t.getId(), t));
//    }
//    try {
//      noSQL.using(type).save(entityList);
//      latch.countDown();
//      latch.await();
//      return data;
//    } catch (Exception e) {
//      Timber.e(e, "interrupt");
//      return null;
//    }
//  }
//
//  public <T extends Persistable> List<T> replaceAll(Class<T> type, List<T> data) {
//    return replaceAll(type.getSimpleName(), type, data);
//  }
//
//  public <T extends Persistable> Observable<List<T>> observeReplaceAll(Class<T> type,
//      List<T> data) {
//    return Observable.create(subscriber -> {
//      try {
//        List<NoSQLEntity<T>> entityList = new ArrayList<>();
//        for (T t : data) {
//          entityList.add(new NoSQLEntity<>(type.getSimpleName(), t.getId(), t));
//        }
//        noSQL.using(type).save(entityList);
//        subscriber.onNext(data);
//        subscriber.onCompleted();
//      } catch (Throwable e) {
//        subscriber.onError(e);
//      }
//    });
//  }
//
//  public <T extends Persistable> Observable<List<T>> observeAll(String bucket, Class<T> type) {
//    return Observable.create((Subscriber<? super List<T>> subscriber) -> {
//      try {
//        noSQL.using(type).bucketId(bucket).retrieve(noSQLEntities -> {
//          try {
//            List<T> result = new ArrayList<>();
//            for (NoSQLEntity<T> entity : noSQLEntities) {
//              result.add(entity.getData());
//            }
//            subscriber.onNext(result);
//            subscriber.onCompleted();
//          } catch (Throwable e) {
//            subscriber.onError(e);
//          }
//        });
//      } catch (Throwable e) {
//        subscriber.onError(e);
//      }
//    })
//        .subscribeOn(Schedulers.io())
//        .doOnError(
//            throwable -> Timber.e(throwable, "Error while observeAll(bucket=%s ; type=%s)", bucket,
//                type.getName()));
//  }
//
//  public <T extends Persistable> Observable<List<T>> observeAll(Class<T> type) {
//    return observeAll(type.getSimpleName(), type);
//  }
//
//  public <T extends Persistable> List<T> getAll(String bucket, Class<T> type) {
//    CountDownLatch latch = new CountDownLatch(1);
//    List<T> result = new ArrayList<>();
//    try {
//      noSQL.using(type).bucketId(bucket).retrieve(noSQLEntities -> {
//        for (NoSQLEntity<T> entity : noSQLEntities) {
//          result.add(entity.getData());
//        }
//        latch.countDown();
//      });
//      latch.await();
//      return result;
//    } catch (Exception e) {
//      Timber.e(e, "interrupt");
//      return null;
//    }
//  }
//
//  public <T extends Persistable> List<T> getAll(Class<T> type) {
//    return getAll(type.getSimpleName(), type);
//  }
//
//  public <T extends Persistable> Observable<T> observeById(String bucket, Class<T> type,
//      String id) {
//    return Observable.create((Subscriber<? super T> subscriber) -> {
//      try {
//        noSQL.using(type).bucketId(bucket).entityId(id).retrieve(noSQLEntities -> {
//          if (noSQLEntities.size() == 0) {
//            subscriber.onError(
//                new DataNotFoundException("Item not found in local NoSQL Storage: id = " + id));
//          } else {
//            subscriber.onNext(noSQLEntities.get(0).getData());
//            subscriber.onCompleted();
//          }
//        });
//      } catch (Throwable e) {
//        subscriber.onError(e);
//      }
//    })
//        .subscribeOn(Schedulers.io())
//        .doOnError(
//            throwable -> Timber.e(throwable, "Error while get(bucket=%s ; type=%s ; id=%s)", bucket,
//                type.getName(), id));
//  }
//
//  public <T extends Persistable> Observable<T> observeById(Class<T> type, String id) {
//    return observeById(type.getSimpleName(), type, id);
//  }
//
//  public <T extends Persistable> T getById(String bucket, Class<T> type, String id) {
//    CountDownLatch latch = new CountDownLatch(1);
//    List<T> result = new ArrayList<>();
//    noSQL.using(type).bucketId(bucket).entityId(id).retrieve(noSQLEntities -> {
//      if (noSQLEntities.size() > 0) {
//        result.add(noSQLEntities.get(0).getData());
//      }
//      latch.countDown();
//    });
//    try {
//      latch.await();
//      if (result.size() > 0) {
//        return result.get(0);
//      } else {
//        return null;
//      }
//    } catch (InterruptedException e) {
//      Timber.e(e, "interrupt");
//      return null;
//    }
//  }
//
//  public <T extends Persistable> T getById(Class<T> type, String id) {
//    return getById(type.getSimpleName(), type, id);
//  }
//
//  public static class DataNotFoundException extends Exception {
//    public DataNotFoundException() {
//    }
//
//    public DataNotFoundException(String detailMessage) {
//      super(detailMessage);
//    }
//  }
//}