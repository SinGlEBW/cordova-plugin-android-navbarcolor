package ru.cordova.android.keyboard;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class AndroidKeyboard extends CordovaPlugin {
    /* Constructor */
    public AndroidKeyboard() {
    }

    /*
     * через execute предполагаем что будет вызываться через js и вызываем методы в
     * java
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            this.coolMethod(args, callbackContext);
            return true;
        }

        if (action.equals("show")) {
            /* callbackContext Если хотим передавать значения из java в js */
            this.show(callbackContext);
            return true;
        }

        if (action.equals("hide")) {
            this.hide(callbackContext);
            return true;
        }

        if (action.equals("getKeyboardHeight")) {
            this.getKeyboardHeight(callbackContext);
            return true;
        }

        return false;
    }

    /* Описываемые методы */
    private void coolMethod(JSONArray args, CallbackContext callbackContext) {

        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
        /*
         * Получение аргументов. В зависимости от передаваемого типа выбираем что хотим
         * тут получать
         * int args1 = args.getInt(0);
         * int args2 = args.getInt(1);
         * String message = args.getString(0);
         */
    }

    private void show(CallbackContext callbackContext) {
        // if (message != null && message.length() > 0) {
        // callbackContext.success(message);//
        // } else {
        // callbackContext.error("Expected one non-empty string argument.");
        // }

        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // SYSTEM_UI_FLAG_FULLSCREEN is available since JellyBean, but we
                // use KitKat here to be aligned with "Fullscreen" preference
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    int uiOptions = window.getDecorView().getSystemUiVisibility();
                    uiOptions &= ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    uiOptions &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;

                    window.getDecorView().setSystemUiVisibility(uiOptions);
                }

                // CB-11197 We still need to update LayoutParams to force status bar
                // to be hidden when entering e.g. text fields
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        });
    }

    private void hide(CallbackContext callbackContext) {
        /* Можно попробовать через callbackContext предположительно передавать event */
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // SYSTEM_UI_FLAG_FULLSCREEN is available since JellyBean, but we
                // use KitKat here to be aligned with "Fullscreen" preference
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    int uiOptions = window.getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN;

                    window.getDecorView().setSystemUiVisibility(uiOptions);
                }

                // CB-11197 We still need to update LayoutParams to force status bar
                // to be hidden when entering e.g. text fields
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        });

    }

    public int getKeyboardHeight(CallbackContext callbackContext) {
        final View rootview = this.getWindow().getDecorView();
        linearChatLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        rootview.getWindowVisibleDisplayFrame(r);
                        int screenHeight = rootview.getRootView().getHeight();
                        int newHeight = screenHeight - (r.bottom - r.top);
                        if (newHeight > heightOfKeyboard) {
                            heightOfKeyboard = screenHeight
                                    - (r.bottom - r.top);
                            // heightOfKeyboard = heightDiff;
                        }

                        Log.d("Keyboard Size", "Size: " + heightOfKeyboard);
                    }
                });
        callbackContext.success(heightOfKeyboard);
    }
}
