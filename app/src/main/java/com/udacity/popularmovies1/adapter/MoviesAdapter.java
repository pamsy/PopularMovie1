package com.udacity.popularmovies1.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.udacity.popularmovies1.DetailsActivity;
import com.udacity.popularmovies1.R;
import com.udacity.popularmovies1.models.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

    private Context mContext;
    private List<Movie> movieList;

    public MoviesAdapter(Context context, List<Movie> movieList){
        this.mContext =context;
        this.movieList =movieList;
    }

    @Override
   public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(movieList.get(i).getOriginalTitle());
        String rating = Double.toString(movieList.get(i).getVoteAverage());
        viewHolder.userRating.setText(rating);

        Glide.with(mContext)
                .load(movieList.get(i).getPosterPath())
                .apply(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .fitCenter())
                .into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView thumbnail;
        private TextView userRating;

        public ViewHolder(View view){
            super(view);
            
            title = (TextView) view.findViewById(R.id.title_tv) ;
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_iv);
            userRating = (TextView) view.findViewById(R.id.userrating_tv);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    //intent.putExtra(DetailsActivity.EXTRA_POSITION, position);
                    intent.putExtra(DetailsActivity.EXTRA_TITLE, movieList.get(position).getOriginalTitle());
                    intent.putExtra(DetailsActivity.EXTRA_POSTER, movieList.get(position).getPosterPath());
                    intent.putExtra(DetailsActivity.EXTRA_OVERVIEW, movieList.get(position).getOverview());
                    intent.putExtra(DetailsActivity.EXTRA_VOTE, movieList.get(position).getVoteAverage());
                    intent.putExtra(DetailsActivity.EXTRA_RELEASE, movieList.get(position).getRelease_date());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    //Toast.makeText(v.getContext(),"You clicked"+movieList.get(position).getOriginalTitle(), Toast.LENGTH_SHORT ).show();
                    Toast.makeText(v.getContext(),"You clicked"+movieList.get(position).getId(), Toast.LENGTH_SHORT ).show();
                }
            });

        }
    }
}
