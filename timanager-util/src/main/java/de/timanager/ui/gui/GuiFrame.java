package de.timanager.ui.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class GuiFrame extends JFrame {
    private static GridBagConstraints c = new GridBagConstraints();

    public GuiFrame() {
        this(100, 100, JFrame.EXIT_ON_CLOSE);
    }

    public GuiFrame(String title) {
        this(100, 100, JFrame.DISPOSE_ON_CLOSE, null, title);
    }

    public GuiFrame(Component componentOrientation) {
        this(0, 0, componentOrientation);
    }

    public GuiFrame(Component componentOrientation, String title) {
        this(0, 0, JFrame.EXIT_ON_CLOSE, componentOrientation, title);
    }

    public GuiFrame(int width, int height) {
        this(width, height, JFrame.DISPOSE_ON_CLOSE, null, "");
    }

    public GuiFrame(int width, int height, int closeOperation) {
        this(width, height, closeOperation, null, "");
    }

    public GuiFrame(Component componentOrientation, String title, int closeOperation) {
        this(0, 0, closeOperation, componentOrientation, title);
    }

    public GuiFrame(int width, int height, int closeOperation, String title) {
        this(width, height, closeOperation, null, title);
    }

    public GuiFrame(int width, int height, Component componentOrientation) {
        this(width, height, JFrame.DISPOSE_ON_CLOSE, componentOrientation, "");
    }

    public GuiFrame(int width, int height, int closeOperation, Component componentOrientation, String title) {
        setTitle(title);
        setResizable(false);
        setLayout();
        setDefaultCloseOperation(closeOperation);
    }

    public static GridBagConstraints setGridBagConstraints() {
        return setGridBagConstraints(0, 0);
    }

    public static GridBagConstraints setGridBagConstraints(int x, int y) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = x;
        c.gridy = y;
        return c;
    }

    final Component newButton(String text) {
        JButton button = new JButton();
        button.setText(text);
        return button;
    }

    private void setLayout() {
        JPanel pane = new JPanel(new GridBagLayout());
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));

        setContentPane(pane);
    }
}
