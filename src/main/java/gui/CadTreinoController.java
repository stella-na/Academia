package gui;
import exception.ElementoJaExisteException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Cliente;
import models.Exercicio;
import javafx.scene.Parent;
import javafx.scene.Scene;
import models.Treino;
import models.Usuario;
import negocio.ServidorAcademia;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CadTreinoController implements Initializable {

	ServidorAcademia servidor = ServidorAcademia.getInstance();

	@FXML
	private Label aviso;
	@FXML
	private ChoiceBox<String> cBTipoTreino;
	private String[] tipo = {"Superior", "Inferior"};
	@FXML
	private Button btnVoltar;
	@FXML
	private Button btnCadastrar;
	@FXML
	private Button btnAdicionar;

	@FXML
	private ListView<Exercicio> lvExercicios;
	private List<Exercicio> listExercicios = new ArrayList<>();
	private List<Exercicio> listExerciciosSelecionados = new ArrayList<>();
	private ObservableList<Exercicio> observableListExercicio;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		carregarListaExercicio();

		cBTipoTreino.getItems().addAll(tipo);
	}

	public void carregarListaExercicio() {

		listExercicios = servidor.exercicioListar();
		observableListExercicio = FXCollections.observableArrayList(listExercicios);
		lvExercicios.setItems(observableListExercicio);
	}

	public void onBntCadastrarTreino() {
		String tipo = cBTipoTreino.getValue();
		Treino treino = new Treino(tipo, listExerciciosSelecionados);

		try {
			servidor.inserir(treino);
			aviso.setText("Treino cadastrado");
		} catch (ElementoJaExisteException e) {
			System.out.println("Treino já cadastrado");
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle("Erro no cadastro");
			alerta.setHeaderText("Treino já cadastrado");
			alerta.setContentText("Esse treino já foi cadastrado");
			alerta.showAndWait();
			throw new RuntimeException(e);
		}
	}

	public void onAddListExercicio() {
		Exercicio exercicio = lvExercicios.getSelectionModel().getSelectedItem();
		listExerciciosSelecionados.add(exercicio);
	}

	public void onBtnVoltar() throws IOException {
		Stage stage;
		Parent root;

		stage = (Stage) btnVoltar.getScene().getWindow();
		root = FXMLLoader.load(getClass().getResource("personal.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void onKeyReleased() {
		boolean cadastrar;
		boolean addExercicio;

		cadastrar = (!cBTipoTreino.getItems().isEmpty() & !lvExercicios.getSelectionModel().getSelectedItems().isEmpty());
		btnCadastrar.setDisable(cadastrar);

		addExercicio = (!cBTipoTreino.getItems().isEmpty() & !lvExercicios.getSelectionModel().getSelectedItems().isEmpty());
		btnCadastrar.setDisable(addExercicio);
	}
}