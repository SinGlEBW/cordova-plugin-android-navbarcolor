package ru.cordova.android.keyboard;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
// import org.json.JSONObject;


import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.os.Build;
import android.content.Context;


import android.util.Log;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import androidx.core.view.WindowCompat;





// import android.graphics.Color;
// import org.apache.cordova.CordovaArgs;
// import org.apache.cordova.CordovaInterface;
// import org.apache.cordova.CordovaWebView;
// import org.apache.cordova.PluginResult;
// import org.apache.cordova.LOG;
// import java.util.Arrays;




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
        /* callbackContext Если хотим передавать значения из java в js */
        // Просто проверить что тут находиться
        final Activity activity = this.cordova.getActivity();
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        final Window window = activity.getWindow();
        View view = window.getDecorView();


        if (action.equals("show")) {
            this.show(imm, callbackContext);
            imm.showSoftInput(view, 0);
            callbackContext.success();
            return true;
        }


        if (action.equals("hide")) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            callbackContext.success();
            return true;
        }


        if (action.equals("getKeyboardHeight1")) {
            this.getKeyboardHeight1(window, callbackContext);
            return true;
        }


        if (action.equals("getKeyboardHeight2")) {
            this.getKeyboardHeight2(window, callbackContext);
            return true;
        }


        if (action.equals("showSoftKeyboard")) {
            this.showSoftKeyboard(window, callbackContext);
            return true;
        }
    

        if (action.equals("overlaysWebView")) {
            this.overlaysWebView(window, callbackContext, args.getBoolean(0));
            return true;
        }

        return false;
    }

    /* Описываемые методы */
    private void coolMethod(JSONArray args, CallbackContext callbackContext) {

        // if (message != null && message.length() > 0) {
        //     callbackContext.success(message);
        // } else {
        //     callbackContext.error("Expected one non-empty string argument.");
        // }
        /*
        * Получение аргументов. В зависимости от передаваемого типа выбираем что хотим
        * тут получать
        * int args1 = args.getInt(0);
        * int args2 = args.getInt(1);
        * String message = args.getString(0);
        */
    }

    // private void show(InputMethodManager imm, CallbackContext callbackContext) {
   
    // }

    // private void hide(InputMethodManager imm, CallbackContext callbackContext) {
    

    // }

    public void getKeyboardHeight1(Window window, CallbackContext callbackContext) {
        View view = window.getDecorView();
        System.out.println(view.getMeasuredHeight());

        /*
            getViewTreeObserver() - метод возвращающий методы принимающие слушатели. 
            Слушатели для данных методов создаются через new ViewTreeObserver.слушатель
        */
        
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();
                // int newHeight = screenHeight - (r.bottom - r.top);

                int heightOfKeyboard = screenHeight - (r.bottom - r.top);
                callbackContext.success(heightOfKeyboard);

                Log.d("Keyboard Size", "Size: " + heightOfKeyboard);
            }
        });
    }


    private void getKeyboardHeight2(Window window, CallbackContext callbackContext) {
        View view = window.getDecorView();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();

                view.getWindowVisibleDisplayFrame(r);

                int screenHeight = view.getRootView().getHeight();

                int keyboardHeight = screenHeight - (r.bottom);

                callbackContext.success(keyboardHeight);


                // if (previousHeightDiffrence - keyboardHeight > 50){
                //     // Do some stuff here
                // }

                // previousHeightDiffrence = keyboardHeight;
                // if (keyboardHeight> 100){
                //     isKeyBoardVisible = true;
                //     if (height > 100){
                //             keyboardHeight = height;
                //             LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, keyboardHeight);
                //             yourLayout.setLayoutParams(params);
                //     }
                // } 
                // else{
                //     isKeyBoardVisible = false;
                // }


            }
        });
    }

    
    public void showSoftKeyboard(Window window, CallbackContext callbackContext) {

        View view = window.getDecorView();
        if (view.requestFocus()) {
            callbackContext.success();
            // InputMethodManager imm = (InputMethodManager)
            //         getSystemService(Context.INPUT_METHOD_SERVICE);
            // imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        }

    }

    public void overlaysWebView(Window window, CallbackContext callbackContext, boolean isFullScreen) {
        WindowCompat.setDecorFitsSystemWindows(window, isFullScreen);
        // if(isFullScreen){
            
        //   WindowInsetsControllerCompat(window, mainContainer).show(WindowInsetsCompat.Type.systemBars());
        // }
        // if(!isFullScreen){
        //     WindowCompat.setDecorFitsSystemWindows(window, false);
        //     // WindowInsetsControllerCompat(window, mainContainer).let { controller ->
        //     //     controller.hide(WindowInsetsCompat.Type.systemBars());
        //     //     controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;
        //     // }
        // }
    }


 


}


      /*------------------------------------------------------------------------------------------------------------- */
    // private int getScreenOrientation() {
	// 	return activity.getResources().getConfiguration().orientation;
	// }
    // private void handleOnGlobalLayout() {
	// 	Point screenSize = new Point();
	// 	activity.getWindowManager().getDefaultDisplay().getSize(screenSize);

	// 	Rect rect = new Rect();
	// 	popupView.getWindowVisibleDisplayFrame(rect);

	// 	// REMIND, you may like to change this using the fullscreen size of the phone
	// 	// and also using the status bar and navigation bar heights of the phone to calculate
	// 	// the keyboard height. But this worked fine on a Nexus.
	// 	int orientation = getScreenOrientation();
	// 	int keyboardHeight = screenSize.y - rect.bottom;

	// 	// fix for phones that give "getWindowVisibleDisplayFrame()" in real screen size
	// 	// but screenSize is smaller because of e.g. carved out display portion (Xiaomi redmi 9)
	// 	if (keyboardHeight <= 0) {
	// 		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
	// 			keyboardPortraitHeightDelta = keyboardHeight;
	// 		} else {
	// 			keyboardLandscapeHeightDelta = keyboardHeight;
	// 		}
	// 		notifyKeyboardHeightChanged(0, orientation);
	// 		return;
	// 	}

	// 	if (orientation == Configuration.ORIENTATION_PORTRAIT) {
	// 		keyboardHeight -= keyboardPortraitHeightDelta;
	// 		keyboardPortraitHeight = keyboardHeight;
	// 	} else {
	// 		keyboardHeight -= keyboardLandscapeHeightDelta;
	// 		keyboardLandscapeHeight = keyboardHeight;
	// 	}
	// 	notifyKeyboardHeightChanged(keyboardHeight, orientation);
	// }
    // private void notifyKeyboardHeightChanged(int height, int orientation) {
	// 	if (observer != null) {
	// 		observer.onKeyboardHeightChanged(height, orientation);
	// 	}
	// }
