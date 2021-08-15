package io.ilyahaker.sokobanserver.database.api.result;

import java.lang.instrument.IllegalClassFormatException;
import java.util.List;
import java.util.Map;

public class Row {

    private Map<String, Field> fieldMap;
    private List<Field> fields;

    public Row(Map<String, Field> fieldMap, List<Field> fields) {
        this.fieldMap = fieldMap;
        this.fields = fields;
    }

    public int getInt(int columnNumber) {
        return getInt(columnNumber, 0);
    }

    public int getInt(int columnNumber, int defaultValue) {
        Field field = fields.get(columnNumber - 1);
        if (field.getClazz() != Integer.class) {
            try {
                throw new IllegalClassFormatException("Field must be integer!");
            } catch (IllegalClassFormatException e) {
                e.printStackTrace();
                return defaultValue;
            }
        }

        return (Integer) field.getValue();
    }

    public int getInt(String column) {
        return getInt(column, 0);
    }

    public int getInt(String column, int defaultValue) {
        Field field = fieldMap.get(column);
        if (field.getClazz() != Integer.class) {
            try {
                throw new IllegalClassFormatException("Field must be integer!");
            } catch (IllegalClassFormatException e) {
                e.printStackTrace();
                return defaultValue;
            }
        }

        return (Integer) field.getValue();
    }

    public String getString(int columnNumber) {
        return getString(columnNumber, "");
    }

    public String getString(int columnNumber, String defaultValue) {
        Field field = fields.get(columnNumber - 1);
        if (field.getClazz() != String.class) {
            try {
                throw new IllegalClassFormatException("Field must be integer!");
            } catch (IllegalClassFormatException e) {
                e.printStackTrace();
                return defaultValue;
            }
        }

        return (String) field.getValue();
    }

    public String getString(String column) {
        return getString(column, "");
    }

    public String getString(String column, String defaultValue) {
        Field field = fieldMap.get(column);
        if (field.getClazz() != String.class) {
            try {
                throw new IllegalClassFormatException("Field must be integer!");
            } catch (IllegalClassFormatException e) {
                e.printStackTrace();
                return defaultValue;
            }
        }

        return (String) field.getValue();
    }

}
