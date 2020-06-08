package de.timanager.time;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
public enum TimeKey {
    WORKTIME_START,
    WORKTIME_END,
    LUNCH_START,
    LUNCH_END,
    ;

    private int number = 0;
    
    public TimeKey getPairPartner() {
        String substringStart = toString().substring(0, getUnderlinePosition());
        String substringEnd = toString().substring(getUnderlinePosition()+1);

        for (TimeKey tk : TimeKey.values()) {

            if (substringStart.equals(tk.toString().substring(0, getUnderlinePosition())) &&
                    !substringEnd.equals(tk.toString().substring(getUnderlinePosition()+1))) {
                return tk;
            }
        }
        return null;
    }
    
    private int getUnderlinePosition() {
        for (int i = 0; i < this.toString().length(); i++) {
            if (this.toString().charAt(i) == '_') {
                return i;
            }
        }
        return -1;
    }

    public String generateKey(LocalDateTime localDateTime) {
        return toString() + localDateTime.getYear() + "" + localDateTime.getMonthValue() + "" + localDateTime.getDayOfMonth();
    }

    int getCurrentAmountOfUses() {
        return number;
    }

    String getCurrentKey() {
        return number <= 0 ? this.toString() + number : this.toString() + (number - 1);
    }

    void setHighestNumberOfTimeKey(HashMap<String, LocalDateTime> timeMap) {
        int counter = 0;
        LocalDateTime localDateTime = timeMap.get(this.toString() + counter);

        while (localDateTime != null) {
            localDateTime = timeMap.get(this.toString() + ++counter);
        }
        setNumber(counter);
    }

    private void setNumber(int counter) {
        number = counter;
    }
}
