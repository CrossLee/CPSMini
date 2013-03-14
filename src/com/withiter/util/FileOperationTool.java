package com.withiter.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileOperationTool {

	private static final int BUFFER_SIZE = 16 * 1024; // 16M

	// 资源类型
	enum FileTypes {
		IMAGETYPE, VIDEOTYPE, AUDIOTYPE, TEXTTYPE, OTHERTYPES
	};

	// 删除指定path的文件
	public static boolean del(String path) {
		File file = new File(path);
		return file.delete();
	}

	// 文件拷贝
	public static void copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (in.read(buffer) > 0)
				out.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in)
					in.close();
				if (null != out)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// file copy 2
	public static void ChannelCopy(File f1, File f2) throws Exception {
		int length = 2097152;
		FileInputStream in = new FileInputStream(f1);
		FileOutputStream out = new FileOutputStream(f2);
		FileChannel inC = in.getChannel();
		FileChannel outC = out.getChannel();
		ByteBuffer b = null;
		boolean flag = true;
		while (flag) {
			if (inC.position() == inC.size()) {
				inC.close();
				outC.close();
				flag = false;
			}else{
				if ((inC.size() - inC.position()) < length) {
					length = (int) (inC.size() - inC.position());
				} else
					length = 2097152;
				b = ByteBuffer.allocateDirect(length);
				inC.read(b);
				b.flip();
				outC.write(b);
				outC.force(false);
			}
		}
	}

	// 获取文件后缀名
	public static String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	public static String getContentOfTxt(String txtPath) throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				txtPath), "GBK");
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String data = br.readLine();
		while (data != null) {
			sb.append(data.trim());
			data = br.readLine();
		}
		return sb.toString();
	}

}