package com.scott.idialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @author ligl
 */
public class ISheetDialog extends Dialog implements DialogInterface {

    public ISheetDialog(Context context) {
        super(context, R.style.ios_sheet_style);
    }

    public static class Builder {
        private ISheetDialog mISheetDialog;
        private Context mContext;
        private SheetItem mTitle;
        private SheetItem mCancel;
        private SheetItem[] mItems;
        private OnClickListener mOnClickListener;

        public Builder(Context context) {
            this.mContext = context;

            this.mCancel = new SheetItem(context.getString(R.string.iossheet_cancel),
                    context.getResources().getColor(R.color.ios_blue));
        }

        public Builder setTitle(SheetItem title) {
            this.mTitle = title;
            return this;
        }

        public Builder setCancel(SheetItem sheetItem) {
            this.mCancel = sheetItem;
            return this;
        }

        public Builder setData(SheetItem[] items, OnClickListener listener) {
            this.mItems = items;
            this.mOnClickListener = listener;
            return this;
        }

        public Builder setData(List<SheetItem> items, OnClickListener listener) {
            mItems = new SheetItem[mItems.length];
            items.toArray(mItems);
            this.mOnClickListener = listener;
            return this;
        }

        public ISheetDialog create() {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View sheetView = inflater.inflate(R.layout.ios_sheet_dialog, null);
            mISheetDialog = new ISheetDialog(mContext);

            TextView tvTitle = sheetView.findViewById(R.id.title);
            IUtils.setStyle(tvTitle, mTitle);

            LinearLayout message_layout = sheetView.findViewById(R.id.message_layout);
            Button btn_cancel = sheetView.findViewById(R.id.btn_cancel);

            // 设置标题
            tvTitle.setText(mTitle.name);
            tvTitle.setTextColor(mTitle.color);
            // 填充列表内容
            for (int i = 0, len = mItems.length; i < len; i++) {

                View itemView = inflater.inflate(R.layout.ios_sheet_item, message_layout, false);
                Button btnItem = itemView.findViewById(R.id.btn_item);
                IUtils.setStyle(btnItem, mItems[i]);
                if (i == mItems.length - 1) {
                    btnItem.setBackgroundResource(R.drawable.iossheet_bottom_btn_selector);
                }
                final int itemIndex = i;
                btnItem.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mOnClickListener != null) {
                            mOnClickListener.onClick(mISheetDialog, itemIndex);
                        }
                        mISheetDialog.dismiss();
                    }
                });
                message_layout.addView(itemView);
            }
            IUtils.setStyle(btn_cancel, mCancel);
            if (mCancel.textSize != -1) {
                btn_cancel.setTextSize(mCancel.textSize);
            }
            // 设置取消事件
            btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mISheetDialog.dismiss();
                }
            });

            // 获取屏幕高度
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            // 设置宽度全屏，底部弹出
            Window window = mISheetDialog.getWindow();
            window.setWindowAnimations(R.style.ios_sheet_anim);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams wml = window.getAttributes();
            wml.width = metrics.widthPixels;
            wml.gravity = Gravity.BOTTOM;
            wml.y = 0;
            window.setAttributes(wml);
            sheetView.setMinimumWidth(metrics.widthPixels);

            // 设置dialog的高度不能超过屏幕的0.7
            LayoutParams vgl = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            int maxHeight = (int) (metrics.heightPixels * 0.7);
            // 测量dialog的高度
            sheetView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            int dialogMeasureHeight = sheetView.getMeasuredHeight();
            if (dialogMeasureHeight > maxHeight) {
                vgl.height = maxHeight;
            }
            mISheetDialog.setContentView(sheetView, vgl);
            return mISheetDialog;
        }

        public ISheetDialog show() {
            mISheetDialog = create();
            mISheetDialog.show();
            return mISheetDialog;
        }
    }

    public static final class SheetItem {
        public static final int RED = 0xFFFD4A2E;
        public static final int BLUE = 0xFF037BFF;
        //public static final int GREY = Color.parseColor("#FFFD4A2E");

        public String name;
        public int color = -1;
        public float textSize = -1;

        public SheetItem() {
        }

        public SheetItem(String name) {
            this.name = name;
        }

        public SheetItem(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public SheetItem(String name, int color, float textSize) {
            this.name = name;
            this.color = color;
            this.textSize = textSize;
        }

    }
}
