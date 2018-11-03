package kodulf.baselibs.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by kodulf on 2017/5/3.
 *
 *
 * 首先需要去下载json相关的jar
 * 如果是其他的公司的，可能需要修改Result，ResultList这两个个类，
 * 然后修改parseJsonStringToResultList,parseJsonStringToResult
 *
 *
 * 一定要设置Content-Type为application/x-www-form-urlencoded的格式，这种格式，在post 请求的时候，会将parameters都作为属性，如果是get，会放在url的路径上面的
 * 当action为get时候，浏览器用x-www-form-urlencoded的编码方式把form数据转换成一个字串（name1=value1&name2=value2...），然后把这个字串append到url后面，用?分割，加载这个新的url。
 * 当action为post时候，浏览器把form数据封装到http body中，然后发送到server
 */
public class HttpUtils {

    private static final int CONNECT_TIMEOUT=5000;


    /**
     * GET 方法 的请求的获取Result
     */
    public static <T> void getRequestForResult(String path, HashMap<String,String> values, RequestCallback<Result<T>> callback, T t) throws Exception {
        requestForResult("GET",path,values,callback,t);
    }

    /**
     * POST 方法 的请求的获取Result
     */
    public static <T> void postRequestForResult(String path, HashMap<String,String> values, RequestCallback<Result<T>> callback,T t) throws Exception {
        requestForResult("POST",path,values,callback,t);
    }

    /**
     * GET 的请求的获取ResultList
     */
    public static <T> void getRequestForResultList(String path, HashMap<String,String> values, RequestCallback<ResultList<T>> callback,T t) throws Exception {
        requestForResultList("GET",path,values,callback,t);
    }

    /**
     * POST 的请求的获取ResultList
     */
    public static <T> void postRequestForResultList(String path, HashMap<String,String> values, RequestCallback<ResultList<T>> callback,T t) throws Exception {
        requestForResultList("POST",path,values,callback,t);
    }


    /**
     * 请求Result
     * @param postOrGet
     * @param path
     * @param values
     * @param callback
     * @param <T>
     * @throws Exception
     */
    private static <T> void requestForResult(String postOrGet,String path, HashMap<String,String> values, RequestCallback<Result<T>> callback,final T t) throws Exception {

        Result<T> result = new Result<T>();
        URL url = new URL(path);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(postOrGet);
        httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);

