import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CharServer {

	public static void main(String[] args) {
	
		//스레드간 공유하는 MAP객체.
		//key는 아이디(채팅명), value는 출력문자 스트림
		
		Map<String, BufferedWriter> map = new ConcurrentHashMap<String, BufferedWriter>();
		
		final int PORT = 6000; // 포트번호
		ServerSocket serverSocket = null;
		
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			
			System.out.println("***채팅서버 ***");
			System.out.println("서버는 클라이언트 소켓의 접속요청을 대기중...");
			
			while(true) {
				//특정 클라이언트 하나가 접속되면 새로운 소켓객체를 리턴함
				socket = serverSocket.accept();
				
				ChatServerTask task = new ChatServerTask(socket,map);
				Thread thread = new Thread(task);
				thread.setDaemon(true);//데몬 보조 스레드
				thread.start();
				
				
			}//while
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				
				
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
					System.out.println("serverSocekt closed");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//finally
		
		
		
		
		
		
		
		
		
		
	}//main

}
