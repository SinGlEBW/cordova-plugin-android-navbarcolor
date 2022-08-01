package ru.cordova.android.keyboard;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;


import java.util.function.Function;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.os.Build;
import android.content.Context;


import android.util.Log;

import android.inputmethodservice.Keyboard;

import androidx.core.view.WindowCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;


// import android.graphics.Color;
// import org.apache.cordova.CordovaArgs;

// import org.apache.cordova.LOG;
// import java.util.Arrays;




import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;

import android.view.WindowInsets;
// import com.google.gson.Gson;

class Person{
     
    String name;    // имя
    int age;        // возраст

    Person(){
        name = "Undefined";
        age = 18;
    }
}

public class AndroidKeyboard extends CordovaPlugin {
    /* Constructor */
    public AndroidKeyboard() {   };
    private int density = 0;
    private int heightKeyboard = 0;
    private int previousHeightDiff = 0;

    private boolean isWatchEvent = false;
    private boolean isInitHeight = false;

    private CallbackContext callbackContext;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

  

        this.initKeyboardEvent();


        // PluginResult dataResult = new PluginResult(PluginResult.Status.OK, "Тест текст");
        // dataResult.setKeepCallback(true);
        // cordova.setActivityResultCallback(this);


        //http://stackoverflow.com/a/4737265/1091751 detect if keyboard is showing
    

        

    // appView.sendJavascript("cordova.plugins.AndroidKeyboard.initHeight = (cb) => cb(" + Integer.toString(keyboardHeight) + ");" );

    }

    


    /*
     * через execute предполагаем что будет вызываться через js и вызываем методы в
     * java
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        /* Получение аргументов. В зависимости от передаваемого типа выбираем что хотим
            тут получать
            int args1 = args.getInt(0);
            int args2 = args.getInt(1);
            String message = args.getString(0);
        */
        /* callbackContext Если хотим передавать значения из java в js */
        // Просто проверить что тут находиться
        final Activity activity = this.cordova.getActivity();
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        final Window window = activity.getWindow();
        View view = window.getDecorView();
      

        if(action.equals("on")){
            isWatchEvent = true;
            callbackContext.success();
            return true;
        }

        if(action.equals("off")){
            isWatchEvent = false;
            callbackContext.success();
            return true;
        }

        if(action.equals("coolMethod")){
            Log.d("ТЕСТ: ", "Запуск coolMethod");
            return true;
        }

        if (action.equals("show")) {
            imm.showSoftInput(view, 0);
            callbackContext.success();
            return true;
        }

        if (action.equals("hide")) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            callbackContext.success();
            return true;
        }

        if (action.equals("getKeyboardHeight")) {
            callbackContext.success(heightKeyboard);
            return true;
        }

        // if (action.equals("fullScreen")) {
        //     boolean isFullScreen = args.getBoolean(0);
        //     boolean isLightNavigation = args.getBoolean(1);
        //     WindowCompat.setDecorFitsSystemWindows(window, isFullScreen);
        //     WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(view);
        //     if (windowInsetsController == null) {
        //         return true;
        //     }

        //     windowInsetsController.setAppearanceLightNavigationBars(isLightNavigation);
        //     callbackContext.success();
        //     return true;
        // }

        // if (action.equals("showSoftKeyboard")) {

        //     if (view.requestFocus()) {
        //         callbackContext.success();
        //         imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    
        //     }
    
   
        //     ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
        //         boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
        //         int imeHeight1 = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
        //         Log.d("Keyboard Size imeHeight1", "Size: " + imeHeight1);
        //         return insets;
        //     });




        //     return true;
        // }
    

     

        return false;

    }

    public void watchHeight(int heightDiff, int keyboardHeight){
        final CordovaWebView appView = this.webView;


        if(heightDiff > 200){
            if(!isInitHeight){
                heightKeyboard = keyboardHeight;
                isInitHeight = true;
            }

            
            if (heightDiff != previousHeightDiff) {
                appView.sendJavascript("cordova.plugins.AndroidKeyboard.isVisible = true");
                // appView.sendJavascript("cordova.plugins.AndroidKeyboard.on('showkeyboard', ("+ Integer.toString(keyboardHeight)+") => {});");                  
                appView.sendJavascript("cordova.fireWindowEvent('showkeyboard', { 'keyboardHeight':" + Integer.toString(keyboardHeight)+"});");                   
            }
            else 
            if ( heightDiff != previousHeightDiff && ( previousHeightDiff - heightDiff ) < 200 ){
                Log.d("initialize keyboardHeight", "Size: " + keyboardHeight);
                appView.sendJavascript("cordova.plugins.AndroidKeyboard.isVisible = false");
                appView.sendJavascript("cordova.fireWindowEvent('hidekeyboard')");

            }
            
            previousHeightDiff = heightDiff;
        
        }

        if(isWatchEvent){
            JSONObject payloadEvent = new JSONObject();
            // TODO: Написать логику
            if(heightDiff != previousHeightDiff){
                payloadEvent.put("isShow", true);
                // payloadEvent.put("height", launchDetails.second);

            }
            if(heightDiff != previousHeightDiff){
                payloadEvent.put("isShow", false);
            }

        
            
            

            this.callbackContext.success('key');
            
        }
    }


    public void initKeyboardEvent(){
    
        DisplayMetrics dm = new DisplayMetrics();
        this.cordova.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = (int)(dm.density);
        AndroidKeyboard _this = this;

        final View rootView = this.cordova.getActivity().getWindow().getDecorView().findViewById(android.R.id.content).getRootView();

        OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            	Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);
                int keyboardHeight = (int)(heightDiff / density);
                
                _this.watchHeight(heightDiff, keyboardHeight);
              
            }
        }; 
        

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);

    }
}



