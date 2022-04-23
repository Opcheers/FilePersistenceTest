package com.example.filepersistencetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdit = this.findViewById(R.id.edit);

        String content = load();
        if (!TextUtils.isEmpty(content)) {//TextUtils.isEmpty(content)即content==null||content.length==0
            mEdit.setText(content);
            mEdit.setSelection(content.length());//将光标移至内容尾部以便继续输入
            Toast.makeText(this, "Restoring succeeded.", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 从文件中读取数据
     * @return
     */
    private String load() {
        FileInputStream input = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            input = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while ((line = reader.readLine())!=null){
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String input = mEdit.getText().toString().trim();
        save(input);//销毁之前保存数据
    }

    /**
     * 将数据存储到文件中
     * @param input
     */
    private void save(String input) {
        FileOutputStream out = null;//文件输出流，用于将数据写入File或FileDescriptor的输出流
        BufferedWriter writer = null;//字符缓冲流
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            //第一个参数是文件名，不可以包含路径，默认存储到/data/data/<package name>/files/目录下
            //第二个参数是文件操作模式：Context.MODE_PRIVATE每次覆盖原文件； MODE_APPEND每次追加到原文件内容后面
            //返回一个FileOutputStream对象
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}