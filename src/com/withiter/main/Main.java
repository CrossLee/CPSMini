/*
 * Created on 2013-2-4
 *
 * Main.java
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
 * 2013-2-4       cross                    1.0         New
 * ---------------------------------------------------------------------------------
 */
/**
 * 
 */
package com.withiter.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

import com.withiter.dto.OnebyoneDTO;
import com.withiter.dto.ProgramDTO;
import com.withiter.dto.ResolutionDTO;
import com.withiter.jna.JoymindCommDLL.JoymindCommDLLLib;
import com.withiter.util.FileOperationTool;
import com.withiter.util.PlaylistService;

/**
 * @author cross
 * 
 */
public class Main extends JFrame {
	private static final long serialVersionUID = 3350060192892360223L;
	
	public static Vector<String> ipVector = new Vector<String>();
	
	public static int limit = 3;
	
	private int failtimes = 0;
	
	class Resolution {
		private String name;
		private String value;

		public Resolution(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String toString() {
			return name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	class Template {
		private Resolution resolution;
		private int areas;

		public Template(Resolution resolution, int areas) {
			super();
			this.resolution = resolution;
			this.areas = areas;
		}

		public Template(int areas) {
			super();
			this.areas = areas;
		}

		public Resolution getResolution() {
			return resolution;
		}

		public void setResolution(Resolution resolution) {
			this.resolution = resolution;
		}

		public int getAreas() {
			return areas;
		}

		public void setAreas(int areas) {
			this.areas = areas;
		}

		@Override
		public String toString() {
			if (areas == 0) {
				return "请选择模板区域数目";
			}
			return "区域数目：" + areas;
		}
	}

	private JLabel resolutionLabel;
	private JLabel templateLabel;

	private JComboBox jcbresolution;
	private JComboBox jcbTemplate;

	private JScrollPane designScrollPanel;
	private JPanel designPanel;

	private JButton btAdd;
	private JButton btReset;

	private JLabel programsListLabel;
	private JList programsList;
	private Vector<String> pVector = new Vector<String>();
	
	private JTextField ip;
	private JButton btnTest;
	private JButton btnPushPlaylist;
	private JButton btnSelectDevices;
	
	//license button
	private JButton btnLicense;

	private List<Resolution> listTimes = new ArrayList<Resolution>();
	private List<Template> listTemplate = new ArrayList<Template>();

	private List<ProgramDTO> playlist = new ArrayList<ProgramDTO>();
	
	JoymindCommDLLLib instance = JoymindCommDLLLib.INSTANCE;

	private static void exchange(Component[] c){
		int x = c.length / 2;
		for(int i = 0; i < x; i ++){
			swap(c, i, c.length - 1 - i);
		}
	}
	
	private static void swap(Component[] c, int x, int y){
		Component tmp = c[x];
		c[x] = c[y];
		c[y] = tmp;
	}
	
	Main() {

		if(!newFile()){
			JOptionPane.showMessageDialog(null, "相关文件被删除或被修改，程序将无法使用！");
			System.exit(0);
		}
		if(!copyIps()){
			JOptionPane.showMessageDialog(null, "相关文件被删除或被修改，程序将无法使用！");
			System.exit(0);
		}
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int x = (int) screenSize.getWidth();
		int y = (int) screenSize.getHeight();
		this.setBounds((x - 1140) / 2, (y - 700) / 2, 1140, 700);
		this.setResizable(false);
		this.setTitle("CPSMini 发布平台");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// add resolutions
		listTimes.add(new Resolution("请选择合适的LCD分辨率", "0"));
		listTimes.add(new Resolution("1920*1080(16:9)", "1920*1080_16:9"));
		listTimes.add(new Resolution("1600*1200(4:3)", "1600*1200_4:3"));
		listTimes.add(new Resolution("1280*1024(5:4)", "1280*1024_5:4"));
		listTimes.add(new Resolution("1280*960(4:3)", "1280*960_4:3"));
		listTimes.add(new Resolution("1280*768(5:3)", "1280*768_5:3"));
		listTimes.add(new Resolution("1024*768(4:3)", "1024*768_4:3"));
		listTimes.add(new Resolution("800*640(5:4)", "800*640_5:4"));
		listTimes.add(new Resolution("800*600(4:3)", "800*600_4:3"));
		listTimes.add(new Resolution("800*450(16:9)", "800*450_16:9"));

		listTimes.add(new Resolution("1080*1920(9:16)", "1080*1920_9:16"));
		listTimes.add(new Resolution("1200*1600(3:4)", "1200*1600_3:4"));
		listTimes.add(new Resolution("1024*1280(4:5)", "1024*1280_4:5"));
		listTimes.add(new Resolution("960*1280(3:4)", "960*1280_3:4"));
		listTimes.add(new Resolution("768*1280(3:5)", "768*1280_3:5"));
		listTimes.add(new Resolution("768*1024(3:4)", "768*1024_3:4"));
		listTimes.add(new Resolution("640*800(4:5)", "640*800_4:5"));
		listTimes.add(new Resolution("600*800(3:4)", "600*800_3:4"));
		listTimes.add(new Resolution("450*800(9:16)", "450*800_9:16"));

		// add templates
		listTemplate.add(new Template(0));
		listTemplate.add(new Template(1));
		listTemplate.add(new Template(2));
		listTemplate.add(new Template(3));

		resolutionLabel = new JLabel("请选择显示屏分辨率");
		jcbresolution = new JComboBox(listTimes.toArray());
		
		templateLabel = new JLabel("请选择节目模板");
		jcbTemplate = new JComboBox(listTemplate.toArray());
		btAdd = new JButton("添加到节目单");
		btReset = new JButton("清除所有节目");
		programsListLabel = new JLabel("节目列表");
		programsList = new JList();

		ip = new JTextField("多个IP用空格分隔");
		btnTest = new JButton("测试");
		btnSelectDevices = new JButton("选择设备");
		btnPushPlaylist = new JButton("发送");
		
		btnLicense = new JButton("上传License");
		
		programsListLabel.setBounds(850, 30, 220, 20);
		programsList.setBounds(850, 60, 260, 400);
		programsList.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		resolutionLabel.setBounds(30, 30, 120, 20);
		jcbresolution.setBounds(170, 30, 200, 20);
		templateLabel.setBounds(420, 30, 120, 20);
		jcbTemplate.setBounds(560, 30, 200, 20);
		btAdd.setBounds(850, 470, 120, 20);
		btReset.setBounds(980, 470, 130, 20);

		ip.setBounds(850, 500, 120, 20);
		btnTest.setBounds(980, 500, 70, 20);
		btnTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = ip.getText().trim();
				String[] ips = str.split(" ");
				Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
				
				// check the ip pattern
				for(int i = 0; i < ips.length; i++){
					Matcher matcher = pattern.matcher(ips[i]); //以验证127.400.600.2为例
					if(!matcher.matches()){
						JOptionPane.showMessageDialog(null, "IP格式不对，请检查！");
						return;
					}
				}
				
				// invoke dll
				for(int i = 0; i < ips.length; i++){
					int j = instance.AdjustTime(ips[i]);
					if(j == 0){
						JOptionPane.showMessageDialog(null, "IP: "+ips[i]+"不通，请检查！");
						return;
					}
				}
				
				JOptionPane.showMessageDialog(null, "IP检查成功！");
			}
		});
		
