package com.scott.idialog;

import android.widget.TextView;

/**
 * @author scott
 * @version 1.0
 */
public class IUtils {
    public static void setStyle(TextView textView, ISheetDialog.SheetItem sheetItem) {
        if (sheetItem != null && textView != null) {
            if (sheetItem.name != null) {
                textView.setText(sheetItem.name);
            }
            if (sheetItem.color != -1) {
                textView.setTextColor(sheetItem.color);
            }
            if (sheetItem.textSize != -1) {
                textView.setTextSize(sheetItem.textSize);
            }

        }
    }
}
