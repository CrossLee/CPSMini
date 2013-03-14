/*
 * Created on 2012-6-5
 *
 * JoymindCommDLL.java
 *
 * Copyright (C) 2012 by Withiter Software & Technology Services (Shanghai) Limited.
 * All rights reserved. Withiter Software & Technology Services (Shanghai) Limited 
 * claims copyright in this computer program as an unpublished work. Claim of copyright 
 * does not imply waiver of other rights.
 *
 * NOTICE OF PROPRIETARY RIGHTS
 *
 * This program is a confidential trade secret and the property of 
 * Withiter Software & Technology Services (Shanghai) Limited. Use, examination, 
 * reproduction, disassembly, decompiling, transfer and/or disclosure to others of 
 * all or any part of this software program are strictly prohibited except by express 
 * written agreement with Withiter Software & Technology Services (Shanghai) Limited.
 */
/*
 * ---------------------------------------------------------------------------------
 * Modification History
 * Date               Author                     Version     Description
 * 2012-6-5       CrossLee                    1.0         New
 * ---------------------------------------------------------------------------------
 */
/**
 * 
 */
package com.withiter.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * @author gl65293
 */
public class JoymindCommDLL {

    public interface JoymindCommDLLLib extends Library {

//      String dllPath = (Thread.currentThread().getContextClassLoader().getResource("tool/").toString() + "JoymindComm.dll").substring(6);
//    	String dllPath = System.getProperty("user.dir") + "\\JoymindComm.dll";
//    	String dllPath = System.getProperty("user.dir").replace("\\", "/") +"/com/withiter/tool/JoymindComm.dll";;
    	String dllPath = "C:/JoymindComm.dll";
//    	String dllPath = (Class.class.getClass().getResource("/").getPath() +"JoymindComm.dll").replaceFirst("/", "");
        JoymindCommDLLLib INSTANCE = (JoymindCommDLLLib) Native.loadLibrary((Platform.isWindows() ? dllPath : "c"), JoymindCommDLLLib.class);

        /* System Configuration */
        /**
         * 设置显示屏参数 
         * @param ip        IP地址
         * @param width     显示屏长度，单位：像素
         * @param height    显示屏高度，单位：像素
         * @return          0：操作失败，1：操作成功
         */
        public int SetScreenPara(String ip, int width, int height);

        /**
         * 开关屏设置
         * @param ip        IP地址
         * @param power     开关屏状态，0：关屏，1：开屏
         * @return          0：操作失败，1：操作成功
         */
        public int SetPower(String ip, int power);

        /**
         * 清屏设置
         * @param ip        IP地址
         * @return          0：操作失败，1：操作成功
         */
        public int ClearScreen(String ip);

        /**
         * 校正时间
         * @param ip        IP地址
         * @return          0：操作失败，1：操作成功
         */
        public int AdjustTime(String ip);

        /**
         * 设置控制器网络参数
         * @param ip            IP地址
         * @param ipAddress     新IP地址
         * @param getway        网关
         * @param subnetmask    子网掩码
         * @return              0：操作失败，1：操作成功
         */
        public int SetNetwork(String ip, String ipAddress, String getway, String subnetmask);

        /**
         * 编辑完节目内容后，发送显示内容
         * @param ip        IP地址
         * @return          0：操作失败，1：操作成功
         */
        public int SendDisplayData(String ip);

        /**
         * 清除动态库 临时文件
         */
        public void ResetDLL();

        /**
         * 上传媒体文件
         * @param ip        IP地址
         * @param fileName  文件路径
         * @return          0：操作失败，1：操作成功
         */
        public int AddMediaFile(String ip, String fileName);

        /**
         * 清除所有媒体文件
         * @param ip        IP地址
         * @return          0：操作失败，1：操作成功
         */
        public int ClearMediaFile(String ip);

        /* System Configuration */

        /* Display Configuration */
        /**
         * 添加节目
         * @param jno       节目号（ >=1 ）
         * @param playTime  播放时间 （单位：秒）
         *                  若 = 0， 则等待播放完成
         *                  若 > 0， 则播放指定时间
         * @param bgFile    背景图片名称（若为0，则无背景图片）【暂不支持】
         * @return          0：操作失败，1：操作成功
         */
        public int AddProgram(int jno, int playTime, String bgFile);

