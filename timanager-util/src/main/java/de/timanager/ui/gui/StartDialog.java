package de.timanager.ui.gui;

import de.timanager.files.CustomFileHandler;
import de.timanager.time.TimeKey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

public final class StartDialog extends GuiFrame {

    public StartDialog() {
        setResizable(false);
        setLocationRelativeTo(null);

        addContent();

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new StartDialog();
    }

    private void addContent() {
        JPanel buttonPanel = new JPanel();

        JLabel text = new JLabel("Soll die Aufzeichnung gestartet werden?");
        text.setBorder(new EmptyBorder(0,0,10,0));

        Component startBtn = buttonPanel.add(newButton("Starten"));
        startBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CustomFileHandler().addTime(LocalDateTime.now(), TimeKey.WORKTIME_END);
                System.exit(0);
            }
        });

        getContentPane().add(text);
        getContentPane().add(buttonPanel);
    }
}
