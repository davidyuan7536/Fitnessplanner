package com.example.davidyuan7536.workoutlog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by davidyuan7536 on 7/23/2016.
 */
public class PasswordValidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String PASSWORD_PATTERN =
                "((?=.*\\d)(?=.*[A-Za-z]).{6,20})";

        public PasswordValidator(){
            pattern = Pattern.compile(PASSWORD_PATTERN);
        }

        /**
         * Validate password with regular expression
         * @param password password for validation
         * @return true valid password, false invalid password
         */
        public boolean validate(final String password){

            matcher = pattern.matcher(password);
            return matcher.matches();

        }

}
