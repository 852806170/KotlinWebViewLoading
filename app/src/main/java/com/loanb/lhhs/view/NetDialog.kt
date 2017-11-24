package com.loanb.lhhs.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import com.loanb.lhhs.loanbport.MainActivity
import com.loanb.lhhs.loanbport.R


class NetDialog : Dialog {
    lateinit var mView: View
    lateinit var mListView: ListView
    lateinit var mEitText: EditText
    lateinit var mContent: Context
    lateinit var listener: MainActivity.IListener

    public constructor(context: Context) : this(context, R.style.Theme_Dialog)


    public constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        mContent = context
        init()
    }

    fun setClck(listener2: MainActivity.IListener) {
        listener = listener2
    }

    /**
     * 初始化布局
     */
    fun init() {
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        createProgressDialog()
    }

    /**
     * 创建dialog布局
     */
    fun createProgressDialog() {
        val window = window
        val params = window.attributes
        params.dimAmount = 0f
        window.attributes = params
        val factory = LayoutInflater.from(mContent)
        setOnCancelListener(DialogInterface.OnCancelListener {

        })
        mView = factory.inflate(R.layout.progress_dialog, null)
        mListView = mView.findViewById(R.id.listview)
        mEitText = mView.findViewById(R.id.mEitText)
        mListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            listener.onClickItem(mListView.adapter.getItem(i) as String)
        }
        window.setContentView(mView)
    }


    /**
     * 变更url列表数据
     */
    fun updateUrlListDate(arrayAdapter: ArrayAdapter<String>) {
        mListView.adapter = arrayAdapter
    }

    /**
     * close
     */
    fun disMiss() {
        if (isShowing)
            dismiss()
    }
}