        /**
         * 添加文本区域
         * @param jno           节目号 (>=1)
         * @param qno           区域号 (>=1)
         * @param left          区域左上角顶点x坐标，单位：象素
         * @param top           区域左上角顶点y坐标，单位：象素
         * @param width         区域宽度，单位：象素
         * @param height        区域高度
         * @param fontColor     字体颜色
         * @param fontName      字体名【暂支持宋体】
         * @param fontSize      字体号
         * @param fontBold      字体粗细(0：不加粗;1：加粗)
         * @param fontItalic    字体斜体(0：不斜体;1：斜体) 【暂不支持】
         * @param fontUnder     字体下划线(0：无;1：有) 【暂不支持】
         * @param line          自动换行(0：不自动;1：自动)
         * @param hAlign        水平对齐(1：左对齐；2：居中；3：右对齐)
         * @param vAlign        垂直对齐(1：上对齐；2：居中；3：下对齐)
         * @param text          显示字符串
         * @param type          显示特技
         * @param speed         显示速度
         * @param delay         停留时间
         * @return              0：操作失败，1：操作成功
         */
        public int AddTextArea(int jno, int qno, int left, int top, int width, int height, int fontColor, String fontName, int fontSize, int fontBold, int fontItalic, int fontUnder, int line,
                int hAlign, int vAlign, String text, int type, int speed, int delay);

        /**
         * 添加数字时钟区域
         * @param jno           节目号 (>=1)
         * @param qno           区域号 (>=1)
         * @param left          区域左上角顶点x坐标，单位：象素
         * @param top           区域左上角顶点y坐标，单位：象素
         * @param width         区域宽度，单位：象素
         * @param height        区域高度
         * @param fontColor     字体颜色
         * @param fontName      字体名
         * @param fontSize      字体号
         * @param fontBold      字体粗细(0：不加粗;1：加粗)
         * @param fontItalic    字体斜体(0：不斜体;1：斜体) 【暂不支持】
         * @param fontUnder     字体下划线(0：无;1：有) 【暂不支持】
         * @param mode          时间格式
                                1：全部显示
                                2：年月日
                                3：月日
                                4：时分秒
                                5：时分
                                6：星期
                                7：年
                                8：月
                                9：日
                                10：时
                                11：分
                                12：秒

         * @param format        显示模式 
                                1：2008年08月08日 08时08分08秒
                                2：2008-08-08 08:08:08
                                3：08/08/2008 08:08:08

         * @return              0：操作失败，1：操作成功
         */
        public int AddDClockArea(int jno, int qno, int left, int top, int widht, int height, int fontColor, String fontName, int fontSize, int fontBold, int fontItalic, int fontUnder, int mode,
                int format);

        /**
         * 添加文件区域，目前支持的文件格式有： 
            1）图片格式：bmp、jpg、png
            2）【暂不支持】文本格式：doc、rtf、doc
            3)动画格式：gif     

         * @param jno           节目号 (>=1)
         * @param qno           区域号 (>=1)
         * @param left          区域左上角顶点x坐标，单位：象素
         * @param top           区域左上角顶点y坐标，单位：象素
         * @param width         区域宽度，单位：象素
         * @param height        区域高度
         * @return              0：操作失败，1：操作成功
         */
        public int AddFileArea(int jno, int qno, int left, int top, int width, int height);

        /**
         * 添加文件到图文区域中
                                 当文件格式为gif时，特技类型、运行速度、停留时间可使用任意值，下位机会按gif原始效果播放。
         * @param jno           节目号 (>=1)
         * @param qno           区域号 (>=1)
         * @param mno           文件号 (>=1)
         * @param fileName      文件路径
         * @param width         文件显示宽度(目前为区域的宽度) 【可忽略】
         * @param height        文件显示高度(目前为区域的高度) 【可忽略】
         * @param type          特技类型（特效：见附录）
         * @param speed         运行速度
         * @param delay         停留时间
         * @return              0：操作失败，1：操作成功
         */
        public int AddFile2Area(int jno, int qno, int mno, String fileName, int width, int height, int type, int speed, int delay);

        /**
         * 添加视频区域（每个节目最多1个）
            1.目前支持的文件格式有： H.264（25M码流）、MPEG1/2（40M码流）、MPEG4/Divx/Xvid（35M码流）、VC-1（30M码流）、RMVB8/9/10（30MB码流）、H.263（40M码流） 
            2.视频区域必须在所有区域最后添加。
            3.传输速度：50MB文件需要30秒  

         * @param jno           节目号 (>=1)
         * @param qno           区域号 (>=1)
         * @param left          区域左上角顶点x坐标，单位：象素
         * @param top           区域左上角顶点y坐标，单位：象素
         * @param width         区域宽度，单位：象素
         * @param height        区域高度
         * @param fileName      文件名称
         * @return              0：操作失败，1：操作成功
         */
        public int AddVideoArea(int jno, int qno, int left, int top, int width, int height, String fileName);
        /* Display Configuration */
        
