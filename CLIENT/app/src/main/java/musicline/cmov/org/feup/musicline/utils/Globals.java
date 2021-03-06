package musicline.cmov.org.feup.musicline.utils;

/**
 * Class that stores all variables and methods used through the application
 */

public class Globals {
    public static String PREFERENCES_NAME = "UserPreferences";
    public static String URL = "https://e72d9a91.ngrok.io";
    public enum Item {Coffee, Soda, Popcorn, Sandwich};
    public static double COFFEE_PRICE = 1.00;
    public static double SODA_PRICE = 1.50;
    public static double POPCORN_PRICE = 2.00;
    public static double SANDWICH_PRICE = 3.00;

    public static String byteToString(byte[] array){
        String array_string = new String();

        for(int i = 0; i < array.length; i++) {
            array_string += Integer.toString(array[i]);
        }

        return array_string;
    }
}
