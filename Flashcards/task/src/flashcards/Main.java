package flashcards;

import java.io.*;
import java.util.*;

public class Main {
    static boolean exit = false;
    static LinkedHashMap<String, String> flashcards = new LinkedHashMap<>();
    static LinkedHashMap<String, Integer> mistakes = new LinkedHashMap<>();
    static Scanner scanner = new Scanner(System.in);
    static LinkedList<String> log = new LinkedList<>();
    static boolean exitFromLog = false;
    static String importFile = "";
    static String exportFile = "";

    public static void main(String[] args) {
        argsParse(args);
        if (!importFile.isBlank()) {
            importCard(importFile);
        }
        while (!exit) {
            if (!exitFromLog) {
                String output = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
                System.out.println(output);
                log.add(output);
            }
            String choice = scanner.nextLine();
            log.add(choice);
            switch (choice) {
                case "add":
                    addCard();
                    break;
                case "remove":
                    removeCard();
                    break;
                case "import":
                    importCard();
                    break;
                case "export":
                    exportCard();
                    break;
                case "ask":
                    askCard();
                    break;
                case "exit":
                    exit = true;
                    if (!exportFile.isBlank()) {
                        exportCard(exportFile);
                    }
                    System.out.println("Bye bye!");
                    break;
                case "log":
                    saveLog();
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                default:
                    System.out.println("Unknown action");
                    System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            }
        }
    }

    public static void addCard() {
        System.out.println("The card:");
        log.add("The card:");
        String term = scanner.nextLine();
        log.add(term);
        if (flashcards.containsKey(term)) {
            String output = "The card \"" + term + "\" already exists.";
            System.out.println(output);
            log.add(output);
            return;
        }
        System.out.println("The definition of the card:");
        log.add("The definition of the card:");
        String definition = scanner.nextLine();
        log.add(definition);
        if (flashcards.containsValue(definition)) {
            String output = "The definition \"" + definition + "\" already exists.";
            System.out.println(output);
            log.add(output);
            return;
        }
        flashcards.put(term, definition);
        String output = "The pair (\"" + term + "\":\"" + definition + "\") has been added.";
        System.out.println(output);
        log.add(output);
    }

    public static void removeCard() {
        System.out.println("Which card?");
        log.add("Which card?");
        String term = scanner.nextLine();
        log.add(term);
        if (flashcards.containsKey(term)) {
            flashcards.remove(term);
            String output = "The card has been removed.";
            System.out.println(output);
            log.add(output);
        } else {
            String output = "Can't remove \"" + term + "\": there is no such card";
            System.out.println(output);
            log.add(output);
        }
    }

    public static void importCard() {
        System.out.println("File name:");
        log.add("File name:");
        String filePath = scanner.nextLine();
        log.add(filePath);
        File file = new File(filePath);

        try (Scanner scanner = new Scanner(file)) {
            int updateCounter = 0;
            while (scanner.hasNext()) {
                String[] card;
                card = scanner.nextLine().split(":");
                var term = card[0];
                var definition = card[1];
                var mistakeCount = card[2];
                flashcards.put(term, definition);
                mistakes.put(term, Integer.valueOf(mistakeCount));
                updateCounter++;
            }
            String output = updateCounter + " cards have been loaded.";
            System.out.println(output);
            log.add(output);
        } catch (FileNotFoundException e) {
            String output = "File not found.";
            System.out.println(output);
            log.add(output);
        }
    }

    public static void importCard(String importFile) {
        File file = new File(importFile);

        try (Scanner scanner = new Scanner(file)) {
            int updateCounter = 0;
            while (scanner.hasNext()) {
                String[] card;
                card = scanner.nextLine().split(":");
                var term = card[0];
                var definition = card[1];
                var mistakeCount = card[2];
                flashcards.put(term, definition);
                mistakes.put(term, Integer.valueOf(mistakeCount));
                updateCounter++;
            }
            String output = updateCounter + " cards have been loaded.";
            System.out.println(output);
            log.add(output);
        } catch (FileNotFoundException e) {
            String output = "File not found.";
            System.out.println(output);
            log.add(output);
        }
    }

    public static void exportCard() {
        System.out.println("File name:");
        log.add("File name:");
        String filePath = scanner.nextLine();
        log.add(filePath);
        File file = new File(filePath);

        int updateCounter = 0;
        try (FileWriter fileWriter = new FileWriter(file)) {
            int mistakeCount = 0;
            for (var entry : flashcards.entrySet()) {
                if (mistakes.get(entry.getKey()) != null) {
                    mistakeCount = mistakes.get(entry.getKey());
                }
                String line = entry.getKey() + ":" + entry.getValue() + ":" + mistakeCount;
                fileWriter.write(line+"\n");
                updateCounter++;
            }
            String output = updateCounter + " cards have been saved.";
            System.out.println(output);
            log.add(output);
        } catch (IOException e) {
            String output = "Not possible to export card: " + e.getMessage();
            System.out.println(output);
            log.add(output);
        }
    }

