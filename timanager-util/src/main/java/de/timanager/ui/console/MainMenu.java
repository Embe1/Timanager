package de.timanager.ui.console;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String input = "";
        log.info("Timanager started!");
        while (!input.equals("exit")) {
            input = readUserInput();
            executeCommand(input);
        }
    }

    public static void executeCommand(String input) {
        String errorMsg = "Dieses Kommando ist dem System nicht bekannt!";
        boolean wasExecuted = false;

        String inputCommand = getCommand(input);
        List<String> inputParams = getCommandParams(input);

        for (Command command : Command.values()) {
            if (command.getCommandName().equals(inputCommand)) {
                command.execute(inputParams);
                wasExecuted = true;
            }
        }

        if (!wasExecuted && !input.equals("exit"))
            log.info(errorMsg);
    }

    private static List<String> getCommandParams(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> params = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                params.add(stringBuilder.toString());
                stringBuilder.replace(0, stringBuilder.length(), "");
            } else {
                stringBuilder.append(input.charAt(i));
            }
        }

        params.add(stringBuilder.toString());
        stringBuilder.replace(0, stringBuilder.length()-1, "");

        if (params.size() > 0)
            params.remove(0);

        return params;
    }

    private static String getCommand(String input) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Integer i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                return stringBuilder.toString();
            } else {
                stringBuilder.append(input.charAt(i));
            }
        }

        return stringBuilder.toString();
    }

    public static String readUserInput() {
        return scanner.nextLine();
    }
}
