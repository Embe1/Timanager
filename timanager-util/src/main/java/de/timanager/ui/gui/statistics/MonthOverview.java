package de.timanager.ui.gui.statistics;

import de.timanager.files.CustomFileHandler;
import de.timanager.time.TimeKey;
import de.timanager.time.TimeMap;
import de.timanager.statistics.WorkingTimeCalculator;
import de.timanager.ui.gui.GuiFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
public final class MonthOverview extends BaseTableView {
    private final Month month = LocalDate.now().getMonth();
    private final CustomFileHandler customFileHandler = new CustomFileHandler(month);
    private final TimeMap<String, LocalDateTime> timeMap = customFileHandler.getTimeMap();

    private final Object[] columnNames = {
            "Datum",
            "Startzeit",
            "Mittagspause (Beginn)",
            "Mittagspause (Ende)",
            "Endzeit",
            "Dauer (Std.)",
            "Löschen"};
    private final JButton saveBtn = new JButton("Speichern");
    private final GridBagConstraints c = GuiFrame.setGridBagConstraints();

    private DefaultTableModel tableModel = new DefaultTableModel(null, columnNames);
    private boolean changedValue = false;
    private boolean removedValue = false;

    public MonthOverview(Component componentOrientation) {
        super(componentOrientation, "Monatsübersicht - " + DateTimeFormatter.ofPattern("MMMM", Locale.GERMANY).format(LocalDateTime.now()), JFrame.DISPOSE_ON_CLOSE);

        createTable();
        addListeners();

        pack();
        setLocationRelativeTo(componentOrientation);
        setVisible(true);
    }

    private void addListeners() {
        saveBtn.addActionListener(e -> saveTableData());
    }

    /**
     * TODO Refactoren und in kleiner Methoden aufteilen.
     * <p>
     * Speichert die Werte aus der Tabelle, wenn ein Wert geändert wurde.
     */
    private void saveTableData() {

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            LocalDate dateCell = LocalDate.now();

            for (int column = 0; column < tableModel.getColumnCount(); column++) {
                // Gets the value out of the current cell in table.
                Object valueTableCell = tableModel.getValueAt(row, column);
                String workStartString;
                String lunchStartString;
                String lunchEndString;
                String workEndString;

                if (column == 0) {
                    dateCell = LocalDate.parse((String) valueTableCell);

                } else if (column != 5) {
                    LocalTime timeCell = LocalTime.now();

                    if (valueTableCell instanceof String)
                        timeCell = !isNullTime((String) valueTableCell) ? LocalTime.parse((String) valueTableCell) : LocalTime.of(0, 0, 0);

                    LocalDateTime dateTimeCell = LocalDateTime.of(dateCell, timeCell);

                    workStartString = TimeKey.WORKTIME_START.generateKey(dateTimeCell);
                    lunchStartString = TimeKey.LUNCH_START.generateKey(dateTimeCell);
                    lunchEndString = TimeKey.LUNCH_END.generateKey(dateTimeCell);
                    workEndString = TimeKey.WORKTIME_END.generateKey(dateTimeCell);

                    if (column == 1 && checkCellChange(dateTimeCell, TimeKey.WORKTIME_START)) {
                        removeOrUpdate(dateTimeCell, workStartString);

                    } else if (column == 2 && checkCellChange(dateTimeCell, TimeKey.LUNCH_START)) {
                        removeOrUpdate(dateTimeCell, lunchStartString);

                    } else if (column == 3 && checkCellChange(dateTimeCell, TimeKey.LUNCH_END)) {
                        removeOrUpdate(dateTimeCell, lunchEndString);

                    } else if (column == 4 && checkCellChange(dateTimeCell, TimeKey.WORKTIME_END)) {
                        removeOrUpdate(dateTimeCell, workEndString);

                    } else if (column == 6 && valueTableCell.toString().equals(Boolean.toString(true))) {
                        removedValue = true;
                        timeMap.remove(workStartString);
                        timeMap.remove(lunchStartString);
                        timeMap.remove(lunchEndString);
                        timeMap.remove(workEndString);
                    }
                }
            }
        }

