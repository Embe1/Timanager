package de.timanager.ui.console;

import de.timanager.recording.Start;
import de.timanager.recording.Stop;
import de.timanager.statistics.ConsoleTableCalculation;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public enum Command {
    START_RECORDING ("start", "main", Start.class),
    STOP_RECORDING ("stop", "main", Stop.class),
    MONTH_TABLE_OVERVIEW ("month", "main", ConsoleTableCalculation.class),
//    ADD_LUNCHTIME ("lunchtime", "addLunchTimeToToday", CustomFileHandler.class),
    ;

    private final String commandName;
    private final String methodName;
    @Getter
    private final Class<?> clazz;

    Command(String commandName, String methodName, Class<?> className) {
        this.commandName = commandName;
        this.clazz = className;
        this.methodName = methodName;
    }

    public final String getCommandName() {
        return commandName;
    }


    public final void execute(List<String> inputParams) {
        try {
            this.getClazz().getMethod(this.methodName, String[].class).invoke(null, (Object) inputParams.toArray(new String[]{}));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
