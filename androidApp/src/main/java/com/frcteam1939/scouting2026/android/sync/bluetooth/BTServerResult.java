package com.frcteam1939.scouting2026.android.sync.bluetooth;

public class BTServerResult {
    public BTServerEvent event;
    public BTConnection connection;

    public BTServerResult(BTServerEvent status, BTConnection connection) {
        this.event = status;
        this.connection = connection;
    }
}
