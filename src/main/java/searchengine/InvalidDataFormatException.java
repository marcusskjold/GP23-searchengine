package searchengine;

/** An checked exception indicating that the data file is formatted incorrectly. */
public class InvalidDataFormatException extends Exception {
    public InvalidDataFormatException(){
        super();
    }
    public InvalidDataFormatException(String message){
        super(message);
    }
}