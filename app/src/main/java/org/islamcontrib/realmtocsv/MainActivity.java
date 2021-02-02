package org.islamcontrib.realmtocsv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Gson gson;
    private Class[] classes;
    private RealmConfiguration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!new File(getFilesDir() + "/files/default0").exists())
            copyBundledRealmFile(this.getResources().openRawResource(R.raw.default0), "default0");

        //REALM SETUP
        Realm.init(this);

        //List of classes to export
        classes= new Class[]{/* classes to convert; Myclass1.class, Myclass2.class,...*/};
    }

    private void handleClass(Class aClass, Realm realm) throws IOException, JSONException {
        FileWriter outFile = new FileWriter(getFilesDir()+"realmTransformed_"+aClass.getName()+".csv");
        PrintWriter out = new PrintWriter(outFile);

        RealmResults<RealmObject> all = realm.where(aClass).findAll();

        out.println(((RealmObjectExportable) all.get(0)).getSchema());

        for (RealmObject realmObjectExportable : all) {
            out.println(((RealmObjectExportable) realmObjectExportable).toRow());
        }
         out.flush();
         out.close();
    }

    public void transform(View view) {
        TextInputEditText viewById = findViewById(R.id.ver);

        String s = viewById.getText().toString();
        if(s.isEmpty())
            config = new RealmConfiguration.Builder()
                    .name("default0")
                    .build();
        else {
            Toast.makeText(this, "using vesion: "+ s, Toast.LENGTH_SHORT).show();
            config = new RealmConfiguration.Builder()
                    .name("default0")
                    .schemaVersion(Integer.parseInt(s))
                    .build();
        }

        try (Realm realm = Realm.getInstance(config)) {
            for (Class aClass : classes) {
                handleClass(aClass, realm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName); //referes to /data/data/appPackaName/files
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}