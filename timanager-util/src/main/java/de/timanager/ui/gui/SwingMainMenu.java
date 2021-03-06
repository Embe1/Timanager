package de.timanager.ui.gui;

import de.timanager.files.CustomFileHandler;
import de.timanager.files.TimeKey;
import de.timanager.ui.gui.popups.PopupSaveTime;
import de.timanager.ui.gui.popups.PopupSaveTimeForDate;
import de.timanager.ui.gui.statistics.MonthOverview;
import de.timanager.ui.gui.statistics.WeekOverview;
import de.timanager.ui.gui.statistics.YearOverview;
import de.timanager.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Slf4j
public final class SwingMainMenu extends GuiFrame {
    private final JButton lunchTimeBtn = new JButton("Mittagspause");
    private final JButton startBtn = (JButton) newButton("Starten");
    private final JButton endBtn = (JButton) newButton("Beenden");

    private Timer timer;
    private SwingMainMenu swingMainMenu = this;
    private GridBagConstraints c = GuiFrame.setGridBagConstraints();
    private CustomFileHandler customFileHandler = new CustomFileHandler();

    private LocalTime workStartTime;
    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;
    private long startToLunchTime = 0;

    public SwingMainMenu() {
        super(0, 0, JFrame.EXIT_ON_CLOSE, "Timanager");
        setLocationRelativeTo(this);

        addMainMenuBar();
        addContent();
        addButtonListener();
        initButtons();

//        if (customFileHandler.getLastStartWorkTime().equals(TimeUtil.getCurrentTimeWithoutNanos()))
//            addCurrentRecordingTimer();
        if (customFileHandler.existWorkTimeStartForToday())
            addCurrentRecordingTimer();

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingMainMenu::new);
    }

    private void addContent() {
        c.insets = new Insets(10, 0, 0, 0);
        c.gridwidth = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 1;
        getContentPane().add(startBtn, c);

        c.gridy = 0;
        c.gridx = 1;
        getContentPane().add(endBtn, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(10, 0, 0, 0);
        getContentPane().add(lunchTimeBtn, c);
    }

    private void initButtons() {
        enableStartBtn();
        enableEndBtn();
        enableLunchBtn();
    }

    private void enableStartBtn() {
        startBtn.setEnabled(!customFileHandler.existWorkTimeStartForToday());
    }

    private void enableEndBtn() {
        endBtn.setEnabled(!customFileHandler.existWorkTimeEndForToday());
    }

    private void enableLunchBtn() {
        lunchTimeBtn.setEnabled(!(customFileHandler.existLunchTimeStartForToday() && customFileHandler.existLunchTimeEndForToday()));
    }

    private JLabel startClock() {
        JLabel clockLabel = new JLabel();

        workStartTime = customFileHandler.getLastStartWorkTime();
        lunchStartTime = customFileHandler.getLastStartLunchTime();
        lunchEndTime = customFileHandler.getLastEndLunchTime();

        startToLunchTime = workStartTime.until(lunchStartTime, ChronoUnit.SECONDS);

        timer = new Timer(1000, (ActionEvent e) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime current = TimeUtil.getCurrentTimeWithoutNanos();
            long until = lunchEndTime.until(current, ChronoUnit.SECONDS) + startToLunchTime;

            // TODO check for lunch time not calculating.
            if (until < 0)
                until = 0;

            clockLabel.setText("Aufzeichnungszeit: " + formatter.format(LocalTime.ofSecondOfDay(until)));
        });
        timer.setRepeats(true);
        timer.start();
        return clockLabel;
    }

    public final void addMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu1 = new JMenu("Statistiken");
        JMenu menu2 = new JMenu("Datenmanipulation");

        JMenuItem menu1Item1 = new JMenuItem("Wochenansicht");
        menu1Item1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new WeekOverview(swingMainMenu);
            }
        });

        JMenuItem menu1Item2 = new JMenuItem("Monatsansicht");
        menu1Item2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new MonthOverview(swingMainMenu);
            }
        });

        JMenuItem menu1Item3 = new JMenuItem("Jahresansicht");
        menu1Item3.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new YearOverview(swingMainMenu);
            }
        });

        JMenuItem menu2Item1 = new JMenuItem("Datum hinzufügen");
        menu2Item1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new PopupSaveTimeForDate(customFileHandler, "Zeit für Datum hinzufügen");
            }
        });

        menu1.add(menu1Item1);
        menu1.add(menu1Item2);
        menu1.add(menu1Item3);
        menu2.add(menu2Item1);

        menuBar.add(menu1);
        menuBar.add(menu2);

        super.setJMenuBar(menuBar);
    }

    private void stopClock() {
        timer.stop();
//        getContentPane().remove(clockLabel);
//        updateFrame();
    }

    private void addCurrentRecordingTimer() {
        JLabel clockLabel = startClock();

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1;
        c.insets = new Insets(10, 0, 10, 0);
        getContentPane().add(clockLabel, c);
    }

    private void addButtonListener() {

        startBtn.addActionListener(e -> {
            customFileHandler = new CustomFileHandler();

            if (!customFileHandler.existWorkTimeStartForToday()) {
                PopupSaveTime aufzeichnungStarten = new PopupSaveTime(
                        customFileHandler,
                        TimeKey.WORKTIME_START,
                        "Aufzeichnung starten");

                if (aufzeichnungStarten.isOkBtnClicked()) {
                    addCurrentRecordingTimer();
                    enableStartBtn();
                    enableLunchBtn();
                    updateFrame();
                }

            } else {
                JOptionPane.showMessageDialog(
                        getContentPane(),
                        "Aufzeichnung der Arbeitszeit wurde für heute bereits gestartet!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
                log.info("Recording already started for today!");
            }
        });

        endBtn.addActionListener(e -> {
            customFileHandler = new CustomFileHandler();

            if (!customFileHandler.existWorkTimeEndForToday()) {
                PopupSaveTime aufzeichnungBeenden = new PopupSaveTime(customFileHandler,
                        TimeKey.WORKTIME_END,
                        "Aufzeichnung beenden");

                if (aufzeichnungBeenden.isOkBtnClicked()) {
                    stopClock();
                    enableEndBtn();
                    updateFrame();
                }

            } else {
                JOptionPane.showMessageDialog(
                        getContentPane(),
                        "Aufzeichnung der Arbeitszeit wurde für heute bereits beendet!"
                );
                log.info("Recording already started for today!");
            }
        });

        lunchTimeBtn.setToolTipText("Es wird automatisch 13:00 - 13:30 Uhr gespeichert.");

        lunchTimeBtn.addActionListener(e -> {
            LocalDateTime defaultLunchTimeStart = LocalDateTime.of(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonth(),
                    LocalDate.now().getDayOfMonth(),
                    13,
                    0,
                    LocalTime.now().getSecond()
            );
            LocalDateTime defaultLunchTimeEnd = LocalDateTime.of(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonth(),
                    LocalDate.now().getDayOfMonth(),
                    13,
                    30,
                    LocalTime.now().getSecond()
            );
            customFileHandler.addTime(defaultLunchTimeStart, TimeKey.LUNCH_START);
            customFileHandler.addTime(defaultLunchTimeEnd, TimeKey.LUNCH_END);

            updateFrame();
        });
    }

    private void updateFrame() {
        getContentPane().revalidate();
        getContentPane().repaint();
        pack();
    }
}
