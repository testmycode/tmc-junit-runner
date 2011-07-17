package fi.helsinki.cs.tmc.testscanner;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestScannerServerTest {

    private Thread serverThread;
    private int portNum;

    @Before
    public void setUp() throws InterruptedException, UnsupportedEncodingException {
        this.serverThread = new Thread() {

            @Override
            public void run() {
                try {
                    TestScannerServer.main(new String[0]);
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        serverThread.setDaemon(true);

        final AtomicBoolean portAvailable = new AtomicBoolean(false);
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream() {

            @Override
            public void close() throws IOException {
                super.close();
                synchronized (portAvailable) {
                    portAvailable.set(true);
                    portAvailable.notify();
                }
            }
        };

        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(outBuf));
        try {
            synchronized (portAvailable) {
                serverThread.start();
                long startTime = System.currentTimeMillis();
                while (!portAvailable.get()) {
                    portAvailable.wait(5000);
                    if (System.currentTimeMillis() - startTime >= 5000) {
                        serverThread.join(1000);
                        throw new RuntimeException("Waiting for port number took too long");
                    }
                }
            }
        } finally {
            System.setOut(oldOut);
        }

        portNum = Integer.parseInt(outBuf.toString("UTF-8").trim());
    }

    @After
    public void tearDown() throws InterruptedException, IOException {
        Socket socket = new Socket("localhost", portNum);
        Writer w = new OutputStreamWriter(socket.getOutputStream());
        w.write("SHUTDOWN!");
        w.flush();
        socket.close();
        serverThread.join();
        serverThread = null;
        portNum = 0;
    }

    @Test
    public void shouldFindTestsFromInputPaths() throws IOException {
        Socket socket = new Socket("localhost", portNum);
        Writer w = new OutputStreamWriter(socket.getOutputStream());
        w.write("test\n\n");
        w.flush();

        Reader reader = new InputStreamReader(socket.getInputStream(), "UTF-8");
        TestMethod[] tms = new Gson().fromJson(reader, TestMethod[].class);

        boolean found = false;
        for (TestMethod tm : tms) {
            if (tm.className.equals("TestScannerTestSubject")
                    && tm.methodName.equals("twoExTestMethod")
                    && Arrays.equals(tm.exercises, new String[]{"one", "two"})) {
                found = true;
                break;
            }
        }
        if (!found) {
            fail("twoExTestMethod() not found");
        }
    }
}
