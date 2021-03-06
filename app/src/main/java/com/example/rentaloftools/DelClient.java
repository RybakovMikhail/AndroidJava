package com.example.rentaloftools;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
Программный класс для удаления клиента из Таблицы: "Clients"
 */
public class DelClient extends MainActivity implements View.OnClickListener {
    //Поле для ввода id клиента, который необходимо удалить
    EditText delId;
    //Кнопка удаления клиента из БД
    Button btnDel;
    //Объект для работы с очередью сообщений
    final Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delclient);
        delId = (EditText) findViewById(R.id.delId);
        btnDel = (Button) findViewById(R.id.delclient);
        //Обработчик нажатия кнопки "Удалить"
        btnDel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast messageNull = Toast.makeText(getApplicationContext(), "Вы не ввели id клиента", Toast.LENGTH_LONG);
        Toast message = Toast.makeText(getApplicationContext(), "Не верный id клиента", Toast.LENGTH_LONG);
        Toast messageSQL = Toast.makeText(getApplicationContext(), "Не верный запрос к базе данных", Toast.LENGTH_LONG);
        //Подключение к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.delclient:
                //Обработка пустого ввода
                if (delId.getText().toString().equals("")) {
                    messageNull.show();
                    messageNull.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageNull.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageNull.cancel();
                        }
                    }, 2000);
                }
                try{
                    //Удаление из Таблицы: "Clients" по введенному id клиента
                    int delCount = db.delete("Clients", "id = " + delId.getText().toString(), null);
                    //В случае удачного удаления возвращаемся на родительскую активность
                    if (delCount == 1) {onBackPressed();}
                    //В случае если id не существует в таблице "Clients" - сообщение об ошибке
                    if (delCount == 0) {
                        message.show();
                        message.setGravity(Gravity.CENTER, 0, 0);
                        ((TextView)((LinearLayout)message.getView()).getChildAt(0))
                                        .setGravity(Gravity.CENTER_HORIZONTAL);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                        message.cancel();
                                    }
                                }, 2000);
                            }
                }
                //Обработка в случае, если произошла ошибка в запросе SQL при удалении клиента
                catch(android.database.sqlite.SQLiteException e){
                    messageSQL.show();
                    messageSQL.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageSQL.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageSQL.cancel();
                        }
                    }, 2000);
                }
                break;
        }
        //Закрываем подключение к БД
        dbHelper.close();
    }
}
