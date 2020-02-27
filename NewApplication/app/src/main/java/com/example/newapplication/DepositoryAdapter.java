package com.example.newapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DepositoryAdapter extends RecyclerView.Adapter<DepositoryAdapter.ViewHolder> implements View.OnClickListener{
    private List<Depository> list;
    public class ViewHolder extends RecyclerView.ViewHolder{
        View depositoryView;
        TextView name;
        TextView author;
        TextView language;
        TextView fork;
        TextView star;
        ImageView clc;
        public ViewHolder(View view){
            super(view);
            depositoryView = view;
            name = (TextView)view.findViewById(R.id.depository_name);
            author = (TextView)view.findViewById(R.id.depository_author);
            language = (TextView)view.findViewById(R.id.depository_language);
            fork = (TextView)view.findViewById(R.id.depository_fork);
            star = (TextView)view.findViewById(R.id.depository_star);
            clc = (ImageView)view.findViewById(R.id.depository_clc);
            clc.setOnClickListener(DepositoryAdapter.this);
        }
    }

    public DepositoryAdapter(List<Depository> list1){
        list = list1;
    }

    @Override
    public DepositoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.depository_item,parent,false);
        final DepositoryAdapter.ViewHolder holder = new DepositoryAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DepositoryAdapter.ViewHolder holder, int position){
        holder.clc.setTag(position);
        Depository depository=list.get(position);
        holder.name.setText("仓库名："+depository.getName());
        holder.author.setText("作者："+depository.getAuthor());
        holder.language.setText("语言："+depository.getLanguage());
        holder.fork.setText("fork数量："+depository.getFork());
        holder.star.setText("star数量："+depository.getStar());
        if(clctArd(depository.getName()+depository.getAuthor())){
            holder.clc.setImageResource(R.drawable.clcard);
        }
        else holder.clc.setImageResource(R.drawable.nclcard);
    }

    @Override
    public void onClick(View view){
        int position = (int)view.getTag();
        if(view.getId() == R.id.depository_clc){
            Depository dep = list.get(position);
            if(clctArd(dep.getName() + dep.getAuthor())){
                dep.delete();
                MainActivity.clctList.remove(dep);
                ImageView img = (ImageView)view;
                img.setImageResource(R.drawable.nclcard);
                Toast.makeText(view.getContext(),"已经从本地收藏夹删除辽！",Toast.LENGTH_SHORT).show();
            }
            else {
                dep.save();
                MainActivity.clctList.add(dep);
                ImageView img = (ImageView)view;
                img.setImageResource(R.drawable.clcard);
                Toast.makeText(view.getContext(),"已经添加到本地收藏夹辽！",Toast.LENGTH_SHORT).show();
            }
            MainActivity.depositoryAdapter.notifyDataSetChanged();
        }
    }

    public boolean clctArd(String s){
        for(int i = 0;i < MainActivity.clctList.size();i++){
            Depository obj = MainActivity.clctList.get(i);
            if(s.equals(obj.getName() + obj.getAuthor()))return true;
            else continue;
        }
        return false;
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

}
