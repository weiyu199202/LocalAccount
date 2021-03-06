package com.spencer.localaccount;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.spencer.localaccount.db.accountdb.AccountBean;
import com.spencer.localaccount.db.accountdb.AccountDBManager;
import com.spencer.localaccount.utils.CommonUtils;
import com.spencer.localaccount.utils.GV;

//添加张账户
public class AddAccountView extends Activity {


    private AccountDBManager accountDBManager;
    private EditText etDes;
    private EditText etAcount;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etRemark;
    private TextView tvTitle;

    private Button btnAdd;
    private ImageButton ibBack;
    private boolean isUpdate = false;
    private AccountBean accountBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_view);
        ((TextView) findViewById(R.id.tv_title)).setText("新增账户");
        accountBean = (AccountBean) getIntent().getSerializableExtra("data");
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        if (accountBean == null) {
            accountBean = new AccountBean();
        }
        if (accountDBManager == null) {
            accountDBManager = new AccountDBManager(this);

        }

        initView();

    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        etDes = findViewById(R.id.et_des);
        etAcount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etRemark = findViewById(R.id.et_remark);

        btnAdd = findViewById(R.id.btn_add);

        if (isUpdate) {
            tvTitle.setText("修改账户");
            etDes.setText(accountBean.getDescription());
            etAcount.setText(accountBean.getAccount());
            etPassword.setText(accountBean.getPassword());
            etEmail.setText(accountBean.getEmail());
            etPhone.setText(accountBean.getPhone());
            etRemark.setText(accountBean.getRemark());
        }
        btnAdd.setText("提 交");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealData();

            }
        });

        final CheckBox cbPsw = findViewById(R.id.cb_psw);
        cbPsw.setChecked(false);


        if (cbPsw.isChecked()) {
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        cbPsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (cbPsw.isChecked()) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void dealData() {


        String des = String.valueOf(etDes.getText());
        String account = String.valueOf(etAcount.getText());
        String password = String.valueOf(etPassword.getText());
        String email = String.valueOf(etEmail.getText());
        String phone = String.valueOf(etPhone.getText());
        String remark = String.valueOf(etRemark.getText());

        if (TextUtils.isEmpty(des) || TextUtils.isEmpty(password)) {
            CommonUtils.showToast(this, "账户描述，密码为必填项");
            return;
        }

        if (TextUtils.isEmpty(account) && TextUtils.isEmpty(email)) {
            CommonUtils.showToast(this, "账户和邮箱至少填一个");
            return;
        }

        if (!TextUtils.isEmpty(email) && !CommonUtils.checkEmaile(email)) {
            CommonUtils.showToast(this, "邮箱格式不正确");
            return;
        }

        accountBean.setAccount(account);
        accountBean.setDescription(des);
        accountBean.setPhone(phone);
        accountBean.setEmail(email);
        accountBean.setUserName(GV.getUserName(this));
        accountBean.setRemark(remark);
        accountBean.setPassword(password);


        if (isUpdate) {
            accountDBManager.update(accountBean);
        } else {
            accountDBManager.insert(accountBean);
        }
        CommonUtils.showToast(AddAccountView.this, "操作成功");
        setResult(RESULT_OK);
        finish();
    }


}
