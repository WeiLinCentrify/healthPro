package com.example.yishafang.healthpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.yishafang.healthpro.Adapter.ChatAdapter;
import com.example.yishafang.healthpro.Model.ChatMessage;
import com.example.yishafang.healthpro.Model.User;

import java.util.ArrayList;
import java.util.Date;

import static com.example.yishafang.healthpro.Constants.CHAT_USER_ID;

/**
 * Created by yellowstar on 12/7/15.
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText msg_edittext;
    private User sender;
    private User receiver;
    private UserSessionManager userSessionManager;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        //getActionBar().setTitle("Chats");
        msg_edittext = (EditText) findViewById(R.id.messageEditText);
        msgListView = (ListView) findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton)findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra(CHAT_USER_ID, 0);
        userSessionManager = new UserSessionManager(getApplicationContext());
        sender = userSessionManager.getUserDetail();
        receiver = userSessionManager.getContact(id);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList();
        chatAdapter = new ChatAdapter(this, chatlist, sender, receiver);
        msgListView.setAdapter(chatAdapter);
    }

    public void sendTextMessage(View v) {
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            ChatMessage chatMessage = new ChatMessage(sender.getId(), receiver.getId(),
                    sender.getUsername(), receiver.getUsername(), message, new Date());
            chatMessage.body = message;
            msg_edittext.setText("");
            chatAdapter.add(chatMessage);
            chatAdapter.notifyDataSetChanged();
            MainActivity.mService.xmpp.sendMessage(chatMessage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                sendTextMessage(v);
        }
    }
}
