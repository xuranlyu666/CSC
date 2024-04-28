package prog10;

import java.io.File;
import java.util.*;

import prog02.UserInterface;
import prog02.ConsoleUI;
import prog02.GUI;

public class Jumble {


    /**
     * Sort the letters in a word.
     * @param word Input word to be sorted, like "computer".
     * @return Sorted version of word, like "cemoptru".
     */
    public static String sort (String word) {
	char[] sorted = new char[word.length()];
	for (int n = 0; n < word.length(); n++) {
	    char c = word.charAt(n);
	    int i = n;

	    while (i > 0 && c < sorted[i-1]) {
		sorted[i] = sorted[i-1];
		i--;
	    }
	    sorted[i] = c;
	}
	return new String(sorted, 0, word.length());
    } // end of sort





    public static void main (String[] args) {
		  UserInterface ui = new GUI("Jumble");
		//UserInterface ui = new ConsoleUI();

		//Map<String,String> map = new TreeMap<String,String>();
		//Map<String,String> map = new BTree(128);
	    //Map<String,String> map = new PDMap();
		//Map<String,String> map = new LinkedMap<String,String>();
		//Map<String,String> map = new SkipMap<String,String>();
		  Map<String,List<String>> map = new HashTable<>();
		  // Map<String, String> map = new HashTable<>();


	Scanner in = null;
	do {
	    String fileName = null;
	    try {
		fileName = ui.getInfo("Enter word file.");
		if (fileName == null)
		    return;
		in = new Scanner(new File(fileName));
	    } catch (Exception e) {
		ui.sendMessage(fileName + " not found.");
		System.out.println(e);
		System.out.println("Try again.");
	    }
	} while (in == null);
	    
	int n = 0;
	while (in.hasNextLine()) {
	    String word = in.nextLine();
	    if (n++ % 1000 == 0)
		System.out.println(word + " sorted is " + sort(word));



		String sorted = sort(word);

		if(!map.containsKey(sorted)){
			List<String> wordList = new ArrayList<>();
			wordList.add(word);
			map.put(sorted, wordList);
		}
		else{
			List<String> wordList2 = map.get(sorted);
			wordList2.add(word);
			map.put(sorted, wordList2);
		}

		/*

	    // EXERCISE: Insert an entry for word into map.
	    // What is the key?  What is the value?
	    ///
		String key = sort(word);
		if (map.containsKey(key)) {
			String existing = map.get(key);
			map.put(key, existing + " " + word); // Append the new word to the existing ones
		} else {
			map.put(key, word);
		}
	    ///

		 */

	} //end of while


		String jumble = ui.getInfo("Enter jumble.");
	//boolean cont = true;
	while (jumble != null) {
	    //String jumble = ui.getInfo("Enter jumble.");

	    if (jumble == null) {
			return;
		}

		/*
		// test of remove
		map.put(sort(word));

		map.remove(sort(word));

		if(map.get(sort(word)) != null){
			System.out.println("Error in remove");
			}
		map.put(sort(word), word);
		*/



	    //String word = null;
	    // EXERCISE:  Look up the jumble in the map.
	    // What key do you use?
	    ///

//		String key = sort(jumble);
//		String word = map.get(key);

	    ///

//	    if (word == null){
//		ui.sendMessage("No match for " + jumble);
//	    }
//		else{
//		ui.sendMessage(jumble + " unjumbled is " + word);
//		}


			List<String> listOfWords = map.get(sort(jumble));

			if (listOfWords == null){
				ui.sendMessage("No match for " + jumble);

			}
			else{
				ui.sendMessage(jumble + " unjumbled is " + listOfWords);

			}
			jumble = ui.getInfo("Enter jumble.");


			}//end of while


		String letters = ui.getInfo("Enter letters from clues: ");
		while(letters != null){

				//String letters = ui.getInfo("Enter letters from clues: ");
				if (letters == null){
					return;
				}
				String sortedLetters = sort(letters);

				int length = 0;
				do{
					String numberOfLetters = ui.getInfo("How many letters in the first word? ");
					try{
						length = Integer.parseInt(numberOfLetters);
					}
					catch (Exception e){
						ui.sendMessage(numberOfLetters + "is not valid");
					}
				}
				while (length <= 0);



				for (String key1 : map.keySet()){
					if (key1.length() == length){
						String key2 = "";
						int j = 0;
						for (int i = 0; i < sortedLetters.length(); i++){
							//int j = 0;
							if(j < key1.length() && sortedLetters.charAt(i) == key1.charAt(j)){
								j++;
							}
							else{
								key2 = key2 + sortedLetters.charAt(i);
							}
						}

						if (j == key1.length()){
							if (map.containsKey(key2)){
								ui.sendMessage(map.get(key1) + " " + map.get(key2));

							}
						}
					}
				}
			letters = ui.getInfo("Enter letters from clues: ");
			}



    }//end of main



} // end of jumble

        
    


