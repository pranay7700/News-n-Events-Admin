package com.vaagdevi.newsneventsadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GuestLecturesMyAdapter extends RecyclerView.Adapter<GuestLecturesMyAdapter.MyViewHolder> {

    Context context;
    ArrayList<GuestLecturesRegdatabase> guestLecturesRegdatabase;

    public GuestLecturesMyAdapter(Context c, ArrayList<GuestLecturesRegdatabase> g){
        context = c;
        guestLecturesRegdatabase = g;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.guestlectures_cardview,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get().load(guestLecturesRegdatabase.get(position).getProfilepic()).into(holder.profilePic);
        holder.name.setText(guestLecturesRegdatabase.get(position).getName());
        holder.email.setText(guestLecturesRegdatabase.get(position).getEmail());
        holder.date.setText(guestLecturesRegdatabase.get(position).getDate());
        holder.time.setText(guestLecturesRegdatabase.get(position).getTime());
        holder.description.setText(guestLecturesRegdatabase.get(position).getDescription());


    }

    @Override
    public int getItemCount() {
        return guestLecturesRegdatabase.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, email,date,time,description;
        ImageView profilePic,guestlecturesDEL;
        private FirebaseAuth mAuth;
        private DatabaseReference databaseReference;
        String currentId;

        public MyViewHolder(View itemView) {
            super(itemView);

            mAuth = FirebaseAuth.getInstance();
            currentId = mAuth.getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Guest Lectures").child(currentId);

            profilePic = (ImageView) itemView.findViewById(R.id.guestlecture_profilepicIV);
            name = (TextView) itemView.findViewById(R.id.guestlecture_nameTV);
            email = (TextView) itemView.findViewById(R.id.guestlecture_emailTV);
            date = (TextView) itemView.findViewById(R.id.guestlecture_dateTV);
            time = (TextView) itemView.findViewById(R.id.guestlecture_timeTV);
            description = (TextView) itemView.findViewById(R.id.guestlecture_descTV);

            guestlecturesDEL = (ImageView) itemView.findViewById(R.id.GuestLectures_closeIV);

            guestlecturesDEL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.removeValue();
                }
            });

        }
    }
}
