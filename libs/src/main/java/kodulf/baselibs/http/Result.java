package kodulf.baselibs.http;


import java.io.Serializable;

/**
 * Created by zhangyinshan on 2017/5/3.
 *
 * {
 "reason": "手机号码",
 "result": null,
 "error_code": 0
 }
 */
public class Result<T> implements Serializable{

    public static final int FAIL = -1;
    public static final int SUCCESS = 0;

    private String reason;
    private T result;
    private int error_code = FAIL;


    public boolean isSuccess(){

        return error_code==SUCCESS;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    @Override
    public String toString() {
        return "http.entity.Result{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                ", error_code=" + error_code +
                '}';
    }
}

