package com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models;

public class BTConnectResult {
    public BTConnectStatus status;
    public BTConnection connection;

    public BTConnectResult(BTConnectStatus status, BTConnection connection) {
        this.status = status;
        this.connection = connection;
    }
}
