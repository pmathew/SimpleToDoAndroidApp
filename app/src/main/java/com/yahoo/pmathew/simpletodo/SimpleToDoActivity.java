package com.yahoo.pmathew.simpletodo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class SimpleToDoActivity extends Activity {

    private ArrayList<String> todoItems;
    private ArrayAdapter<String> todoAdapter; // convert from the storage model to view model
    private ListView lvItems;
    private EditText etEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readItems();
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        // this could be getBaseContext() as well.
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(todoAdapter);
        setupListViewListener();
    }

    public void addTodoItem(View v) {
        //egt handle
        etEditText = (EditText) findViewById(R.id.etNewItem);
        String newItem = etEditText.getText().toString();
        //Add it to adapter
        //Check for empty clicks on Add button
        if (newItem != null && newItem.length() > 1) {
            todoAdapter.add(newItem);
            saveItems();
            //Reset new mytodo item
            etEditText.setText("");
        }
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int position, long id) {
                todoItems.remove(position);
                saveItems();
                todoAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    // To read the saved todo items
    private void readItems() {
        File fileDir = getFilesDir();
        System.out.println(fileDir);
        File todoFile = new File(fileDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            //usecase: File not found
            todoItems = new ArrayList<String>();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //To save the tdo items
    private void saveItems() {
        File fileDir = getFilesDir();
        System.out.println(fileDir);
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, todoItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
