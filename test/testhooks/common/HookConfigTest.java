package testhooks.common;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Method;

public class HookConfigTest {

    private static final String jdbc = "jdbc:postgresql://localhost:5432/testhooks?user=testhooks&password=testhooks";

    static {
        HookConfig.initializeDb(jdbc);
    }

    @Before
    public void cleanDb() {
        HookConfig.cleanAll();
    }

    @Test
    public void toDb() throws SQLException {
        HookConfig conf = HookConfig.getInstance();
        conf.setHostname("my-hostname");
        conf.setPort(42);
        conf.setUri("my-uri");
        conf.setMethod(Method.DELETE);
        HookConfig.toDb();

        Connection conn = DriverManager.getConnection(jdbc);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM hookconfig");
        rs.next();
        assertEquals("my-hostname", rs.getString(1));
        assertEquals(42, rs.getInt(2));
        assertEquals("my-uri", rs.getString(3));
        assertEquals("DELETE", rs.getString(4));
        rs.close();
        st.close();
        conn.close();
    }

    @Test
    public void fromDb() throws SQLException {
        Connection conn = DriverManager.getConnection(jdbc);
        Statement st = conn.createStatement();
        String query = String.format("insert into hookconfig values ('%s', %s, '%s', '%s')",
            "my-hostname-again", 84, "my-uri-again", "CONNECT");
        System.out.println(query);
        st.execute(query);
        st.close();
        conn.close();

        HookConfig conf = HookConfig.getInstance();
        HookConfig.fromDb();
        conf.setHostname("my-hostname");
        conf.setPort(42);
        conf.setUri("my-uri");
        conf.setMethod(Method.DELETE);
        conf.toDb();
    }
}
