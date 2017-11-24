package com.loanb.lhhs.loanbport

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Toast
import com.loanb.lhhs.utils.SPUtils
import com.loanb.lhhs.view.NetDialog


class MainActivity : Activity() {

    private lateinit var mWebView: WebView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var dialog: NetDialog
    private var tagint = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mWebView = findViewById(R.id.webview)
        initWebView()
        initDialog()
    }

    /**
     * 屏蔽dialog返回键
     */
    private var keylistener: DialogInterface.OnKeyListener = object : DialogInterface.OnKeyListener {
        override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
            return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
        }
    }

    /**
     * 初始化dialog
     */
    private fun initDialog() {
        dialog = NetDialog(this)
        dialog.updateUrlListDate(getAdapterDate())
        dialog.setClck(listenersOut)
        dialog.setOnKeyListener(keylistener)
        dialog.show()
    }

    /**
     * 历史url列表选择
     */
    private val listenersOut = object : IListener {
        override fun onClickItem(str: String) {
            loadindUrlWebView(str)
            dialog.disMiss()
        }
    }

    /**
     * 初始化webview
     */
    private fun initWebView() {
        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false// 返回false
            }
        }

    }

    /**
     * 加载url
     */
    private fun loadindUrlWebView(str: String) {
        mWebView.loadUrl(str)
    }

    /**
     * 提交加载
     */
    fun commit(view: View) {
        if (dialog.mEitText.text.toString().equals("")) {
            Toast.makeText(this, "请输入地址url", Toast.LENGTH_SHORT).show()
            return
        }
        if (arrayAdapter.count != 4) {
            if (!repeatWithoutAdding("http://" + dialog.mEitText.text.toString())) {
                SPUtils.put(this, (arrayAdapter.count + 1).toString(), "http://" + dialog.mEitText.text.toString())
                dialog.updateUrlListDate(getAdapterDate())
            }

        } else {
            if (!repeatWithoutAdding("http://" + dialog.mEitText.text.toString())) {
                if (tagint == 5) tagint = 1
                SPUtils.put(this, tagint.toString(), "http://" + dialog.mEitText.text.toString())
                dialog.updateUrlListDate(getAdapterDate())
                tagint++
            }

        }
        loadindUrlWebView("http://" + dialog.mEitText.text.toString())
        dialog.disMiss()

    }


    /**
     * 适配数据
     */
    fun getAdapterDate(): ArrayAdapter<String> {
        arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1)
        if (SPUtils.contains(this, "1")) {
            arrayAdapter.add(SPUtils.get(this, "1", "") as String)
        }
        if (SPUtils.contains(this, "2")) {
            arrayAdapter.add(SPUtils.get(this, "2", "") as String)
        }
        if (SPUtils.contains(this, "3")) {
            arrayAdapter.add(SPUtils.get(this, "3", "") as String)
        }
        if (SPUtils.contains(this, "4")) {
            arrayAdapter.add(SPUtils.get(this, "4", "") as String)
        }

        return arrayAdapter
    }

    /**
     * 重复不添加
     */
    private fun repeatWithoutAdding(str: String): Boolean {
        var i = 0
        while (i < arrayAdapter.count) {
            if (arrayAdapter.getItem(i).equals(str)) {
                return true
            }
            i++
        }
        return false
    }

    /**
     * 定义出历史列表的listener
     */
    interface IListener {
        fun onClickItem(str: String)
    }

}