package fr.zabricraft.delta.api;

import fr.zabricraft.delta.R;

public enum APISyncStatus {

    local, synchro, checkingforupdate, downloading, uploading, failed;

    public int text() {
        switch (this) {
            case local:
                return R.string.status_local;
            case synchro:
                return R.string.status_synchro;
            case checkingforupdate:
                return R.string.status_checkingforupdate;
            case downloading:
                return R.string.status_downloading;
            case uploading:
                return R.string.status_uploading;
            case failed:
                return R.string.status_failed;
        }
        return 0;
    }

    public int colorForText() {
        switch (this) {
            case synchro:
                return android.R.color.holo_green_dark;
            case checkingforupdate:
                return android.R.color.holo_orange_dark;
            case downloading:
                return android.R.color.holo_orange_dark;
            case uploading:
                return android.R.color.holo_orange_dark;
            case failed:
                return android.R.color.holo_red_dark;
            default:
                return android.R.color.primary_text_light;
        }
    }

}
