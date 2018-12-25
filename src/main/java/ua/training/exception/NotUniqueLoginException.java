package ua.training.exception;

public class NotUniqueLoginException extends Exception{
    public NotUniqueLoginException(String message){
        super("Login already exists: " + message);
    }
}
