package prog05;

import prog02.GUI;
import prog02.UserInterface;
import prog04.ArrayStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

// NotWordle class
public class NotWordle {

    // class variable
    UserInterface ui;


    // constructor that takes a UserInterface
    NotWordle(UserInterface ui) {
        // and stores it in a class variable
        this.ui = ui;
    } // end of constructor

    List<Node> wordEntries = new ArrayList<Node>();
    protected class Node{

        String word;
        Node next;
        Node previous;

        // constructor
        Node (String word){
            this.word = word;
            next = null;
        }

    } // end of Node


    public void play(String startWord, String targetWord){

        while(true){
            // temp variable
            String nextWord;

            // tell user the start word and the target word
            ui.sendMessage("current word: " + startWord + "\ntarget word: " + targetWord);

            // ask for the next word
            nextWord = ui.getInfo("what is your next word? ");
            if (nextWord == null){
                return;
            }
            if (find(nextWord) == null){
                continue;
            }
            if (!differByOneLetter(nextWord, startWord)){
                ui.sendMessage("Sorry, but " + nextWord + "differs by more than one letter.");
                continue;
            }

            // set the start word variable to the next word
            startWord = nextWord;

            // if the next is the target, inform that you win. Otherwise keep looping
            if (startWord.equals(targetWord)){
                ui.sendMessage("you win! ");
                return;
            }
            else{
                continue;
            }
        } // end of while


    } // end of play

    public void solve(String startWord, String targetWord){

        // create a queue of node
        Queue<Node> wordQueue = new LinkedQueue<>();

        ArrayStack<String> wordTrail = new ArrayStack<>();
        String solution = "null";

        wordQueue.offer(find(startWord));
        Node startNode = wordQueue.peek();

        while(!wordQueue.isEmpty()){

            Node theNode = wordQueue.poll();

            for (int i = 0; i < wordEntries.size() - 1; i ++){
                if (!wordEntries.get(i).equals(startNode) &&
                        wordEntries.get(i).next == null &&
                        !differByOneLetter(wordEntries.get(i).word, theNode.word)){

                    Node nextNode = wordEntries.get(i);
                    nextNode.next = theNode;
                    wordQueue.offer(nextNode);

                    if(nextNode.word.equals(targetWord)){
                        ui.sendMessage("Got to " + targetWord + "from " + nextNode.word);

                        while(nextNode != null){
                            wordTrail.push(nextNode.word);
                            nextNode = nextNode.next;
                        }

                        while (!wordTrail.empty()){
                            solution = solution + wordTrail.pop() + "\n";
                        }
                        ui.sendMessage(solution);
                    } // end of if



                } // end of if
            } // end of for

        } // end of while





    } // end of solve

    public Node find(String wordToFind){

        // binary search for sorted array
        int low = 0;
        int high = wordEntries.size();

        while( low < high ){
            int middle = (low + high) / 2;
            String middleWord = wordEntries.get(middle).word;

            if (middleWord.compareTo(wordToFind) < 0 ){
                low = middle + 1;
            }
            else if (middleWord.compareTo(wordToFind) > 0){
                high = middle;
            }
            else{
                return wordEntries.get(middle);
            }
        } // end of while

        ui.sendMessage(wordToFind + " is not a word");
        return null;

    }





    static boolean differByOneLetter(String string1, String string2){

        int difference = 0;

        // same length before check each letter
        if(string1.length() == string2.length()){
            // check each letter
            for (int i = 0; i < string1.length(); i++){
                if (string1.charAt(i) != string2.charAt(i)){
                    difference ++;
                } // differ by exactly one letter

                }
            return true;
            } // done if differ by one
        else{
            difference = 2;
            return false;
        }

    } // end of differByOneLetter

    public boolean loadWords(String wordFile){

        try{
            BufferedReader reader = new BufferedReader(new FileReader(wordFile));
            String wordToNode = "";

            while(wordToNode != null){
                wordToNode = reader.readLine();
                Node wordNode = new Node(wordToNode);
                wordEntries.add(wordNode);

            }

        } // end of try

        catch (IOException e){
            ui.sendMessage("Uh..." + e); // need to pass a string para...
            return false;
        } // end of catch

        return true;
    } // end of loadWords



    public static void main(String[] args) {

        // variables to use
        String startingWord;
        String targetWord;
        String wordFile;

        // instantiate a new NotWordle
        NotWordle game = new NotWordle(new GUI("NotWordle"));

        // file object
        do{
            wordFile = game.ui.getInfo("Enter a word file: ");
        }while (!game.loadWords(wordFile));

        // ask user for a starting word and a target word
        startingWord = game.ui.getInfo("Enter a starting word");

        // if the user didn't enter anything
        if (startingWord == null){
            return;
        }
        // if the word entered is not in the directory
        while (game.find(startingWord) == null){
            startingWord = game.ui.getInfo("Try again: "); // need to study getInfo

            if (startingWord == null){
                return;
            }
        } // end of while

        // Now ask for the target word
        targetWord = game.ui.getInfo("Enter the target: ");
        if (targetWord == null){
            return;
        }
        // case where target is not in the directory
        while(game.find(targetWord) == null ){
            targetWord = game.ui.getInfo("Try again: ");
            if (targetWord == null){
                return;
            }
        } // end of while

        // play the game
        String[] commands = {"Human plays", "computer plays"};
        int choice = game.ui.getCommand(commands);
        switch(choice){
            case 0:
                game.play(startingWord, targetWord);
                break;

            case 1:
                game.solve(startingWord, targetWord);

            case -1:
                return;
        } // end of switch


    } // end of main


} // end of NotWordle