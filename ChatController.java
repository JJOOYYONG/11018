package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController implements Initializable {
	@FXML
	private TextArea textArea;
	@FXML
	private TextField textField;
	@FXML
	private Button btnSend, btnExit;
	
	private static final int SERVER_PORT = 6000;
	private Socket socket;
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	private String userId, line;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
		textField.setOnAction(event -> send());
		btnSend.setOnAction(event -> send());
		btnExit.setOnAction(event -> Platform.exit());
	} // initialize
	
	
	public void init() {
		String serverIp = Data.map.get("serverIp");
		userId = Data.map.get("name");
		
		try {
			// 서버에 연결
			socket = new Socket(serverIp, SERVER_PORT);
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
			
			// 대화명을 서버에게 전송
			writer.write(userId + "\n");
			writer.flush(); // 버퍼 비우기
			System.out.println("userId : " + userId);
			
			// 서버의 broadcast 또는 귓속말을 받아들이는 작업스레드
			Thread thread = new Thread(() -> receive());
			thread.setDaemon(true); // 데몬(보조)스레드
			thread.start();
			
			// 텍스트입력상자에 포커스주기
			textField.requestFocus();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // init method
	
	
	public void receive() {
		line = "";
		
		while (true) {
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			Platform.runLater(() -> {
				textArea.appendText(line + "\n");
				
				int pos = textArea.getCaretPosition();
				textArea.positionCaret(pos);
			});
		} // while
		
	} // receive method
	
	
	public void send() {
		String message = textField.getText().trim();
		
		try {
			writer.write(message + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			textArea.setText(e.getMessage() + "\n");
		}
		
		textField.setText("");
		textField.requestFocus();
	} // send method
	
	

}