		btnPushPlaylist.setBounds(1050, 500, 60, 20);

		btnSelectDevices.setBounds(850, 530, 120, 20);
		btnLicense.setBounds(980, 530, 130, 20);
		
		btnSelectDevices.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(playlist.size() > 0){
					new DeviceListFrame(playlist).setVisible(true);
				}else{
					JOptionPane.showMessageDialog(null, "请先添加节目，再选择设备进行下发节目！");
				}
			}
		});

		btnPushPlaylist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(playlist.size() <= 0){
					JOptionPane.showMessageDialog(null, "请先添加节目，再选择设备进行下发节目！");
					return;
				}
				
				String str = ip.getText().trim();
				String[] ips = str.split(" ");
				Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
				
				// check the ip pattern
				for(int i = 0; i < ips.length; i++){
					Matcher matcher = pattern.matcher(ips[i]); //以验证127.400.600.2为例
					if(!matcher.matches()){
						JOptionPane.showMessageDialog(null, "IP格式不对，请检查！");
						return;
					}
				}
				
				// invoke dll
				for(int i = 0; i < ips.length; i++){
					int j = instance.AdjustTime(ips[i]);
					if(j == 0){
						JOptionPane.showMessageDialog(null, "IP: "+ips[i]+"不通，请检查！");
						return;
					}
				}
				
				int yes = JOptionPane.showConfirmDialog(null, "节目正在下发中，在弹出下发结果前，请务必不要再下发节目！");
				if(yes != 0){
					JOptionPane.showMessageDialog(null, "节目下发已经取消！");
					return;
				}
				//push playlist
				StringBuffer sb = new StringBuffer();
				PlaylistService pService = new PlaylistService();
				for(int i = 0; i < ips.length; i++){
					try {
						boolean flag = pService.pushPlaylist(playlist, ips[i]);
						String str1 = null;
						if(flag){
							str1 = "下发成功";
						}else{
							str1 = "下发失败";
						}
						sb.append(ips[i]+": " + str1 + "\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				JOptionPane.showMessageDialog(null, sb.toString());
			}
		});
		
		ip.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(ip.getText().trim().equals("多个IP用空格分隔")){
					ip.setText("");
				}
			}
		});
		
		designPanel = new JPanel();
		designScrollPanel = new JScrollPane(designPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// 设计区域大小默认800*640
		designPanel.setBackground(Color.BLACK);
		designPanel.setLayout(new GridLayout());
		designScrollPanel.setBounds(30, 60, 804, 442);

		jcbresolution.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String selected = ((Resolution) jcbresolution
							.getSelectedItem()).getValue();
					if (selected.equals("0")) {
						return;
					}
					String[] xy = selected.split("_");

					if (xy[1].equals("4:3")) {
						designScrollPanel.setSize(800, 600);
						designPanel.setPreferredSize(new Dimension(800, 600));
					}
					if (xy[1].equals("5:3")) {
						designScrollPanel.setSize(800, 480);
						designPanel.setPreferredSize(new Dimension(800, 480));
					}
					if (xy[1].equals("5:4")) {
						designScrollPanel.setSize(800, 600);
						designPanel.setPreferredSize(new Dimension(800, 640));
					}
					if (xy[1].equals("16:9")) {
						designScrollPanel.setSize(800, 450);
						designPanel.setPreferredSize(new Dimension(800, 450));
					}

					if (xy[1].equals("3:4")) {
						designScrollPanel.setSize(600, 600);
						designPanel.setPreferredSize(new Dimension(600, 800));
					}
					if (xy[1].equals("3:5")) {
						designScrollPanel.setSize(480, 600);
						designPanel.setPreferredSize(new Dimension(480, 800));
					}
					if (xy[1].equals("4:5")) {
						designScrollPanel.setSize(640, 600);
						designPanel.setPreferredSize(new Dimension(640, 800));
					}
					if (xy[1].equals("9:16")) {
						designScrollPanel.setSize(450, 600);
						designPanel.setPreferredSize(new Dimension(450, 800));
					}
					jcbTemplate.setSelectedIndex(0);
					designPanel.updateUI();
				}
			}
		});

		// select the number of ares
		jcbTemplate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int selected = ((Template) jcbTemplate.getSelectedItem()).getAreas();
					designPanel.removeAll();
					designPanel.updateUI();
					switch (selected) {
					case 0:
						break;
					case 1:
						designPanel.setLayout(new BorderLayout());
						final JLabel label1 = new JLabel("<html><font size=\"5\" color =\"white\">单击选择资源（图片、视频）</font></html>");
						label1.setBorder(BorderFactory.createLineBorder(Color.GREEN));
						label1.addMouseListener(new MouseAdapter(){
							@Override
							public void mouseClicked(MouseEvent arg0) {
								//打开文件选择对话框
				                JFileChooser fileChooser = new JFileChooser();
				                fileChooser.setFileFilter(new PicAndVideoFilter());
				                int i = fileChooser.showOpenDialog(null);
				                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				                if(i == JFileChooser.APPROVE_OPTION){
				                    File file = fileChooser.getSelectedFile();
				                    String url = file.getAbsolutePath().replace("\\", "/");
				                    if(url.endsWith(".bmp") || url.endsWith(".jpg") || url.endsWith("png") || url.endsWith(".gif")){
				                    	label1.setName("image");
				                    }
				                    if(url.endsWith(".mp4") || url.endsWith(".mpeg") || url.endsWith(".mpg")){
				                    	label1.setName("video");
				                    }
				                    label1.setToolTipText(url);
				                    label1.setText("<html><font size=\"5\" color =\"white\">"+url+"</font></html>");
//				                    pack();
				                }
							}
						});
						designPanel.add(label1);
						designPanel.updateUI();
						break;
						
					case 2:
						designPanel.setLayout(null);
						int height = designPanel.getHeight();
						int width = designPanel.getWidth();
						
						// label21
						final JLabel label21 = new JLabel("<html><font size=\"5\" color =\"white\">单击选择资源（图片、视频）</font></html>");
						label21.setBorder(BorderFactory.createLineBorder(Color.GREEN));
						label21.setBounds(0, 0, width, height/8*7);
						label21.addMouseListener(new MouseAdapter(){
							@Override
							public void mouseClicked(MouseEvent arg0) {
				                JFileChooser fileChooser = new JFileChooser();
				                fileChooser.setFileFilter(new PicAndVideoFilter());
				                int i = fileChooser.showOpenDialog(null);
				                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				                if(i == JFileChooser.APPROVE_OPTION){
				                    File file = fileChooser.getSelectedFile();
				                    String url = file.getAbsolutePath().replace("\\", "/");
				                    if(url.endsWith(".bmp") || url.endsWith(".jpg") || url.endsWith("png") || url.endsWith(".gif")){
				                    	label21.setName("image");
				                    }
				                    if(url.endsWith(".mp4") || url.endsWith(".mpeg") || url.endsWith(".mpg")){
				                    	label21.setName("video");
				                    }

				                    label21.setText("<html><font size=\"5\" color =\"white\">"+url+"</font></html>");
				                    label21.setToolTipText(url);
				                }
							}
						});
						designPanel.add(label21);
						designPanel.updateUI();

						// label22
						final JLabel label22 = new JLabel("<html><font size=\"5\" color =\"white\">单击设置文字信息</font></html>");
						label22.setBorder(BorderFactory.createLineBorder(Color.GREEN));
						label22.setBounds(0, height/8*7, width, height/8);
						label22.addMouseListener(new MouseAdapter(){
							@Override
							public void mouseClicked(MouseEvent arg0) {
								new TextFrame(label22).setVisible(true);
							}
						});
						designPanel.add(label22);
						designPanel.updateUI();
						break;
						
					case 3:
						designPanel.setLayout(null);
						int height1 = designPanel.getHeight();
						int width1 = designPanel.getWidth();
						// label31
						final JLabel label31 = new JLabel("<html><font size=\"5\" color =\"white\">单击选择资源（图片、视频）</font></html>");
						label31.setBorder(BorderFactory.createLineBorder(Color.GREEN));
						label31.setBounds(0,0,width1,height1/8*7);
						label31.addMouseListener(new MouseAdapter(){
							@Override
							public void mouseClicked(MouseEvent arg0) {
				                JFileChooser fileChooser = new JFileChooser();
				                fileChooser.setFileFilter(new PicAndVideoFilter());
				                int i = fileChooser.showOpenDialog(null);
				                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				                if(i == JFileChooser.APPROVE_OPTION){
				                    File file = fileChooser.getSelectedFile();
				                    String url = file.getAbsolutePath().replace("\\", "/");
				                    if(url.endsWith(".bmp") || url.endsWith(".jpg") || url.endsWith("png") || url.endsWith(".gif")){
				                    	label31.setName("image");
				                    }
				                    if(url.endsWith(".mp4") || url.endsWith(".mpeg") || url.endsWith(".mpg")){
				                    	label31.setName("video");
				                    }

				                    label31.setText("<html><font size=\"5\" color =\"white\">"+url+"</font></html>");
				                    label31.setToolTipText(url);
				                }
							}
						});
						designPanel.add(label31);
		
						// label32
						final JLabel label32 = new JLabel("<html><font size=\"5\" color =\"white\">单击设置文字信息</font></html>");
						label32.setBorder(BorderFactory.createLineBorder(Color.GREEN));
						label32.setBounds(0, height1/8*7, width1 / 4 * 3, height1/8);
						label32.addMouseListener(new MouseAdapter(){
							@Override
							public void mouseClicked(MouseEvent arg0) {
								new TextFrame(label32).setVisible(true);
							}
						});
						designPanel.add(label32);
						
						// label33
						final JLabel label33 = new JLabel("<html><font size=\"5\" color =\"white\">此处将显示时间</font></html>");
						label33.setBorder(BorderFactory.createLineBorder(Color.GREEN));
						label33.setBounds(width1 / 4 * 3, height1 / 8 * 7, width1 / 4, height1 / 6);
						label33.setName("clock");
						label33.setToolTipText("clock");
						designPanel.add(label33);
						designPanel.updateUI();
						break;
					}
				}
			}
		});

		btnLicense.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new TextFilter());
					int i = fileChooser.showOpenDialog(null);
					fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					if(i == JFileChooser.APPROVE_OPTION){
					    File file = fileChooser.getSelectedFile();
					    File existFile = new File("C:/CPSMini/date");
					    if(existFile.exists()){ 
					    	InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
							BufferedReader br = new BufferedReader(isr);
							String data = br.readLine();
							InputStreamReader existFileist = new InputStreamReader(new FileInputStream(existFile), "GBK");
							BufferedReader existFilebr = new BufferedReader(existFileist);
							String existFiledata = existFilebr.readLine();
							if(data.equals(existFiledata)){
								JOptionPane.showMessageDialog(null, "请勿使用已使用过的License，否则会导致系统无法使用！");
								return;
							}
							br.close();
							existFilebr.close();
					    }
						InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
						BufferedReader br = new BufferedReader(isr);
						String data = br.readLine();
						String header =  data.substring(0,2);
						boolean flag = false;
						while (data != null && !data.trim().isEmpty()) {
							if(data.equals("ee49cf02f4857be0ccd7d909cedd6ec11ff2ca5f23dfcc1ce4b48c06a86a9d8b49467067e3cdd9b497c429f90f300594eaf446802b85fcff")){
								FileWriter fw = new FileWriter(new File("C:/CPSMini/date.txt"), false);
								BufferedWriter bw = new BufferedWriter(fw);
								bw.append(header + String.valueOf(System.currentTimeMillis() + 50l * 366 * 24 * 60 * 60 * 1000));
								bw.flush();
								bw.close();
								br.close();
								flag = true;
								JOptionPane.showMessageDialog(null, "License验证成功！");
								break;
							}
							data = br.readLine();
						}
						br.close();
						if(!flag){
							if(failtimes == 3){
								JOptionPane.showMessageDialog(null, "不合法的Licens，验证失败！，已验证3次不通过，系统将无法使用！");
								System.exit(0);
							}
							failtimes ++;
							JOptionPane.showMessageDialog(null, "不合法的Licens，验证失败！，请注意：验证3次不通过，系统将无法使用！");
						}else{
							try {
								FileOperationTool.ChannelCopy(file, new File("C:/CPSMini/date"));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					    if(file.exists()){
					    	file.deleteOnExit();
					    }
					}
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		btAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int resolutionindex = jcbresolution.getSelectedIndex();
				String resolutionSelected = listTimes.get(resolutionindex).getValue().split("_")[0];
				
				int templateindex = jcbTemplate.getSelectedIndex();
				int templateSelected = listTemplate.get(templateindex).areas;
				
				if(resolutionindex == 0){
					JOptionPane.showMessageDialog(null, "请选择分辨率");
					return;
				}
				if(templateindex == 0){
					JOptionPane.showMessageDialog(null, "请选择区域数");
					return;
				}
				
				int realResolutionWidth = Integer.parseInt(resolutionSelected.split("\\*")[0]);
				int designResolutionWidth = designPanel.getWidth();
				float beishu = realResolutionWidth * 1.0f / designResolutionWidth;
				
				Map<Integer, OnebyoneDTO> ones = new HashMap<Integer, OnebyoneDTO>();
				
				Component[] coms = designPanel.getComponents();
				exchange(coms);
				
				for(int i = 0; i < coms.length; i++){
					JLabel label = (JLabel)coms[i];
					if(label.getName() == null){
						JOptionPane.showMessageDialog(null, "请选择资源或输入文字信息");
						return;
					}

					float x = (float)(label.getBounds().getX() * beishu);
					float y = (float)(label.getBounds().getY() * beishu);
					float width = label.getWidth() * beishu;
					float height = label.getHeight() * beishu;

					ResolutionDTO dimention = new ResolutionDTO((int)java.lang.Math.floor(x),(int)java.lang.Math.floor(y),(int)java.lang.Math.floor(width),(int)java.lang.Math.floor(height));
					
					OnebyoneDTO one = null;
					if(label.getName().equals("image")){
						one = new OnebyoneDTO(dimention,label.getToolTipText(),null);
					}
					if(label.getName().equals("video")){
						one = new OnebyoneDTO(dimention,label.getToolTipText(),null);
					}
					if(label.getName().equals("text")){
						String direction = label.getToolTipText().split("_")[0];
						one = new OnebyoneDTO(dimention,label.getToolTipText().split("_")[1],direction);
					}
					if(label.getName().equals("clock")){
						one = new OnebyoneDTO(dimention,"clock",null);
					}
					
					one.setType(label.getName());
					ones.put(i, one);
				}

				ProgramDTO program = new ProgramDTO(templateSelected,ones);
				playlist.add(program);
				if(program.getAreas() == 1){
					pVector.add("节目："+program.getAreas()+"区域（图片/视频）");
				}
				if(program.getAreas() == 2){
					pVector.add("节目："+program.getAreas()+"区域（图片/视频，文字）");
				}
				if(program.getAreas() == 3){
					pVector.add("节目："+program.getAreas()+"区域（图片/视频，文字，时间）");
				}
				
				programsList.setListData(pVector);
			}
		});

		btReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				programsList.removeAll();
				pVector.clear();
				programsList.updateUI();
				playlist.clear();
			}
		});

		this.setLayout(null);
		this.add(programsListLabel);
		this.add(programsList);
		this.add(resolutionLabel);
		this.add(jcbresolution);
		this.add(templateLabel);
		this.add(jcbTemplate);
		this.add(btAdd);
		this.add(btReset);
		
		this.add(ip);
		this.add(btnTest);
		this.add(btnPushPlaylist);
		this.add(btnSelectDevices);
		this.add(btnLicense);
		
		this.add(designScrollPanel);
		this.setVisible(true);
		
		jcbresolution.setSelectedIndex(3);
		jcbresolution.setEnabled(false);
	}

	public static void main(String[] args) {
		new Main();
	}
	public static boolean isTest = false;
	
	private boolean copyIps(){
		try {
			File f = new File("C:/CPSMini/device");
			if(!f.exists()){
				f.createNewFile();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean newFile(){
		try {
			File f = new File("C:/CPSMini/date.txt");
			if(f.exists()){
				if(!isTest){
					long now = System.currentTimeMillis();
					InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "GBK");
					BufferedReader br = new BufferedReader(isr);
					String date = br.readLine();
					if(date == null){
						JOptionPane.showMessageDialog(null, "相关文件被删除或被修改，程序将无法使用！");
						System.exit(0);
					}
					
					if (date != null) {
						if(date.startsWith("CPSMini")){
							FileWriter fw = new FileWriter(f, false);
							BufferedWriter bw = new BufferedWriter(fw);
							long currentMill = System.currentTimeMillis();
							long exp = currentMill + 30L * 1 * 24 * 60 * 60 * 1000;
							bw.append(String.valueOf(exp));
							bw.flush();
							bw.close();
							date = String.valueOf(exp);
						}else{
							if(date.contains("a")){
								Main.limit = Integer.parseInt(date.substring(0, 1)); 
								date = date.substring(2);
							}
						}
						long start = Long.parseLong(date);
						if((now - start) >= 30L * 3 * 24 * 60 * 60 * 1000){
							JOptionPane.showMessageDialog(null, "试用期已到，请联系我们获取License!");
							System.exit(0);
						}
					}
					br.close();
				}
			}else{
				JOptionPane.showMessageDialog(null, "相关文件被删除或被修改，程序将无法使用！");
				System.exit(0);
			}
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}

class PicFilter extends FileFilter {
	
	public boolean accept(java.io.File f) {
		if (f.isDirectory()){
			return true;
		}
		String ext = f.getName().toLowerCase();
		boolean flag = ext.endsWith(".bmp") || ext.endsWith(".jpg") || ext.endsWith("png") || ext.endsWith(".gif");
		return flag;
	}

	@Override
	public String getDescription() {
		return "请选择图片格式：*.bmp，*.jpg，*.png，*.gif";
	}
}

class VideoFilter extends FileFilter {
	
	public boolean accept(java.io.File f) {
		if (f.isDirectory()){
			return true;
		}
		String ext = f.getName().toLowerCase();
		boolean flag = ext.endsWith(".mp4") || ext.endsWith(".mpeg") || ext.endsWith(".mpg");
		return flag;
	}

	@Override
	public String getDescription() {
		return "请选择视频格式：*.mp4，*.mpeg，*.mpg";
	}
}

class PicAndVideoFilter extends FileFilter {
	
	public boolean accept(java.io.File f) {
		if (f.isDirectory()){
			return true;
		}
		String ext = f.getName().toLowerCase();
		boolean flag = ext.endsWith(".bmp") || ext.endsWith(".jpg") || ext.endsWith("png") || ext.endsWith(".gif") || ext.endsWith(".mp4") || ext.endsWith(".mpeg") || ext.endsWith(".mpg");
		return flag;
	}

	@Override
	public String getDescription() {
		return "请选择图片或者视频格式：*.bmp，*.jpg，*.png，*.gif，*.mp4，*.mpeg，*.mpg";
	}
}

class TextFilter extends FileFilter {
	
	public boolean accept(java.io.File f) {
		if (f.isDirectory()){
			return true;
		}
		String ext = f.getName().toLowerCase();
		boolean flag = ext.endsWith(".txt");
		return flag;
	}

	@Override
	public String getDescription() {
		return "请选择License文件";
	}
}

// text frame
class TextFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	public static String text;
	public static String direction;
	
	public TextFrame(final JLabel label){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int x = (int) screenSize.getWidth();
		int y = (int) screenSize.getHeight();
		this.setBounds((x - 800) / 2, (y - 150) / 2, 800, 150);
		this.setResizable(false);
		this.setTitle("文本信息编辑");
		this.setLayout(null);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		JLabel flyLabel = new JLabel("请选择文字滚动方向：");
		List<String> directions = new ArrayList<String>();
		directions.add("自右向左");
		directions.add("静止不动");
		directions.add("自左向右");
		final JComboBox directionDrop = new JComboBox(directions.toArray());
		
		flyLabel.setBounds(20, 20, 200, 20);
		directionDrop.setBounds(240, 20, 200, 20);
		this.add(flyLabel);
		this.add(directionDrop);
		
		final JTextField text = new JTextField(1);
		text.setBounds(20, 60, 600, 20);
		this.add(text);
		text.addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e) {
				String value = text.getText().trim();
				if(value.equals("") || value.equals("请输入文字信息")){
					text.setText("");
				}
			}
		});
		
		JButton button = new JButton("确定");
		button.setBounds(680, 60, 60, 20);
		this.add(button);
		
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String value = text.getText().trim();
				if(value.equals("") || value.equals("请输入文字信息")){
					text.setText("请输入文字信息");
					return;
				}
				TextFrame.text = value;
				TextFrame.direction = directionDrop.getSelectedItem().toString();
				label.setText("<html><font size=\"5\" color =\"white\">"+TextFrame.text+"</font></html>");
				label.setToolTipText(TextFrame.direction+"_"+TextFrame.text);
				label.setName("text");
				TextFrame.this.dispose();
			}
		});
	}
}

