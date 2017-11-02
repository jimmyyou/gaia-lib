package edu.umich.gaialib;

import edu.umich.gaialib.gaiaprotos.ShuffleInfo;

import java.io.IOException;
import java.util.logging.Logger;

// A simple unit test for GaiaClient and Hadoop integration

public class GaiaTestServerSCP {
    private static final Logger logger = Logger.getLogger(GaiaTestServerSCP.class.getName());
    private static TestServer testServer;

    static class TestServer extends GaiaAbstractServer {

        public TestServer(int port) {
            super(port);
        }

        public void processReq(ShuffleInfo req) {
            logger.info("Received req: " + req);



            String dataName = req.getFlowsList().get(0).getDataFilename();
            String trimmedData = dataName.substring( 0 , dataName.lastIndexOf("output") );

            // trim to only include the /output
            String dstIP = req.getReducersList().get(0).getReducerIP().split(":",2)[0];

            String cmd = "scp -r " +  trimmedData + " " + dstIP + ":" + trimmedData;

            System.out.println("Invoking " + cmd);

            try {
                int inChar = System.in.read();
                System.out.print("Now proceeding");
            }
            catch (IOException e){
                System.out.println("Error reading from user");
            }

        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        testServer = new TestServer(50051);

        testServer.start();
        testServer.blockUntilShutdown();
    }
}