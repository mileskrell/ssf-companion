package org.cpsscifair.ssfcompanion;

class ChecklistItem {

    private boolean checked;
    private String text;

    ChecklistItem(boolean checked, String text) {
        this.checked = checked;
        this.text = text;
    }

    void setChecked(boolean checked) {
        this.checked = checked;
    }

    void setText(String text) {
        this.text = text;
    }

    boolean isChecked() {
        return checked;
    }

    String getText() {
        return text;
    }
}
