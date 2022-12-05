package org.grammo;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Index {
    private final List<Map<String, Object>> index = new ArrayList<>();

    public void addPage(Map<String, Object> preamble) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALY);
        LocalDate localeDate = LocalDate.parse(preamble.get("date").toString(), formatter);
        Timestamp timestamp = Timestamp.valueOf(localeDate.atStartOfDay());
        preamble.put("timestamp", timestamp.getTime());
        index.add(preamble);
    }

    public List<Map<String, Object>> getIndex() {

        return index
                .stream()
                .sorted((o1, o2) -> {
                    long tA = (long) o1.get("timestamp");
                    long tB = (long) o2.get("timestamp");
                    int result = 0;
                    if (tA > tB) {
                        result = -1;
                    }
                    if (tA < tB) {
                        result = 1;
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    public String toJSON() {
        return getIndex().stream()
                .map(JSONConverter::convertMap)
                .collect(Collectors.toList())
                .toString();
    }
}
