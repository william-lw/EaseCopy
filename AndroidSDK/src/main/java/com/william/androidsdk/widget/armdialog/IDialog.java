package com.william.androidsdk.widget.armdialog;

import android.view.View;

public interface IDialog {

    void dismiss();

    interface OnBuildListener {
        void onBuildChildView(IDialog dialog, View view, int layoutRes);
    }

    interface OnClickListener {
        void onClick(IDialog dialog);
    }

    interface EditTextOkBtnClickListener{
        void onClick(IDialog dialog, String [] texts);
    }


    interface OnDismissListener {
        /**
         * This method will be invoked when the dialog is dismissed.
         *
         * @param dialog the dialog that was dismissed will be passed into the
         *               method
         */
        void onDismiss(IDialog dialog);
    }
}
