package com.example.rentaloftools;

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
 * Программный класс для работы с Таблицей "Instruments"
 */
public class InstrumentActivity extends MainActivity implements View.OnClickListener {
    //Кнопки для работы с Таблицей "Instruments" в БД
    Button btnAdd, btnEdit, btnDel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument);
        setTitle("Инструменты");
        TabHost tabHost = (TabHost) findViewById(R.id.tabHostInstrument);
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
                    //Чтение данных из Таблицы: "Instruments" для БД
                    initTableInstrument(dbHelper);
                }
            }});
        ContentValues cv = new ContentValues();
        //Обработка кнопок
        btnAdd = (Button) findViewById(R.id.addinstruments);
        btnAdd.setOnClickListener(this);
        btnEdit = (Button) findViewById(R.id.editinstruments);
        btnEdit.setOnClickListener(this);
        btnDel = (Button) findViewById(R.id.delinstruments);
        btnDel.setOnClickListener(this);
        //Чтение данных из Таблицы: "Instruments" для БД
        initTableInstrument(dbHelper);
    }

    /**
     * Чтение данных из Таблицы: "Instruments" для БД
     * @param dbHelper
     */
    public void initTableInstrument(DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Список инструментов
        ArrayList<HashMap<String, Object>> instruments = new ArrayList<HashMap<String, Object>>();
        //Список параметров каждого инструмента
        HashMap<String, Object> instrument;
        //Отправляем запрос в БД для Таблицы: "Instruments"
        Cursor cursor = db.rawQuery("SELECT * FROM Instruments", null);
        cursor.moveToFirst();
        //Цикл по все инструментам
        while (!cursor.isAfterLast()) {
            instrument = new HashMap<String, Object>();
            //Заполняем инструмент для отображения
            instrument.put("id", "id: " + cursor.getString(0));
            instrument.put("name", "name: " + cursor.getString(1));
            instrument.put("rentalFees", "rentalFees: " + cursor.getString(2));
            instrument.put("rentStatus", "rentStatus: " + cursor.getString(3));
            //Добавляем инструмент в список
            instruments.add(instrument);
            //Переходим к следующему
            cursor.moveToNext();
        }
        cursor.close();
        //Параметры инструмента, которые будем отображать в соответствующих
        //элементах из разметки adapter.xml
        String[] from = {"id", "name", "rentalFees", "rentStatus"};
        int[] to = {R.id.textId, R.id.textName, R.id.textRentalFees, R.id.textRentStatus};
        //Создаем адаптер для работы с ListView
        SimpleAdapter adapter = new SimpleAdapter(this, instruments, R.layout.adapter_instrument, from, to);
        ListView listView = (ListView) findViewById(R.id.listViewInstrument);
        listView.setAdapter(adapter);
        dbHelper.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Добавление инструмента
            case R.id.addinstruments:
                Intent addInstrument = new Intent(this, AddInstrument.class);
                //Запуск activity AddInstrument
                startActivity(addInstrument);
                break;
            //Изменение инструмента
            case R.id.editinstruments:
                Intent editInstrument = new Intent(this, EditInstrument.class);
                //Запуск activity EditInstrument
                startActivity(editInstrument);
                break;
            //Удаление инструмента
            case R.id.delinstruments:
                Intent delInstrument = new Intent(this, DelInstrument.class);
                //Запуск activity DelInstrument
                startActivity(delInstrument);
                break;
        }
    }
}