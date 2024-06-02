package com.example.profiledatabase.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.profiledatabase.R;
import com.example.profiledatabase.ui.MainActivity;

import java.util.ArrayList;

//класс занимается выводом данных из коллекции данных в activity_main(recicler_view)
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    ArrayList<Profile> profiles;
    //передаем mainactivity и коллекцию данных
    public ProfileAdapter(Context context, ArrayList<Profile> profiles){
        this.inflater = LayoutInflater.from(context);
        this.profiles = profiles;
    }
    //методы
    @Override
    //пытается получить профиль данных recicler view который мы используем в качестве шаблона для представления каждого элемента нашего списка
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recicler_view_item, parent, false);
        //ViewHolder это каждый отдельный элемент нашего списка
        return new ViewHolder(view);
    }
    //при создании нового холдера представляет элемент списка и его позицию, заполняет элемент данными
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag((long)profiles.get(position)._id);//получение id текущей позиции и передаем его в тег холдера
        holder.textViewSname.setText(profiles.get(position).sName.toString());
        holder.textViewName.setText(profiles.get(position).name.toString());
        holder.textViewAge.setText(String.valueOf(profiles.get(position).age));
        holder.imageViewPhoto.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }
    //передает данные каждого поля
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewSname, textViewName, textViewAge;
        final ImageView imageViewPhoto;

        //конструктор
        public ViewHolder(View itemView) {
            super(itemView);
            textViewSname = itemView.findViewById(R.id.profileSname);
            textViewName = itemView.findViewById(R.id.profileName);
            textViewAge = itemView.findViewById(R.id.profileAge);
            imageViewPhoto = itemView.findViewById(R.id.profilePhoto);
        }
    }
}
