package com.hillavas.filmvazhe.utils;

/**
 * Created by Arash on 15/09/06.
 */
import java.util.regex.*;

public class Validator {

    public static boolean isValidEmailAddress(String email) {
        boolean stricterFilter = true;
        String stricterFilterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
        String emailRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailRegex);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isMobileNumber(String mobileNumber) {
        //java.util.regex.Pattern p = java.util.regex.Pattern.compile("^09[19]{1,1}[0-9]{8,8}$");
        Pattern p = Pattern.compile("09-?[0-9]{2}-?[0-9]{3}-?[0-9]{4}");
        java.util.regex.Matcher m = p.matcher(mobileNumber);
        return m.matches();
    }

    public static boolean IsPassV2(String Text) {
        Pattern reg = Pattern.compile("^[a-zA-Z0-9]{4,10}$");
        return reg.matcher(Text).find();
    }

    public static boolean IsUnicode(String Text) {
        Pattern reg = Pattern.compile("^[a-zA-Z0-9]{4,10}$");
        return !reg.matcher(Text).matches();
    }

    public static boolean IsEnglish(String Text) {
        Pattern reg = Pattern.compile("^[a-zA-Z0-9]{1,15}$");
        return reg.matcher(Text).matches();
    }
}
