package com.example.dell.v_clock.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.v_clock.R;
import com.smartshino.face.SsDuck;

/**
 * This activity is the interface for the stuff to login which can jump to RegisterActivity.
 * 这个活动是工作人员登录的接口，能够跳转到注册界面。
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //“下一步”按钮
    private Button bt_next;
    //“注册”文字
    private TextView tv_sign_up;
    //手机号输入框
    private EditText et_phone;
    //顶部的LinearLayout
    private LinearLayout linear_top;
    //底部的RelativeLayout
    private RelativeLayout relative_bottom;
    //圆形logo
    private ImageView iv_logo;
    //图片标题
    private ImageView iv_title;
    //权限申请RequestCode
    private final int MY_PERMISSION_REQUEST_CAMERA = 1;
    //用户的手机号
    String phoneNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化控件
        initComponents();

        //TODO 测试SsDuck
        SsDuck ssDuck = new SsDuck();
        int test = ssDuck.SsMobiVersn(1, "w3434t4");
//        int test2 = ssDuck.Test();
        Log.i("Test", "test = " + test );

    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断是不是从注册界面跳转过来 获取intent中的手机号信息
        try {
            Intent register_intent = this.getIntent();
            phoneNum = register_intent.getStringExtra("etel");
            et_phone.setText(phoneNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化各控件
     */
    private void initComponents() {

        bt_next = (Button) findViewById(R.id.bt_next);
        tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
        et_phone = (EditText) findViewById(R.id.edit_text_phone);
        linear_top = (LinearLayout) findViewById(R.id.linear_top);
        relative_bottom = (RelativeLayout) findViewById(R.id.relative_bottom);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        iv_title = (ImageView) findViewById(R.id.iv_title);

        //获取屏幕分辨率
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
//        int screenHeight = display.getHeight();
        //计算logo、title合适的宽高
        int logo_length = screenWidth / 38 * 20;
//        int tile_height = logo_length / 572 * 165;
//        Log.i("Login",logo_length+" ");
        //设置logo、title的宽高  在布局文件中已设置adjustViewBounds=true 只设置一边即可
        iv_logo.setMaxWidth(logo_length);
        iv_logo.setMaxHeight(logo_length);
        iv_title.setMaxWidth(logo_length);
//        iv_title.setMaxHeight(tile_height);

        //为控件设置监听器
        bt_next.setOnClickListener(this);
        tv_sign_up.setOnClickListener(this);
    }

    /**
     * 实现OnClickListener接口的onClick监听方法
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //点击“下一步”按钮后的操作
            case R.id.bt_next:
                operateAfterNext();
                break;
            //点击“注册”文字后的操作
            case R.id.tv_sign_up:
                //通过Intent对象跳转到注册界面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                phoneNum = et_phone.getText().toString();
                intent.putExtra("etel", phoneNum);
                startActivity(intent);
                break;
            //默认操作
            default:
                break;
        }
    }

    /**
     * 点击“下一步”之后的一系列操作
     */
    private void operateAfterNext() {
        //检测手机号输入的长度是否合适
        //在布局设置中已经设置过只能输入数字，且最多11位，所以只需检测数字是否足够11位即可
        String phoneNumber = et_phone.getText().toString();
        int lengthOfPhone = phoneNumber.length();
        if (lengthOfPhone < 11) {
            Toast.makeText(LoginActivity.this, "请检查您的手机号是否输入正确！", Toast.LENGTH_SHORT).show();
            //TODO  方便测试 暂时注释掉下一行
//            return;
        }
        //TODO 向服务器查询输入手机号是否已注册

        //动态添加权限
        //Android 6.0 以上需要添加运行时权限 才可以使用Camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            //Camera权限未授予  申请Camera权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        } else {
            //Camera权限被授予
            //跳转到人脸识别界面
            Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Camera权限被授予
                    //跳转到人脸识别界面
                    Intent intent = new Intent(LoginActivity.this, CameraActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "权限不足，摄像头无法打开！", Toast.LENGTH_SHORT).show();
                    //TODO 跳转到权限设置界面 小米手机在该界面授予权限后会有问题 程序会崩掉
                    Context context = this.getApplicationContext();
                    Uri packageURI = Uri.parse("package:" + context.getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    startActivity(intent);
                }
        }

    }
}