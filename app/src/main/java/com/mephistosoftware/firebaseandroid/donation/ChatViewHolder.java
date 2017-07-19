package com.mephistosoftware.firebaseandroid.donation;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.model.ChatRoom;

import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;


public class ChatViewHolder extends RecyclerView.ViewHolder {

    public TextView roomNameView;
    public TextView timestampView;
    private static final String TAG = "ChatViewHolder";

    public ChatViewHolder(View itemView) {
        super(itemView);

        roomNameView = (TextView) itemView.findViewById(R.id.chatroom_title);
        timestampView = (TextView) itemView.findViewById(R.id.chatroom_timestamp);
    }

    public void bindToPost(ChatRoom room, View.OnClickListener titleClickListener) {
        roomNameView.setText(room.getName());
        String displayDate = "";
        if (room.getCreatedAt() != null) {
            displayDate = getDate(room.getCreatedAt());
        }
        timestampView.setText(displayDate);
        roomNameView.setOnClickListener(titleClickListener);
    }

    private String getDateX(long time) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy HH:mm", cal).toString();
        return date;
    }

    private String getDate(long timeStamp){
        String displayDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {
            displayDate = sdf.format((new Date(timeStamp)));
        } catch(Exception ex){
            Log.e(TAG, ex.getMessage());
        }
        return displayDate;
    }

}
