package de.timanager.ui.gui.statistics;

import de.timanager.ui.gui.GuiFrame;

import java.awt.*;

public abstract class BaseTableView extends GuiFrame {

    public BaseTableView(Component componentOrientation, String title, int closeOperation) {
        super(componentOrientation, title, closeOperation);
    }

    public abstract void createTable();
}
