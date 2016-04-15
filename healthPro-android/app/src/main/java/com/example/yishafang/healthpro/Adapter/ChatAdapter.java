package com.example.yishafang.healthpro.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yishafang.healthpro.Model.ChatMessage;
import com.example.yishafang.healthpro.Model.User;
import com.example.yishafang.healthpro.R;

import java.util.ArrayList;

/**
 * Created by yellowstar on 12/9/15.
 */
public class ChatAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<ChatMessage> chatMessageList;
    private User sender;
    private User receiver;

    public ChatAdapter(Activity activity, ArrayList<ChatMessage> list, User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        chatMessageList = list;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = chatMessageList.get(position);
        View vi = convertView;
            if (convertView == null) {
                vi = inflater.inflate(R.layout.chatbubble, null);
            }
        TextView msg = (TextView) vi.findViewById(R.id.message_text);
        msg.setText(message.body);
        ImageView avatar = (ImageView) vi.findViewById(R.id.chat_avatar_iv);

        LinearLayout layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout);
        LinearLayout parent_layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout_parent);
        LinearLayout before = (LinearLayout) vi.findViewById(R.id.before_layout);
        LinearLayout after = (LinearLayout) vi.findViewById(R.id.after_layout);
        if (message.fromUser(sender)) {
            layout.setBackgroundResource(R.drawable.bubble2);
            parent_layout.setGravity(Gravity.RIGHT);
            if (after.findViewById(R.id.chat_avatar_iv) == null) {
                before.removeView(avatar);
                after.addView(avatar);
            }
            if (sender.getAvatarUrl() == null) {
                String gender = sender.getGender();
                avatar.setImageResource(gender == null || gender.equals("male") ? R.drawable.d_male : R.drawable.d_female);
            } else {
                avatar.setImageResource(R.drawable.head);
            }
        } else {
            layout.setBackgroundResource(R.drawable.bubble1);
            parent_layout.setGravity(Gravity.LEFT);
            if (before.findViewById(R.id.chat_avatar_iv) == null) {
                after.removeView(avatar);
                before.addView(avatar);
            }
            if (receiver.getAvatarUrl() == null) {
                String gender = receiver.getGender();
                avatar.setImageResource(gender == null || gender.equals("male") ? R.drawable.p_male : R.drawable.p_female);
            } else {
                avatar.setImageResource(R.drawable.head);
            }
        }
        msg.setTextColor(Color.BLACK);
        return vi;
    }

    public void add(ChatMessage object) {
        chatMessageList.add(object);
    }

}
