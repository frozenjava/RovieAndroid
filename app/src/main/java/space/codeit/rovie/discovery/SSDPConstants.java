package space.codeit.rovie.discovery;

/**
 * Josh Artuso
 * 11/2/2015
 *
 *
 * I have to give credit to 146kok on stackoverflow for inspiring my SSDP code
 * http://stackoverflow.com/questions/15743550/unable-to-receive-proper-udp-packets-using-ssdp
 *
 */

public class SSDPConstants {

    /* NEW LINE CHAR */
    public static final String NEW_LINE = "\r\n";

    /* NETWORK INFO */
    public static final String ADDRESS = "239.255.255.250";
    public static final int PORT = 1900;

    /* START LINES */
    public static final String SL_MSEARCH = "M-SEARCH * HTTP/1.1";

    /* PARTS OF THE REQUEST */
    public static final String ST = "ST";
    public static final String LOCATION = "LOCATION";

    /* Definitions of search targets */
    public static final String ST_DEVICE = "device:rovie";

}
