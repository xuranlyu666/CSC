package prog05;

import prog02.GUI;
import prog02.UserInterface;
import java.util.*;
import java.io.*;

public class NotMyWordle {
    /*
    UserInterface ui;
    List<Node> wordEntries = new ArrayList<>();

    public NotWordle(UserInterface ui) {
        this.ui = ui;
    }

    private class Node {
        String word;
        Node next;
        Node parent;

        Node(String word) {
            this.word = word;
            this.next = null;
            this.parent = null;
        }
    }

    public static void main(String[] args) {

        NotWordle game = new NotWordle(new GUI("NotWordle"));

        String filename = game.ui.getInfo("Enter word file:");

        if (filename != null && game.loadWords(filename)) {

            String startWord = game.ui.getInfo("Starting Word: ");

            String targetWord = game.ui.getInfo("Target Word: ");

            if (startWord == null || targetWord == null) return;


            String[] commands = { "Human plays.", "Computer plays." };

            int command = game.ui.getCommand(commands);

            if (command == 0) {

                game.play(startWord, targetWord);

            } else if (command == 1) {

                game.solve(startWord, targetWord);

            }
        }
    }

    boolean loadWords(String filename) {

        try {

            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();
                wordEntries.add(new Node(word));
            }
            scanner.close();

            return true;

        } catch (FileNotFoundException e) {
            ui.sendMessage("File not found: " + filename);
            return false;
        }
    }

    Node find(String word) {

        for (Node node : wordEntries) {
            if (node.word.equals(word)) {
                return node;
            }
        }
        return null;
    }

    void play(String start, String target) {
        if (find(start) == null || find(target) == null) {
            ui.sendMessage("Start or target word not in dictionary.");
            return;
        }

        String current = start;
        while (true) {
            ui.sendMessage("Current word: " + current + "\nTarget word: " + target);
            String nextWord = ui.getInfo("What is your next word?");
            if (nextWord == null) {
                return; // Exit if the user cancels
            } else if (nextWord.equals(target)) {
                if (differByOneLetter(current, nextWord)) {
                    ui.sendMessage("You win!");
                    return;
                } else {
                    ui.sendMessage("Sorry, " + nextWord + " differs by more than one letter from " + current);
                }
            } else if (!differByOneLetter(current, nextWord)) {
                ui.sendMessage("Sorry, " + nextWord + " differs by more than one letter from " + current);
            } else if (find(nextWord) == null) {
                ui.sendMessage(nextWord + " is not in the dictionary.");
            } else {
                current = nextWord;
            }
        }
    }


    void solve(String start, String target) {

        for (Node node : wordEntries) {
            node.parent = null;
        }

        Node startNode = find(start);
        Node targetNode = find(target);
        if (startNode == null || targetNode == null) {
            ui.sendMessage("Start or target word not in dictionary.");
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        startNode.parent = startNode;
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.word.equals(target)) {
                ui.sendMessage("Found path to " + target);
                String path = reconstructPath(startNode, current);
                ui.sendMessage(path);
                return;
            }

            for (Node node : wordEntries) {
                if (node.parent == null && differByOneLetter(current.word, node.word)) {
                    node.parent = current;
                    queue.add(node);
                }
            }
        }

        ui.sendMessage("No path from " + start + " to " + target + ".");
    }

    String reconstructPath(Node startNode, Node targetNode) {
        LinkedList<String> path = new LinkedList<>();

        Stack<String> stack = new Stack<>();

        Node current = targetNode;
        while (current != startNode) {
            stack.push(current.word);
            current = current.parent;
        }
        stack.push(startNode.word);


        while (!stack.isEmpty()) {
            path.add(stack.pop());
        }

        return String.join("\n", path);
    }


    static boolean differByOneLetter(String firstWord, String secondWord) {

        if (firstWord.length() != secondWord.length())
            return false;

        int diffCount = 0;

        for (int i = 0; i < firstWord.length(); i++) {
            if (firstWord.charAt(i) != secondWord.charAt(i)) diffCount++;
        }

        return diffCount == 1;
    }

     */
}
