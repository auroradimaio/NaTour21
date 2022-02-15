package com.example.natour21.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.natour21.Controller.ChatController;
import com.example.natour21.Item.ChatRoom;
import com.example.natour21.R;
import com.example.natour21.Utils.ImagePicker;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private List<ChatRoom> chatRooms;

    public ChatListAdapter(){
        chatRooms = new ArrayList<ChatRoom>();
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_layout, parent, false);
        ChatListViewHolder chatListViewHolder = new ChatListViewHolder(view);
        return chatListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {

        ChatRoom chatRoom = chatRooms.get(position);


        holder.userImage.setImageResource(ImagePicker.getImage(chatRoom.getUsername()));
        holder.username.setText(chatRoom.getUsername());
        holder.lastMessage.setText(chatRoom.getLastMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatController.chattingWith = chatRoom.getUsername();
                ChatController.openSingleChat(chatRoom.getUsername());
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        public TextView username,lastMessage;
        public ImageView userImage;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            userImage = itemView.findViewById(R.id.userImage);
        }
    }

    public void update(List<ChatRoom> newlist) {
        chatRooms.clear();
        chatRooms.addAll(newlist);
        this.notifyDataSetChanged();
    }
}
