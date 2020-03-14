package megvii.testfacepass.pa.utils;

import android.content.Context;
import android.util.Log;

import com.common.pos.api.util.TPS980PosUtil;
import com.hwit.HwitManager;
import com.lztek.toolkit.Lztek;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import megvii.testfacepass.pa.MyApplication;
import megvii.testfacepass.pa.utils.fx.FxTool;

public class DengUT {
    public static final int WHITE_LAMP = 3;        // 白色灯
    public static final int RED_LAMP  = 4;        // 红色灯
    public static final int GREEN_LAMP  = 5;        // 绿色灯
    public static final int GPIO6  = 6;        // GPIO6
    public static final int GPIO7   = 7;        // GPIO7
    public static final int GPIO8  = 8;        // GPIO8
    public static final int GPIO9  = 9;        // GPIO9    只读

    public static final String GPIO_BASE_PATH = "/sys/class/leds/led";//GPIO基础路径
    public static final String GPIO_END_PATH = "/brightness";//

    public static final String WHITE_LAMP_PATH = GPIO_BASE_PATH+WHITE_LAMP+GPIO_END_PATH;//白灯节点路径
    public static final String RED_LAMP_PATH =GPIO_BASE_PATH+RED_LAMP+GPIO_END_PATH;//红灯节点路径
    public static final String GREEN_LAMP_PATH = GPIO_BASE_PATH+GREEN_LAMP+GPIO_END_PATH;//绿灯节点路径
    public static final String GPIO6_PATH = GPIO_BASE_PATH+GPIO6+GPIO_END_PATH;//GPIO6口
    public static final String GPIO7_PATH = GPIO_BASE_PATH+GPIO7+GPIO_END_PATH;//GPIO7口
    public static final String GPIO8_PATH = GPIO_BASE_PATH+GPIO8+GPIO_END_PATH;//GPIO8口
    public static final String GPIO9_PATH = GPIO_BASE_PATH+GPIO9+GPIO_END_PATH;//GPIO9口
    public static final int LEVEL_HIG = 1;  //高
    public static final int LEVEL_LOW = 0;  //低

    public static final String CAMERA_RED_PATH = "/sys/class/leds/camera_red/brightness";
    public static final String RELAY_CTL_PATH = "/sys/class/leds/relay_ctl/brightness";
    public static final String CAMERA_WHITE_PATH = "/sys/class/leds/camera_white/brightness";
    public static final String BRIGHTNESS_PATH = "/sys/class/backlight/backlight/brightness";


    public static final String WIEGAND_PATH = "/dev/ttyWG0";
    public static final String RS485_PATH = "/dev/ttyS4";


    //    public static final String UUID_URL = "http://47.110.50.51:8787/auth";
    public static final String UUID_URL = "http://47.110.50.51:8787/auth";


    public static final String EXTERNALPATHHEADER = "/storage";
    public static final String MACFILENAME = "mac.xls";
    public static final String MACUSED = "used";

    public  static boolean isOPEN=false;//默认关的 false
    public  static boolean isOPENGreen=false;//默认关的 false
    public  static boolean isOPENRed=false;//默认关的 false
    public  static boolean isOpenDOR=false;//默认关的 false

    public  static boolean isExecution=false;//是否执行过 false

    public static void closeWrite(){
        try {
            HwitManager.HwitSetIOValue(5,0);
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
        FxTool.fxLED1Control(false);
        FxTool.fxLED2Control(false);
        FxTool.fxLED3Control(false);
        writeGpio(CAMERA_WHITE_PATH,0);
        writeFile("0");
    }
    public static void openWrite(){
        writeGpio(RED_LAMP_PATH,0); //关红灯
        writeGpio(GREEN_LAMP_PATH,0);//关绿灯
        writeGpio(GPIO7_PATH,1);
        writeGpio(CAMERA_WHITE_PATH,255);
        try {
            HwitManager.HwitSetIOValue(5,1);
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
        FxTool.fxLED2Control(false);
        FxTool.fxLED3Control(false);
        FxTool.fxLED1Control(true);
        writeFile("1");
    }





    public static void closeRed(){
        writeGpio(RED_LAMP_PATH,0);
        FxTool.fxLED2Control(false);
    }

    public static void openRed(){
        writeGpio(GREEN_LAMP_PATH,0);//关绿灯
        writeGpio(GPIO7_PATH,0); // 关白灯
        writeGpio(RED_LAMP_PATH,1);
        writeGpio(CAMERA_WHITE_PATH,255);
        FxTool.fxLED1Control(false);
        FxTool.fxLED3Control(false);
        FxTool.fxLED2Control(true);
        Log.d("DengUT", "红灯");
    }

    public static void closeLOED(){//关屏幕
        writeGpio(BRIGHTNESS_PATH,0);
        try {
            Lztek lztek=Lztek.create(MyApplication.myApplication);
            lztek.setLcdBackLight(false);
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
        try {
            HwitManager.HwitCloseBacklight();
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
        Log.d("DengUT", "关屏幕");
    }

    public static void openLOED(){//开屏幕
        writeGpio(BRIGHTNESS_PATH,255);
        try {
            Lztek lztek=Lztek.create(MyApplication.myApplication);
            lztek.setLcdBackLight(true);
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
        try {
            HwitManager.HwitOpenBacklight();
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
        Log.d("DengUT", "开屏幕");
    }


    public static void closeGreen(){
        writeGpio(GREEN_LAMP_PATH,0);
        FxTool.fxLED3Control(false);
    }
    public static void openGreen(){
        writeGpio(GPIO7_PATH,0); // 关白灯
        writeGpio(RED_LAMP_PATH,0); //关红灯
        writeGpio(GREEN_LAMP_PATH,1);
        writeGpio(CAMERA_WHITE_PATH,255);
        FxTool.fxLED1Control(false);
        FxTool.fxLED2Control(false);
        FxTool.fxLED3Control(true);
    }

    public static void closeDool(){
        writeGpio(RELAY_CTL_PATH,0);
        TPS980PosUtil.setRelayPower(0);
        FxTool.fxDoorControl(false);
        try {
            HwitManager.HwitSetIOValue(9,0);
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
    }


    public static void openDool(){
        writeGpio(RELAY_CTL_PATH,1);
        TPS980PosUtil.setRelayPower(1);
        FxTool.fxDoorControl(true);
        try {
            HwitManager.HwitSetIOValue(9,1);
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
    }


    private static void writeGpio(String path, int value){
        BufferedWriter bw = null;
        try{
            bw = new BufferedWriter(new FileWriter(path));
            bw.write(value+"");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
///
    private int getGpioStatus(String path){
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            String s = br.readLine();
            return Integer.parseInt(s);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    private static void writeFile(String text){
        File file = new File("/sys/class/gpio/gpio45/value");
        FileOutputStream fis = null;
        try {
            fis = new FileOutputStream(file);
            fis.write(text.getBytes());
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // public static

}
