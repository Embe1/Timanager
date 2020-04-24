package de.timanager.ui.gui.statistics;

import de.timanager.ui.gui.GuiFrame;

import javax.swing.*;
import java.awt.*;

public final class YearOverview extends BaseTableView {
    private final GridBagConstraints c = GuiFrame.setGridBagConstraints();

    public YearOverview(Component componentOrientation) {
        super(componentOrientation, "Jahresübersicht", JFrame.DISPOSE_ON_CLOSE);

        getContentPane().add(new JLabel("In progress..."), c);
        createTable();

        pack();
        setLocationRelativeTo(componentOrientation);
        setVisible(true);
    }

    @Override
    public void createTable() {}
}
