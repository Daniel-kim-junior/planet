package rocket.planet.util.exception;

public class ReqNotFoundException extends RuntimeException{
    public ReqNotFoundException(String message) {
        super(message);
    }
}
