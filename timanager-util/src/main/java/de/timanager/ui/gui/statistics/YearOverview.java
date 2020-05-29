package de.timanager.ui.gui.statistics;

import de.timanager.files.CustomFileHandler;
import de.timanager.time.TimeMap;
import de.timanager.statistics.WorkingTimeCalculator;
import de.timanager.ui.gui.GuiFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class YearOverview extends BaseTableView {
    private final GridBagConstraints c = GuiFrame.setGridBagConstraints();

    private final Object[] columnNames = {
            "Monat",
            "Dauer (Std.)"};
    private DefaultTableModel tableModel = new DefaultTableModel(null, columnNames);

    public YearOverview(Component componentOrientation) {
        super(componentOrientation, "Jahresübersicht", JFrame.DISPOSE_ON_CLOSE);

        createTable();

        pack();
        setLocationRelativeTo(componentOrientation);
        setVisible(true);
    }

    @Override
    public void createTable() {

        for (int j = 1; j < LocalDate.MAX.getMonthValue() + 1; j++) {
            TimeMap<String, LocalDateTime> timeMap = new CustomFileHandler(CustomFileHandler.getPath() + Month.of(j) + CustomFileHandler.getYear() + CustomFileHandler.getPostfix()).getTimeMap();

            tableModel.addRow(new Object[]{
                    DateTimeFormatter.ofPattern("MMMM", Locale.GERMANY).format(Month.of(j)),
                    new WorkingTimeCalculator(timeMap, Month.of(j)),
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
                        return Month.class;
                    case 1:
                        return String.class;
                    default:
                        return Object.class;
                }
            }
        };

        // Set the column width for every column.
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);

        // Set the cells to centered design.
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        // TODO set background of modified cells.

        JScrollPane scrollPane = new JScrollPane(table);

        c.insets = new Insets(0, 0, 10, 0);
        c.gridy = 0;
        getContentPane().add(scrollPane, c);
    }
}
