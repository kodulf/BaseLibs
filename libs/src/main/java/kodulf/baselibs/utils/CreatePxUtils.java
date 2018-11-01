package kodulf.baselibs.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Kodulf on 2017/4/28.
 */

public class CreatePxUtils {


    //app 1080 x 1776
    //DisplayManager displayManager = (DisplayManager)getSystemService(DISPLAY_SERVICE);
    //Display[] displays = displayManager.getDisplays() 来获取显示的对象，然后display[0].toString就可以获取到当前屏幕的参数了

    /**
     * 基准的x
     */
    public static final int  BASE_X = 1776;
    /**
     * 基准的y
     */
    public static final int BASE_Y = 1080;
    /**
     * 需要适配的布局的
     */
    public static  ArrayList<int[]> targetMachineLayoutXYs =new ArrayList<>();;

    public static void main(String[] args){
        // 首先需要获取的是app的宽度和高度，然后再以这个宽度和高度去设计
        // 自动创建这个机型适配的文件，
        // 首先是要定标准的依据那个尺寸开发的，
        // 然后需要生成哪些尺寸的
        // 然后哪些尺寸需要调整的

        initNeedCreateLayoutParameters();

        try {
            //创建标准的机型的文件
            createValuesFiles(BASE_X,BASE_Y);

            //创建目标的文件
            for (int[] size: targetMachineLayoutXYs){
                createValuesFiles(size[0],size[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化需要创建的布局的参数
     * 例如我们需要创建1920*1080的
     * @return
     */
    private static void initNeedCreateLayoutParameters() {
        targetMachineLayoutXYs.add(new int[]{1920, 1080});
    }

    public static void createValuesFiles(int x, int y) throws IOException {
        //先创建values-BASE_XxBASE_Y的
        String baseDirName = "values-"+x+"x"+y;

        File baseDir = new File(baseDirName);
        if(!baseDir.exists()){
            baseDir.mkdir();
        }

        //创建x文件
        createXFile(baseDir,x);

        //创建y文件
        createYFile(baseDir,y);

        //创建字体大小
        creatTextSize(baseDir,x,y);



    }

    /**
     * 创建x文件
     * @param dir
     * @throws IOException
     */
    public static void createXFile(File dir,float xSize) throws IOException {
        String tamplateX ="<dimen name=\"px@_x\">truepx</dimen>";
        File xBaseFile = new File(dir,"dimens_x.xml");
        if(!xBaseFile.exists()){
            xBaseFile.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(xBaseFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        printWriter.println("<resources>");

        for (int i = 1; i <= BASE_X; i++) {
            String updatedX = tamplateX.replace("@", "" + i);
            float percent = xSize / BASE_X;
            int trueX = (int)(i * percent);
            String newLine = updatedX.replace("true", "" + trueX);
            printWriter.println(newLine);
        }

        printWriter.println("</resources>");

        fileWriter.close();
        printWriter.close();
    }


    /**
     * 创建y文件
     * @param dir
     * @throws IOException
     */
    private static void createYFile(File dir,float ySize) throws IOException {
        String tamplateX ="<dimen name=\"px@_y\">truepx</dimen>";
        File xBaseFile = new File(dir,"dimens_y.xml");
        if(!xBaseFile.exists()){
            xBaseFile.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(xBaseFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        printWriter.println("<resources>");

        for (int i = 1; i <= BASE_Y; i++) {
            String updatedY = tamplateX.replace("@", "" + i);
            float percent = ySize / BASE_Y;
            int trueY = (int)(i * percent);
            String newLine = updatedY.replace("true", "" + trueY);
            printWriter.println(newLine);
        }

        printWriter.println("</resources>");

        fileWriter.close();
        printWriter.close();
    }

    private static void creatTextSize(File dir,int x, int y) throws IOException {

        String tamplateX ="<dimen name=\"sp@\">truepx</dimen>";
        File textSizeFile = new File(dir,"dimens_sp.xml");
        if(!textSizeFile.exists()){
            textSizeFile.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(textSizeFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        printWriter.println("<resources>");

        int num = BASE_X>BASE_Y?BASE_X:BASE_Y;

        for (int i = 1; i <= num; i++) {

            String updatedY = tamplateX.replace("@", "" + i);

            double percent = (Math.sqrt(x*x+y*y))/Math.sqrt(BASE_Y * BASE_Y + BASE_X * BASE_X);
            int truePX = (int)(i * percent);
            String newLine = updatedY.replace("true", "" + truePX);
            printWriter.println(newLine);
        }

        printWriter.println("</resources>");

        fileWriter.close();
        printWriter.close();

    }
}
