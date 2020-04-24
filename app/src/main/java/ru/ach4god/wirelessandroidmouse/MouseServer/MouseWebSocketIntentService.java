package ru.ach4god.wirelessandroidmouse.MouseServer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;

import ru.ach4god.wirelessandroidmouse.common.Preference;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MouseWebSocketIntentService extends IntentService {
    private static final String TAG = "MouseJobService";
    private static final String ACTION_START_SERVICE = "ru.ach4god.wirelessandroidmouse.MouseServer.action.START_SERVICE";
    private static final String ACTION_STOP_SERVICE = "ru.ach4god.wirelessandroidmouse.MouseServer.action.STOP_SERVICE";
    public static String ACTION_START = "ru.ach4god.wirelessandroidmouse.MouseServer.ON_START";
    public static String ACTION_CLOSE = "ru.ach4god.wirelessandroidmouse.MouseServer.ON_CLOSE";
    public static String ACTION_MOUSE_EVENT = "ru.ach4god.wirelessandroidmouse.MouseServer.ON_MOUSE_EVENT";
    public static String ACTION_CLIENT_CONNECTED = "ru.ach4god.wirelessandroidmouse.MouseServer.ON_CLIENT_CONNECTED";
    public static String ACTION_CLIENT_DISCONNECTED = "ru.ach4god.wirelessandroidmouse.MouseServer.ON_CLIENT_DISCONNECTED";
    public static String ACTION_ERROR = "ru.ach4god.wirelessandroidmouse.MouseServer.ON_ERROR";
    WebSocketServer wsServer;

    public MouseWebSocketIntentService() {
        super("MouseWebSocketIntentService");
    }

    public static void startWebSocketServer(Context applicationContext, String ip, int port) {
        Intent intent = new Intent(applicationContext, MouseWebSocketIntentService.class);
        intent.putExtra("ip", ip);
        intent.putExtra("port", port);
        intent.setAction(ACTION_START_SERVICE);
        applicationContext.startService(intent);
    }

    public static void stopServer(Context applicationContext) {
        Intent intent = new Intent(applicationContext, MouseWebSocketIntentService.class);
        intent.setAction(ACTION_STOP_SERVICE);
        applicationContext.stopService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_SERVICE.equals(action)) {
                String ip = intent.getStringExtra("ip");
                int port = intent.getIntExtra("port", Preference.DEFAULT_PORT);
                Log.d(TAG, "START SERVICE " + ip + ":" + port);
                InetSocketAddress address = new InetSocketAddress(ip, port);
                wsServer = new MouseWebSocket(address);
                wsServer.run();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wsServer != null) {
            Log.d(TAG, "stopped");
            try {
                wsServer.stop();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static enum ACTIONS {
        MOVE,
        CLICK
    }

    private class MouseWebSocket extends WebSocketServer {

        public MouseWebSocket(InetSocketAddress address) {
            super(address);
            Log.d(TAG, "onCreateWebSocket");
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            Log.d(TAG, "onOpen");
            sendBroadcast(new Intent(ACTION_CLIENT_CONNECTED));
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            Log.d(TAG, "onClose: " + reason);
            sendBroadcast(new Intent(ACTION_CLIENT_DISCONNECTED));
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            Log.d(TAG, "onMessage: " + message);
            JSONObject json = handleJson(message);
            try {
                if (json.has("action")) {
                    String action = null;
                    action = json.getString("action");
                    if ("MOVE".equals(action)) {
                        int deltaX = json.getInt("deltaX");
                        int deltaY = json.getInt("deltaY");
                        Intent intent = new Intent(ACTION_MOUSE_EVENT);
                        intent.putExtra("deltaX", deltaX);
                        intent.putExtra("deltaY", deltaY);
                        intent.putExtra("action", action);
                        sendBroadcast(intent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
            sendBroadcast(new Intent(ACTION_ERROR));
            conn.close();
        }

        @Override
        public void onStart() {
            Log.d(TAG, "onStart");
            Intent data = new Intent(ACTION_START);
            data.putExtra("address", getAddress().toString());
            sendBroadcast(data);
        }

        private JSONObject handleJson(String jsonString) {
            try {
                return new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "invalid Json");
                return new JSONObject();
            }
        }
    }


}
