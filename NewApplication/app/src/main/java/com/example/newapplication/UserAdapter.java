package com.example.newapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> list;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View userView;
        TextView name;
        ImageView pic;
        public ViewHolder(View view){
            super(view);
            userView = view;
            name = (TextView)view.findViewById(R.id.user_name);
            pic = (ImageView)view.findViewById(R.id.user_pic);
        }
    }

    public UserAdapter(List<User> list1){
        list = list1;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        final ViewHolder Holder = new ViewHolder(view);
        Holder.userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Holder.getLayoutPosition();
                String name = list.get(position).getName();
                Intent intent = new Intent(MyApplication.getContext(),DetailActivity.class);
                intent.putExtra("name",name);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intent);
            }
        });
        return Holder;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position){
        User user = list.get(position);
        holder.name.setText(user.getName());
        holder.pic.setImageBitmap(user.getPic());
    }

    @Override
    public int getItemCount(){
        return list.size();
    }
}
