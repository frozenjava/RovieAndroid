package space.codeit.rovie.models;

import android.content.Context;

/**
 * Josh Artuso
 * 11/4/2015
 *
 * The connection object
 *
 */


public class Connection {

    public String hostAddress;
    public String connectionDate;
    public int connectionId;

    /**
     * Connection constructor
     * @param hostAddress: The address of the host connected to
     * @param connectionDate: The date this host was connected to
     */
    public Connection(String hostAddress, String connectionDate) {
        this.hostAddress = hostAddress;
        this.connectionDate = connectionDate;
    }

    /**
     * Just an empty constructor
     */
    public Connection() {

    }

    /**
     * setHostAddress
     * This method will set the host address and make sure that it is a valid ip address
     * @param address: The IPv4 address of the host
     */
    public void setHostAddress(String address) {
        this.hostAddress = address;
    }

    /**
     * setConnectionDate
     * This method will set the connection date of the Connection object
     * @param connectionDate: The date that this connection was used last
     */
    public void setConnectionDate(String connectionDate) {
        this.connectionDate = connectionDate;
    }

    /**
     * setConnectionId
     * This method will set the connection id of the Connection object
     * @param id: The unique identifier
     */
    public void setConnectionId(int id) {
        this.connectionId = id;
    }

    /**
     * getHostAddress
     * This method returns the host address formatted properly
     * @return this.hostAddress
     */
    public String getHostAddress() {
        return this.hostAddress;
    }

    /**
     * getConnectionDate
     * This method returns the last connection date
     * @return this.connectionDate
     */
    public String getConnectionDate() {
        return this.connectionDate;
    }

    /**
     * getConnectionId
     * This method returns the unique connection id
     * @return this.connectionId
     */
    public int getConnectionId() {
        return this.connectionId;
    }

}
