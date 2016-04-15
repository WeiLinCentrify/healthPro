package com.example.yishafang.healthpro;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.yishafang.healthpro.Model.ChatMessage;
import com.google.gson.Gson;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

import java.io.IOException;
import java.util.Date;

/**
 * Created by yellowstar on 12/9/15.
 */
public class XMPP {
    public static boolean connected = false;
    public boolean loggedin = false;
    public static boolean isConnecting = false;
    public static boolean isToasted = true;
    private boolean chat_created = false;

    private MyService context;
    private String serverAddr;
    private String username;
    private String password;

    public static XMPP instance = null;
    public static boolean instanceCreated = false;

    public static XMPPTCPConnection connection;

    private Gson gson;

    public XMPP(MyService context, String serverAddr, String username,
                  String password) {
        this.context = context;
        this.serverAddr = serverAddr;
        this.username = username;
        this.password = password;
        init();
    }

    public static XMPP getInstance(MyService context, String server,
                                     String user, String pass) {
        if (instance == null) {
            instance = new XMPP(context, server, user, pass);
            instanceCreated = true;
        }
        return instance;
    }


    Chat Mychat;
    ChatManagerListenerImpl mChatManagerListener;
    MMessageListener mMessageListener;

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (ClassNotFoundException ex) {
            // problem loading reconnection manager
        }
    }

    public void init() {
        gson = new Gson();
        mMessageListener = new MMessageListener(context);
        mChatManagerListener = new ChatManagerListenerImpl();
        initialiseConnection();
    }

    private void initialiseConnection() {

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                .builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(this.serverAddr);
        config.setHost(this.serverAddr);
        config.setPort(5222);
        config.setDebuggerEnabled(true);
        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);
        connection = new XMPPTCPConnection(config.build());
        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);
    }

    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    public void connect(final String caller) {
        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {
                if (connection.isConnected()) return false;
                isConnecting = true;
                if (isToasted)
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, caller + "=>connecting....", Toast.LENGTH_LONG).show();
                        }
                    });
                Log.d("Connect() Function", caller + "=>connecting....");
                try {
                    connection.connect();
                    DeliveryReceiptManager dm = DeliveryReceiptManager.getInstanceFor(connection);
                    dm.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
                    dm.addReceiptReceivedListener(new ReceiptReceivedListener() {
                        @Override
                        public void onReceiptReceived(final String fromid,
                                                      final String toid, final String msgid,
                                                      final Stanza packet) {

                        }
                    });
                    connected = true;
                } catch (IOException e) {
                    if (isToasted)
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "(" + caller + ")" + "IOException: ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    Log.e("(" + caller + ")", "IOException: " + e.getMessage());
                } catch (SmackException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "(" + caller + ")" + "SMACKException: ", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("(" + caller + ")", "SMACKException: " + e.getMessage());
                } catch (XMPPException e) {
                    if (isToasted)
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "(" + caller + ")" + "XMPPException: ",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    Log.e("connect(" + caller + ")", "XMPPException: " + e.getMessage());

                }
                return isConnecting = false;
            }
        };
        connectionThread.execute();
    }

    public void login() {
        try {
            connection.login(username, password);
            Log.i("LOGIN", "Yey! We're connected to the Xmpp server!");
        } catch (XMPPException | SmackException | IOException e) {
            e.printStackTrace();
        }
    }

    private class ChatManagerListenerImpl implements ChatManagerListener {
        @Override
        public void chatCreated(final Chat chat, final boolean createdLocally) {
            if (!createdLocally)
                chat.addMessageListener(mMessageListener);
        }
    }

    private class MMessageListener implements ChatMessageListener {
        public MMessageListener(Context contxt) {
        }

        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat,
                                   final Message message) {
            Log.i("MyXMPP_MESSAGE_LISTENER", "Xmpp message received: '" + message);
            if (message.getType() == Message.Type.chat && message.getBody() != null) {
                ChatMessage chatMessage = new ChatMessage(message.getFrom(), message.getTo(), message.getBody(), new Date());
                processMessage(chatMessage);
            }
        }

        private void processMessage(final ChatMessage chatMessage) {
            ChatActivity.chatlist.add(chatMessage);
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    ChatActivity.chatAdapter.notifyDataSetChanged();

                }
            });
        }

    }

    public void sendMessage(ChatMessage chatMessage) {
        String body = chatMessage.body;

        if (!chat_created) {
            Mychat = ChatManager.getInstanceFor(connection).createChat(
                    chatMessage.rUsername + "@"
                            + context.getString(R.string.xmpp_host),
                    mMessageListener);
            chat_created = true;
        }
        final Message message = new Message();
        message.setBody(body);
        message.setStanzaId(chatMessage.id);
        message.setType(Message.Type.chat);

        try {
            if (connection.isAuthenticated()) {
                Mychat.sendMessage(message);
            } else {
                login();
            }
        } catch (SmackException.NotConnectedException e) {
            Log.e("xmpp.SendMessage()", "msg Not sent!-Not Connected!");

        } catch (Exception e) {
            Log.e("xmpp.SendMessage()", "msg Not sent!" + e.getMessage());
        }

    }

    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {
            Log.d("xmpp", "Connected!");
            connected = true;
            if (!connection.isAuthenticated()) {
                login();
            }
        }

        @Override
        public void connectionClosed() {
            if (isToasted)
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "ConnectionCLosed!", Toast.LENGTH_SHORT).show();
                    }
                });
            Log.d("xmpp", "ConnectionCLosed!");
            connected = false;
            chat_created = false;
            loggedin = false;
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            if (isToasted)
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "ConnectionClosedOn Error!!", Toast.LENGTH_SHORT).show();
                    }
                });
            Log.d("xmpp", "ConnectionClosedOn Error!");
            connected = false;
            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectingIn(int arg0) {
            Log.d("xmpp", "Reconnectingin " + arg0);
            loggedin = false;
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(context, "ReconnectionFailed!",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ReconnectionFailed!");
            connected = false;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        Toast.makeText(context, "REConnected!",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            Log.d("xmpp", "ReconnectionSuccessful");
            connected = true;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d("xmpp", "Authenticated!");
            loggedin = true;

            ChatManager.getInstanceFor(connection).addChatListener(
                    mChatManagerListener);

            chat_created = false;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        Toast.makeText(context, "Connected!",
                                Toast.LENGTH_SHORT).show();

                    }
                });
        }
    }

}
