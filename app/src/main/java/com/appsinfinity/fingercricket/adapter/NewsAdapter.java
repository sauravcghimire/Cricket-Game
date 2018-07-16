package com.appsinfinity.fingercricket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinfinity.fingercricket.R;
import com.appsinfinity.fingercricket.interfaces.NewsSelection;
import com.appsinfinity.fingercricket.models.NewsDto;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Saurav on 8/6/2014.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<NewsDto> newsDtoList;
    private NewsSelection newsSelection;


    public NewsAdapter( NewsSelection newsSelection, List<NewsDto> newsDtoList) {
        this.newsDtoList = newsDtoList;
        this.newsSelection = newsSelection;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textViewTitle.setText("" + newsDtoList.get(position).getTitle());
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date date = null;
        try {
            date = formatter.parse(newsDtoList.get(position).getDate());
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        holder.tvDate.setText(date.getDate()+"\n"+getMonthForInt(date.getMonth()).substring(0,3));
    }


    @Override
    public int getItemCount() {
        return newsDtoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle,tvDate;
        RelativeLayout card;
        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            card = (RelativeLayout) itemView.findViewById(R.id.card);
            textViewTitle = (TextView) itemView.findViewById(R.id.text_view_news_title);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newsSelection.onNewsSelected(newsDtoList.get(getAdapterPosition()));
                }
            });
        }
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
}