// public class KeyboardHeightProvider extends PopupWindow {

//     /** The tag for logging purposes */
//     private final static String TAG = "sample_KeyboardHeightProvider";

//     /** The keyboard height observer */
//     private KeyboardHeightObserver observer;

//     /** The cached landscape height of the keyboard */
//     private int keyboardLandscapeHeight;

//     /** The cached portrait height of the keyboard */
//     private int keyboardPortraitHeight;

//     /** The view that is used to calculate the keyboard height */
//     private View popupView;

//     /** The parent view */
//     private View parentView;

//     /** The root activity that uses this KeyboardHeightProvider */
//     private Activity activity;

//     /** 
//      * Construct a new KeyboardHeightProvider
//      * 
//      * @param activity The parent activity
//      */
//     public KeyboardHeightProvider(Activity activity) {
// 		super(activity);
//         this.activity = activity;

//         LayoutInflater inflator = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//         this.popupView = inflator.inflate(R.layout.popupwindow, null, false);
//         setContentView(popupView);

//         setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//         setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

//         parentView = activity.findViewById(android.R.id.content);

//         setWidth(0);
//         setHeight(LayoutParams.MATCH_PARENT);

//         popupView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

//                 @Override
//                 public void onGlobalLayout() {
//                     if (popupView != null) {
//                         handleOnGlobalLayout();
//                     }
//                 }
//             });
//     }

//     /**
//      * Start the KeyboardHeightProvider, this must be called after the onResume of the Activity.
//      * PopupWindows are not allowed to be registered before the onResume has finished
//      * of the Activity.
//      */
//     public void start() {

//         if (!isShowing() && parentView.getWindowToken() != null) {
//             setBackgroundDrawable(new ColorDrawable(0));
//             showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
//         }
//     }

//     /**
//      * Close the keyboard height provider, 
//      * this provider will not be used anymore.
//      */
//     public void close() {
//         this.observer = null;
//         dismiss();
//     }

//     /** 
//      * Set the keyboard height observer to this provider. The 
//      * observer will be notified when the keyboard height has changed. 
//      * For example when the keyboard is opened or closed.
//      * 
//      * @param observer The observer to be added to this provider.
//      */
//     public void setKeyboardHeightObserver(KeyboardHeightObserver observer) {
//         this.observer = observer;
//     }
   
