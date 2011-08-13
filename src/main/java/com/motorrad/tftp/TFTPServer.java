/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorrad.tftp;

import com.motorrad.util.Log;
import org.apache.commons.net.io.FromNetASCIIOutputStream;
import org.apache.commons.net.io.ToNetASCIIInputStream;
import org.apache.commons.net.tftp.*;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Iterator;


public class TFTPServer implements Runnable {
    private static final int DEFAULT_TFTP_PORT = 1669;

    public static enum ServerMode {GET_ONLY, PUT_ONLY, GET_AND_PUT;}

    private HashSet<TFTPTransfer> transfers_ = new HashSet<TFTPTransfer>();
    private volatile boolean shutdownServer = false;
    private TFTP serverTftp;
    private File serverDirectory;
    private int port;
    private Exception serverException = null;
    private ServerMode mode;

    private int maxTimeoutRetries = 3;
    private int socketTimeout;
    private Thread serverThread;

    public TFTPServer(File serverDirectory, ServerMode mode) throws IOException {
        this.port = DEFAULT_TFTP_PORT;
        this.mode = mode;
        launch(serverDirectory);
    }

    /**
     * Set the max number of retries in response to a timeout. Default 3. Min 0.
     *
     * @param retries
     */
    public void setMaxTimeoutRetries(int retries) {
        if (retries < 0) {
            throw new RuntimeException("Invalid Value");
        }
        maxTimeoutRetries = retries;
    }

    /**
     * Get the current value for maxTimeoutRetries
     */
    public int getMaxTimeoutRetries() {
        return maxTimeoutRetries;
    }

    /**
     * Set the socket timeout in milliseconds used in transfers. Defaults to the value here:
     * http://commons.apache.org/net/apidocs/org/apache/commons/net/tftp/TFTP.html#DEFAULT_TIMEOUT
     * (5000 at the time I write this) Min value of 10.
     */

    public void setSocketTimeout(int timeout) {
        if (timeout < 10) {
            throw new RuntimeException("Invalid Value");
        }
        socketTimeout = timeout;
    }

