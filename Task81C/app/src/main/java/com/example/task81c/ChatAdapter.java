package com.example.task81c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder>{
    private List<ChatMessage> messageList;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage msg = messageList.get(position);
        holder.textMessage.setText(msg.message);
        holder.textTimestamp.setText(timeFormat.format(new Date(msg.timestamp)));

        //Set alignment and bubble style based on who sent
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.bubble.getLayoutParams();
        if (msg.isUser) {
            //User message: right side
            params.horizontalBias = 1.0f;
            params.startToStart = ConstraintLayout.LayoutParams.UNSET;
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            holder.bubble.setBackgroundResource(R.drawable.bubble_user);
            holder.textMessage.setTextColor(Color.WHITE);
            holder.textTimestamp.setTextColor(Color.parseColor("#CCCCCC"));
        } else {
            //Bot message: left side
            params.horizontalBias = 0.0f;
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
            holder.bubble.setBackgroundResource(R.drawable.bubble_bot);
            holder.textMessage.setTextColor(Color.BLACK);
            holder.textTimestamp.setTextColor(Color.GRAY);
        }
        holder.bubble.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void updateList(List<ChatMessage> newList) {
        this.messageList = newList;
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessage message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() -1);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textTimestamp;
        LinearLayout bubble;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTimestamp = itemView.findViewById(R.id.textTimestamp);
            bubble = itemView.findViewById(R.id.messageBubble);
        }
    }
}
