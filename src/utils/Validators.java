package utils;

import java.time.LocalDate;

public class Validators {
    public static boolean isEmpty(String value) {
        return (value == null) || (value.trim().equals(""));
    }
    
    public static boolean isNumericOnly (String fieldValue){
        return fieldValue.matches("\\d+");      
    }
    
    public static boolean isAlphabetOnly (String fieldValue){
        return fieldValue.matches("[A-Za-z ]+");      
    }
    
    public static boolean isValidDatePickerValue(LocalDate datePickerValue){
       return datePickerValue != null;  
    }
    
    public static boolean isAlphaNumeric(String fieldValue){
       return fieldValue.matches("^[a-zA-Z0-9]*$");      
    }
}
