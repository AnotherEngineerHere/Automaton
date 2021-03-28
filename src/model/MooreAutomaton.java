package model;

import java.util.*;

public class MooreAutomaton extends Automaton {
	
	private HashMap<String, HashMap<String, String>> connections;
	private HashMap<String, String> state_output;
	private HashMap<String, HashSet<String>> initial_Partition;
	private Vector<HashSet<String>> sets;// 

	public MooreAutomaton(int number_states, String initial_state, String[] alphabet_input, String[] alphabet_ouput) {
		super(number_states, initial_state, alphabet_input, alphabet_ouput);
		this.connections = new HashMap<>();
		this.state_output = new HashMap<>();
		this.initial_Partition = new HashMap<>();
		this.sets = new Vector<>();
	}

	@Override
	public void addStates(String state_name, String input_char, String destiny_state, String output_char) {
		
		state_output.put(state_name, output_char);
		HashMap<String, String> tempHashMap = connections.getOrDefault(state_name, new HashMap<>());
		tempHashMap.put(input_char, destiny_state);
		connections.put(state_name, tempHashMap);
		
	}

	@Override
	public void eliminateInaccesibleStates() {
		
		HashMap<String, Boolean> visited = new HashMap<>();
		
		for (int i = 0; i < number_states; i++) {
			visited.put(states[i], false);
		}

		Stack<String> stack = new Stack<>();
		String s;
		stack.add(initial_state);

		while (!stack.isEmpty()) {
			s = stack.pop();

			if (!visited.get(s)) {
				visited.put(s, true);
			}

			for (Map.Entry<String, String> entry : connections.get(s).entrySet()) {
				String v = entry.getValue();
				if (!visited.get(v))
					stack.push(v);
			}

		}

		for (int i = 0; i < number_states; i++) {
			if (!visited.get(states[i])) {
				connections.remove(states[i]);
			}
		}
		
	}

	@Override
	public void createEquivalentMinimum() {
		
		eliminateInaccesibleStates();
		createFirstPartition();
		
		for (Map.Entry<String, HashSet<String>> entry : initial_Partition.entrySet()) {
			sets.add(entry.getValue());
		}

		boolean state = true;

		while (state) {
			int size = sets.size();
			for (int i = 0; i < sets.size(); i++) {
				verify(i);
			}
			if (size == sets.size()) {
				state = false;
			} else {
				size = sets.size();
			}
		}
		
	}
	
	public void verify(int value) {
		
		int size = sets.size();
		boolean create = false;
		String last = "";
		LinkedList<String> queue = new LinkedList<>();

		for (String strings : sets.get(value)) {
			if (!last.isEmpty()) {
				boolean exist = true;
				for (HashSet<String> c : sets) {
					for (int i = 0; i < alphabet_input.length && exist; i++) {
						if (!c.contains(connections.get(last).get(alphabet_input[i])) && c.contains(connections.get(strings).get(alphabet_input[i]))) {
							exist = false;
						}
						if (c.contains(connections.get(last).get(alphabet_input[i])) && !c.contains(connections.get(strings).get(alphabet_input[i]))) {
							exist = false;
						}
					}
				}
				if (!exist && !create) {
					sets.add(new HashSet<>());
					create = true;
				}
				if (!exist && create) {
					sets.get(size).add(strings);
					queue.offer(strings);
				}
			}
			if (!create) {
				last = strings;
			}
		}
		if (create) {
			while (!queue.isEmpty()) {
				sets.get(value).remove(queue.poll());
			}
			verify(size);
		}
	}


	@Override
	public void createFirstPartition() {
		HashMap<String, HashSet<String>> map = new HashMap<>();

		for (Map.Entry<String, String> entry : state_output.entrySet()) {
			HashSet<String> set = map.getOrDefault(entry.getValue(), new HashSet<>());
			set.add(entry.getKey());
			map.put(entry.getValue(), set);
		}

		initial_Partition = map;
		
	}


	@Override
	public String[][] getDataToShow() {
		
		String result[][] = new String[sets.size()][alphabet_input.length + 2];

		for (int i = 0; i < result.length; i++) {
			Arrays.fill(result[i], "");
		}

		HashMap<String, String> rename = new HashMap<>();

		int verifier = 0;
		for (HashSet<String> s : sets) {
			for (String st : s) {
				rename.put(st, "P" + verifier);
			}
			verifier++;
		}
		
		for (int i = 0; i < result.length; i++) {
			result[i][0] = "P" + (i);
			String temp = "";
			for (int j = 1; j < result[0].length - 1; j++) {
				for (String s : sets.get(i)) {
					result[i][j] = rename.get(connections.get(s).get(alphabet_input[j - 1]));
					temp = s;
					break;
				}
			}
			result[i][alphabet_input.length + 1] = state_output.get(temp);
		}

		return result;
	}
	
	

}
