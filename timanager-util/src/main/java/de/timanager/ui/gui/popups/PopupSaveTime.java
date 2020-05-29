package de.timanager.ui.gui.popups;

import de.timanager.files.CustomFileHandler;
import de.timanager.time.TimeKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public final class PopupSaveTime extends PopupSaver {
    private final GridBagConstraints c = new GridBagConstraints();
    private final SpinnerDateModel model = new SpinnerDateModel();
    private final JSpinner spinner = new JSpinner(model);

    @Getter
    private boolean okBtnClicked = false;

    public PopupSaveTime(CustomFileHandler customFileHandler, TimeKey timeKey, String title, Component orientation) {
        super(title);

        setActionListeners(customFileHandler, timeKey);
        createContent();

        // Sets the listeners to the two buttons.
        completeConstruction(orientation);
    }

    @Override
    void setActionListeners(CustomFileHandler customFileHandler, TimeKey timeKey) {
        setOkListener(e -> {
            okBtnClicked = true;
            Object value = spinner.getValue();
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.ofInstant(((Date) value).toInstant(), ZoneId.systemDefault()));
            LocalDateTime newDateTime = LocalDateTime.of(
                    dateTime.getYear(),
                    dateTime.getMonth(),
                    dateTime.getDayOfMonth(),
                    dateTime.getHour(),
                    dateTime.getMinute(),
                    LocalTime.now().getSecond()
            );
            customFileHandler.addTime(newDateTime, timeKey);
            dispose();

        });
    }

    @Override
    void createContent() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, LocalTime.now().getHour()); // 24 == 12 PM == 00:00:00
        calendar.set(Calendar.MINUTE, LocalTime.now().getMinute());

        model.setValue(calendar.getTime());

        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm");
        DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();

        formatter.setAllowsInvalid(false); // this makes what you want
        formatter.setOverwriteMode(true);
        spinner.setEditor(editor);

        c.insets = new Insets(0, 0, 10, 0);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = getGridYCounter();
        addComponentWithGridBagConstraint(spinner, c);

        c.gridy = getGridYCounter();
        c.weightx = 1;
        c.gridwidth = 1;
        addComponentWithGridBagConstraint(ok, c);

        c.gridx = 1;
        addComponentWithGridBagConstraint(cancel, c);
    }
}
