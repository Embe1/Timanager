package de.timanager.files;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores the elements of one month-data-file.
 *
 * @param <K> key of the time.
 * @param <V> value of the time.
 */
public final class TimeMap<K extends String, V extends LocalDateTime> extends HashMap<K, V> {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = 1_007_835_783_558_073_638L;

    public TimeMap(Object readObject) {
        super((HashMap<K, V>) readObject);
    }

    public TimeMap() {
        super(new HashMap<>());
    }

    /**
     * Gets the values of the {@link TimeKey} and the depending partner given.
     *
     * @param timeKey the key, to gets its values from.
     * @param number  the number of the key.
     * @return
     */
    @Deprecated
    public Entry<K, V> getPair(TimeKey timeKey, int number) {
        TimeKey pairPartner = timeKey.getPairPartner();
        V value = get(timeKey.toString() + number);
        V value2 = get(pairPartner.toString() + number);
        return null;
    }
    
    int getHighestKey(TimeKey timeKey) {
        ArrayList<K> entries = new ArrayList<>(keySet());

        for (K k : entries) {
            String ksub = k.toString().substring(0, timeKey.toString().length());

            if (ksub.equals(timeKey.toString())) {
                return Integer.parseInt(k.toString().substring(ksub.length() + 1));
            }
        }
        return 0;
    }

    /**
     * Counts the entries for every day, and returns the number of entrie-sets.
     *
     * @return an int, representing the count of day-entries.
     */
    public int countDateEntries() {
        int result = 0;

        for (int i = 0; i < size(); i++) {
            boolean lineCounted = false;

            for (int j = 1; j < LocalDateTime.MAX.getDayOfMonth(); j++) {

                LocalDateTime dateTime = LocalDateTime.of(
                        LocalDate.now().getYear(),
                        LocalDate.now().getMonth(),
                        j,
                        0,
                        0,
                        0);

                for (TimeKey timeKey : TimeKey.values()) {

                    if (get(timeKey.generateKey(dateTime)) != null && !lineCounted) {
                        result++;
                        lineCounted = true;
                    }
                }
            }
        }
        return result;
    }
}
