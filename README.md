# 数据持久化存储

三大存储方式：文件存储，SharedPreference存储，数据库存储



### 文件存储

#### **1.将数据存储到文件中**

​	Context类提供了openFileOutput()方法，可用于将数据存储到指定文件中

```Java
public class MainActivity extends AppCompatActivity {

    private EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdit = this.findViewById(R.id.edit);
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
```

#### **2.从文件中读取数据**

​	Context类提供了openFileInput()方法，可用于将数据存储到指定文件中

```Java
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
            input = openFileInput("data");//一个参数：文件名
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
}
```





### SharedPreferences存储

​	SharedPreferences使用键值对的方式来存储的

#### 1.将数据存储到SharedPreferences文件中

**1）Context类中的getSharedPreferences()方法**

```Java
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button saveData = this.findViewById(R.id.save_data);
        Button restoreData = this.findViewById(R.id.restore_data);

        //保存数据
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                //getSharedPreferences第一个参数是文件名，第二个参数是文件模式，只有MODE_PRIVATE，表示至于当前程序才可以对这个文件进行读写
                editor.putString("name", "yxq");
                editor.putInt("age", 23);
                editor.putBoolean("married", false);
                editor.apply();//提交数据
            }
        });

        //读取数据
        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String name = pref.getString("name", null);
                int age = pref.getInt("age", 0);
                boolean married = pref.getBoolean("married", false);
                Log.d(TAG, "name --> " + name + ", age --> " + age + ", married --> " + married);
            }
        });
    }
}
```

**2）Activity类中的getPreferences()方法**

只接收一个操作模式参数，使用这个方法会自动将当前活动的类名作为SharedPreferences的文件名

**3）PreferenceManager类中的getDefaultSharedPreferences()方法**

这是一个静态方法，只接受Context参数，自动将当前应用程序的包名作为前缀来命名SharedPreferences文件

```Java
SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//写数据
SharedPreferences.Editor editor = pref.edit();
editor.putString("account", account);
editor.putString("password", password);
editor.putBoolean("remember", true);
editor.apply();

//读数据
String account = pref.getString("account", ""));
int age = pref.getInt("age", 0);
boolean married = pref.getBoolean("married", false);
```

#### 2.从SharedPreferences 中读数据

```Java
SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
String name = pref.getString("name", null);
int age = pref.getInt("age", 0);
boolean married = pref.getBoolean("married", false);
```

