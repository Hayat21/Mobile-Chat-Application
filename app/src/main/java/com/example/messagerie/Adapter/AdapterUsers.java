package com.example.messagerie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagerie.R;
import com.example.messagerie.model.Users;

import java.util.List;

public class AdapterUsers extends  RecyclerView.Adapter<AdapterUsers.MyHolder> {

Context context;
List<Users> usersList;
// constructor

    public AdapterUsers(Context context,List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.users_single,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
              // get data
        String userImage=usersList.get(position).getImage();
        String username=usersList.get(position).getName();
        final String userstatut=usersList.get(position).getStatus();
        // set data
        holder.Name.setText(username);
        holder.status.setText(userstatut);

        try {
          //  Picasso.get().load(userImage)
                  //  .placeholder(R.drawable.profile)
                 //   .into(holder.profileimg);

        }
        catch (Exception e){}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(context,""+userstatut,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

       ImageView profileimg;
        TextView Name,status;
        Button request_send,request_decline;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.profileimg=itemView.findViewById(R.id.user_single_image);
            this.Name=itemView.findViewById(R.id.user_single_name);
           this. status=itemView.findViewById(R.id.user_single_status );





        }
    }
}
