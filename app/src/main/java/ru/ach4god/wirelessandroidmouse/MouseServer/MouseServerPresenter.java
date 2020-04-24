package ru.ach4god.wirelessandroidmouse.MouseServer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.ach4god.wirelessandroidmouse.base.PresentorBase;
import ru.ach4god.wirelessandroidmouse.common.Preference;
import ru.ach4god.wirelessandroidmouse.common.Utils;

public class MouseServerPresenter extends PresentorBase<MouseServerContract.View> implements MouseServerContract.Presenter {

    private static final String TAG = "MouseServerPRESENTER";

    @Override
    public void startServer() {
        Log.d(TAG, "StartServer Service");
        MouseWebSocketIntentService.startWebSocketServer(getApplicationContext(), Utils.getIPAddress(true), Preference.DEFAULT_PORT);
    }

    @Override
    public void stopServer() {
        MouseWebSocketIntentService.stopServer(getApplicationContext());
    }

    @Override
    public void onServerStarted() {

    }

    @Override
    public void onClosedServer() {

    }

    @Override
    public void onClientConnected() {

    }

    @Override
    public void onClientDisconnected() {

    }

    @Override
    public void onMouseEvent(@NonNull String event, @NonNull int touchX, @NonNull int touchY, @NonNull int deltaX, @NonNull int deltaY) {

    }

    @Override
    public void viewIsReady() {
        startServer();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MouseWebSocketIntentService.ACTION_CLIENT_CONNECTED);
        intentFilter.addAction(MouseWebSocketIntentService.ACTION_CLIENT_DISCONNECTED);
        intentFilter.addAction(MouseWebSocketIntentService.ACTION_CLOSE);
        intentFilter.addAction(MouseWebSocketIntentService.ACTION_ERROR);
        intentFilter.addAction(MouseWebSocketIntentService.ACTION_MOUSE_EVENT);
        intentFilter.addAction(MouseWebSocketIntentService.ACTION_START);
        creareBroadcastObservable(intentFilter)
                .subscribe(handleMouseServer());
    }

    private Observer<? super Intent> handleMouseServer() {
        return new Observer<Intent>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Intent intent) {
                String action = intent.getAction();
                if (MouseWebSocketIntentService.ACTION_CLIENT_CONNECTED.equals(action)) {

                } else if (MouseWebSocketIntentService.ACTION_CLIENT_DISCONNECTED.equals(action)) {

                } else if (MouseWebSocketIntentService.ACTION_CLOSE.equals(action)) {

                } else if (MouseWebSocketIntentService.ACTION_ERROR.equals(action)) {

                } else if (MouseWebSocketIntentService.ACTION_START.equals(action)) {
                    getView().showStatus("STARTED");
                    getView().showAddress(" ws"+intent.getStringExtra("address"));
                } else if (MouseWebSocketIntentService.ACTION_MOUSE_EVENT.equals(action)) {

                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }

                ;
    }

    @io.reactivex.annotations.NonNull
    Observable<Intent> creareBroadcastObservable(final IntentFilter intentFilter) {
        return Observable.create(new ObservableOnSubscribe<Intent>() {
            @Override
            public void subscribe(final ObservableEmitter<Intent> emitter) {
                final BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        emitter.onNext(intent);
                    }
                };

                getApplicationContext().registerReceiver(receiver, intentFilter);

                emitter.setDisposable(new Disposable() {
                    @Override
                    public void dispose() {
                        getApplicationContext().unregisterReceiver(receiver);
                    }

                    @Override
                    public boolean isDisposed() {
                        return false;
                    }
                });
            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
