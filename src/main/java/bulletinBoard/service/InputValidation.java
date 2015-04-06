package bulletinboard.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    //checks whether the number is included in the specified range
    public boolean isCorrectInput (int inputNumber, int minNumber, int maxNumber){
        boolean isCorrectNumber = false;
        if(inputNumber < minNumber || inputNumber > maxNumber){
            isCorrectNumber = false;
        } else {
            isCorrectNumber = true;
        }
        return isCorrectNumber;
    }

    public boolean isFirstCharacterIsLetter(String string) {
        String letter = string.substring(0, 1);
        try {
            Integer.parseInt(letter);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public boolean isContainsNonLatinCharacters(String string) {
        Pattern p = Pattern.compile("\\W");
        Matcher m = p.matcher(string);
        return m.find();
    }
}

