package de.timanager.ui.gui.statistics;

import de.timanager.ui.gui.GuiFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.TableView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class WeekOverview extends BaseTableView {
    private final GridBagConstraints c = GuiFrame.setGridBagConstraints();

    public WeekOverview(Component componentOrientation) {
        super(componentOrientation, "Wochenübersicht", JFrame.DISPOSE_ON_CLOSE);

        getContentPane().add(new JLabel("In progress..."), c);
        createTable();

        pack();
        setLocationRelativeTo(componentOrientation);
        setVisible(true);
    }

    @Override
    public void createTable() {
        JPanel topPanel;
        JTable table;
        JScrollPane scrollPane;
        String[] columnNames;
        String[][] dataValues;
        JTextField textBox = new JTextField();

        setTitle("JTable Cell Color");
        setSize(300, 300);
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        getContentPane().add(topPanel);

        columnNames = new String[]{"Column 1", "Column 2", "Column 3"};

        dataValues = new String[][]{
                {"1", "2", "3"},
                {"4", "5", "6"},
                {"7", "8", "9"}
        };

        TableModel model = new myTableModel(dataValues, columnNames);

        table = new JTable();
        table.setRowHeight(50);
        table.setModel(model);

        TableColumn soprtColumn = table.getColumnModel().getColumn(1);
        soprtColumn.setCellEditor(new DefaultCellEditor(textBox));
        table.setCellSelectionEnabled(false);
        scrollPane = new JScrollPane(table);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                textBox.setBackground(Color.ORANGE);
            }
        });
    }

    private static class myTableModel extends DefaultTableModel {

        myTableModel(String[][] dataValues, String[] columnNames) {
            super(dataValues, columnNames);
        }

        public boolean isCellEditable(int row, int cols) {
            return true;
        }
    }
}
