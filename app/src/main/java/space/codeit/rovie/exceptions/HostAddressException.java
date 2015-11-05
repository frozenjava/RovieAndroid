package space.codeit.rovie.exceptions;

/**
 * Josh Artuso
 * 11/4/2015
 *
 * Throw this exception if a valid IPv4 address is not given when expected
 *
 */

public class HostAddressException extends Exception {

    public HostAddressException() {
        super();
    }

    public HostAddressException(String message) {
        super(message);
    }

    public HostAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public HostAddressException(Throwable cause) {
        super(cause);
    }

}
