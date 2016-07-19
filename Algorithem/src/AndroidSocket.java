import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class AndroidSocket {
	//����˿ڣ�
	private static final int SOCKET_PORT = 50000;
	//ʹ��ArrayList������е�Socket
	public static ArrayList<Socket> sockets = new ArrayList<>();
	
	public void initMyServer(){
//		System.out.println("2222222222222SERVER IS RUNNING!!!");
		try(ServerSocket serverSocket = new ServerSocket(SOCKET_PORT)) {
			while(true){
				Socket socket = serverSocket.accept();
				//������˽��յ���socket��Ž��б�
				sockets.add(socket);
				System.out.println("SERVER IS RUNNING!!!");
				new Thread(new ServerThread(socket)).start();
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR��������SERVER IS RUNNING!!!");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		AndroidSocket androidSocket = new AndroidSocket();
//		System.out.println("111111111111111SERVER IS RUNNING!!!");
		androidSocket.initMyServer();
	}
}