    /**
     * The current socket timeout used during transfers in milliseconds.
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /*
     * start the server, throw an error if it can't start.
     */
    private void launch(File serverDirectory) throws IOException {
        Log.info(this, "Starting TFTP Server on port " + port + ".  Server directory: "
                + serverDirectory + " Server Mode is " + mode);

        this.serverDirectory = serverDirectory.getCanonicalFile();
        if (!this.serverDirectory.exists() || !serverDirectory.isDirectory()) {
            throw new IOException("The server read directory " + serverDirectory
                    + " does not exist");
        }

        serverTftp = new TFTP();

        // This is the value used in response to each client.
        socketTimeout = serverTftp.getDefaultTimeout();

        // we want the server thread to listen forever.
        serverTftp.setDefaultTimeout(0);

        serverTftp.open(port);

        serverThread = new Thread(this);
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @Override
    protected void finalize() throws Throwable {
        shutdown();
    }

    /**
     * check if the server thread is still running.
     *
     * @return true if running, false if stopped.
     * @throws Exception throws the exception that stopped the server if the server is stopped from
     *                   an exception.
     */
    public boolean isRunning() throws Exception {
        if (shutdownServer && serverException != null) {
            throw serverException;
        }
        return !shutdownServer;
    }

    public void run() {
        try {
            while (!shutdownServer) {
                TFTPPacket tftpPacket;

                tftpPacket = serverTftp.receive();

                TFTPTransfer tt = new TFTPTransfer(tftpPacket);
                synchronized (transfers_) {
                    transfers_.add(tt);
                }

                Thread thread = new Thread(tt);
                thread.setDaemon(true);
                thread.start();
            }
        } catch (Exception e) {
            if (!shutdownServer) {
                serverException = e;
                Log.error(this, "Unexpected Error in TFTP Server - Server shut down! + " + e);
            }
        } finally {
            shutdownServer = true; // set this to true, so the launching thread can check to see if it started.
            if (serverTftp != null && serverTftp.isOpen()) {
                serverTftp.close();
            }
        }
    }

    /**
     * Stop the tftp server (and any currently running transfers) and release all opened network
     * resources.
     */
    public void shutdown() {
        Log.info(this, "Shutting down TFTP Server");
        shutdownServer = true;

        synchronized (transfers_) {
            Iterator<TFTPTransfer> it = transfers_.iterator();
            while (it.hasNext()) {
                it.next().shutdown();
            }
        }

        try {
            serverTftp.close();
        } catch (RuntimeException e) {
            // noop
        }

        try {
            serverThread.join();
        } catch (InterruptedException e) {
            // we've done the best we could, return
        }
    }

    /*
     * An instance of an ongoing transfer.
     */
    private class TFTPTransfer implements Runnable {
        private TFTPPacket tftpPacket;

        private boolean shutdownTransfer = false;

        TFTP transferTftp = null;

        public TFTPTransfer(TFTPPacket tftpPacket) {
            tftpPacket = tftpPacket;
        }

        public void shutdown() {
            shutdownTransfer = true;
            try {
                transferTftp.close();
            } catch (RuntimeException e) {
                // noop
            }
        }

        public void run() {
            try {
                transferTftp = new TFTP();

                transferTftp.beginBufferedOps();
                transferTftp.setDefaultTimeout(socketTimeout);

                transferTftp.open();

                if (tftpPacket instanceof TFTPReadRequestPacket) {
                    handleRead(((TFTPReadRequestPacket) tftpPacket));
                } else if (tftpPacket instanceof TFTPWriteRequestPacket) {
                    handleWrite((TFTPWriteRequestPacket) tftpPacket);
                } else {
                    Log.info(this, "Unsupported TFTP request (" + tftpPacket + ") - ignored.");
                }
            } catch (Exception e) {
                if (!shutdownTransfer) {
                    Log.error(this, "Unexpected Error in during TFTP file transfer.  Transfer aborted. " + e);
                }
            } finally {
                try {
                    if (transferTftp != null && transferTftp.isOpen()) {
                        transferTftp.endBufferedOps();
                        transferTftp.close();
                    }
                } catch (Exception e) {
                    // noop
                }
                synchronized (transfers_) {
                    transfers_.remove(this);
                }
            }
        }

        /*
         * Handle a tftp read request.
         */
        private void handleRead(TFTPReadRequestPacket trrp) throws IOException, TFTPPacketException {
            InputStream is = null;
            try {
                if (mode == ServerMode.PUT_ONLY) {
                    transferTftp.bufferedSend(new TFTPErrorPacket(trrp.getAddress(), trrp
                            .getPort(), TFTPErrorPacket.ILLEGAL_OPERATION,
                            "Read not allowed by server."));
                    return;
                }

                try {
                    is = new BufferedInputStream(new FileInputStream(buildSafeFile(
                            serverDirectory, trrp.getFilename(), false)));
                } catch (FileNotFoundException e) {
                    transferTftp.bufferedSend(new TFTPErrorPacket(trrp.getAddress(), trrp
                            .getPort(), TFTPErrorPacket.FILE_NOT_FOUND, e.getMessage()));
                    return;
                } catch (Exception e) {
                    transferTftp.bufferedSend(new TFTPErrorPacket(trrp.getAddress(), trrp
                            .getPort(), TFTPErrorPacket.UNDEFINED, e.getMessage()));
                    return;
                }

                if (trrp.getMode() == TFTP.NETASCII_MODE) {
                    is = new ToNetASCIIInputStream(is);
                }

                byte[] temp = new byte[TFTPDataPacket.MAX_DATA_LENGTH];

                TFTPPacket answer;

                int block = 1;
                boolean sendNext = true;

                int readLength = TFTPDataPacket.MAX_DATA_LENGTH;

                TFTPDataPacket lastSentData = null;

                // We are reading a file, so when we read less than the
                // requested bytes, we know that we are at the end of the file.
                while (readLength == TFTPDataPacket.MAX_DATA_LENGTH && !shutdownTransfer) {
                    if (sendNext) {
                        readLength = is.read(temp);
                        if (readLength == -1) {
                            readLength = 0;
                        }

                        lastSentData = new TFTPDataPacket(trrp.getAddress(), trrp.getPort(), block,
                                temp, 0, readLength);
                        transferTftp.bufferedSend(lastSentData);
                    }

                    answer = null;

                    int timeoutCount = 0;

                    while (!shutdownTransfer
                            && (answer == null || !answer.getAddress().equals(trrp.getAddress()) || answer
                            .getPort() != trrp.getPort())) {
                        // listen for an answer.
                        if (answer != null) {
                            // The answer that we got didn't come from the
                            // expected source, fire back an error, and continue
                            // listening.
                            Log.info(this, "TFTP Server ignoring message from unexpected source.");
                            transferTftp.bufferedSend(new TFTPErrorPacket(answer.getAddress(),
                                    answer.getPort(), TFTPErrorPacket.UNKNOWN_TID,
                                    "Unexpected Host or Port"));
                        }
                        try {
                            answer = transferTftp.bufferedReceive();
                        } catch (SocketTimeoutException e) {
                            if (timeoutCount >= maxTimeoutRetries) {
                                throw e;
                            }
                            // didn't get an ack for this data. need to resend
                            // it.
                            timeoutCount++;
                            transferTftp.bufferedSend(lastSentData);
                            continue;
                        }
                    }

                    if (answer == null || !(answer instanceof TFTPAckPacket)) {
                        if (!shutdownTransfer) {
                            Log.error(this,"Unexpected response from tftp client during transfer (" + answer + ").  Transfer aborted.");
                        }
                        break;
                    } else {
                        // once we get here, we know we have an answer packet
                        // from the correct host.
                        TFTPAckPacket ack = (TFTPAckPacket) answer;
                        if (ack.getBlockNumber() != block) {
                            /*
                             * The origional tftp spec would have called on us to resend the
                             * previous data here, however, that causes the SAS Syndrome.
                             * http://www.faqs.org/rfcs/rfc1123.html section 4.2.3.1 The modified
                             * spec says that we ignore a duplicate ack. If the packet was really
                             * lost, we will time out on receive, and resend the previous data at
                             * that point.
                             */
                            sendNext = false;
                        } else {
                            // send the next block
                            block++;
                            if (block > 65535) {
                                // wrap the block number
                                block = 0;
                            }
                            sendNext = true;
                        }
                    }
                }
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    // noop
                }
            }
        }

        /*
         * handle a tftp write request.
         */
        private void handleWrite(TFTPWriteRequestPacket twrp) throws IOException,
                TFTPPacketException {
            OutputStream bos = null;
            try {
                if (mode == ServerMode.GET_ONLY) {
                    transferTftp.bufferedSend(new TFTPErrorPacket(twrp.getAddress(), twrp
                            .getPort(), TFTPErrorPacket.ILLEGAL_OPERATION,
                            "Write not allowed by server."));
                    return;
                }

                int lastBlock = 0;
                String fileName = twrp.getFilename();

                try {
                    File temp = buildSafeFile(serverDirectory, fileName, true);
                    if (temp.exists()) {
                        transferTftp.bufferedSend(new TFTPErrorPacket(twrp.getAddress(), twrp
                                .getPort(), TFTPErrorPacket.FILE_EXISTS, "File already exists"));
                        return;
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(temp));

                    if (twrp.getMode() == TFTP.NETASCII_MODE) {
                        bos = new FromNetASCIIOutputStream(bos);
                    }
                } catch (Exception e) {
                    transferTftp.bufferedSend(new TFTPErrorPacket(twrp.getAddress(), twrp
                            .getPort(), TFTPErrorPacket.UNDEFINED, e.getMessage()));
                    return;
                }

                TFTPAckPacket lastSentAck = new TFTPAckPacket(twrp.getAddress(), twrp.getPort(), 0);
                transferTftp.bufferedSend(lastSentAck);

                while (true) {
                    // get the response - ensure it is from the right place.
                    TFTPPacket dataPacket = null;

                    int timeoutCount = 0;

                    while (!shutdownTransfer
                            && (dataPacket == null
                            || !dataPacket.getAddress().equals(twrp.getAddress()) || dataPacket
                            .getPort() != twrp.getPort())) {
                        // listen for an answer.
                        if (dataPacket != null) {
                            // The data that we got didn't come from the
                            // expected source, fire back an error, and continue
                            // listening.
                            Log.info(this,"TFTP Server ignoring message from unexpected source.");
                            transferTftp.bufferedSend(new TFTPErrorPacket(dataPacket.getAddress(),
                                    dataPacket.getPort(), TFTPErrorPacket.UNKNOWN_TID,
                                    "Unexpected Host or Port"));
                        }

                        try {
                            dataPacket = transferTftp.bufferedReceive();
                        } catch (SocketTimeoutException e) {
                            if (timeoutCount >= maxTimeoutRetries) {
                                throw e;
                            }
                            // It didn't get our ack. Resend it.
                            transferTftp.bufferedSend(lastSentAck);
                            timeoutCount++;
                            continue;
                        }
                    }

                    if (dataPacket != null && dataPacket instanceof TFTPWriteRequestPacket) {
                        // it must have missed our initial ack. Send another.
                        lastSentAck = new TFTPAckPacket(twrp.getAddress(), twrp.getPort(), 0);
                        transferTftp.bufferedSend(lastSentAck);
                    } else if (dataPacket == null || !(dataPacket instanceof TFTPDataPacket)) {
                        if (!shutdownTransfer) {
                            Log.error(this, "Unexpected response from tftp client during transfer (" + dataPacket + ").  Transfer aborted.");
                        }
                        break;
                    } else {
                        int block = ((TFTPDataPacket) dataPacket).getBlockNumber();
                        byte[] data = ((TFTPDataPacket) dataPacket).getData();
                        int dataLength = ((TFTPDataPacket) dataPacket).getDataLength();
                        int dataOffset = ((TFTPDataPacket) dataPacket).getDataOffset();

                        if (block > lastBlock || (lastBlock == 65535 && block == 0)) {
                            // it might resend a data block if it missed our ack
                            // - don't rewrite the block.
                            bos.write(data, dataOffset, dataLength);
                            lastBlock = block;
                        }

                        lastSentAck = new TFTPAckPacket(twrp.getAddress(), twrp.getPort(), block);
                        transferTftp.bufferedSend(lastSentAck);
                        if (dataLength < TFTPDataPacket.MAX_DATA_LENGTH) {
                            // end of stream signal - The tranfer is complete.
                            bos.close();

                            // But my ack may be lost - so listen to see if I
                            // need to resend the ack.
                            for (int i = 0; i < maxTimeoutRetries; i++) {
                                try {
                                    dataPacket = transferTftp.bufferedReceive();
                                } catch (SocketTimeoutException e) {
                                    // this is the expected route - the client
                                    // shouldn't be sending any more packets.
                                    break;
                                }

                                if (dataPacket != null
                                        && (!dataPacket.getAddress().equals(twrp.getAddress()) || dataPacket
                                        .getPort() != twrp.getPort())) {
                                    // make sure it was from the right client...
                                    transferTftp.bufferedSend(new TFTPErrorPacket(dataPacket
                                                    .getAddress(), dataPacket.getPort(),
                                                    TFTPErrorPacket.UNKNOWN_TID,
                                                    "Unexpected Host or Port"));
                                } else {
                                    // This means they sent us the last
                                    // datapacket again, must have missed our
                                    // ack. resend it.
                                    transferTftp.bufferedSend(lastSentAck);
                                }
                            }

                            // all done.
                            break;
                        }
                    }
                }
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        }

        /*
         * Utility method to make sure that paths provided by tftp clients do not get outside of the
         * serverRoot directory.
         */
        private File buildSafeFile(File serverDirectory, String fileName, boolean createSubDirs)
                throws IOException {
            File temp = new File(serverDirectory, fileName);
            temp = temp.getCanonicalFile();

            if (!isSubdirectoryOf(serverDirectory, temp)) {
                throw new IOException("Cannot access files outside of tftp server root.");
            }

            // ensure directory exists (if requested)
            if (createSubDirs) {
                createDirectory(temp.getParentFile());
            }

            return temp;
        }

        /*
         * recursively create subdirectories
         */
        private void createDirectory(File file) throws IOException {
            File parent = file.getParentFile();
            if (parent == null) {
                throw new IOException("Unexpected error creating requested directory");
            }
            if (!parent.exists()) {
                // recurse...
                createDirectory(parent);
            }

            if (parent.isDirectory()) {
                if (file.isDirectory()) {
                    return;
                }
                boolean result = file.mkdir();
                if (!result) {
                    throw new IOException("Couldn't create requested directory");
                }
            } else {
                throw new IOException("Invalid directory path - file in the way of requested folder");
            }
        }

        /*
         * recursively check to see if one directory is a parent of another.
         */
        private boolean isSubdirectoryOf(File parent, File child) {
            File childsParent = child.getParentFile();
            if (childsParent == null) {
                return false;
            }
            if (childsParent.equals(parent)) {
                return true;
            } else {
                return isSubdirectoryOf(parent, childsParent);
            }
        }
    }
}
