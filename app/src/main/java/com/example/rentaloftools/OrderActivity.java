package com.example.rentaloftools;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Программный класс для работы с Таблицей "Orders"
 */
public class OrderActivity extends MainActivity implements View.OnClickListener {
    //Кнопки для работы с Таблицей "Orders" в БД
    Button btnAdd, btnEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle("Заказы");
        TabHost tabHost = (TabHost) findViewById(R.id.tabHostOrder);
        tabHost.setup();
        //Формирование вкладки с таблицей данных
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("tag1");
        tabSpec1.setContent(R.id.linearLayout1);
        tabSpec1.setIndicator("Таблица");
        tabHost.addTab(tabSpec1);
        //Формирование вкладки с настройками
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tag2");
        tabSpec2.setContent(R.id.linearLayout2);
        tabSpec2.setIndicator("Настройки");
        tabHost.addTab(tabSpec2);
        //Установка текущей вкладки
        tabHost.setCurrentTab(0);
        //Обработка выбора вкладки таблицы
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                //Если выбрана вкладка "Таблица"
                if("tag1".equals(tabId)) {
                    //Чтение данных из Таблицы: "Orders" для БД
                    initTableInstrument(dbHelper);
                }
            }});
        ContentValues cv = new ContentValues();
        //Обработка кнопок
        btnAdd = (Button) findViewById(R.id.addorder);
        btnAdd.setOnClickListener(this);
        btnEdit = (Button) findViewById(R.id.editorder);
        btnEdit.setOnClickListener(this);
        //Чтение данных из Таблицы: "Orders" для БД
        initTableInstrument(dbHelper);
    }

    /**
     * Чтение данных из Таблицы: "Orders" для БД
     * @param dbHelper
     */
    @SuppressLint("Range")
    public void initTableInstrument(DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Список заказов
        ArrayList<HashMap<String, Object>> orders = new ArrayList<HashMap<String, Object>>();
        //Список параметров каждого заказа
        HashMap<String, Object> order;
        //Отправляем запрос в БД для Таблицы: "Orders"
        Cursor cursor = db.rawQuery("SELECT * FROM Orders", null);
        cursor.moveToFirst();
        //Цикл по все заказам
        while (!cursor.isAfterLast()) {
            order = new HashMap<String, Object>();
            //Заполняем заказ для отображения
            order.put("id", "id: " + cursor.getString(cursor.getColumnIndex("id")));
            order.put("money", "money: " + cursor.getString(cursor.getColumnIndex("money")));
            order.put("startdate", "startdate: " + cursor.getString(cursor.getColumnIndex("startdate")));
            order.put("time", "time: " + cursor.getString(cursor.getColumnIndex("time")));
            order.put("idClient", "idClient: " + cursor.getString(cursor.getColumnIndex("idClient")));
            order.put("idInstruments", "idInstruments: " + cursor.getString(cursor.getColumnIndex("idInstruments")));
            order.put("status", "status: " + cursor.getString(cursor.getColumnIndex("status")));
            //Добавляем заказ в список
            orders.add(order);
            //Переходим к следующему
            cursor.moveToNext();
        }
        cursor.close();
        //Параметры инструмента, которые будем отображать в соответствующих
        //элементах из разметки adapter_order.xml
        String[] from = {"id", "money", "startdate", "time", "idClient", "idInstruments", "status"};
        int[] to = {R.id.textId, R.id.textMoney, R.id.textStartdate, R.id.textTime, R.id.textIdClient, R.id.textIdInstruments, R.id.textStatus};
        //Создаем адаптер для работы с ListView
        SimpleAdapter adapter = new SimpleAdapter(this, orders, R.layout.adapter_order, from, to);
        ListView listView = (ListView) findViewById(R.id.listViewOrder);
        listView.setAdapter(adapter);
        dbHelper.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Добавление заказа
            case R.id.addorder:
                Intent addOrder = new Intent(this, AddOrder.class);
                //Запуск activity AddOrder
                startActivity(addOrder);
                break;
            //Изменение заказа
            case R.id.editorder:
                Intent editOrder = new Intent(this, EditOrder.class);
                //Запуск activity EditOrder
                startActivity(editOrder);
                break;
        }
    }
}