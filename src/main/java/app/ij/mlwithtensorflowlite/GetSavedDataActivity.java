package app.ij.mlwithtensorflowlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GetSavedDataActivity extends AppCompatActivity {

    TextView test_ct_back;
    String date, classImg;
    byte[] image;
    int id;
    private ListView listView;
    private ArrayList<UserModel> userModelArrayList;
    private CustomAdapter customAdapter;
    private Database dbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_saved_data);

        Intent intent = getIntent();
        String getPassword = intent.getStringExtra("password");

        listView = (ListView) findViewById(R.id.lv);
        test_ct_back = (TextView) findViewById(R.id.test_ct_back);

        test_ct_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dbase = new Database(this);

        userModelArrayList = new ArrayList<>();
        customAdapter = new CustomAdapter(this, R.layout.custom_list_layout, userModelArrayList);

        listView.setAdapter(customAdapter);

        Cursor data = dbase.getData(getPassword);

        userModelArrayList.clear();
        while (data.moveToNext()) {

            id = data.getInt(0);
            image = data.getBlob(1);
            classImg = data.getString(2);
            date = data.getString(3);

            userModelArrayList.add(new UserModel(id, image, classImg, date));
        }
        data.close(); //<<<<<<<<<< SHOULD ALWAYS CLOSE CURSOR WHEN DONE WITH IT
        customAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GetSavedDataActivity.this, SelectedDataActivity.class);
                intent.putExtra("user", userModelArrayList.get(position));
                intent.putExtra("password", getPassword);
                startActivity(intent);
            }
        });

    }
}