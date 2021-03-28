package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Automaton;
import model.MealyAutomaton;
import model.MooreAutomaton;


public class Controller {

	public static final String MEALY = "Mealy Aut.";
	public static final String MOORE = "Moore Aut.";

	@FXML
	private TextField textInput;

	@FXML
	private TextField textOutput;

	@FXML
	private TextField numStates;

	@FXML
	private ComboBox<String> machines;

	@FXML
	private Button table;

	@FXML
	private Button calculate;

	@FXML
	private ScrollPane pane;
	
	@FXML
	private ScrollPane pane1;

	private GridPane matrix;
	
	private GridPane matrix1;

	private int rows;

	private int columns;

	private TextField[][] tf;

	private String[] arrInput;

	private String[] arrOutput;

	private Automaton logicAutomata;

	
	//It initialises the options to choose a type of automaton
	@FXML
	public void initialize() {
		machines.getItems().addAll(MEALY, MOORE);
		machines.setValue(MEALY);
		calculate.setDisable(true);

	}
	
	
	//Create a table according to the entered data, it allows to make a specific format according if the machine is a Moore or Mealy automaton
	@FXML
	public void create(ActionEvent e) {

		if (textInput.getText().isEmpty() || textOutput.getText().isEmpty() || numStates.getText().isEmpty()
				|| machines.getValue().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("¡Error!");
			alert.setTitle("Campos no completos");
			alert.setContentText("Debe rellenar todos los campos antes de continuar");
			alert.showAndWait();
		}

		try {
			rows = Integer.parseInt(numStates.getText());
			arrInput = textInput.getText().split(",");
			columns = arrInput.length;
		} catch (NumberFormatException e2) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("¡Error!");
			alert.setTitle("Formato Invalido");
			alert.setContentText("Debe ingresar un numero");
			alert.showAndWait();
		}

