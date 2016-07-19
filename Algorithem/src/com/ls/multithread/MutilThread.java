package com.ls.multithread;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
/*
 * ���߳�������
	���߳�����ԭ����ȡҪ���ص��ļ��ĳ��ȣ��������õ��߳�������ÿ���߳����ص���ʼ�����ֹ�㣬
	�ļ����ȣ�length
	�߳�����threadCount
	�߳����س��ȣ�size = length/threadCount
	�߳�ID��threadID ��0��ʼ��
	��ʼ�㣺startIndex = threadID * size
	��ֹ�㣺endIndex = (threadID + 1) * size - 1
*/
public class MutilThread {
	static int length;
	static int threadCount = 3;
	//�����ļ�·��
		static 	String path = "http://192.168.199.166:8080/TGPSetup.exe";
	/*//��ȡ�����ļ��ĳ���
*/
	public static void main(String[] args){
		int size,startIndex,endIndex;
		
		
		try {
			URL url = new URL(path);
			HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
			httpsURLConnection.setRequestMethod("GET");
			//���ó�ʱ
			httpsURLConnection.setConnectTimeout(5000);
			httpsURLConnection.setReadTimeout(5000);
			httpsURLConnection.connect();
			if(httpsURLConnection.getResponseCode() == 200){
				length = httpsURLConnection.getContentLength();
			}else {
				return;
			}
			//�ڱ�������һ���������ļ�ͬ��С����ʱ�ļ�
			/*
			 * ����洢�ļ�
			 * file �����д����·�����򽫴洢����Ŀ��Ŀ¼��
			 * mode ��,r,rw,rws,rwd��ģʽ��һ����rwd����Ϊ�ڶ�д��ͬʱ�Ὣ�ļ�ͬ��д��Ӳ���ϣ�����������
			 * */
			RandomAccessFile randomAccessFile = new RandomAccessFile("TGPSetup.exe", "rwd");
			//�����������ļ�ͬ��С����ʱ�ļ�����ռ�ռ�
			randomAccessFile.setLength(length);
			//��Ҫһֱ����RandomAccessFile������
			randomAccessFile.close();
			size = length / threadCount;
			for (int i = 0; i < threadCount; i++) {
//				����ÿ���߳����صĿ�ʼλ�������λ��
				startIndex = i * size;
				endIndex = (i + 1) * size -1;
				if(i == threadCount -1){
					endIndex = length - 1;
				}
				//��ʼ���߳�
				System.out.println(i+"----"+startIndex+"---"+endIndex);
				new DownloadThread(startIndex, endIndex, i).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
class DownloadThread extends Thread{
	int startIndex;
	int endIndex;
	int threadID;
	
	public DownloadThread(int startIndex, int endIndex, int threadID) {
		super();
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.threadID = threadID;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			URL url = new URL(MutilThread.path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(5000);
			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.setRequestProperty("Range", "bytes=" + startIndex + 
					"-" + endIndex);
			RandomAccessFile randomAccessFile = new RandomAccessFile("TGPSetup.exe", "rwd");
			if (httpURLConnection.getResponseCode() == 206) {
				InputStream  inputStream = httpURLConnection.getInputStream();
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(bytes)) != -1) {
					randomAccessFile.write(bytes, 0, len);
				}
				randomAccessFile.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
