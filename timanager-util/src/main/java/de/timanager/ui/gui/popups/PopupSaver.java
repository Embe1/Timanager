package de.timanager.ui.gui.popups;

import de.timanager.files.CustomFileHandler;
import de.timanager.files.TimeKey;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Calendar;

public abstract class PopupSaver extends JDialog {
    final JButton ok = new JButton("Bestätigen");
    final JButton cancel = new JButton("Abbrechen");
    @Setter
    private ActionListener okListener;
    private int gridYCounter = 0;

    PopupSaver(String title) {
        setModal(true);
        setTitle(title);
        setResizable(false);

        JPanel pane = new JPanel(new GridBagLayout());
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(pane);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(this);

        setLocation(this.getX(), this.getY());
        setAlwaysOnTop(true);
        setLayout(new GridBagLayout());
    }

    abstract void createContent();

    abstract void setActionListeners(CustomFileHandler customFileHandler, TimeKey timeKey);

    public int getGridYCounter() {
        return gridYCounter++;
    }

    void completeConstruction(Component orientation) {
        ok.addActionListener(okListener);
        cancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(orientation);
        setVisible(true);
    }

    final void addComponentWithGridBagConstraint(Component component, GridBagConstraints gridBagConstraint) {
        getContentPane().add(component, gridBagConstraint);
    }

    JSpinner createSpinner(int calendarValue, int dateTime, String formatterPatttern) {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarValue, dateTime);
        model.setValue(calendar.getTime());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, formatterPatttern);
        DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        spinner.setEditor(editor);

        return spinner;
    }
}
