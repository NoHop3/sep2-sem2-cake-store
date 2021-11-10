package common.utility.converter;

import javafx.util.StringConverter;

public class IntegerToString extends StringConverter<Number> {

    @Override
    public String toString(Number number) {
        return number == null || number.intValue() == 0 ? "0" : number.toString();
    }

    @Override
    public Number fromString(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
