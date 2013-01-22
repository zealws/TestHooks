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

import testhooks.source.Hook;

public class HookConfigTest {

    private static final String jdbc = "jdbc:postgresql://localhost:5432/testhooks?user=testhooks&password=testhooks";

    static {
        Hook.initializeDb(jdbc);
    }

    @Before
    public void cleanDb() {
        HookConfig.cleanAll(jdbc);
    }

    @Test
    public void toDb() throws SQLException {
        HookConfig conf = new HookConfig("my-hostname", 42, "my-uri", Method.DELETE);
        conf.toDb(jdbc);

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
        st.execute(query);
        st.close();
        conn.close();

        HookConfig conf = HookConfig.fromDb(jdbc);
        assertEquals("my-hostname-again", conf.getHostname());
        assertEquals("my-uri-again", conf.getUri());
        assertEquals(84, conf.getPort());
        assertEquals(Method.CONNECT, conf.getMethod());
    }
}
