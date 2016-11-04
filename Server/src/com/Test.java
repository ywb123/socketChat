package com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	static ServerSocket server=null;
	static Socket s=null;
	static ExecutorService mes=null;
	static ArrayList<Socket> sockets=new ArrayList<Socket>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			server=new ServerSocket(4444);
			System.out.println("�ȴ����ӡ�����");
			mes=Executors.newCachedThreadPool();
			while(true){
				s=server.accept();
				sockets.add(s);
				mes.execute(new MyConThread(s));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				s.close();
				server.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("���ӽ���");
	}
	public static class MyConThread implements Runnable{
		private Socket s;
		public MyConThread(Socket s){
			this.s=s;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println(s.getInetAddress()+"��������");
			sendMessage(s.getInetAddress()+"��������");
			
			String strget="";
			while(!strget.equals("exit")){
				//��ȡ�ͻ�������
				InputStream iStream;
				try {
					iStream = s.getInputStream();
					byte b[]=new byte[1024];
					int n=iStream.read(b);
					strget=new String(b,0,n);
					System.out.println(s.getInetAddress()+":"+strget);
					sendMessage(s.getInetAddress()+":"+strget);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(s.getInetAddress()+"�˳���");
			sendMessage(s.getInetAddress()+"�˳���");
			
		}
		
		
	}

	public static void  sendMessage(String str){
		//���ͻ��˷�������
		OutputStream oStream;
		try {
			
			for(Socket soc:sockets){
				oStream = soc.getOutputStream();
				byte[] ob=new byte[1024];
				String ostr=soc.getInetAddress()+":"+str;
				ob=ostr.getBytes();
				oStream.write(ob);
				oStream.flush();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
