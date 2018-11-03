package kodulf.baselibs.http;

/**
 * Created by zhangyinshan on 2017/5/3.
 *
 * 请求的接口回调
 */
public interface RequestCallback<T> {
    void onFailure(T t, Exception e);
    void onResponse(T t);
}

