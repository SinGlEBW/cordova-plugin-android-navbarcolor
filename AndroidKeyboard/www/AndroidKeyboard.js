var exec = require('cordova/exec');

// exports.coolMethod = function (arg0, success, error) {
//     exec(success, error, 'AndroidKeyboard', 'coolMethod', [arg0]);
// };

let classInJava = 'AndroidKeyboard';
let AndroidKeyboard = {
    hide: function () {
        exec(null, null, classInJava, 'hide', []);
    },

    show: function () {
        exec(null, null, classInJava, 'show', []);
    },
    getKeyboardHeight1: function (success) {
        exec(success, null, classInJava, 'getKeyboardHeight1', []);
    },
    getKeyboardHeight2: function (success) {
        exec(success, null, classInJava, 'getKeyboardHeight2', []);
    },
    showSoftKeyboard: function (success) {
        exec(success, null, classInJava, 'showSoftKeyboard', []);
    },

    overlaysWebView: function (isFullScreen) {
        if(typeof isFullScreen === 'boolean'){
            exec(null, null, classInJava, 'showSoftKeyboard', [isFullScreen]);
        }
    }
}
module.exports = AndroidKeyboard;
