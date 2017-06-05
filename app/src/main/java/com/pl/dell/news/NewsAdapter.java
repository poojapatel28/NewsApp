package com.pl.dell.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by DELL on 24-05-2017.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsModelHolder>{

    ArrayList<NewsModel> newsModelArrayList=new ArrayList<>();
    ItemClickListener itemClickListener;
    Activity a;
    Context c;



    @Override
    public NewsAdapter.NewsModelHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_model1,parent,false);
        NewsModelHolder newsModelHolder= new NewsModelHolder(view);
        return newsModelHolder;
    }

    public void setItemClickListner(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onBindViewHolder(NewsAdapter.NewsModelHolder holder, final int position) {

        View view=holder.itemView;
        holder.tvAuthor.setText(newsModelArrayList.get(position).getA_name());
        holder.tvDescription.setText(newsModelArrayList.get(position).getDescription());
        holder.tvTitle.setText(newsModelArrayList.get(position).getTitle());
       // holder.tvImage.setText(newsModelArrayList.get(position).getImage());

        Picasso.with(c).load(newsModelArrayList.get(position).getImage()).placeholder(R.drawable.loading).into(holder.tvImage);
        Picasso.with(c).load(R.drawable.iconshare).into(holder.tvButton);
       if(newsModelArrayList.get(position).getDate().equals("date1")||newsModelArrayList.get(position).getTime().equals("time1"))
        {
            holder.tvPublishedAt.setText(" ");
        }
        else
        { holder.tvPublishedAt.setText(newsModelArrayList.get(position).getDate() + "  " + newsModelArrayList.get(position).getTime());}



        holder.tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, newsModelArrayList.get(position).getUrl().toString());
                a.startActivity(Intent.createChooser(sharingIntent, "share via"));
            }
        });


       // hello



    }

    @Override
    public int getItemCount() {
        return newsModelArrayList.size();
    }



    public class NewsModelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvAuthor,tvDescription,tvTitle,tvPublishedAt,tvUrl, tvTime;
        ImageView tvImage,tvButton;
        Context c;




        public NewsModelHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);

            tvAuthor=(TextView)itemView.findViewById(R.id.author);
            tvImage=(ImageView)itemView.findViewById(R.id.image);
            tvDescription=(TextView)itemView.findViewById(R.id.des);
            tvPublishedAt=(TextView)itemView.findViewById(R.id.date_time);
            tvTitle=(TextView)itemView.findViewById(R.id.title);
            tvButton=(ImageView)itemView.findViewById(R.id.button);

        }



        @Override
        public void onClick(View view) {

            if(itemClickListener!=null)
            {
                itemClickListener.onItemClick(view,getAdapterPosition());
            }
        }



    }







    public NewsAdapter(ArrayList<NewsModel> messageModelArrayList, Context c, Activity a) {
        this.newsModelArrayList = messageModelArrayList;
        this.a=a;
        this.c = c;
    }
}