        //参数设置
        StringBuilder parameters = HttpHelpUtils.getParametersString(values);
        String parametersString = parameters.toString();
        byte[] bytes = parametersString.getBytes();
        //一定要设置Content-Type为application/x-www-form-urlencoded的格式，这种格式，在post 请求的时候，会将parameters都作为属性，如果是get，会放在url的路径上面的
        //当action为get时候，浏览器用x-www-form-urlencoded的编码方式把form数据转换成一个字串（name1=value1&name2=value2...），然后把这个字串append到url后面，用?分割，加载这个新的url。
        //当action为post时候，浏览器把form数据封装到http body中，然后发送到server。
        httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");//application/x-www-form-urlencoded
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));


        //设置可以进行读写
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);

        //将参数都写出去
        OutputStream outputStream =null;
        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(bytes);
        }finally {
            if(outputStream!=null){
                outputStream.close();
            }
        }

        //获取返回值
        int responseCode = httpURLConnection.getResponseCode();
        if(responseCode >=200&&responseCode<300){

            InputStream inputStream =null;
            ByteArrayOutputStream byteArrayOutputStream=null ;
            try {
                inputStream = httpURLConnection.getInputStream();
                byteArrayOutputStream = new ByteArrayOutputStream();

                int length = -1;
                byte[] readBytes = new byte[1024];
                while ((length = inputStream.read(readBytes))!=-1){
                    byteArrayOutputStream.write(readBytes,0,length);
                }
                String readString = byteArrayOutputStream.toString();

                //这里进行json解析
                HttpHelpUtils.parseJsonStringToResultGeneric(result, readString,t);

            }finally {
                if(inputStream!=null&& byteArrayOutputStream !=null) {
                    inputStream.close();
                    byteArrayOutputStream.close();
                    if(result.isSuccess()) {
                        //只有这个时候才设置是成功的onResponse的，result 在上面已经设置了
                        callback.onResponse(result);
                    }else{
                        callback.onFailure(result,new Exception("服务器返回成功200，但是数据处理失败:"+(result.getReason()!=null?result.getReason():"")));
                    }
                }else{
                    callback.onFailure(result,new Exception("服务器返回码200\n 本地在进行返回处理的时候报错"));
                }
            }
        }else{
            callback.onFailure(
                    result,//默认的就是失败的
                    new Exception("服务器返回码 "+responseCode+"\n"+httpURLConnection.getResponseMessage()));
        }

    }


    /**
     * 请求ResultList
     * @param postOrGet
     * @param path
     * @param values
     * @param callback
     * @param <T>
     * @throws Exception
     */
    private static <T> void requestForResultList(String postOrGet,String path, HashMap<String,String> values, RequestCallback<ResultList<T>> callback,T t) throws Exception {

        ResultList<T> resultList = new ResultList<T>();
        URL url = new URL(path);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(postOrGet);
        httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);

        //参数设置
        StringBuilder parameters = HttpHelpUtils.getParametersString(values);
        String parametersString = parameters.toString();
        byte[] bytes = parametersString.getBytes();
        //一定要设置Content-Type为application/x-www-form-urlencoded的格式，这种格式，在post 请求的时候，会将parameters都作为属性，如果是get，会放在url的路径上面的
        httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");//application/x-www-form-urlencoded
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));


        //设置可以进行读写
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);

        //将参数都写出去
        OutputStream outputStream =null;
        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(bytes);
        }finally {
            if(outputStream!=null){
                outputStream.close();
            }
        }

        //获取返回值
        int responseCode = httpURLConnection.getResponseCode();
        if(responseCode >=200&&responseCode<300){

            InputStream inputStream =null;
            ByteArrayOutputStream byteArrayOutputStream=null ;
            try {
                inputStream = httpURLConnection.getInputStream();
                byteArrayOutputStream = new ByteArrayOutputStream();

                int length = -1;
                byte[] readBytes = new byte[1024];
                while ((length = inputStream.read(readBytes))!=-1){
                    byteArrayOutputStream.write(readBytes,0,length);
                }
                String readString = byteArrayOutputStream.toString();

                //这里进行json解析
                HttpHelpUtils.parseJsonStringToResultListGeneric(resultList, readString,t);

            }finally {
                if(inputStream!=null&& byteArrayOutputStream !=null) {
                    inputStream.close();
                    byteArrayOutputStream.close();
                    if(resultList.isSuccess()) {
                        //只有这个时候才设置是成功的onResponse的，resultList 在上面已经设置了
                        callback.onResponse(resultList);
                    }else{
                        callback.onFailure(resultList,new Exception("服务器返回成功200，但是数据处理失败:"+(resultList.getReason()!=null?resultList.getReason():"")));
                    }
                }else{
                    callback.onFailure(resultList,new Exception("服务器返回码200\n 本地在进行返回处理的时候报错"));
                }
            }
        }else{
            callback.onFailure(
                    resultList,//默认的就是失败的
                    new Exception("服务器返回码 "+responseCode+"\n"+httpURLConnection.getResponseMessage()));
        }

    }


    public static <T> void getDownload(String urlString, File returnFile, RequestCallback<Result<T>> requestCallback) throws Exception {
        download("GET",urlString,returnFile,requestCallback);
    }

    public static <T> void postDownload(String urlString, File returnFile, RequestCallback<Result<T>> requestCallback) throws Exception {
        download("POST",urlString,returnFile,requestCallback);
    }
    /**
     * 例如下载这个：http://pic.qiushibaike.com/system/pictures/11895/118959315/medium/app118959315.jpg
     * @param urlString
     * @param returnFile
     * @param requestCallback
     * @throws Exception
     */
    private static <T> void download(String getOrPost, String urlString, File returnFile, RequestCallback<Result<T>> requestCallback) throws Exception{
        //urlString = "http://pic.qiushibaike.com/system/pictures/11895/118959315/medium/app118959315.jpg";
        URL downloadUrl = new URL(urlString);

        Result<T> result = new Result();
        HttpURLConnection httpURLConnection = (HttpURLConnection) downloadUrl.openConnection();

        httpURLConnection.setRequestMethod(getOrPost);
        httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);

        httpURLConnection.setDoInput(true);

        int contentLength = httpURLConnection.getContentLength();
        if(returnFile.exists()){
            long length = returnFile.length();
            if(contentLength==length){

                System.out.println("该文件已经创建了，并且文件完整");
                result.setError_code(Result.SUCCESS);
                requestCallback.onResponse(result);
                return;
            }else{
                System.out.println("该文件已经创建了，但是文件不完整，重新下载");
            }
        }

        InputStream inputStream =null;
        FileOutputStream fileOutputStream = null;

        try {
            inputStream = httpURLConnection.getInputStream();

            fileOutputStream= new FileOutputStream(returnFile);
            byte[] bytes = new byte[1024 * 1024];
            int length = -1;
            while ((length = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        }finally{
            if(fileOutputStream!=null&&inputStream!=null){
                fileOutputStream.close();
                inputStream.close();
                result.setError_code(Result.SUCCESS);
            }else{
                result.setError_code(Result.FAIL);
            }

            if(result.isSuccess()){
                requestCallback.onResponse(result);
            }else{
                requestCallback.onFailure(result,new Exception("failed"));
            }
        }
    }




}

