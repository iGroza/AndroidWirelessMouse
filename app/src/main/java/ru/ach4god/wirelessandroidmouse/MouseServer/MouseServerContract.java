package ru.ach4god.wirelessandroidmouse.MouseServer;

import androidx.annotation.NonNull;

import ru.ach4god.wirelessandroidmouse.base.IActivityView;
import ru.ach4god.wirelessandroidmouse.base.IPresenter;

public interface MouseServerContract {

    interface View extends IActivityView {
        void showAddress(String address);

        void showStatus(String status);
    }

    interface Presenter extends IPresenter<View> {
        void startServer();

        void stopServer();

        void onServerStarted();

        void onClosedServer();

        void onClientConnected();

        void onClientDisconnected();

        void onMouseEvent(@NonNull String event, @NonNull int touchX, @NonNull int touchY, @NonNull int deltaX, @NonNull int deltaY);
    }
}
