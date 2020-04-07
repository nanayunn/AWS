package com.example.p502;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    ListView listView;
    LinearLayout container;
    ArrayList<Item> list;
    ItemAdapter itemAdapter;
    ProgressDialog progressDialog;

    public static final int REQUEST_CODE_THIRD = 101;
    public static final String KEY_LOCATION = "loc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        listView = findViewById(R.id.listView);
        container = findViewById(R.id.container);
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        getData();
    }

    private void getData() {
        String url = "http://70.12.113.248/webview/cafe.jsp";
        ItemAsync itemAsync = new ItemAsync(url);
        itemAsync.execute();
    }


    class ItemAsync extends AsyncTask<Void,Void,String>{
        String url;

        public ItemAsync(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("HTTP Connect ..");
            progressDialog.setMessage("Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            result = HttpHandler.getString(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("---",s.trim());

            progressDialog.dismiss();
            JSONArray ja = null;
            try {
                ja = new JSONArray(s);
                for(int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    String name = jo.getString("name");
                    String description = jo.getString("description");
                    String img = jo.getString("image");
                    int  star = jo.getInt("star");
                    double leti  = jo.getDouble("leti");
                    double longti  = jo.getDouble("longti");
                    Item item = new Item(name,img,star,description,leti,longti);
                    list.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemAdapter = new ItemAdapter(list);
            listView.setAdapter(itemAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent =
//                            new Intent(getApplicationContext(),
//                                    ThirdActivity.class);

                    Intent intent =
                            new Intent(getApplicationContext(),
                                    MapsActivity.class);
                    intent.putExtra("loc", (Serializable) list.get(position));

                    startActivity(intent);
                }
            });



        }
    }




    class ItemAdapter extends BaseAdapter{
        ArrayList<Item> alist;

        public ItemAdapter(ArrayList<Item> alist){
            this.alist = alist;
        }

        @Override
        public int getCount() {
            return alist.size();
        }

        @Override
        public Object getItem(int position) {
            return alist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = null;
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.list_layout,container,true);
            TextView description = itemView.findViewById(R.id.textView2);
            TextView name = itemView.findViewById(R.id.textView);
            RatingBar ratingBar = itemView.findViewById(R.id.ratingBar);
            final ImageView imageView = itemView.findViewById(R.id.imageView);


            name.setText(alist.get(position).getName());
            description.setText(alist.get(position).getDescription());
            ratingBar.setRating(alist.get(position).getStar());
            String img = alist.get(position).getImage();

            img = "http://70.12.113.248/webview/img/"+img;
            final String finalImg = img;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url = null;
                    InputStream is = null;
                    try{
                        url = new URL(finalImg);
                        is = url.openStream();
                        final Bitmap bm = BitmapFactory.decodeStream(is);
                        //바이트로 들어온 정보를 한번에 번들화 해서 이미지로 출력해주는 함수
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bm);
                            }
                        });


                    }
                    catch (Exception e){

                    }
                }
            });
            t.start();

            return itemView;
        }
    }

}
