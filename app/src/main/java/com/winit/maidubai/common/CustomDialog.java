package com.winit.maidubai.common;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.winit.maidubai.BaseActivity;
import com.winit.maidubai.R;

import java.lang.ref.WeakReference;

/** class to create the Custom Dialog **/
public class CustomDialog extends Dialog
{
    //initializations
    boolean isCancellable = true;
    /**
     * Constructor
     * @param context
     *
     *
     *
     * @param view
     */
    private WeakReference<BaseActivity> baseActivity;

    public CustomDialog(Context context, View view)
    {
        super(context, R.style.Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        if(context instanceof BaseActivity)
            this.baseActivity= new WeakReference<BaseActivity >((BaseActivity) context);
    }
    /**
     * Constructor
     * @param context
     * @param view
     * @param lpW
     * @param lpH
     */
    public CustomDialog(Context context,View view, int lpW, int lpH)
    {
        this(context, view, lpW, lpH, true);
        if(context instanceof BaseActivity)
            this.baseActivity= new WeakReference<BaseActivity >((BaseActivity) context);
    }
    /**
     * Constructor
     * @param context
     * @param view
     * @param lpW
     * @param lpH
     * @param isCancellable
     */
    public CustomDialog(Context context,View view, int lpW, int lpH, boolean isCancellable)
    {
        super(context, R.style.Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view, new ViewGroup.LayoutParams(lpW, lpH));
        this.isCancellable = isCancellable;
        if(context instanceof BaseActivity)
            this.baseActivity= new WeakReference<BaseActivity >((BaseActivity) context);
    }

    public CustomDialog(Context context, View view, boolean isTrans)
    {
        super(context,R.style.Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        this.baseActivity= new WeakReference<BaseActivity >((BaseActivity) context);
    }

    public CustomDialog(Context context, View view, int lpW, int lpH, boolean isCancellable, int style)
    {
        super(context, R.style.Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view, new ViewGroup.LayoutParams(lpW, lpH));
        this.isCancellable = isCancellable;
        if(context instanceof BaseActivity)
            this.baseActivity= new WeakReference<BaseActivity >((BaseActivity) context);
    }

    @Override
    public void onBackPressed()
    {
        if(isCancellable)
            super.onBackPressed();
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel)
    {
        super.setCanceledOnTouchOutside(cancel);
    }

    public void showCustomDialog(){
        try {
            if(baseActivity != null && baseActivity.get()!=null && !baseActivity.get().isFinishing())
                show();
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException("This can never happen", e);
        }
    }
}