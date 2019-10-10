package com.william.androidsdk.widget.armdialog.manager;


import com.william.androidsdk.widget.armdialog.ArmDialog;

public class DialogWrapper {

    private ArmDialog.Builder dialog;//统一管理dialog的弹出顺序

    public DialogWrapper(ArmDialog.Builder dialog) {
        this.dialog = dialog;
    }

    public ArmDialog.Builder getDialog() {
        return dialog;
    }

    public void setDialog(ArmDialog.Builder dialog) {
        this.dialog = dialog;
    }

}
