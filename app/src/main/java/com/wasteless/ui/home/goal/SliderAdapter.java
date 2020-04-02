package com.wasteless.ui.home.goal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.wasteless.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private List<SliderModel> models;
    private LayoutInflater layoutInflater;

    public SliderAdapter(List<SliderModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    private Context context;

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);
        ImageView image;
        TextView title;
        TextView comment;
        TextView goalValue;

        image = view.findViewById(R.id.image);
        image.setImageResource(models.get(position).getImage());

        title = view.findViewById(R.id.title);
        title.setText(models.get(position).getTitle());

        goalValue = view.findViewById(R.id.goal_value);
        goalValue.setText(models.get(position).getGoal());

        comment = view.findViewById(R.id.goal_comment);
        comment.setText(models.get(position).getComment());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
