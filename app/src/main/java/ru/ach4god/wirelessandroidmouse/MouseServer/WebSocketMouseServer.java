package ru.ach4god.wirelessandroidmouse.MouseServer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

import ru.ach4god.wirelessandroidmouse.common.Preference;

public class WebSocketMouseServer extends JobIntentService {




    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }


}
