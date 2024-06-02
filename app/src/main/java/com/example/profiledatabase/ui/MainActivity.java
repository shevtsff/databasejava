package com.example.profiledatabase.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profiledatabase.R;
import com.example.profiledatabase.data.DatabaseAdapter;
import com.example.profiledatabase.data.Profile;
import com.example.profiledatabase.data.ProfileAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textViewCount;
    ProfileAdapter profileAdapter;
    DatabaseAdapter databaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        textViewCount = findViewById(R.id.textView);

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);


    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseAdapter = new DatabaseAdapter(MainActivity.this);
        databaseAdapter.Open();
        profileAdapter = new ProfileAdapter(MainActivity.this, databaseAdapter.profiles()); //проинициализировали
        recyclerView.setAdapter(profileAdapter);
        textViewCount.setText("Найдено элементов" + profileAdapter.getItemCount());
    }

    //экземпляр класса
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT: {
                    Profile deletedProfile = databaseAdapter.getSingleProfile((long) viewHolder.itemView.getTag());//получение id той записи конкретно в тот холдер по которому свайпаем
                    new AlertDialog.Builder(MainActivity.this).setTitle("Удаление пользователя")
                            .setMessage("Удалить пользователя " + deletedProfile.name + "?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseAdapter.delete((long) viewHolder.itemView.getTag());
                                    onResume();
                                    Snackbar.make(recyclerView, "Пользователь " + deletedProfile.name + " удален!", Snackbar.LENGTH_LONG)
                                            .setAction("Восстановить?", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    databaseAdapter.insert(deletedProfile);
                                                    onResume();//обновление записей в recicle view
                                                }
                                            }).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onResume();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
                }
                case ItemTouchHelper.RIGHT: {
                    editProfile(recyclerView, (long)viewHolder.itemView.getTag());
                    break;
                }
            }
        }
    };


    //метод который добавляет записи
    public void addProfile(View view) {
        Dialog addDialog = new Dialog(this, R.style.Theme_ProfileDatabase); //проинициализировали диалоговое окно
        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        addDialog.setContentView(R.layout.add_profile_dialog);
        //создание переменных для полей которые находятся на диалоговом окне
        EditText dialogSname = addDialog.findViewById(R.id.personSname);
        EditText dialogName = addDialog.findViewById(R.id.personName);
        EditText dialogAge = addDialog.findViewById(R.id.personAge);
        //метод для кнопки, по нажатию которого формируется профиль который передается в метод Insert
        addDialog.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogSname.getText().toString().isEmpty()) {
                    dialogSname.setError("Это обязательно поле!");
                    dialogSname.requestFocus();
                    return;
                }
                if (dialogName.getText().toString().isEmpty()) {
                    dialogName.setError("Это обязательно поле!");
                    dialogName.requestFocus();
                    return;
                }
                if (dialogAge.getText().toString().isEmpty()) {
                    dialogAge.setError("Это обязательно поле!");
                    dialogAge.requestFocus();
                    return;
                }
                //записано значение полей в строковых переменных
                String sname = dialogSname.getText().toString();
                String name = dialogName.getText().toString();
                int age = Integer.parseInt(dialogAge.getText().toString());

                databaseAdapter.insert(new Profile(sname, name, age, R.drawable.ic_launcher_background));
                Toast.makeText(MainActivity.this, "Данные успешно добавлены!", Toast.LENGTH_SHORT).show();
                onResume();//обновление данных в recicle view
                addDialog.hide();
            }
        });
        addDialog.show();//вызов окна
    }
    public void editProfile(View view, long id) {
        Dialog editDialog = new Dialog(MainActivity.this, R.style.Theme_ProfileDatabase); //проинициализировали диалоговое окно
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        editDialog.setContentView(R.layout.add_profile_dialog);
        //создание переменных для полей которые находятся на диалоговом окне
        EditText dialogSname = editDialog.findViewById(R.id.personSname);
        EditText dialogName = editDialog.findViewById(R.id.personName);
        EditText dialogAge = editDialog.findViewById(R.id.personAge);

        Profile profile =  databaseAdapter.getSingleProfile(id);
        dialogSname.setText(profile.sName);
        dialogName.setText(profile.name);
        dialogAge.setText(String.valueOf(profile.age));
        //метод для кнопки, по нажатию которого формируется профиль который передается в метод Insert
        editDialog.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogSname.getText().toString().isEmpty()) {
                    dialogSname.setError("Это обязательное поле!");
                    dialogSname.requestFocus();
                    return;
                }
                if (dialogName.getText().toString().isEmpty()) {
                    dialogName.setError("Это обязательное поле!");
                    dialogName.requestFocus();
                    return;
                }
                if (dialogAge.getText().toString().isEmpty()) {
                    dialogAge.setError("Это обязательное поле!");
                    dialogAge.requestFocus();
                    return;
                }
                // Значение полей в строковых переменных
                String sname = dialogSname.getText().toString();
                String name = dialogName.getText().toString();
                int age = Integer.parseInt(dialogAge.getText().toString());

                databaseAdapter.update(new Profile(id, sname, name, age, R.drawable.ic_launcher_background));
                Toast.makeText(MainActivity.this, "Данные успешно добавлены!", Toast.LENGTH_SHORT).show();
                onResume();//Обновление данных в recycle
                editDialog.hide();
            }
        });
        editDialog.show();//Диалоговое окно
    }
}