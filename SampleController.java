package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SampleController implements Initializable {
	@FXML
	private TextField tfServerIp, tfName;
	@FXML
	private Button btnConnect;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnConnect.setOnAction(event -> handleBtnConnect(event));
	} // initialize
	
	public void handleBtnConnect(ActionEvent event) {
		String serverIp = tfServerIp.getText().trim();
		String name = tfName.getText().trim();
		
		Data.map.put("serverIp", serverIp);
		Data.map.put("name", name);
		
		
		// Chat.fxml 화면으로 이동하기
		try {
			// fxml 로딩해서 최상위 컨테이너 가져오기
			AnchorPane anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("Chat.fxml"));
			// 최상위 컨터이너를 부착한 새 화면객체 생성
			Scene scene = new Scene(anchorPane);
			// 현재 윈도우창 참조 가져오기
			Stage primaryStage = (Stage) btnConnect.getScene().getWindow();
			// 윈도우창에 새 화면객체 부착
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // handleBtnConnect method
	
}
