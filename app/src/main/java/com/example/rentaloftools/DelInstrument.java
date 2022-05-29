package com.example.rentaloftools;

import android.content.ContentValues;
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
Программный класс - наследник главной активности,
для удаления инструмента из Таблицы: "Instruments" в БД
 */
public class DelInstrument extends MainActivity implements View.OnClickListener {
    //Поле для ввода id инструмента, который необходимо удалить
    EditText delId;
    //Кнопка удаления инструмента из БД
    Button btnDel;
    //Объект для работы с очередью сообщений
    final Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delinstrument);
        delId = (EditText) findViewById(R.id.delId);
        btnDel = (Button) findViewById(R.id.delinstrument);
        //Обработчик нажатия кнопки "Удалить"
        btnDel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();
        Toast messageNull = Toast.makeText(getApplicationContext(), "Вы не ввели id инструмента", Toast.LENGTH_LONG);
        Toast message = Toast.makeText(getApplicationContext(), "Не верный id инструмента", Toast.LENGTH_LONG);
        Toast messageSQL = Toast.makeText(getApplicationContext(), "Не верный запрос к базе данных", Toast.LENGTH_LONG);
        //Подключение к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.delinstrument:
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
                            //onBackPressed();
                        }
                    }, 2000);// 5 sec
                }

                try{
                    //Удаление из Таблицы: "Instruments" по введенному id
                    int delCount = db.delete("Instruments", "id = " + delId.getText().toString(), null);
                    //В случае удачного удаления возвращаемся на родительскую активность
                    if (delCount == 1) {onBackPressed();}
                    //В случае если id не существует в таблице
                    if (delCount == 0) {
                        message.show();
                        message.setGravity(Gravity.CENTER, 0, 0);
                        ((TextView)((LinearLayout)message.getView()).getChildAt(0))
                                .setGravity(Gravity.CENTER_HORIZONTAL);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                message.cancel();
                                //onBackPressed();
                            }
                        }, 2000);// 5 sec
                        }
                }
                //В случае если произошла ошибка при удалении
                //Например: пользователь ввел ошибочные данные (вместо числа - ввел текст)
                catch(android.database.sqlite.SQLiteException e){
                    messageSQL.show();
                    messageSQL.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageSQL.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageSQL.cancel();
                            //onBackPressed();
                        }
                    }, 2000);// 5 sec
                }
                break;
        }
        //Закрываем подключение к БД
        dbHelper.close();
    }
}
