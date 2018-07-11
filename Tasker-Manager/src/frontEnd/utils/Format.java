package frontEnd.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Format {
    public static void setDatePickerFormat(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd/MM/yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        //As to avoid input errors where the user could enter invalid dates, the text field is disabled.
        //This way the user can still enter dates using the visual interface but cannot enter invalid dates through
        //the text field.
        datePicker.getEditor().setDisable(true);
        datePicker.getEditor().setStyle("-fx-opacity: 1.0;");
     }
    public static StringProperty formatTableDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        if(date.equals(today.minusDays(1)))
            return new SimpleStringProperty("Yesterday");
        else if(date.equals(today.plusDays(1)))
            return new SimpleStringProperty("Tomorrow");
        else if(date.equals(today))
            return new SimpleStringProperty("Today");
        else
            return new SimpleStringProperty(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

     public static boolean validateTaskDescription(String description) {
        return !description.trim().isEmpty();
     }
}
