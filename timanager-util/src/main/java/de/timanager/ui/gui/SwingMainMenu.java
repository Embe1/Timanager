package de.timanager.ui.gui;

import de.timanager.files.CustomFileHandler;
import de.timanager.time.TimeKey;
import de.timanager.ui.gui.popups.PopupConfirmation;
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
    private static SwingMainMenu swingMainMenu;

    private final JButton lunchTimeBtn = (JButton) newButton("Mittagspause");
    private final JButton startBtn = (JButton) newButton("Starten");
    private final JButton endBtn = (JButton) newButton("Beenden");

    private JLabel clockLabel = new JLabel();
    private Timer timer;
    private GridBagConstraints c = GuiFrame.setGridBagConstraints();
    private CustomFileHandler customFileHandler = new CustomFileHandler();

    private LocalTime workStartTime;
    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;
    private long startToLunchTime = 0;

    public SwingMainMenu() {
        super(0, 0, JFrame.EXIT_ON_CLOSE, "Timanager");
        swingMainMenu = this;

        addMainMenuBar();
        addContent();
        addButtonListener();
        initButtons();
        setActionBeforeShutdown();

//        if (customFileHandler.getLastStartWorkTime().equals(TimeUtil.getCurrentTimeWithoutNanos()))
//            addCurrentRecordingTimer();
        if (customFileHandler.existWorkTimeStartForToday())
            addCurrentRecordingTimer();

        pack();
        setLocationRelativeTo(null);
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

    private void setActionBeforeShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(customFileHandler::writeBackup));
    }

    /**
     * Initialize the quick-buttons
     */
    private void initButtons() {
//        enableStartBtn();
//        enableEndBtn();
//        enableLunchBtn();
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
        workStartTime = customFileHandler.getLastStartWorkTime();
        lunchStartTime = customFileHandler.getLastStartLunchTime();
        lunchEndTime = customFileHandler.getLastEndLunchTime();

        startToLunchTime = workStartTime.until(lunchStartTime, ChronoUnit.SECONDS);

        timer = new Timer(1000, (ActionEvent e) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime current = TimeUtil.getCurrentTimeWithoutNanos();
            long until = lunchEndTime.until(current, ChronoUnit.SECONDS) + startToLunchTime;

            // TODO check for lunch time not included calculating.
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
        JMenu menu3 = new JMenu("Administration");

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
                new PopupSaveTimeForDate(customFileHandler, "Zeit für Datum hinzufügen", swingMainMenu);
            }
        });

        JMenuItem menu3Item1 = new JMenuItem("Backup laden");
        menu3Item1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showConfirmDialogForBackup();
            }
        });

        menu1.add(menu1Item1);
        menu1.add(menu1Item2);
        menu1.add(menu1Item3);
        menu2.add(menu2Item1);
        menu3.add(menu3Item1);

        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);

        super.setJMenuBar(menuBar);
    }

    /**
     * Shows a confirmation-dialog, for the assurance of the loading of the backup.
     */
    private void showConfirmDialogForBackup() {
        int confirmDialog = JOptionPane.showConfirmDialog(
                this,
                "Das Backup überschreibt alle momentanen gespeicherten Monate! Sind Sie sicher, das das Backup geladen werden soll?",
                "Speicherung",
                JOptionPane.YES_NO_OPTION);

        if (confirmDialog == JOptionPane.YES_OPTION)
            customFileHandler.readBackup();
    }

    private void stopClock() {
        timer.stop();
//        getContentPane().remove(clockLabel);
//        updateFrame();
    }

    private void addCurrentRecordingTimer() {
        clockLabel = startClock();

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
                        "Aufzeichnung starten",
                        swingMainMenu);

                if (aufzeichnungStarten.isOkBtnClicked()) {
                    remove(clockLabel);
                    addCurrentRecordingTimer();
//                    enableStartBtn();
//                    enableLunchBtn();
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
                PopupSaveTime stopRecord = new PopupSaveTime(customFileHandler,
                        TimeKey.WORKTIME_END,
                        "Aufzeichnung beenden",
                        swingMainMenu);

                if (stopRecord.isOkBtnClicked()) {
                    stopClock();
//                    enableEndBtn();
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
                    LocalTime.now().getHour(),
                    LocalTime.now().getMinute(),
                    LocalTime.now().getSecond()
            );
            LocalDateTime defaultLunchTimeEnd = defaultLunchTimeStart.plusMinutes(30);
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
