package kodulf.baselibs.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kodulf on 2016/10/11.
 */

public class HashMapUtil {
    /**
     * 获取某个类的属性和值
     * @param object
     * @param classname
     * @return
     */
    public static HashMap<String,String> getHashMap(Object object, String classname){
        HashMap<String,String> map = new HashMap<String,String>();
        try {
            Class clazz = Class.forName(classname);//根据类名获得其对应的Class对象 写上你想要的类名就是了 注意是全名 如果有包的话要加上 比如java.Lang.String
            Object obj = clazz.newInstance();//默认用空参的构造方法创建的对象
            Field[] fields = clazz.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
            String key=null;
            String value = null;

            for(Field f : fields) {
                //value = f.getType().getName();//打印每个属性的类型名字
                f.setAccessible(true); //设置些属性是可以访问的
                Object o = f.get(object);//打印每个属性的类型名字
                //只有在属性的值不是空的时候才获取。
                if(o!=null){
                    value = o.toString();
                    key = f.getName();
                    map.put(key,value);
                }

            }
        } catch(Exception e) {
            e.printStackTrace();
        }


        return map;
    }

    /**
     * 获取某个类的属性和值，支持Date类型的
     * @param object
     * @param classname
     * @return
     */
    public static HashMap<String,String> getHashMapFormatDate(Object object, String classname){
        HashMap<String,String> map = new HashMap<String,String>();
        try {
            Class clazz = Class.forName(classname);//根据类名获得其对应的Class对象 写上你想要的类名就是了 注意是全名 如果有包的话要加上 比如java.Lang.String
            Object obj = clazz.newInstance();//默认用空参的构造方法创建的对象
            Field[] fields = clazz.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
            String key=null;
            String value = null;

            for(Field f : fields) {
                //value = f.getType().getName();//打印每个属性的类型名字
                f.setAccessible(true); //设置些属性是可以访问的
                Object o = f.get(object);//打印每个属性的类型名字
                //只有在属性的值不是空的时候才获取。

                if(o!=null){
                    //如果类型是date的类型的，那么转化为java.util.相应的格式
                    String typename = f.getType().getName();
                    if("java.util.Date".equals(typename)){
                        Date date = (Date)o;
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formatedDate = format.format(date);
                        value = formatedDate;
                    }
                    else
                        value = o.toString();


                    key = f.getName();
                    map.put(key,value);
                }

            }
        } catch(Exception e) {
            e.printStackTrace();
        }


        return map;
    }

}
