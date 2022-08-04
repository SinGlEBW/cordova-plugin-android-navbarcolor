package ru.cordova.android.navbarcolor;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONException;

public class AndroidNavBar extends CordovaPlugin {
    private static final String TAG = "AndroidNavBar";


    @Override
    public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
        LOG.v(TAG, "AndroidNavBar: initialization");

        super.initialize(cordova, webView);

        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Window window = cordova.getActivity().getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            }
        });
    }


    @Override
    public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        LOG.v(TAG, "Executing action: " + action);
        final Activity activity = this.cordova.getActivity();
        final Window window = activity.getWindow();

        if ("_ready".equals(action)) {
            boolean navigationBarVisible = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0;
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, navigationBarVisible));
            return true;
        }

        if ("show".equals(action)) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        int uiOptions = window.getDecorView().getSystemUiVisibility();
                        uiOptions &= ~View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                        uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

                        window.getDecorView().setSystemUiVisibility(uiOptions);

                        window.getDecorView().setOnFocusChangeListener(null);
                        window.getDecorView().setOnSystemUiVisibilityChangeListener(null);
                    }

                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            });
            return true;
        }

        if ("hide".equals(action)) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // SYSTEM_UI_FLAG_FULLSCREEN is available since JellyBean, but we
                    // use KitKat here to be aligned with "Fullscreen"  preference
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        final int uiOptions = window.getDecorView().getSystemUiVisibility()
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

                        window.getDecorView().setSystemUiVisibility(uiOptions);

                        window.getDecorView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    window.getDecorView().setSystemUiVisibility(uiOptions);
                                }
                            }
                        });

                        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                            @Override
                            public void onSystemUiVisibilityChange(int visibility) {
                                window.getDecorView().setSystemUiVisibility(uiOptions);
                            }
                        });
                    }

             
                }
            });
            return true;
        }

        if ("backgroundColorByHexString".equals(action)) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setNavigationBarBackgroundColor(args.getString(0), args.getBoolean(1));
                    } catch (JSONException ignore) {
                        LOG.e(TAG, "Invalid hexString argument, use f.i. '#777777'");
                    }
                }
            });
            return true;
        }

        return false;
    }

    private void setNavigationBarBackgroundColor(final String colorPref, Boolean lightNavigationBar) {

        lightNavigationBar = lightNavigationBar == null ? false : lightNavigationBar;

        if (Build.VERSION.SDK_INT >= 21) {
            if (colorPref != null && !colorPref.isEmpty()) {
                final Window window = cordova.getActivity().getWindow();
                int uiOptions = window.getDecorView().getSystemUiVisibility();
                             
                // 0x80000000 FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                // 0x00000010 SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

                uiOptions = uiOptions | 0x80000000;

                if(Build.VERSION.SDK_INT >= 26 && lightNavigationBar)
                    uiOptions = uiOptions | 0x00000010;
                else
                    uiOptions = uiOptions & ~0x00000010;

                // window.getDecorView().setSystemUiVisibility(uiOptions);
                
            
                // Hide both the navigation bar and the status bar.
                // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
                // a general rule, you should design your app to hide the status bar whenever you
                // hide the navigation bar.

                boolean testFlag = lightNavigationBar;
                int uiOptions1 = testFlag ? View.SYSTEM_UI_FLAG_HIDE_NAVIGATION : View.SYSTEM_UI_FLAG_FULLSCREEN;

                window.getDecorView().setSystemUiVisibility(uiOptions1);



                try {
                    // Using reflection makes sure any 5.0+ device will work without having to compile with SDK level 21
                    window.getClass().getMethod("setNavigationBarColor", int.class).invoke(window, Color.parseColor(colorPref));

                } catch (IllegalArgumentException ignore) {
                    LOG.e(TAG, "Invalid hexString argument, use f.i. '#999999'");
                } catch (Exception ignore) {
                    // this should not happen, only in case Android removes this method in a version > 21
                    LOG.w(TAG, "Method window.setNavigationBarColor not found for SDK level " + Build.VERSION.SDK_INT);
                }
            }
        }
    }
    private void testColor(){

    }

}
