/***********************************************************
* Developer: Minhas Kamal (minhaskamal024@gmail.com)       *
* Date: 21-Nov-2016                                        *
* License: MIT License                                     *
***********************************************************/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SpellChecker {
	private HashMap<String, Integer> dictionary;
	private char targetCharacter;
	
	private char[] alphabets; 
	
	public SpellChecker(String dictionaryPath) throws Exception{
		this.dictionary = constructDictionary(dictionaryPath);
		
		this.targetCharacter = '#';
		
		char start = '\u0985', end = '\u09DF';
		int length = end-start+1;
		this.alphabets = new char[length];
		int i=0;
		for(char c=start; c<=end; c++){
			alphabets[i]=c;
			i++;
		}
	}
	
	private HashMap<String, Integer> constructDictionary(String dictionaryPath) throws Exception{
		HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
		
		BufferedReader mainBR = new BufferedReader(new InputStreamReader(
				new FileInputStream(dictionaryPath), "UTF-8"));
		
		int i=0;
		String string = mainBR.readLine();
		while(string!=null){
			
			dictionary.put(string, i);
			
			i++;
			string = mainBR.readLine();
		}
		
		mainBR.close();
		
		return dictionary;
	}
	
	public String checkSpell(String string){
		String[] words = string.split(" ");
		
		string = "";
		for(int i=0; i<words.length; i++){
			string += checkSpellOfAWord(words[i])+" ";
		}
		
		return string;
	}
	
	public String checkSpellOfAWord(String word){
		if(dictionary.containsKey(word)){
			return word;
		}

		ArrayList<String> edits = getAllEdits(word);
		
		HashMap<Integer, String> candidates = new HashMap<>();
		for(String edit: edits){
			Integer value = dictionary.get(edit);
			if(value != null){
				candidates.put(value, edit);
			}
		}
		
		if (!candidates.isEmpty()) {
			word = candidates.get(Collections.min(candidates.keySet()));
		}
		
		return word;
	}
	
	public ArrayList<String> getSuggestionSpellsOfAWord(String word){
		ArrayList<String> suggestions = new ArrayList<String>();
		
		if(dictionary.containsKey(word)){
			return suggestions;
		}

		ArrayList<String> edits = getAllEdits(word);
		
		for(String edit: edits){
			Integer value = dictionary.get(edit);
			if(value != null){
				suggestions.add(edit);
			}
		}
		
		return suggestions;
	}
	
	private ArrayList<String> getAllEdits(String word){
		ArrayList<String> edits = new ArrayList<String>();
		
		String editedWord;
		for (int i=0; i<word.length(); i++) {
			if(word.charAt(i)==targetCharacter){
				for (int j=0; j<alphabets.length; j++) {
					editedWord = word.substring(0,i)+alphabets[j]+word.substring(i+1);
					edits.add(editedWord);
				}
			}
		}
				
		return edits;
	}
	
	public static void main(String[] args) throws Exception {
		SpellChecker spellChecker = new SpellChecker("BengaliWordList_48.txt");
		ArrayList<String> correctStrings;
		
		correctStrings = spellChecker.getSuggestionSpellsOfAWord("কল#");
		for(int i=0; i<correctStrings.size(); i++){
			System.out.print(correctStrings.get(i)+" ");
		}System.out.println();
		
		correctStrings = spellChecker.getSuggestionSpellsOfAWord("আ#মান");
		for(int i=0; i<correctStrings.size(); i++){
			System.out.print(correctStrings.get(i)+" ");
		}System.out.println();
		
		System.out.println(spellChecker.checkSpellOfAWord("#দী"));
	}
}
