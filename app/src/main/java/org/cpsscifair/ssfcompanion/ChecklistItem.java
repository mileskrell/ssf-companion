package org.cpsscifair.ssfcompanion;

class ChecklistItem {

    private boolean checked;
    private String text;

    ChecklistItem(String text) {
        this.checked = false;
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
