package com.example.messagerie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagerie.R;
import com.example.messagerie.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

 public class Messageadapter extends RecyclerView.Adapter<Messageadapter.MessageViewHolder>{

     /*public static  final int MSG_TYPE_LEFT = 0;
     public static  final int MSG_TYPE_RIGHT = 1;
    private List<Message> mMessageList;
    private DatabaseReference mUserDatabase;

    public Messageadapter(List<Message> mMessageList) {

        this.mMessageList = mMessageList;

    }
    View v;
    Message c;
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single ,parent, false);

        return new MessageViewHolder(v);

    }
    String from_user;
    String message_type;
    String userid=FirebaseAuth.getInstance().getUid();
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
       // public TextView messagetime;

        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);

           // messagetime = (TextView) view.findViewById(R.id.time_text_layout);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        c = mMessageList.get(i);

        from_user = c.getFrom();
        message_type = c.getType();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("username").getValue().toString();
                String image = dataSnapshot.child("imageURL").getValue().toString();
              // viewHolder.messagetime.setText(convertDateToHour(c.getTime()));
               // viewHolder.messagetime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",c.getTime()));
                viewHolder.displayName.setText(name);

                Picasso.get().load(image)
                        .placeholder(R.drawable.ic_account).into(viewHolder.profileImage);
               // (viewHolder.profileImage.getContext());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")) {

            viewHolder.messageText.setText(c.getMessage());


        }

    }

    @Override
    public int getItemCount() {

        return mMessageList.size();
    }

     @Override
     public int getItemViewType(int position) {
         fuser = FirebaseAuth.getInstance().getCurrentUser();
         if (mChat.get(position).getSender().equals(fuser.getUid())){
             return MSG_TYPE_RIGHT;
         } else {
             return MSG_TYPE_LEFT;
         }
     }

         private String convertDateToHour(long date){
             DateFormat dfTime = new SimpleDateFormat("HH:mm");
             return dfTime.format(date);
         }
     }

*/
     public static  final int MSG_TYPE_LEFT = 0;
     public static  final int MSG_TYPE_RIGHT = 1;

     private Context mContext;
     private List<Message> mChat;
     private String imageurl;

     FirebaseUser fuser;

     public Messageadapter(Context mContext, List<Message> conv){
         this.mChat =conv;
         this.mContext = mContext;
        // this.imageurl = imageurl;
     }

     @NonNull
     @Override
     public Messageadapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if (viewType == MSG_TYPE_RIGHT) {
             View view = LayoutInflater.from(mContext).inflate(R.layout.single_right_message, parent, false);
             return new Messageadapter.MessageViewHolder(view);
         } else {
             View view = LayoutInflater.from(mContext).inflate(R.layout.message_single, parent, false);
             return new Messageadapter.MessageViewHolder(view);
         }
     }

     @Override
     public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

         Message chat = mChat.get(position);

         holder.show_message.setText(chat.getMessage());

        //if (imageurl.equals("default")){
       //    holder.profile_image.setImageResource(R.drawable.ic_account);
        // } else {
          //  Picasso.get().load(imageurl)
            //        .placeholder(R.drawable.ic_account).into(holder.profile_image);
      //}

         if (position == mChat.size()-1){
             if (chat.isSeen()){
                 holder.txt_seen.setText("Vu");
             } else {
                 holder.txt_seen.setText("");
             }
         } else {
             holder.txt_seen.setVisibility(View.GONE);
         }

     }

     @Override
     public int getItemCount() {
         return mChat.size();
     }

     public  class MessageViewHolder extends RecyclerView.ViewHolder{

         public TextView show_message;
         public ImageView profile_image;
         public TextView txt_seen;

         public MessageViewHolder(View itemView) {
             super(itemView);

             show_message = itemView.findViewById(R.id.show_message);
             profile_image = itemView.findViewById(R.id.profile_image);
             txt_seen = itemView.findViewById(R.id.txt_seen);
         }


     }

     @Override
     public int getItemViewType(int position) {
         fuser = FirebaseAuth.getInstance().getCurrentUser();
         if (mChat.get(position).getFrom().equals(fuser.getUid())){
             return MSG_TYPE_RIGHT;
         } else {
             return MSG_TYPE_LEFT;
         }
     }
 }