		if (rows <= 15) {

			matrix = new GridPane();

			matrix.setHgap(3);
			matrix.setVgap(3);

			pane.setContent(matrix);

			try {

				tf = new TextField[columns + 1][rows];
				makeHeader(matrix);

				for (int i = 1; i < rows + 1; i++) {
					TextField f1 = new TextField("Q" + (i - 1) + "");
					f1.setEditable(false);
					f1.setPrefWidth(45);
					matrix.add(f1, 0, i);
					for (int j = 1; j < columns + 1; j++) {
						f1 = new TextField("");
						f1.setPrefWidth(45);
						f1.setPromptText("Qi" + (machines.getValue().equals(MEALY) ? ",r" : ""));
						tf[j - 1][i - 1] = f1;
						matrix.add(f1, j, i);
					}
					if (machines.getValue().equals(MOORE)) {
						f1 = new TextField("");
						f1.setPrefWidth(45);
						f1.setPromptText("r");
						tf[columns][i - 1] = f1;
						matrix.add(f1, columns + 1, i);
					}
				}

			} catch (NegativeArraySizeException | IllegalArgumentException | NullPointerException e2) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("¡Error!");
				alert.setTitle("Campos no completos");
				alert.setContentText("El automata ingresado esta mal digitado o no es valido");
				alert.showAndWait();
			}
		} else {

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("¡Restriccion!");
			alert.setTitle("Limite de estados excedidos");
			alert.setContentText("No se admiten mas de 15 estados");
			alert.showAndWait();
		}
		calculate.setDisable(false);
		

	}
	
	
	//Allows to show a new table with the reduced automata
	@FXML
	private void calculate(ActionEvent c) {

		try {
			minimizeMachine();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("¡Error!");
			alert.setTitle("Campos no completos");
			alert.setContentText("El automata ingresado esta mal digitado o no es valido");
			alert.showAndWait();
		}

	}
	
	
	//Used to set the header of the table
	private void makeHeader(GridPane grid) {
		for (int i = 1; i < columns + 1; i++) {
			TextField tf = new TextField(arrInput[i - 1]);
			tf.setEditable(false);
			tf.setPrefWidth(45);
			grid.add(tf, i, 0);
		}

	}
	
	
	
	//According to the selected machine and the entered data generates a reduced automaton using the respective algorithms in each case
	@FXML
	public void minimizeMachine() {

		if (machines.getValue().equals(MOORE)) {
			Moore();
		} else {
			Mealy();
		}
		pane.setContent(matrix);
		

	}
	
	//Calling the Moore Automaton functions
	public void Moore() {

		String split = "\\W+"; // split any non word
		String[] tokensInput = textInput.getText().split(split);
		String[] tokensOutput = textOutput.getText().split(split);
		int states = Integer.parseInt(numStates.getText());
		
		String[][] mt = readTextFields(MOORE);
		
		String[][] toUse = new String[mt[0].length][mt.length];
		
		String[] sta = new String[states];
		
		logicAutomata = new MooreAutomaton(states, "Q0" + "", tokensInput, tokensOutput);	
		for (int x=0; x < mt.length; x++) {
			for (int y=0; y < mt[x].length; y++) {
			    toUse[y][x] = mt[x][y];
			}
		}
		
		for (int i = 0; i < toUse.length; i++) {
			for (int j = 0; j < toUse[0].length-1; j++) {
				logicAutomata.addStates("Q" + i + "", tokensInput[j], toUse[i][j] ,toUse[i][2] + "");
			}
			sta[i] = "Q" + i + "";
		}
		
		
		
		logicAutomata.setStates(sta);
		logicAutomata.createEquivalentMinimum();
		
		String[][] toSet = logicAutomata.getDataToShow();
		
		
		matrix1 = new GridPane();
		
		matrix1.setHgap(3);
		matrix1.setVgap(3);

		pane1.setContent(matrix1);

		tf = new TextField[toSet[0].length+1][toSet.length];
		makeHeader(matrix1);
		
		
		for (int i = 1; i < toSet.length+1; i++) {
			TextField f1 = new TextField(toSet[i-1][0]);
			f1.setEditable(false);
			f1.setPrefWidth(45);
			matrix1.add(f1, 0, i);
			for (int j = 1; j < toSet[0].length; j++) {
				f1 = new TextField(toSet[i-1][j]);
				f1.setPrefWidth(45);
				tf[toSet[0].length][i-1] = f1;
				matrix1.add(f1, j, i);
			}	
		}
		
	}
	
	//Calling the Mealy Automaton functions
	public void Mealy() {
		
		String split = "\\W+"; // split any non word
		String[] tokensInput = textInput.getText().split(split);
		String[] tokensOutput = textOutput.getText().split(split);
		int states = Integer.parseInt(numStates.getText());
		
		String[][] mt = readTextFields(MEALY);
		
		String[][] toUse = new String[mt[0].length][mt.length];
		
		String[] sta = new String[states];
		
		
		
		logicAutomata = new MealyAutomaton(states, "Q0" + "", tokensInput, tokensOutput);
		
		for (int x=0; x < mt.length; x++) {
			for (int y=0; y < mt[x].length; y++) {
			    toUse[y][x] = mt[x][y];
			}
		}
		
		for (int i = 0; i < toUse.length; i++) {
			for (int j = 0; j < toUse[0].length; j++) {
				String s = toUse[i][j];
				String[] plex = s.split(split);
				logicAutomata.addStates("Q" + i + "", tokensInput[j], plex[0] ,plex[1] + "");
				System.out.println("Q" + i + "");
				System.out.println(tokensInput[j]);
				System.out.println(plex[0]);
				System.out.println(plex[1]);
				
				
			}
			sta[i] = "Q" + i + "";
		}
		
		logicAutomata.setStates(sta);
		logicAutomata.createEquivalentMinimum();
		
		String[][] toSet = logicAutomata.getDataToShow();
		
		matrix1 = new GridPane();
		
		matrix1.setHgap(3);
		matrix1.setVgap(3);

		pane1.setContent(matrix1);

		tf = new TextField[toSet[0].length+1][toSet.length];
		makeHeader(matrix1);
		
		for (int i = 1; i < toSet.length+1; i++) {
			TextField f1 = new TextField(toSet[i-1][0]);
			f1.setEditable(false);
			f1.setPrefWidth(45);
			matrix1.add(f1, 0, i);
			for (int j = 1; j < toSet[0].length; j++) {
				f1 = new TextField(toSet[i-1][j]);
				f1.setPrefWidth(45);
				tf[j - 1][i-1] = f1;
				matrix1.add(f1, j, i);
			}	
		}
		
		
	}
	
	//Auxiliar method to set information in the tables
	private String[][] readTextFields(String type) {
		String[][] matrix = new String[type.equals(MOORE) ? columns + 1 : columns][rows];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = tf[i][j].getText();
			}
		}
		return matrix;
	}

}
