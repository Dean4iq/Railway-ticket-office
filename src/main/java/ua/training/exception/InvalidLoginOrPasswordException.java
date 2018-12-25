package ua.training.exception;

public class InvalidLoginOrPasswordException extends Exception {
    public InvalidLoginOrPasswordException(String message){
        super(message + " tried to log in");
    }
}