        if (changedValue && removedValue)
            showOptionDialog("Möchten Sie die vorgenommen Änderungen speichern und die markierten Einträge wirklich löschen?");
        else if (changedValue)
            showOptionDialog("Möchten Sie die vorgenommenen Änderungen speichern?");
        else if (removedValue)
            showOptionDialog("Möchten Sie die markierten Einträge wirklich löschen?");
        else
            JOptionPane.showMessageDialog(this, "Es wurden keine Werte geändert, und somit wurde auch nichts gespeichert!");
    }

    private void removeOrUpdate(LocalDateTime dateTime, String key) {
        if ((isNullTime(dateTime.toLocalTime().toString()) && timeMap.get(key) != null)) {
            removedValue = true;
            timeMap.remove(key, timeMap.get(key));
            log.info(String.format("%s key with value %s removed!", key, dateTime));
        } else {
            changedValue = true;
            timeMap.put(key, dateTime);
            log.info(String.format("%s value changed!", timeMap.get(key)));
        }
    }

    private void showOptionDialog(String message) {

        try {
            UIManager.put("OptionPane.yesButtonText", "Ja");
            UIManager.put("OptionPane.noButtonText", "Nein");

            int confirmDialog = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "Speicherung",
                    JOptionPane.YES_NO_OPTION);

            if (confirmDialog == JOptionPane.YES_OPTION) {
                customFileHandler.serializeForUpdate();
                resetTable();
                log.info("Updates successfully saved!");

            } else if (confirmDialog == JOptionPane.NO_OPTION) {
                dispose(); // Do dispose() or resetTable() ???
            }

        } catch (IOException e) {
            log.error("Werte konnten nicht übernommen werden!");
            e.printStackTrace();
        }
    }

    /**
     * Move to a super-class.
     */
    private void resetTable() {
        getContentPane().removeAll();

        tableModel = new DefaultTableModel(null, columnNames);
        createTable();

        getContentPane().revalidate();
        getContentPane().repaint();
        pack();
    }

    private boolean checkCellChange(LocalDateTime dateTime, TimeKey timeKey) {
        LocalDateTime mapTime = timeMap.get(timeKey.generateKey(dateTime));

        if (mapTime != null && (!dateTime.equals(mapTime) && !isNullTime(dateTime.toLocalTime().toString()))) {
            log.debug("Saved value exists, values are different and not 0!");
            return true;

        } else if (mapTime == null && !isNullTime(dateTime.toLocalTime().toString())) {
            log.debug("Value not in saved values and not 0!");
            return true;

        } else if (isNullTime(dateTime.toLocalTime().toString()) && mapTime != null) {
            log.debug("Value is 0 and an value is existing -> delete value from map!");
            return true;
        }
        log.debug("No cell change detected!");
        return false;
    }

    private boolean isNullTime(String time) {
        return time.equals("00:00:00") || time.equals("00:00") || time.equals("0:0") || time.equals("0");
    }

    @Override
    public void createTable() {

        // TODO muss an jeden Monat angepasst werden.
        for (int j = 1; j < 31; j++) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now().getYear(), month, j, 0, 0, 0);

            LocalDateTime startTime = timeMap.get(TimeKey.WORKTIME_START.generateKey(dateTime));
            LocalDateTime endTime = timeMap.get(TimeKey.WORKTIME_END.generateKey(dateTime));
            LocalDateTime lunchStart = timeMap.get(TimeKey.LUNCH_START.generateKey(dateTime));
            LocalDateTime lunchEnd = timeMap.get(TimeKey.LUNCH_END.generateKey(dateTime));

            WorkingTimeCalculator workingTimeCalculator = new WorkingTimeCalculator(timeMap, dateTime);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.GERMANY);

            tableModel.addRow(new Object[]{
                    startTime != null ? startTime.toLocalDate().toString() : dateTime.toLocalDate().toString(),
                    startTime != null ? dtf.format(startTime.toLocalTime()) : "00:00:00",
                    lunchStart != null ? dtf.format(lunchStart.toLocalTime()) : "00:00:00",
                    lunchEnd != null ? dtf.format(lunchEnd.toLocalTime()) : "00:00:00",
                    endTime != null ? dtf.format(endTime.toLocalTime()) : "00:00:00",
                    workingTimeCalculator.getWorkingTimeDayHours(),
                    Boolean.FALSE
            });
        }

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);

        JTable table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        return String.class;
                    case 6:
                        return Boolean.class;
                    default:
                        return Object.class;
                }
            }
        };

        // Set the column width for every column.
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(140);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(70);

        // Set the cells to centered design.
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);

        // TODO set background of modified cells.

        JScrollPane scrollPane = new JScrollPane(table);

        c.insets = new Insets(0, 0, 10, 0);
        c.gridy = 0;
        getContentPane().add(scrollPane, c);

        c.insets = new Insets(10, 230, 4, 230);
        c.gridy = 1;
        getContentPane().add(saveBtn, c);
    }
}
