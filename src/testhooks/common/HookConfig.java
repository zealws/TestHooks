package testhooks.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.restlet.data.Method;

public class HookConfig {

    private static final boolean dbInitialized;

    static {
        boolean initialize = true;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("HookConfig: Could not load postgresql driver.");
            initialize = true;
        }
        dbInitialized = initialize;
    }

    private int port;
    private Method method;
    private String hostname;
    private String uri;

    public HookConfig(String hostname, int port, String uri, Method method) {
        this.hostname = hostname;
        this.port = port;
        this.uri = uri;
        this.method = method;
    }

    public synchronized int getPort() {
        return port;
    }

    public synchronized void setPort(int port) {
        this.port = port;
    }

    public synchronized Method getMethod() {
        return method;
    }

    public synchronized void setMethod(Method method) {
        this.method = method;
    }

    public synchronized String getHostname() {
        return hostname;
    }

    public synchronized void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public synchronized String getUri() {
        return uri;
    }

    public synchronized void setUri(String uri) {
        this.uri = uri;
    }

    public synchronized String getFullUri(String subsys) {
        return String.format("http://" + hostname + ":" + port + uri.replace("{subsys}", subsys));
    }

    public static HookConfig fromDb(String jdbcConn) {
        if (!dbInitialized)
            throw new RuntimeException("HookConfig: Could not load postgresql driver, using default configuration.");
        try {
            Connection conn = DriverManager.getConnection(jdbcConn);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM hookconfig");
            try {
                if (rs.next())
                    return new HookConfig(rs.getString(1), rs.getInt(2), rs.getString(3), Method.valueOf(rs.getString(4)));
                else
                    return null;
            } finally {
                rs.close();
                st.close();
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not read hook config from database.", e);
        }
    }

    public void toDb(String jdbcConn) {
        if (!dbInitialized)
            throw new RuntimeException("HookConfig: Could not load postgresql driver, could not store in db.");
        try {

            Connection conn = DriverManager.getConnection(jdbcConn);
            Statement st = conn.createStatement();
            String query = String.format("insert into hookconfig values ('%s', %s, '%s', '%s')",
                getHostname(), getPort(), getUri(), getMethod().getName());
            st.execute(query);
            st.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not write hook config to database.", e);
        }
    }

    public static void cleanAll(String jdbcConn) {
        if (!dbInitialized)
            throw new RuntimeException("Could not load postgresql driver, could not store in db.");
        try {

            Connection conn = DriverManager.getConnection(jdbcConn);
            Statement st = conn.createStatement();
            st.execute("truncate hookconfig");
            st.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not clean hookconfig table", e);
        }
    }

}
