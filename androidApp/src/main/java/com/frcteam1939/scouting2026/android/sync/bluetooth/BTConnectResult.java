package com.frcteam1939.scouting2026.android.sync.bluetooth;

public class BTConnectResult {
    public BTConnectStatus status;
    public BTConnection connection;

    public BTConnectResult(BTConnectStatus status, BTConnection connection) {
        this.status = status;
        this.connection = connection;
    }
}
