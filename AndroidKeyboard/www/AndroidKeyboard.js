var exec = require('cordova/exec');
let channel = require('cordova/channel');
console.dir(channel);
// require('cordova/channel').onCordovaReady.subscribe(function() {

//     exec(win, null, 'Plugin', 'method', []);

//     function win(message) {
    
//     }
// });



// exports.coolMethod = function (arg0, success, error) {
//     exec(success, error, 'AndroidKeyboard', 'coolMethod', [arg0]);
// };

// cordova.plugins.AndroidKeyboard.initHeight = ( (e) => console.dir(e) );

let classInJava = 'AndroidKeyboard';
let AndroidKeyboard = {
    isVisible: false,
    // initHeight: () => {},

    hide: function () {
        exec(null, null, classInJava, 'hide', []);
    },

    show: function () {
        exec(null, null, classInJava, 'show', []);
    },
    getKeyboardHeight: function (cb) {
        exec(cb, null, classInJava, 'getKeyboardHeight', []);
    },
 
    // showSoftKeyboard: function (success) {
    //     exec(success, null, classInJava, 'showSoftKeyboard', []);
    // },

    // fullScreen: function (isFullScreen, isLightNavigation) {
    //     if(typeof isFullScreen === 'boolean'){
    //         exec(null, null, classInJava, 'fullScreen', [isFullScreen, isLightNavigation]);
    //     }
    // },
    on: function(nameEvent, cbSuccess) {

        let success = function(message) {
            cbSuccess(message);
        };

        if(nameEvent === 'keyboard'){
            exec(success, null, classInJava, "on", []);
        }

    }
   
}


const sendVoice = (params) => {
    
}
exec(sendVoice, null, classInJava, "coolMethod", []);


module.exports = AndroidKeyboard;
