import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


/*�������˽��տͻ��˷��͹�������Ϣ����*/
public class ServerThread implements Runnable {
	private Socket socket = null;
	//�߳��������socket��Ӧ��������
	private BufferedReader bufferedReader = null;
	
	public ServerThread(Socket socket) throws IOException {
		this.socket = socket;
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String content = null;
			while((content = bufferedReader.readLine()) != null){
				for(Socket socket:AndroidSocket.sockets){
					PrintStream printStream = new PrintStream(socket.getOutputStream());
					printStream.println(packMessage(content));
				}
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	
	/*
	 * Function �Թ㲥�����ݽ��а�װ
	 */
	private String packMessage(String content) {
		String result = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		if(content.startsWith("USER_ONE")){
			String message = content.substring(8);
			result = "\n" +"�������" + simpleDateFormat.format(new Date()) + "\n" + message;
		}
		if(content.startsWith("USER_TWO")){
			String message = content.substring(8);
			result = "\n" +"���ɵ�Ȼ" + simpleDateFormat.format(new Date()) + "\n" + message;
		}
		return result;
	}
	
}

