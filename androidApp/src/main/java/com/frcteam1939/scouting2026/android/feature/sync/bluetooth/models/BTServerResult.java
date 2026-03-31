package com.frcteam1939.scouting2026.android.feature.sync.bluetooth.models;

public class BTServerResult {
    public BTServerEvent event;
    public BTConnection connection;

    public BTServerResult(BTServerEvent event, BTConnection connection) {
        this.event = event;
        this.connection = connection;
    }
}