    public static void exportCard(String exportFile) {
        File file = new File(exportFile);

        int updateCounter = 0;
        try (FileWriter fileWriter = new FileWriter(file)) {
            int mistakeCount = 0;
            for (var entry : flashcards.entrySet()) {
                if (mistakes.get(entry.getKey()) != null) {
                    mistakeCount = mistakes.get(entry.getKey());
                }
                String line = entry.getKey() + ":" + entry.getValue() + ":" + mistakeCount;
                fileWriter.write(line+"\n");
                updateCounter++;
            }
            String output = updateCounter + " cards have been saved.";
            System.out.println(output);
            log.add(output);
        } catch (IOException e) {
            String output = "Not possible to export card: " + e.getMessage();
            System.out.println(output);
            log.add(output);
        }
    }

    public static void askCard() {
        System.out.println("How many times to ask?");
        log.add("How many times to ask?");
        int numOfCards = scanner.nextInt();
        log.add(String.valueOf(numOfCards));
        Object[] terms = flashcards.keySet().toArray();

        scanner.nextLine();
        for (int i = 0; i < numOfCards; i++) {
            String term = String.valueOf(terms[i]);
            String definition = flashcards.get(term);
            String output = "Print the definition of \"" + term + "\":";
            System.out.println(output);
            log.add(output);
            String answer = scanner.nextLine();
            log.add(answer);
            if (answer.equals(definition)) {
                String output2 = "Correct!";
                System.out.println(output2);
                log.add(output2);
            } else if (flashcards.containsValue(answer)) {
                String answeredTerm = null;
                for (var entry : flashcards.entrySet()) {
                    if (entry.getValue().equals(answer)) {
                        answeredTerm = entry.getKey();
                    }
                }
                String output2 = "Wrong. The right answer is \"" + definition + "\", but your definition is correct for \"" + answeredTerm + "\".";
                System.out.println(output2);
                log.add(output2);
                if (mistakes.get(term) != null) {
                    mistakes.put(term, mistakes.get(term) + 1);
                } else {
                    mistakes.put(term, 1);
                }
            } else {
                String output2 = "Wrong. The right answer is \"" + definition + "\":";
                System.out.println(output2);
                log.add(output2);
                if (mistakes.get(term) != null) {
                    mistakes.put(term, mistakes.get(term) + 1);
                } else {
                    mistakes.put(term, 1);
                }
            }
        }
    }

    public static void saveLog() {
        System.out.println("File name:");
        log.add("File name:");
        String filePath = scanner.nextLine();
        log.add(filePath);
        File file = new File(filePath);

        try (PrintWriter fileWriter = new PrintWriter(file)) {
//            System.out.println("=========================");
            for (var l : log) {
                fileWriter.println(l);
//                System.out.println(l);
            }
            String output = "The log has been saved.\n";
            fileWriter.println(output);
            System.out.println(output);
            exitFromLog = true;
        } catch (IOException e) {
            String output = "Not possible to save the file.";
            System.out.println(output);
            log.add(output);
        }
    }

    public static void hardestCard() {
        HashSet<String> terms = new HashSet<>();
        int highestValue = 0;

        for (var mistakeCount : mistakes.values()) {
            if (mistakeCount > highestValue) {
                highestValue = mistakeCount;
            }
        }

        if (highestValue > 0) {
            for (var entry : mistakes.entrySet()) {
                if (entry.getValue().equals(highestValue)) {
                    terms.add(entry.getKey());
                }
            }
            if (terms.size() > 1) {
                String output = "The hardest cards are ";
                for (String term : terms) {
                    output += "\"" + term + "\", ";
                }
                String fixedOutput = output.substring(0, output.length() - 2);
                fixedOutput = fixedOutput + ". You have " + highestValue + " errors answering them";
                System.out.println(fixedOutput);
                log.add(fixedOutput);
            }
            if (terms.size() == 1) {
                String term = terms.toString().replace("[", "");
                term = term.replace("]", "");

                String output = "The hardest card is \"" + term + "\". You have " + highestValue + " errors answering it";
                System.out.println(output);
                log.add(output);
            }
        } else {
            String output = "There are no cards with errors.";
            System.out.println(output);
            log.add(output);
        }
    }

    public static void resetStats() {
        mistakes.clear();
        String output = "Card statistics have been reset.";
        System.out.println(output);
        log.add(output);
    }

    public static void argsParse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) {
                importFile = args[i + 1];
            } else if (args[i].equals("-export")) {
                exportFile = args[i + 1];
            }
        }
    }
}
