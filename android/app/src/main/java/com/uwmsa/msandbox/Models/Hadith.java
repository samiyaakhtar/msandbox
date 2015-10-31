package com.uwmsa.msandbox.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by mohamed on 2015-10-31.
 */
@ParseClassName(Hadith.CLASSNAME)
public class Hadith extends ParseObject {
    public static final String CLASSNAME = "Hadith";
    public static final String NUMBER = "number";
    public static final String ENGLISHTEXT = "englishHadith";
    public static final String ARABICTEXT = "arabicHadith";

    public Hadith() {

    }

    public Number getNumber() {
        return getNumber(NUMBER);
    }

    public String getEnglishText() {
        return getString(ENGLISHTEXT);
    }

    public String getArabicText() {
        return getString(ARABICTEXT);
    }
}
