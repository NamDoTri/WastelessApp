package com.wasteless.ui.settings.achievements;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wasteless.R;
import com.wasteless.roomdb.entities.Achievement;
import com.wasteless.ui.settings.SettingsViewModel;
import com.wasteless.ui.settings.achievements.AchievementViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AchievementFragment extends Fragment {
    private SettingsViewModel SettingsViewModel;
    private AchievementViewModel achievementViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        SettingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        achievementViewModel = ViewModelProviders.of(this).get(AchievementViewModel.class);
        View NotificationsFragment = inflater.inflate(R.layout.achievements_fragment, container, false);
        ListView listView;
        List<String> mTitle = new ArrayList<String>();
        List<String> mDescription = new ArrayList<String>();
        List<Boolean> isDone = new ArrayList<Boolean>();

        listView = NotificationsFragment.findViewById(R.id.listView);
        List<Achievement> allAchievements = achievementViewModel.getAllAchievements();
        class MyAdapter extends ArrayAdapter<String> {
            List<Boolean> isDone;
            Context context;
            List<String> title;
            List<String> description;
            int img;
            MyAdapter(Context c, List<String> title, List<String> description, List<Boolean> isDone) {
                super(c, R.layout.achievements_row, R.id.textView1, title);
                this.context = c;
                this.title = title;
                this.description = description;
                this.img = img;
                this.isDone = isDone;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
                LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = layoutInflater.inflate(R.layout.achievements_row, parent, false);
                ImageView image = row.findViewById(R.id.image);
                TextView myTitle = row.findViewById(R.id.textView1);
                TextView myDescription = row.findViewById(R.id.textView2);
                myTitle.setText(title.get(position));
                myDescription.setText(description.get(position));
                if (false == isDone.get(position)) {
                    image.setImageResource(R.drawable.not_achieved);
                } else  {
                    image.setImageResource(R.drawable.achievements);
                }
                return row;
            }
        }
        for (int i = 0; i < allAchievements.size(); i++) {
            Log.i("achievements", allAchievements.get(i).description);
            mTitle.add(allAchievements.get(i).name);
            mDescription.add(allAchievements.get(i).description);
            isDone.add(allAchievements.get(i).isDone);
        }

        MyAdapter adapter = new MyAdapter(getContext(), mTitle, mDescription, isDone);
        listView.setAdapter(adapter);


        return NotificationsFragment;
    }
}
