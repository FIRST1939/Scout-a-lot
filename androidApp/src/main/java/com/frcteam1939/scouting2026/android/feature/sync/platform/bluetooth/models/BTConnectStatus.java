package com.frcteam1939.scouting2026.android.feature.sync.platform.bluetooth.models;

public enum BTConnectStatus {
    SUCCEEDED,
    SKIPPED_ALREADY_CONNECTED,
    FAILED_REFUSED,
    FAILED_TO_CONNECT,
    FAILED_NO_ADAPTER,
    FAILED_NO_PERMISSION,
    FAILED
}
