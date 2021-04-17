package com.jordy.backup;

import java.io.IOException;

import com.jordy.backup.xmpp.runnables.DataStore;
import com.jordy.backup.xmpp.runnables.XmppSubscriber;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

public class App {
    public static void main(String[] args) {
        String xmppUser = "backup01";
        String xmppUserPwd = "B@ckup!0";
        String xmppUserDomain = "example.com";
        AbstractXMPPConnection xmppConnection = null;

        /**
         * XMPP connection
         */
        XMPPTCPConnectionConfiguration config = null;
        try {
            config = XMPPTCPConnectionConfiguration.builder().setUsernameAndPassword(xmppUser, xmppUserPwd)
                    .setXmppDomain(xmppUserDomain).setHost("example.com").addEnabledSaslMechanism("SCRAM-SHA-1")
                    .build();
        } catch (XmppStringprepException e1) {
            e1.printStackTrace();
        }

        xmppConnection = new XMPPTCPConnection(config);

        try {
            xmppConnection.connect();
        } catch (InterruptedException | SmackException | XMPPException | IOException e) {
            e.printStackTrace();
        }

        try {
            xmppConnection.login();
        } catch (InterruptedException | XMPPException | SmackException | IOException e) {
            e.printStackTrace();
        }

        /**
         * XMPP THREADS
         */
        if (xmppConnection.isConnected()) {
            XmppSubscriber xmpp = new XmppSubscriber(xmppConnection);
            Thread xmppThread = new Thread(xmpp);
            xmppThread.start();
        }
        /**
         * DATABASE STORE THREADS
         */
        DataStore store = new DataStore();
        Thread storeTread = new Thread(store);
        storeTread.start();
    }
}
