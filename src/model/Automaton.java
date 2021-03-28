package model;

public abstract class Automaton {
	
	protected int number_states; //Automaton Number of states 
	protected String initial_state;//The Q0 state
	protected String alphabet_input[];//It refers to the symbols which will be entered in the automaton
	protected String alphabet_ouput[];//It refers to the responses which will be the output in the automaton
	protected String states[];//The Q1..Qn states

	

	//Create an Automaton, it is used in both modes, Mealy and Moore
	public Automaton(int number_states, String initial_state, String[] alphabet_input, String[] alphabet_ouput) {
		
		this.number_states = number_states;
		this.initial_state = initial_state;
		this.alphabet_input = alphabet_input;
		this.alphabet_ouput = alphabet_ouput;
		states = new String[number_states];
	}

	public int getNumber_states() {
		return number_states;
	}

	public void setNumber_states(int number_states) {
		this.number_states = number_states;
	}

	public String getInitial_state() {
		return initial_state;
	}

	public void setInitial_state(String initial_state) {
		this.initial_state = initial_state;
	}

	public String[] getAlphabet_input() {
		return alphabet_input;
	}

	public void setAlphabet_input(String[] alphabet_input) {
		this.alphabet_input = alphabet_input;
	}

	public String[] getAlphabet_ouput() {
		return alphabet_ouput;
	}

	public void setAlphabet_ouput(String[] alphabet_ouput) {
		this.alphabet_ouput = alphabet_ouput;
	}

	public String[] getStates() {
		return states;
	}

	public void setStates(String[] states) {
		this.states = states;
	}
	
	
	//Allows to add to a kind of graph the states of the machine
	public abstract void addStates(String state_name, String input_char, String destiny_state, String output_char);
	
	//Method to eliminate the inaccessible states in the graph
	public abstract void eliminateInaccesibleStates();
	
	//It uses the P1 partition in order to obtain,using the Equivalent minimum algorithm for an automaton, a set which contains the states 
	//a new reduced machine
	public abstract void createEquivalentMinimum();
	
	//It allows to create the P1 partition in order to obtain a set of correspondent states
	public abstract void createFirstPartition();
	
	//Allows to get the final information from the data structures and create a matrix with this data to be used in the user output
	public abstract String[][] getDataToShow();
}