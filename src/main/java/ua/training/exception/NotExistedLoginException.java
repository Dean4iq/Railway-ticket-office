package ua.training.exception;

public class NotExistedLoginException extends Exception {
    public NotExistedLoginException(String message){
        super(message + " username doesn't exists");
    }
}
