package com.scott.idialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author ligl
 */
public class IDialog extends Dialog {

    public IDialog(Context context) {
        super(context, R.style.ios_dialog_style);
    }

    public static class Builder {
        private Context mContext;
        private IDialog mIDialog;
        private ISheetDialog.SheetItem mTitle;
        private CharSequence mMessage;
        private ISheetDialog.SheetItem mPositiveSt;
        private ISheetDialog.SheetItem mNegativeSt;
        private View mContentView;
        private OnClickListener mPositiveButtonClickListener;
        private OnClickListener mNegativeButtonClickListener;
        private boolean mCancelable = true;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(ISheetDialog.SheetItem title) {
            mTitle = title;
            return this;
        }

        public Builder setMessage(int messageId) {
            this.mMessage = mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        public Builder setPositiveButton(ISheetDialog.SheetItem positiveButton, OnClickListener listener) {
            mPositiveSt = positiveButton;
            this.mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(ISheetDialog.SheetItem negativeButton, OnClickListener listener) {
            mNegativeSt = negativeButton;
            this.mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.mContentView = contentView;
            return this;
        }

        public IDialog create() {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View dialogView = inflater.inflate(R.layout.ios_dialog, null);
            mIDialog = new IDialog(mContext);
            mIDialog.setCancelable(mCancelable);

            TextView tvTitle = dialogView.findViewById(R.id.title);
            TextView tvMessage = dialogView.findViewById(R.id.message);
            Button btnCancel = dialogView.findViewById(R.id.cancel_btn);
            Button btnConfirm = dialogView.findViewById(R.id.confirm_btn);
            View horizontal_line = dialogView.findViewById(R.id.horizontal_line);
            View vertical_line = dialogView.findViewById(R.id.vertical_line);
            View btns_panel = dialogView.findViewById(R.id.btns_panel);

            // 设置标题
            if (mTitle != null) {
                IUtils.setStyle(tvTitle, mTitle);
            }
            tvTitle.setVisibility(mTitle == null ? View.GONE : View.VISIBLE);

            // 设置内容区域
            if (mContentView != null) {
                // if no message set add the contentView to the dialog body
                LinearLayout rl = dialogView
                        .findViewById(R.id.message_layout);
                rl.removeAllViews();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rl.addView(mContentView, params);
            } else {
                tvMessage.setText(mMessage);
            }
            // 设置按钮区域
            if (mPositiveSt != null) {
                IUtils.setStyle(btnConfirm, mPositiveSt);
                btnConfirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mPositiveButtonClickListener != null) {
                            mPositiveButtonClickListener.onClick(mIDialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                        mIDialog.dismiss();
                    }
                });
                if (mNegativeSt == null) {
                    btnConfirm.setBackgroundResource(R.drawable.iossheet_bottom_btn_selector);
                }
            }
            btnConfirm.setVisibility(mPositiveSt == null ? View.GONE : View.VISIBLE);
            if (mNegativeSt != null) {
                IUtils.setStyle(btnCancel, mNegativeSt);
                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mNegativeButtonClickListener != null) {
                            mNegativeButtonClickListener.onClick(mIDialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                        mIDialog.dismiss();
                    }
                });
                if (mPositiveSt == null) {
                    btnConfirm.setBackgroundResource(R.drawable.iossheet_bottom_btn_selector);
                }
            }
            btnCancel.setVisibility(mNegativeSt == null ? View.GONE : View.VISIBLE);
            int visible = (mNegativeSt == null && mPositiveSt == null) ? View.GONE : View.VISIBLE;
            btns_panel.setVisibility(visible);
            vertical_line.setVisibility(visible);
            horizontal_line.setVisibility(visible);

            // 调整一下dialog的高度，如果高度充满屏幕不好看
            // 计算一下Dialog的高度,如果超过屏幕的4/5，则最大高度限制在4/5
            // 1.计算dialog的高度
            // TODO 测试发现的问题：如果放入一大串没有换行的文本到message区域，会导致测量出来的高度偏小，从而导致实际显示出来dialog充满了整个屏幕
            dialogView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int dialogHeight = dialogView.getMeasuredHeight();
            // 2.得到屏幕高度
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            int maxHeight = (int) (metrics.heightPixels * 0.8);
            ViewGroup.LayoutParams dialogParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // 3.如果高度超限，限制dialog的高度
            if (dialogHeight >= maxHeight) {
                dialogParams.height = maxHeight;
            }
            mIDialog.setContentView(dialogView, dialogParams);

            return mIDialog;
        }

        public IDialog show() {
            mIDialog = create();
            mIDialog.show();
            return mIDialog;
        }
    }
}