        /**
         * 附录1：特技列表
            0：翻页（静止）
            1：左移
            2：右移
            3：上移
            4：下移
         */
        
        public int GetRGB( int r, int g, int b);
        
        
        
        
        /**
         * the interface need from my side
         */
        /**
         * @param ip            
         * @param configFile    
         * @return              0：操作失败，1：操作成功
         */
        public int SetTimerPower(String ip, String configFile);
        
        /**
         * @param ip
         * @return the file path
         */
        public String GetScreenShot(String ip);
        
        /**
         * @param macAddress
         * @return the device information
         */
        public String AddDevice(String macAddress);
        
        /**
         * @param ip
         * @return              0：操作失败，1：操作成功
         */
        public int ResetDevice(String ip);
        
        /**
         * 推送节目压缩包
         * @param ip
         * @param fileName
         * @return              0：操作失败，1：操作成功
         */
        public int PushProgram(String ip, String fileName);
        
        /**
         * [CTRL_INFO]   	 

			PNO 		=（屏号） 根据控制器硬件拨码值
			VERSION 	=（系统软件版本号）
			WIDTH		=（屏宽）
			HEIGHT		=（屏高）
			STORAGE		=（存储模式）
							1：FLASH存储
							2：RAM存储
							3：SD卡存储
			IP			=（IP地址）
			GATEWAY		=（网关）
			SUBNETMASK	=（子网掩码）
			MACADDRESS	=（MAC地址）
							格式：xx-xx-xx-xx-xx-xx
			STATUS		=（设备运行状态）
							0 : 关屏状态
							1 : 开屏状态
			TIME		=（设备当前时间）
							Eg: 2010/7/23 19:16:15
			FREEDISK	=（剩余存储空间大小,单位BYTE） 
			PRGTOTAL	=（设备节目总数）
			PRGINDEX	=（当前播放节目序号）
			BOOT_TIME	=（设备开机时间）
							Eg: 2010/7/23 19:16:15
							
         * @param ip			IP地址
         * @param fileName		生成的文件名称
         * @return				0：操作失败，1：操作成功
         */
        public int GetSysInfo(String ip, String fileName);
        
        /**
         * @param jno			节目号 （>=1）
         * @param type			定时类型：0（无定时）；1（循环播放）；2（指定播放）
         * @param startYear		开始日期（年）
         * @param startMonth	开始日期（月）
         * @param startDay		开始日期（日）
         * @param startHour		开始时间（时）
         * @param startMinute	开始时间（分）
         * @param startSecond	开始时间（秒）
         * @param endYear		结束日期（年）
         * @param endMonth		结束日期（月）
         * @param endDay		结束日期（日）
         * @param endHour		结束时间（时）
         * @param endMinute		结束时间（分）
         * @param endSecond		结束时间（秒）
         * @param monday		星期一显示 （0：不显示；1：显示）
         * @param tuesday		星期二显示 （0：不显示；1：显示）
         * @param wednesday		星期三显示 （0：不显示；1：显示）
         * @param thursday		星期四显示 （0：不显示；1：显示）
         * @param friday		星期五显示 （0：不显示；1：显示）
         * @param saturday		星期六显示 （0：不显示；1：显示）
         * @param sunday		星期日显示 （0：不显示；1：显示）
         * @return				0：操作失败，1：操作成功
         * 
         * 注：
         * 1）当定时类型为无定时，其他参数可以设置为0，则正常播放节目；
         * 2）当定时类型为循环播放，按照开始日期到结束日期，且星期显示的设置的日期，然后每天根据开始时间到结束时间播放节目；
         * 3）当定时类型为指定播放，按照开始日期的开始时间一直到结束日期的结束时间播放节目
         */
        public int SetProgTimer(int jno, int type, 
        		int startYear, int startMonth, int startDay, int startHour, int startMinute, int startSecond,
        		int endYear, int endMonth, int endDay, int endHour, int endMinute, int endSecond,
        		int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday);
    }
    
    public static void main(String[] args) {
    	String dllPath = (Thread.currentThread().getContextClassLoader().getResource("").toString()+"tool/JoymindComm.dll").substring(6);
    	System.out.println(dllPath);
    	
    	String s = System.getProperty("user.dir");
    	System.out.println(s);
    	
    	
    	String dllPath1 = System.getProperty("user.dir").replace("\\", "/") +"/com/withiter/tool/JoymindComm.dll";
    	System.out.println(dllPath1);
    	
    	String dl = Class.class.getClass().getResource("/").getPath().toString();
    	System.out.println(dl);
    	
    	String ss = JoymindCommDLL.class.getResource("").toString();
    	System.out.println(ss);
	}
}














