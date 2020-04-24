package de.timanager.ui.gui;

import de.timanager.files.CustomFileHandler;
import de.timanager.files.TimeKey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

public final class StopDialog extends GuiFrame {

    public StopDialog() {
        setResizable(false);
        setLocationRelativeTo(null);

        addContent();

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new StopDialog();
    }

    private void addContent() {
        JPanel buttonPanel = new JPanel();

        JLabel text = new JLabel("Soll die Aufzeichnung gestoppt werden?");
        text.setBorder(new EmptyBorder(0,0,10,0));

        Component startBtn = buttonPanel.add(newButton("Stoppen"));
        startBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CustomFileHandler().addTime(LocalDateTime.now(), TimeKey.WORKTIME_START);
                System.exit(0);
            }
        });

        getContentPane().add(text);
        getContentPane().add(buttonPanel);
    }
}
