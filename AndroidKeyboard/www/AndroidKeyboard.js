var exec = require('cordova/exec');

// exports.coolMethod = function (arg0, success, error) {
//     exec(success, error, 'AndroidKeyboard', 'coolMethod', [arg0]);
// };

let classInJava = 'AndroidKeyboard';
let AndroidKeyboard = {
    isVisible: false,
    watch: () => {},

    hide: function () {
        exec(null, null, classInJava, 'hide', []);
    },

    show: function () {
        exec(null, null, classInJava, 'show', []);
    },
    getKeyboardHeight: function (success, cb) {
        exec(success, null, classInJava, 'getKeyboardHeight', []);

    },
 
    showSoftKeyboard: function (success) {
        exec(success, null, classInJava, 'showSoftKeyboard', []);
    },

    fullScreen: function (isFullScreen, isLightNavigation) {
        if(typeof isFullScreen === 'boolean'){
            exec(null, null, classInJava, 'fullScreen', [isFullScreen, isLightNavigation]);
        }
    },
   
}



module.exports = AndroidKeyboard;
