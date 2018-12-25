package ua.training.exception;

public class LoggedInLoginException extends Exception{
    public LoggedInLoginException(String message){
        super(message + " already logged in");
    }
}