//     /**
//      * Popup window itself is as big as the window of the Activity. 
//      * The keyboard can then be calculated by extracting the popup view bottom 
//      * from the activity window height. 
//      */
//     private void handleOnGlobalLayout() {

//         Point screenSize = new Point();
//         activity.getWindowManager().getDefaultDisplay().getSize(screenSize);

//         Rect rect = new Rect();
//         popupView.getWindowVisibleDisplayFrame(rect);

//         // REMIND, you may like to change this using the fullscreen size of the phone
//         // and also using the status bar and navigation bar heights of the phone to calculate
//         // the keyboard height. But this worked fine on a Nexus.
//         int orientation = getScreenOrientation();
//         int keyboardHeight = screenSize.y - rect.bottom;
        
//         if (keyboardHeight == 0) {
//             notifyKeyboardHeightChanged(0, orientation);
//         }
//         else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//             this.keyboardPortraitHeight = keyboardHeight; 
//             notifyKeyboardHeightChanged(keyboardPortraitHeight, orientation);
//         } 
//         else {
//             this.keyboardLandscapeHeight = keyboardHeight; 
//             notifyKeyboardHeightChanged(keyboardLandscapeHeight, orientation);
//         }
//     }

//     private int getScreenOrientation() {
//         return activity.getResources().getConfiguration().orientation;
//     }
    
//     private void notifyKeyboardHeightChanged(int height, int orientation) {
//         if (observer != null) {
//             observer.onKeyboardHeightChanged(height, orientation);
//         }
//     }
// }



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



          /*Запрос разрешений
            cordova.requestPermission(CordovaPlugin plugin, int requestCode, String permission);
            cordova.requestPermission(this, requestCode, READ); пример
            public static final String READ = Manifest.permission.READ_CONTACTS;
        */

        //calculate density-independent pixels (dp)
        //http://developer.android.com/guide/practices/screens_support.html
        

        // DisplayMetrics dm = new DisplayMetrics();
        // cordova.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        // density = (int)(dm.density);
        
        // final CordovaWebView appView = webView;
        // final View rootView = cordova.getActivity().getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
       



        // OnGlobalLayoutListener list = new OnGlobalLayoutListener() {
        //     int previousHeightDiff = 0;
           

        //     @Override
        //     public void onGlobalLayout() {
        //     	Rect r = new Rect();
        //         rootView.getWindowVisibleDisplayFrame(r);

        //         int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);
        //         int keyboardHeight = (int)(heightDiff / density);

        //         Log.d("initialize heightDiff", "Size: " + heightDiff);
        //         Log.d("initialize keyboardHeight", "Size: " + keyboardHeight);

        //         if(heightDiff > 200){

        //             if(!isInitHeight){
        //                 heightKeyboard = keyboardHeight;
        //                 isInitHeight = true;
        //             }
    
        //             if (heightDiff != previousHeightDiff) {
                     
    
        //                 appView.sendJavascript("cordova.plugins.AndroidKeyboard.isVisible = true");
        //                 // appView.sendJavascript("cordova.plugins.AndroidKeyboard.on('showkeyboard', ("+ Integer.toString(keyboardHeight)+") => {});");                  
        //                 appView.sendJavascript("cordova.fireWindowEvent('showkeyboard', { 'keyboardHeight':" + Integer.toString(keyboardHeight)+"});");                   
        //             }
        //             else if ( heightDiff != previousHeightDiff && ( previousHeightDiff - heightDiff ) < 200 ){
        //                 Log.d("initialize keyboardHeight", "Size: " + keyboardHeight);
    
        //                 appView.sendJavascript("cordova.plugins.AndroidKeyboard.isVisible = false");
        //                 appView.sendJavascript("cordova.fireWindowEvent('hidekeyboard')");
        //             }
                    
        //             previousHeightDiff = heightDiff;
                
        //         }


               
               
        //     }
        // }; 
        


        // rootView.getViewTreeObserver().addOnGlobalLayoutListener(list);