//device list frame
class DeviceListFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	JoymindCommDLLLib instance = JoymindCommDLLLib.INSTANCE;
	
	public DeviceListFrame(final List<ProgramDTO> playlist){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int x = (int) screenSize.getWidth();
		int y = (int) screenSize.getHeight();
		this.setBounds((x - 800) / 2, (y - 600) / 2, 800, 600);
		this.setResizable(false);
		this.setTitle("选择设备");
		this.setLayout(null);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		JLabel unSelLabel = new JLabel("未选择设备列表，双击添加");
		JLabel selectedLabel = new JLabel("已选择设备列表，双击删除");
		JLabel allLable = new JLabel("所有设备列表");
		
		// read ips
		try {
			Main.ipVector.clear();
			File f = new File("C:/CPSMini/device");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "GBK");
			BufferedReader br = new BufferedReader(isr);
			String data = br.readLine();
			while (data != null) {
				if(!data.trim().isEmpty()){
					Main.ipVector.add(data);
				}
				data = br.readLine();
			}
			br.close();
		} catch (HeadlessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		final Vector<String> unSel = new Vector<String>();
		for(int i = 0; i < Main.ipVector.size(); i++){
			unSel.add(Main.ipVector.get(i));
		}
		final JList unSelect = new JList(unSel);
		
		final Vector<String> sel = new Vector<String>();
		final JList onSelect = new JList(sel);
		
		final Vector<String> all = Main.ipVector;
		final JList allList = new JList(all);
		
		JScrollPane unscroller = new JScrollPane(unSelect);
		unscroller.setAutoscrolls(true);
		JScrollPane onscroller = new JScrollPane(onSelect);
		onscroller.setAutoscrolls(true);
		JScrollPane allscroller = new JScrollPane(allList);
		allscroller.setAutoscrolls(true);
		
		unSelLabel.setBounds(20, 20, 200, 20);
		selectedLabel.setBounds(240, 20, 200, 20);
		allLable.setBounds(500, 20, 200, 20);
		
		unscroller.setBounds(20, 60, 200, 400);
		onscroller.setBounds(240, 60, 200, 400);
		allscroller.setBounds(500, 60, 200, 400);
		
		this.add(unSelLabel);
		this.add(selectedLabel);
		this.add(allLable);
		
		this.add(unscroller);
		this.add(onscroller);
		this.add(allscroller);
		
		// add double-click action on unselected ips
		unSelect.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String selvalue = (String)unSelect.getSelectedValue();
					int i = instance.AdjustTime(selvalue);
					if(i == 0){
						JOptionPane.showMessageDialog(null, "此IP设备不在线，无法选择！");
						return;
					}
					unSel.remove(selvalue);
					sel.add(selvalue);
					unSelect.updateUI();
					onSelect.updateUI();
				}
			}
		});
		
		// add double-click action on onselected ips
		onSelect.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String selvalue = (String)onSelect.getSelectedValue();
					sel.remove(selvalue);
					unSel.add(selvalue);
					unSelect.updateUI();
					onSelect.updateUI();
				}
			}
		});
		
		JButton button = new JButton("确定");
		button.setBounds(20, 480, 60, 20);
		this.add(button);
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(sel.size() <= 0){
					JOptionPane.showMessageDialog(null, "请选择设备！");
					return;
				}
				int yes = JOptionPane.showConfirmDialog(null, "节目正在下发中，在弹出下发结果前，请务必不要再下发节目！");
				if(yes != 0){
					JOptionPane.showMessageDialog(null, "节目下发取消！");
					return;
				}
				DeviceListFrame.this.dispose();
				PlaylistService pService = new PlaylistService();
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < sel.size(); i++){
					try {
						boolean flag = pService.pushPlaylist(playlist, sel.get(i));
						String str1 = null;
						if(flag){
							str1 = "下发成功";
						}else{
							str1 = "下发失败";
						}
						sb.append(sel.get(i)+": " + str1 + "\n");
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(null, sb.toString());
			}
		});
		
		final JTextField ip = new JTextField("请输入设备的IP");
		ip.setBounds(500, 480, 100, 20);
		ip.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(ip.getText().trim().equals("请输入设备的IP")){
					ip.setText("");
				}
			}
		});
		this.add(ip);
		
		JButton add = new JButton("保存到列表");
		add.setBounds(600, 480, 100, 20);
		this.add(add);
		
		add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Main.ipVector.size() >= Main.limit){
					JOptionPane.showMessageDialog(null, "申请License以添加更多设备至设备列表！");
					return;
				}
				
				
				String str = ip.getText().trim();
				Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
				
				// check the ip pattern
				Matcher matcher = pattern.matcher(str);
				if(!matcher.matches()){
					JOptionPane.showMessageDialog(null, "IP格式不对，请检查！");
					return;
				}
				if(Main.ipVector.contains(str)){
					JOptionPane.showMessageDialog(null, "此IP已存在，无需重复添加！");
					return;
				}
				
				int j = instance.AdjustTime(str);
				if(j == 0){
					JOptionPane.showMessageDialog(null, "IP: "+str+"不通，请检查！");
					return;
				}
				
				Main.ipVector.add(str);
				unSel.add(str);
				File f = new File("C:/CPSMini/device");
				try {
					FileWriter fw = new FileWriter(f, true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.newLine();
					bw.append(str);
					bw.flush();
					bw.close();
					JOptionPane.showMessageDialog(null, "IP: "+str+"添加成功！");
					unSelect.updateUI();
					allList.updateUI();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
