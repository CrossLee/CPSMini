/*
 * Created on 2013-2-17
 *
 * PlaylistService.java
 *
 * Copyright (C) 2013 by Withiter Software & Technology Services (Shanghai) Limited.
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
 * 2013-2-17       cross                    1.0         New
 * ---------------------------------------------------------------------------------
 */
/**
 * 
 */
package com.withiter.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.withiter.dto.OnebyoneDTO;
import com.withiter.dto.ProgramDTO;
import com.withiter.jna.JoymindCommDLL.JoymindCommDLLLib;

/**
 * @author cross
 *
 */
public class PlaylistService {

	private static final Log log = LogFactory.getLog(com.withiter.util.PlaylistService.class);
	
	// 发送节目单，并且播放
	public boolean pushPlaylist(List<ProgramDTO> playlist, String ip) throws Exception {
		try {
			// the resource list need to push
			List<String> resources = new ArrayList<String>();

			if (log.isDebugEnabled()) {
				log.debug("Start to push playlist");
			}

			for (ProgramDTO program : playlist) {
				// 获取节目的资源name
				Map<Integer, OnebyoneDTO> ones = program.getOnebyones();
				Collection<OnebyoneDTO> values = ones.values();
				
				for(OnebyoneDTO one : values){
					System.out.println("one.getType(): " + one.getType());
					System.out.println("one.getResource(): " + one.getResource());
					System.out.println("one.getDirection(): " + one.getDirection());
					System.out.println("one.getDimension(): " + one.getDimension().getX()+","+one.getDimension().getY()+","+one.getDimension().getWidth()+","+one.getDimension().getHeight());
					if(one.getType().equals("image") || one.getType().equals("video")){
						resources.add(one.getResource());
					}
				}
			}

			/** invoke DLL method **/
			if (log.isDebugEnabled()) {
				log.debug("*******************************Start to invoke DLL*********************************");
			}
			File f = new File("C:/MiniCPSDLLfunctionsTest.txt");
			JoymindCommDLLLib instance = JoymindCommDLLLib.INSTANCE;
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(new Date().toString());
			bw.newLine();
			bw.append("*******************************Start to invoke DLL*********************************");
			bw.newLine();
			// clear DLL TEMP file
			instance.ResetDLL();
			bw.append("instance.ResetDLL();");
			bw.newLine();
			if (log.isDebugEnabled()) {
				log.debug("ResetDLL finished!");
			}
			
			// clear disk files
			@SuppressWarnings("unused")
			int clearMediaFlag = 1;
//			int clearMediaFlag = instance.ClearMediaFile(ip);
//			if (log.isDebugEnabled()) {
//				log.debug("the function is : ClearMediaFile(" + ip + ");");
//				log.debug("ClearMediaFile result is : " + clearMediaFlag);
//				bw.append("System.out.println(instance.ClearMediaFile(\"" + ip
//						+ "\"));");
//				bw.newLine();
//				bw.append("//ClearMediaFile result is : " + clearMediaFlag);
//				bw.newLine();
//			}
			
			// clear screen
			@SuppressWarnings("unused")
			int clearScreenResult = 1;
//			int clearScreenResult = instance.ClearScreen(ip);
//			if (log.isDebugEnabled()) {
//				log.debug("the function is : ClearScreen(" + ip + ");");
//				log.debug("ClearScreen result is : " + clearScreenResult);
//				bw.append("System.out.println(instance.ClearScreen(\"" + ip
//						+ "\"));");
//				bw.newLine();
//				bw.append("//ClearScreen result is : " + clearScreenResult);
//				bw.newLine();
//			}

			boolean stepOneFlag = true;
			boolean stepTwoFlag = true;
			boolean stepThreeFlag = true;

			// step1 push resources
			int flag = 0;
			for (int i = 0; i < resources.size(); i++) {
				int temp = instance.AddMediaFile(ip, resources.get(i));
				flag += temp;
				if (log.isDebugEnabled()) {
					log.debug("the function is : AddMediaFile(" + ip + "," + resources.get(i) + ");");
					log.debug("AddMediaFile result is : " + temp);
					bw.append("System.out.println(instance.AddMediaFile(\""	+ ip + "\",\"" + resources.get(i) + "\"));");
					bw.newLine();
					bw.append("//AddMediaFile result is : " + temp);
					bw.newLine();
				}
			}
			if (flag == resources.size()) {
				stepOneFlag &= true;
			} else {
				stepOneFlag &= false;
			}

			// step2 add programs
			for (int i = 0; i < playlist.size(); i++) {
				ProgramDTO program = playlist.get(i);
				// 设置屏幕分辨率
				int stepTwoflag1 = 1;
//				int stepTwoflag1 = instance.SetScreenPara(ip, Integer
//						.parseInt(program.getTemplateWidth()), Integer
//						.parseInt(program.getTemplateHeight()));
//				if (log.isDebugEnabled()) {
//					log.debug("the function is : SetScreenPara(" + ip + ","
//							+ Integer.parseInt(program.getTemplateWidth())
//							+ ","
//							+ Integer.parseInt(program.getTemplateHeight())
//							+ ");");
//					log.debug("SetScreenPara result is : " + stepTwoflag1);
//					bw.append("System.out.println(instance.SetScreenPara(\""
//							+ ip + "\","
//							+ Integer.parseInt(program.getTemplateWidth())
//							+ ","
//							+ Integer.parseInt(program.getTemplateHeight())
//							+ "));");
//					bw.newLine();
//					bw.append("//SetScreenPara result is : " + stepTwoflag1);
//					bw.newLine();
//				}

				// 设置节目编号
				int jno = i + 1;
				int stepTwoflag2 = instance.AddProgram(jno, 0, null);
				if (log.isDebugEnabled()) {
					log.debug("the function is : AddProgram(" + jno + "," + 0 + ", null);");
					log.debug("AddProgram result is : " + stepTwoflag2);
					bw.append("System.out.println(instance.AddProgram(" + jno + "," + 0 + ", null));");
					bw.newLine();
					bw.append("//AddProgram result is : " + stepTwoflag2);
					bw.newLine();
				}
				
				// check cycle type
				if("0".equals("0")){
					int flag8 = instance.SetProgTimer(jno, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
					if (log.isDebugEnabled()) {
						log.debug("the function is : SetProgTimer("+jno+", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)");
						log.debug("SetProgTimer result is : " + flag8);
						bw.append("System.out.println(SetProgTimer("+jno+", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);");
						bw.newLine();
						bw.append("//SetProgTimer result is : " + flag8);
						bw.newLine();
					}
					if(flag8 == 1){
						stepTwoFlag &= true;
					}else{
						stepTwoFlag &= false;
					}
				}

				Map<Integer, OnebyoneDTO> dllAreas = program.getOnebyones();
				if(log.isDebugEnabled()){
					log.debug("pushPlaylist function --> program's onebyone size is : " + dllAreas.size());
				}

				for (int j = 0; j < dllAreas.size(); j++) {
					OnebyoneDTO onebyone = dllAreas.get(j);
					String resourceType = onebyone.getType();
					
					// 设置区域坐标及宽度高度
					int left = onebyone.getDimension().getX();
					int top = onebyone.getDimension().getY();
					int width = onebyone.getDimension().getWidth();
					int height = onebyone.getDimension().getHeight();
					if(log.isDebugEnabled()){
						log.debug("left:"+left);
						log.debug("top:"+top);
						log.debug("width:"+width);
						log.debug("height:"+height);
					}

					// 设置区域编号
					int qno = j + 1;

					// 文字区域
					if (resourceType.contains("text")) {
						// 设置文字颜色
						int fontColor = instance.GetRGB(255, 255, 255);
						// 设置字体
						String fontName = "宋体";
						// 设置字体大小
						int fontSize = 72;
						int fontBold = 1;// 字体粗细(0：不加粗;1：加粗)
						int fontItalic = 0; // 字体斜体(0：不斜体;1：斜体) 【暂不支持】
						int fontUnder = 0;// 字体下划线(0：无;1：有) 【暂不支持】
						int line = 1; // 自动换行(0：不自动;1：自动)
						// 设置特技
						int type = 1;
						// 设置速度
						int speed = 1;
						// 设置延迟
						int delay = 0;
						String direction = onebyone.getDirection();
						// 设置文字对齐方式
						int hAlign = 2;
						int vAlign = 2;
						// 设置文本
						String text = onebyone.getResource();
						if (direction.equals("静止不动")) {
							type = 0;
						}
						if (direction.equals("自左向右")) {
							type = 2;
							hAlign = 3;
							StringBuilder sb = new StringBuilder(text);
							text = sb.reverse().toString();

						}
						if (direction.equals("自右向左")) {
							type = 1;
							hAlign = 1;
						}
						
						if(log.isDebugEnabled()){
							log.debug("add program's text content is : " + text);
						}
						
						int stepTwoflag3 = instance.AddTextArea(jno, qno, left,
								top, width, height, fontColor, fontName,
								fontSize, fontBold, fontItalic, fontUnder,
								line, hAlign, vAlign, text, type, speed, delay);
						if (log.isDebugEnabled()) {
							log.debug("the function is : AddTextArea(" + jno
									+ "," + qno + "," + left + "," + top + ","
									+ width + "," + height + "," + fontColor
									+ "," + fontName + "," + fontSize + ","
									+ fontBold + "," + fontItalic + ","
									+ fontUnder + "," + line + "," + hAlign
									+ "," + vAlign + "," + text + "," + type
									+ "," + speed + "," + delay + ");");
							log
									.debug("AddTextArea result is : "
											+ stepTwoflag3);
							bw
									.append("System.out.println(instance.AddTextArea("
											+ jno
											+ ","
											+ qno
											+ ","
											+ left
											+ ","
											+ top
											+ ","
											+ width
											+ ","
											+ height
											+ ","
											+ fontColor
											+ ",\""
											+ fontName
											+ "\","
											+ fontSize
											+ ","
											+ fontBold
											+ ","
											+ fontItalic
											+ ","
											+ fontUnder
											+ ","
											+ line
											+ ","
											+ hAlign
											+ ","
											+ vAlign
											+ ",\""
											+ text
											+ "\","
											+ type
											+ ","
											+ speed
											+ "," + delay + "));");
							bw.newLine();
							bw.append("//AddTextArea result is : "
									+ stepTwoflag3);
							bw.newLine();
						}

						if (stepTwoflag3 == 1) {
							stepTwoFlag &= true;
						} else {
							stepTwoFlag &= false;
							throw new Exception("AddTextAreaException");
						}
					}

					// 天气区域
					if(resourceType.contains("weather")){
						
					}
					
					// 时间区域
					if (resourceType.contains("clock")) {
						// 设置文字颜色
						int fontColor = instance.GetRGB(7,158,56);
						// 设置字体
						String fontName = "宋体";
						// 设置字体大小
						int fontSize = 68;
						int fontBold = 1;// 字体粗细(0：不加粗;1：加粗)
						int fontItalic = 0; // 字体斜体(0：不斜体;1：斜体) 【暂不支持】
						int fontUnder = 0;// 字体下划线(0：无;1：有) 【暂不支持】

						// 设置时间格式
						int mode = 4;
						// 设置显示模式
						int format = 2;

						int stepTwoflag4 = instance.AddDClockArea(jno, qno,
								left, top, width, height, fontColor, fontName,
								fontSize, fontBold, fontItalic, fontUnder,
								mode, format);
						if (log.isDebugEnabled()) {
							log.debug("the function is : AddDClockArea(" + jno
									+ "," + qno + "," + left + "," + top + ","
									+ width + "," + height + "," + fontColor
									+ "," + fontName + "," + fontSize + ","
									+ fontBold + "," + fontItalic + ","
									+ fontUnder + "," + mode + "," + format
									+ ");");
							log.debug("AddDClockArea result is : "
									+ stepTwoflag4);
							bw
									.append("System.out.println(instance.AddDClockArea("
											+ jno
											+ ","
											+ qno
											+ ","
											+ left
											+ ","
											+ top
											+ ","
											+ width
											+ ","
											+ height
											+ ","
											+ fontColor
											+ ",\""
											+ fontName
											+ "\","
											+ fontSize
											+ ","
											+ fontBold
											+ ","
											+ fontItalic
											+ ","
											+ fontUnder
											+ ","
											+ mode
											+ ","
											+ format + "));");
							bw.newLine();
							bw.append("//AddDClockArea result is : "
									+ stepTwoflag4);
							bw.newLine();
						}

						if (stepTwoflag4 == 1) {
							stepTwoFlag &= true;
						} else {
							stepTwoFlag &= false;
							throw new Exception("AddDClockAreaException");
						}
					}

					// 图片区域
					if (resourceType.contains("image")) {
						// 文件号（文件个数）
						int mno = 1;
						// 文件名
						String[] whole = onebyone.getResource().split("/");
						int size = whole.length;
						String fileName = whole[size -1];
						// 设置特技
						int type = 0;
						// 设置速度
						int speed = 0;
						// 设置延迟
						int delay = 0;
						String direction = onebyone.getDirection();
						// 设置文字对齐方式
						if (direction == null) {
							type = 0;
						}

						int stepTwoflag5 = instance.AddFileArea(jno, qno, left,
								top, width, height);
						if (log.isDebugEnabled()) {
							log.debug("the function is : AddFileArea(" + jno
									+ "," + qno + "," + left + "," + top + ","
									+ width + "," + height + ");");
							log
									.debug("AddFileArea result is : "
											+ stepTwoflag5);
							bw
									.append("System.out.println(instance.AddFileArea("
											+ jno
											+ ","
											+ qno
											+ ","
											+ left
											+ ","
											+ top
											+ ","
											+ width
											+ ","
											+ height + "));");
							bw.newLine();
							bw.append("//AddFileArea result is : "
											+ stepTwoflag5);
							bw.newLine();
						}

						int stepTwoflag6 = instance.AddFile2Area(jno, qno, mno,
								fileName, width, height, type, speed, delay);
						if (log.isDebugEnabled()) {
							log.debug("the function is : AddFile2Area(" + jno
									+ "," + qno + "," + mno + "," + fileName
									+ "," + width + "," + height + "," + type
									+ "," + speed + "," + delay + ");");
							log.debug("AddFile2Area result is : "
									+ stepTwoflag6);
							bw
									.append("System.out.println(instance.AddFile2Area("
											+ jno
											+ ","
											+ qno
											+ ","
											+ mno
											+ ",\""
											+ fileName
											+ "\","
											+ width
											+ ","
											+ height
											+ ","
											+ type
											+ ","
											+ speed + "," + delay + "));");
							bw.newLine();
							bw.append("//AddFile2Area result is : "
									+ stepTwoflag6);
							bw.newLine();
						}

						if (stepTwoflag5 == 1) {
							stepTwoFlag &= true;
						} else {
							stepTwoFlag &= false;
							throw new Exception("AddFileAreaException");
						}
						if (stepTwoflag6 == 1) {
							stepTwoFlag &= true;
						} else {
							stepTwoFlag &= false;
							throw new Exception("AddFile2AreaException");
						}
					}

					// 视频区域
					if (resourceType.contains("video")) {
						// 设置视频名称
						String[] whole = onebyone.getResource().split("/");
						int size = whole.length;
						String fileName = whole[size -1];
						int stepTwoflag7 = instance.AddVideoArea(jno, qno,
								left, top, width, height, fileName);
						if (log.isDebugEnabled()) {
							log.debug("the function is : AddVideoArea(" + jno
									+ "," + qno + "," + left + "," + top + ","
									+ width + "," + height + "," + fileName
									+ ");");
							log.debug("AddVideoArea result is : "
									+ stepTwoflag7);
							bw
									.append("System.out.println(instance.AddVideoArea("
											+ jno
											+ ","
											+ qno
											+ ","
											+ left
											+ ","
											+ top
											+ ","
											+ width
											+ ","
											+ height
											+ ",\""
											+ fileName
											+ "\"));");
							bw.newLine();
							bw.append("//AddVideoArea result is : "
									+ stepTwoflag7);
							bw.newLine();
						}

						if (stepTwoflag7 == 1) {
							stepTwoFlag &= true;
						} else {
							stepTwoFlag &= false;
							throw new Exception("AddVideoAreaException");
						}
					}

					if (stepTwoflag1 == 1 && stepTwoflag2 == 1) {
						stepTwoFlag &= true;
					} else {
						stepTwoFlag &= false;
						throw new Exception("StepTwoException");
					}
				}
			}

			// 播放节目
			int stepThree = instance.SendDisplayData(ip);
			if (log.isDebugEnabled()) {
				log.debug("the function is:SendDisplayData(" + ip + ");");
				log.debug("SendDisplayData result is : " + stepThree);
				bw.append("System.out.println(instance.SendDisplayData(\"" + ip
						+ "\"));");
				bw.newLine();
				bw.append("//SendDisplayData result is : " + stepThree);
				bw.newLine();
				bw.append("*******************************End to invoke DLL*********************************");
			}
			bw.flush();
			bw.close();
			if (stepThree == 1) {
				stepThreeFlag &= true;
			} else {
				stepThreeFlag &= false;
			}
			if (log.isDebugEnabled()) {
				log.debug("*******************************End to invoke DLL*********************************");
			}
			/** invoke DLL method **/

			boolean flag123 = stepOneFlag && stepTwoFlag && stepThreeFlag;
			return flag123;
		} catch (IOException e) {
			e.printStackTrace();
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
			return false;
		}
	}

}
