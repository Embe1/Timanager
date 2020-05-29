package de.timanager.ui.gui.popups;

import de.timanager.files.CustomFileHandler;
import de.timanager.time.TimeKey;
import de.timanager.ui.gui.SwingMainMenu;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public final class PopupSaveTimeForDate extends PopupSaver {
    private final JSpinner spinnerYear = createSpinner(Calendar.YEAR, LocalDateTime.now().getYear(), "YYYY");
    private final JSpinner spinnerMonth = createSpinner(Calendar.MONTH, LocalDateTime.now().getMonthValue()-1, "MMMM");
    private final JSpinner spinnerDay = createSpinner(Calendar.DAY_OF_MONTH, LocalDateTime.now().getDayOfMonth(), "d");
    private final JComboBox<TimeKey> comboBoxTime = new JComboBox<>(TimeKey.values());

    private final SpinnerDateModel model = new SpinnerDateModel();
    private final JSpinner spinnerHourMinute = new JSpinner(model);

    private final GridBagConstraints c = new GridBagConstraints();

    public PopupSaveTimeForDate(CustomFileHandler customFileHandler, String title, SwingMainMenu orientation) {
        super(title);

        setActionListeners(customFileHandler, null);
        createContent();

        // Sets the listeners to the two buttons.
        completeConstruction(orientation);
    }

    @Override
    void setActionListeners(CustomFileHandler customFileHandler, TimeKey timeKey) {

        setOkListener(e -> {
            Object year = spinnerYear.getValue();
            Object month = spinnerMonth.getValue();
            Object day = spinnerDay.getValue();
            Object hourMinute = spinnerHourMinute.getValue();
            TimeKey timeKeyBox = comboBoxTime.getItemAt(comboBoxTime.getSelectedIndex());

            boolean isSaved = customFileHandler.addTime(
                    LocalDateTime.of(
                            LocalDateTime.ofInstant(((Date) year).toInstant(), ZoneId.systemDefault()).getYear(),
                            LocalDateTime.ofInstant(((Date) month).toInstant(), ZoneId.systemDefault()).plusMonths(1).getMonth(),
                            LocalDateTime.ofInstant(((Date) day).toInstant(), ZoneId.systemDefault()).getDayOfMonth(),
                            LocalDateTime.ofInstant(((Date) hourMinute).toInstant(), ZoneId.systemDefault()).getHour(),
                            LocalDateTime.ofInstant(((Date) hourMinute).toInstant(), ZoneId.systemDefault()).getMinute()),
                    timeKeyBox);

            if (!isSaved)
                JOptionPane.showMessageDialog(
                        this,
                        String.format(
                                "Es wurde für das Datum %s und den Zeitstempel %s bereits eine Aufzeichnung gespeichert!",
                                LocalDate.of(
                                        LocalDateTime.ofInstant(((Date) year).toInstant(), ZoneId.systemDefault()).getYear(),
                                        LocalDateTime.ofInstant(((Date) month).toInstant(), ZoneId.systemDefault()).getMonth(),
                                        LocalDateTime.ofInstant(((Date) day).toInstant(), ZoneId.systemDefault()).getDayOfMonth()),
                                timeKeyBox
                        )
                );
            else
                dispose();
        });
    }

    @Override
    void createContent() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, LocalTime.now().getHour()); // 24 == 12 PM == 00:00:00
        calendar.set(Calendar.MINUTE, LocalTime.now().getMinute());

        model.setValue(calendar.getTime());

        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerHourMinute, "HH:mm");
        DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();

        formatter.setAllowsInvalid(false); // this makes what you want
        formatter.setOverwriteMode(true);
        spinnerHourMinute.setEditor(editor);

        c.insets = new Insets(0, 5, 10, 0);
        c.gridx = 0;
        c.gridy = getGridYCounter();
        addComponentWithGridBagConstraint(new JLabel("Welche Zeit:"), c);

        c.insets = new Insets(0, 5, 10, 0);
        c.gridx = 1;
        addComponentWithGridBagConstraint(comboBoxTime, c);

        c.insets = new Insets(0, 5, 10, 0);
        c.gridx = 0;
        c.gridy = getGridYCounter();
        addComponentWithGridBagConstraint(new JLabel("Datum:"), c);

        c.insets = new Insets(0, 5, 10, 0);
        c.gridx = 1;
        addComponentWithGridBagConstraint(spinnerYear, c);

        c.insets = new Insets(0, 5, 10, 0);
        c.gridx = 2;
        addComponentWithGridBagConstraint(spinnerMonth, c);

        c.insets = new Insets(0, 5, 10, 0);
        c.gridx = 3;
        addComponentWithGridBagConstraint(spinnerDay, c);

        c.insets = new Insets(0, 5, 10, 0);
        c.gridx = 0;
        c.gridy = getGridYCounter();
        addComponentWithGridBagConstraint(new JLabel("Startzeit:"), c);

        c.insets = new Insets(0, 5, 10, 0);
        c.gridx = 1;
        addComponentWithGridBagConstraint(spinnerHourMinute, c);

        c.gridy = getGridYCounter();
        c.gridx = 2;
        addComponentWithGridBagConstraint(ok, c);

        c.gridx = 3;
        addComponentWithGridBagConstraint(cancel, c);
    }
}
