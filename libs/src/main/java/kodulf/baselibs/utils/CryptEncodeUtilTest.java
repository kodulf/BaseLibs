package kodulf.baselibs.utils;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CryptEncodeUtilTest {

    private String mPrivateKeyString;
    private String mPublicKeyString;
    private PublicKey mPublic;
    private PrivateKey mPrivate;

    public void testRSA(){
            rsaInit();
            rsaTest();
            rsaTestString("hello world");
    }

    public void allTest(Bundle savedInstanceState) {
        //Base64编码解码测试
        base64Test();
        //URLEncoding编码解码测试
        urlEncodingTest();

        //DES 加密解密的测试
        desTest();
        //DESede 加密解密的测试
        desedeTest();
        //AES 加密解密测试
        aesTest();

        //RSA 的初始化，获得私钥和密钥
        rsaInit();
        //RSA 加密解密测试
        rsaTest();

        //测试md5算法
        md5Test();
    }

    /**
     * 首先要说明的是Base64是编码解码，而不是加密解密，因为加密解密你是需要密钥的，编码是大家都知道怎么解码的，不需要密钥的。
     base64，将任意的字节数组，通过算法，生成只有（英文大小写（52个字母），数字（10个）+/（2个））内容标识的字符串数据；相当于将任何的内容转换为可见字符串的表示。
     Base64, 将原始数据按照3个字节一个分组,按位进行分割为每6位一个字节的形式，进行转换，形成新的4个字节，这4个字节的再通过Base64的编码表进行映射。
     6位能够表示的字符数就是64所以称为Base64，其实就是字节总长度增加了3分之一。
     */
    public void base64Test(){
        /**
         * 最好 使用 encodeToString，不然还要自己转换。
         byte[] encode = Base64.encode("你好".getBytes(), Base64.NO_WRAP);
         String encodeString = new String(encode);
         Log.d("kodulf","你好的Base64 的编码是"+encodeString);
         */

        //编码
        String encodeToString = Base64.encodeToString("你好".getBytes(), Base64.NO_WRAP);//NO_WRAP 不换行
        Log.d("kodulf", "你好的Base64 的编码是" + encodeToString);

        //解码
        //模拟解码的数据
        String fakeString = Base64.encodeToString("Kodulf 是一个好人".getBytes(), Base64.NO_WRAP);
        Log.d("kodulf", "模拟解码数据得到的Base64编码：" + fakeString);

        // 解码时注意事项，第二个参数的取值，应该和编码时一致；
        byte[] decodeStringbytes = Base64.decode(fakeString.getBytes(), Base64.NO_WRAP);
        String decodeString = new String(decodeStringbytes);
        Log.d("kodulf", "模拟解码数据得到的Base64解码：" + decodeString);




    }

    public void hexEncodingText(){

    }

    /**
     *URLEncode 简单一句话，URLEncoding 就是为了将网址中的非ASCII码内容，转换成可以传输字符

     在编码的时候，保留所有英文字母，数字，以及特定的字符，（这些字符全部都是ASCII中的），除此之外将会转换为十六进制标识，并且在每一个十六进制之前加上%
     内容中的' '空格，全部采用加号+替换
     URLEncoding 的格式，将要转换的内容，用十六进制表示法转换出来，然后每一个字节表示之前，用%开头；例如0x9c字节经过URLEncoding 就变成了%9C

     应用场景：所有GET请求，网址用到中文的情况，以及POST请求中，所有的Key 和Value，在提交之前，必须要经过URLEncoding

     */
    public void urlEncodingTest(){
        try {
            //URLEncoder的编码
            String encode = URLEncoder.encode("千雅爸爸", "UTF-8");//第二个参数一定要是服务器段支持的格式
            Log.d("kodulf","千雅爸爸 的URLEncoding 是"+encode);

            //URLDecoder的解码：
            String decoderString = URLDecoder.decode("%E5%8D%83%E9%9B%85%E7%88%B8%E7%88%B8","UTF-8");
            Log.d("kodulf","%E5%8D%83%E9%9B%85%E7%88%B8%E7%88%B8 URLDecoder的解码为："+decoderString);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    //DES 加密解密的测试
    public void desTest(){
        String content="ABCDEFGH Hello Kodulf 千雅爸爸";
        String password="abcdefgh";//DES 密钥必须是8个字节的。

        // 加密解密用的都是字节数组
        byte[] contentBytes = content.getBytes();
        byte[] passwordBytes = password.getBytes();
        //加密
        byte[] bytes = CryptUtil.desEncrypt(contentBytes, passwordBytes);
        //TODO  !!! 加密的结果不允许直接 new String() !!!
        String encodeString = Base64.encodeToString(bytes, Base64.NO_WRAP);
        Log.d("Kodulf","DES加密后的数据 "+encodeString);

        //解密
        byte[] base64Decodedbytes = Base64.decode(encodeString.getBytes(), Base64.NO_WRAP);
        byte[] byteDecode = CryptUtil.desDecrypt(base64Decodedbytes, passwordBytes);
        String decodeString = new String(byteDecode);
        Log.d("Kodulf","DES解密后的数据 "+decodeString);

    }

    //DESede 加密解密的测试
    public void desedeTest(){
        String content="DESede加密解密 Hello Kodulf 千雅爸爸";
        String password="abcdefghabcdefghabcdefgh";//DESede 密钥必须是24个字节的。

        // 加密解密用的都是字节数组
        byte[] contentBytes = content.getBytes();
        byte[] passwordBytes = password.getBytes();
        //加密
        byte[] bytes = CryptUtil.desedeEncrypt(contentBytes, passwordBytes);
        //TODO  !!! 加密的结果不允许直接 new String() !!!
        String encodeString = Base64.encodeToString(bytes, Base64.NO_WRAP);
        Log.d("Kodulf","DESede加密后的数据 "+encodeString);

        //解密
        byte[] base64Decodedbytes = Base64.decode(encodeString.getBytes(), Base64.NO_WRAP);
        byte[] byteDecode = CryptUtil.desedeDecrypt(base64Decodedbytes, passwordBytes);
        String decodeString = new String(byteDecode);
        Log.d("Kodulf","DESede解密后的数据 "+decodeString);

    }

    //AES 加密解密测试
    public void aesTest(){
        String content="AES加密解密 Hello Kodulf 千雅爸爸";
        String password="abcdefghabcdefgh";//DESede 密钥必须是16个字节的。
        // 加密解密用的都是字节数组
        byte[] contentBytes = content.getBytes();
        byte[] passwordBytes = password.getBytes();
        //加密
        byte[] bytes = CryptUtil.aesEncrypt(contentBytes, passwordBytes);
        //TODO  !!! 加密的结果不允许直接 new String() !!!
        String encodeString = Base64.encodeToString(bytes, Base64.NO_WRAP);
        Log.d("Kodulf","AES加密后的数据 "+encodeString);

        //解密
        byte[] base64Decodedbytes = Base64.decode(encodeString.getBytes(), Base64.NO_WRAP);
        byte[] byteDecode = CryptUtil.aesDecrypt(base64Decodedbytes, passwordBytes);
        String decodeString = new String(byteDecode);
        Log.d("Kodulf","AES解密后的数据 "+decodeString);

    }


    //RSA 的初始化，获得私钥和密钥
    public void rsaInit(){
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");//RAS 密钥生成器
            kpg.initialize(1024, new SecureRandom());//生成制定长度的密钥
            KeyPair keyPair = kpg.generateKeyPair();//生成密钥对
            mPrivate = keyPair.getPrivate();//获取私钥
            mPublic = keyPair.getPublic();//获取公钥
            //通过getEncoded方法来获取密钥的具体内容
            byte[] privateEncoded = mPrivate.getEncoded();
            byte[] publicEncoded = mPublic.getEncoded();
            //为了防止乱码，使用Base64来转换，这样显示的时候就不会有乱码了
            mPrivateKeyString = Base64.encodeToString(privateEncoded, Base64.NO_WRAP);
            mPublicKeyString = Base64.encodeToString(publicEncoded, Base64.NO_WRAP);

            Log.d("kodulf","RSA私钥："+ mPrivateKeyString);
            Log.d("kodulf","RSA公钥："+ mPublicKeyString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void rsaTest(){
        String data = "Hello， Kodulf is a good father, 千雅爸爸是个好爸爸";
        byte[] bytes = data.getBytes();
        byte[] encryptByte = CryptUtil.rsaEncrypt(bytes, mPrivate);

        //要使用Base64来进行编码，如果不这样做就会显示乱码
        //String encryptString = new String(encryptByte);//这里产生乱码
        String encryptString = Base64.encodeToString(encryptByte, Base64.NO_WRAP);

        Log.d("kodulf","Hello， Kodulf is a good father, 千雅爸爸是个好爸爸 RSA加密后:"+encryptString);

        //解密：
        //要使用Base64来进行解码，如果不这样做就会显示乱码
        //byte[] sourceBytes = encryptString.getBytes();//这里会报错java.lang.ArrayIndexOutOfBoundsException: too much data for RSA block
        byte[] sourceBytes = Base64.decode(encryptString.getBytes(), Base64.NO_WRAP);
        byte[] decryptBytes = CryptUtil.rsaDecrypt(sourceBytes, mPublic);
        String decryptString = new String(decryptBytes);
        Log.d("kodulf","Hello， Kodulf is a good father, 千雅爸爸是个好爸爸 RSA解密后:"+decryptString);
    }


    public void rsaTestString(String data){
        byte[] bytes = data.getBytes();
        byte[] encryptByte = CryptUtil.rsaEncrypt(bytes, mPrivate);

        //要使用Base64来进行编码，如果不这样做就会显示乱码
        //String encryptString = new String(encryptByte);//这里产生乱码
        String encryptString = Base64.encodeToString(encryptByte, Base64.NO_WRAP);

        Log.d("kodulf","Hello， Kodulf is a good father, 千雅爸爸是个好爸爸 RSA加密后:"+encryptString);

        //解密：
        //要使用Base64来进行解码，如果不这样做就会显示乱码
        //byte[] sourceBytes = encryptString.getBytes();//这里会报错java.lang.ArrayIndexOutOfBoundsException: too much data for RSA block
        byte[] sourceBytes = Base64.decode(encryptString.getBytes(), Base64.NO_WRAP);
        byte[] decryptBytes = CryptUtil.rsaDecrypt(sourceBytes, mPublic);
        String decryptString = new String(decryptBytes);
        Log.d("kodulf","Hello， Kodulf is a good father, 千雅爸爸是个好爸爸 RSA解密后:"+decryptString);
    }



    public void md5Test(){
        String source = "abcdef";
        String md5CreatedString = md5String(source);
        Log.d("kodulf","MD5 算法得到的String:"+md5CreatedString);
    }
    //md5算法生成String
    private String md5String(String url) {
        String ret = null;

        if (url != null) {

            try {
                // 创建MD5的消息摘要算法的类，进行调用
                MessageDigest digest = MessageDigest.getInstance("MD5");

                // 计算出一个唯一识别的信息；
                byte[] data = digest.digest(url.getBytes());

                StringBuilder sb = new StringBuilder();

                // 字节数组转换为十六进制字符串
                for (byte b : data) {
                    int ib = b & 0x0FF;
                    String s = Integer.toHexString(ib);

                    if(ib < 16){  // 15 -> 0F 0 -> 00
                        sb.append('0');
                    }
                    sb.append(s);
                }

                ret = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }
}
