package com.example.rentaloftools;

import android.content.ContentValues;
import android.database.Cursor;
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
для изменения параметров клиента в Таблице: "Clients"
 */
public class EditClient extends MainActivity implements View.OnClickListener {
    //Метки полей для ввода данных
    TextView txtPhonesClient, txtNameClient, txtIndividualDiscountClient;
    //Поля для ввода данных
    EditText idClient, nameClient, phonesClient, individualDiscountClient;
    //Кнопка сохранения измененного клиента в Таблицу: "Clients"
    Button btnSave;
    //Кнопка отображения параметров выбранного клиента
    Button btnShow;
    //Объект для работы с очередью сообщений
    final Handler handler = new Handler();
    //Количество строк в Таблице: "Clients"
    int countClients = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editclient);
        idClient = (EditText) findViewById(R.id.idClient);
        nameClient = (EditText) findViewById(R.id.nameClient);
        phonesClient = (EditText) findViewById(R.id.phonesClient);
        individualDiscountClient = (EditText) findViewById(R.id.individualDiscountClient);
        btnShow = (Button) findViewById(R.id.showclient);
        btnShow.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.saveclient);
        btnSave.setOnClickListener(this);
        //Подключение к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Выполняем запрос на выборку данных из Таблицы: "Clients"
        //Получаем интерфейс для чтения и записи значений результата запроса в БД
        Cursor cursor = db.rawQuery("SELECT * FROM Clients", null);
        cursor.moveToLast();
        //Количество строк в Таблице: "Clients"
        countClients = cursor.getInt(0);
        //Устанавливаем в поле для ввода id - номер последнего инструмента
        idClient.setText(String.valueOf(countClients));
    }

    @Override
    public void onClick(View v) {
        //Используется для обновления параметров клиента
        ContentValues cv = new ContentValues();
        Toast messageInt = Toast.makeText(getApplicationContext(), "Данные id клиента, индивидуальная скидка должны быть числовыми", Toast.LENGTH_LONG);
        Toast messageNull = Toast.makeText(getApplicationContext(), "Пустые поля ввода данных", Toast.LENGTH_LONG);
        Toast messageSQL = Toast.makeText(getApplicationContext(), "Не верный запрос к базе данных", Toast.LENGTH_LONG);
        //Получаем данные из полей ввода
        String id = idClient.getText().toString();
        String name = nameClient.getText().toString();
        String phones = phonesClient.getText().toString();
        String individualDiscount = individualDiscountClient.getText().toString();
        //Подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.showclient:
                //Отправляем запрос в БД для Таблицы: "Clients"
                Cursor cursor = db.rawQuery("SELECT * FROM Clients WHERE "+"id = " + id, null);
                cursor.moveToFirst();
                //Цикл по результату запроса в БД
                while (!cursor.isAfterLast()) {
                    nameClient.setText(cursor.getString(1));
                    phonesClient.setText(cursor.getString(2));
                    individualDiscountClient.setText(cursor.getString(3));
                    cursor.moveToNext();
                }
                cursor.close();
                break;
            case R.id.saveclient:
                //Обработка не числовых значений ввода
                try{
                    int id1 = Integer.parseInt(idClient.getText().toString());
                    int individualDiscount1 = Integer.parseInt(individualDiscountClient.getText().toString());
                }catch (Exception e){
                    messageInt.show();
                    messageInt.setGravity(Gravity.CENTER, 0, 0);
                    ((TextView)((LinearLayout)messageInt.getView()).getChildAt(0))
                            .setGravity(Gravity.CENTER_HORIZONTAL);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageInt.cancel();
                        }
                    }, 2000);
                    btnShow.setClickable(false);
                    break;
                }

                cv.put("id", idClient.getText().toString());
                cv.put("name", nameClient.getText().toString());
                cv.put("phones", phonesClient.getText().toString());
                cv.put("individualDiscount", individualDiscountClient.getText().toString());
                System.out.println(idClient.getText().toString());
                System.out.println(nameClient.getText().toString());
                System.out.println(phonesClient.getText().toString());
                System.out.println(individualDiscountClient.getText().toString());
                //Обработка пустого ввода
                if (!(id.equals("") && name.equals("")
                        && phones.equals("") && individualDiscount.equals(""))) {
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
                }else{
                try{
                    //Изменяем параметры инструмента в Таблице: "Clients"
                    int updateCount = (int) db.update("Clients", cv, "id = " + id, null);
                    System.out.println(updateCount);
                    //В случае удачного обновления параметров клиента возвращаемся на родительскую активность
                    if (updateCount == 1) {onBackPressed();}
                    if (updateCount == 0) {
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
                }
                //В случае если произошла ошибка в запросе на обновление
                catch(android.database.sqlite.SQLiteConstraintException e){
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
            }
        }
        //Закрываем подключение к БД
        dbHelper.close();
    }
}
