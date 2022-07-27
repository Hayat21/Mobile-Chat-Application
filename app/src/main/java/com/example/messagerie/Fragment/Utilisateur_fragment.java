package com.example.messagerie.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagerie.Adapter.AdapterUsers;
import com.example.messagerie.ProfileActivity;
import com.example.messagerie.R;
import com.example.messagerie.model.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Utilisateur_fragment extends Fragment {
    EditText search_users;
    private DatabaseReference mFriendReqDatabase;
    //AdapterUsers adapterUsers;
    private View userFragmentView;
    private RecyclerView myuserList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mUsersDatabase;//referrence of user in firebase
    //private FirebaseAuth mAuth;
    private String currentUserID;
    private AdapterUsers userAdapter;
    List<Users> userList;
    private DatabaseReference mFriendDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrent_user;
    private String mCurrent_state;
    private DatabaseReference mRootRef;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    public Utilisateur_fragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userFragmentView= inflater.inflate(R.layout.fragment_utilisateur_fragment, container, false);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        //mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        //mAuth = FirebaseAuth.getInstance();
        //mRootRef = FirebaseDatabase.getInstance().getReference();

        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
      //  mCurrent_state = "not_friends";
       // mAuth = FirebaseAuth.getInstance();
        //currentUserID = mAuth.getCurrentUser().getUid();
      //  UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
      //  ChatRequestsRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
       // ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        myuserList = (RecyclerView) userFragmentView.findViewById(R.id.musersList);
        myuserList.setHasFixedSize(true);
        myuserList.setLayoutManager(new LinearLayoutManager(getContext()));
        userList=new ArrayList<Users>();

        return userFragmentView;
    }



    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Users, RequestsViewHolder> userssRecyclerViewAdapter = new FirebaseRecyclerAdapter<Users, RequestsViewHolder>(


                Users.class,
                R.layout.users_single,
                RequestsViewHolder.class,
                mUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(final RequestsViewHolder holder, final Users model,  int i) {

                final String list_user_id = getRef(i).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String userName = dataSnapshot.child("username").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        String image = dataSnapshot.child("imageURL").getValue().toString();
                        holder.setName(userName);
                        holder.setStatus(status);
                        holder.setImage(image, getContext());
                        if(list_user_id.equals(mCurrent_user.getUid())){
                          //  holder.mView.removeCallbacks()
                        }

                        mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot){

                                if (dataSnapshot.hasChild(list_user_id)) {

                                    String req_type = dataSnapshot.child(list_user_id).child("request_type").getValue().toString();

                                    if (req_type.equals("received")) {
                                        ImageView notify= (ImageView) holder.mView.findViewById(R.id.acceptnotify_icon);
                                        notify.setVisibility(View.VISIBLE);

                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                profileIntent.putExtra("user_id", list_user_id);
                                startActivity(profileIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

            }

        };
        myuserList.setAdapter(userssRecyclerViewAdapter);

    }

public static class RequestsViewHolder extends RecyclerView.ViewHolder
{
    View mView;

    public RequestsViewHolder(@NonNull View itemView) {
        super(itemView);


            mView=itemView;
        }


    public void setName(String Name){
        TextView username=(TextView) mView.findViewById(R.id.user_single_name);
        username.setText(Name);
    }


    public void setStatus(String statis){
        TextView status= (TextView) mView.findViewById(R.id.user_single_status);
        status.setText(statis);
    }


    public void setImage(String image, Context ctx) {
        // ImageView images=mview.findViewById(R.id.user_single_image);
        //   Picasso.get().load(image).into(images);

        CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
        Picasso.get().load(image).placeholder(R.drawable.ic_account).into(userImageView);
    }
}



}
