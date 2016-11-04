package your.ywb.client1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Client1Activity extends Activity {
	public TextView tvContent;
	public EditText etSend;
	public StringBuffer sb=new StringBuffer();
	public boolean isRun=true;
	public Socket socket;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tvContent=(TextView) findViewById(R.id.tvContent);
        etSend=(EditText) findViewById(R.id.etSend);
        try {
			socket=new Socket("10.0.2.2",4444);
		} catch (Exception e) {
			e.printStackTrace();
		}
        new Thread(new Runnable(){
			@Override
			public void run() {
				while(isRun){
					try {
						Thread.sleep(500);
						
						InputStream iStream= socket.getInputStream();
						byte b[]=new byte[1024];
						int n=iStream.read(b);
						String str=new String(b,0,n);
						System.out.println(socket.getInetAddress()+":"+str);
						
						Message msg=new Message();
						msg.what=1000;
						Bundle data=new Bundle();
						data.putString("content", str);
						msg.setData(data);
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}}).start();
    }
    public void send(View v){
    	String strSend=etSend.getText().toString().trim();   	
    	OutputStream oStream;
		try {
			oStream = socket.getOutputStream();
			byte[] ob=new byte[1024];
			ob=strSend.getBytes();
			oStream.write(ob);
			oStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
    Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1000:
				sb.append(msg.getData().getString("content")+"\n");
				tvContent.setText(sb);
				break;

			default:
				break;
			}
		}
    	
    };
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	isRun=false;
    }
}