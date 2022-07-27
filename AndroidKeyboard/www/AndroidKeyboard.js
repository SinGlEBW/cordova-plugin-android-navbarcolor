var exec = require('cordova/exec');

// exports.coolMethod = function (arg0, success, error) {
//     exec(success, error, 'AndroidKeyboard', 'coolMethod', [arg0]);
// };

let AndroidKeyboard = {
    coolMethod: function (arg0, success, error) {
        /* */
        exec(success, error, 'AndroidKeyboard', 'coolMethod', [arg0]);
    },

    hide: function () {
        exec(null, null, 'AndroidKeyboard', 'hide', []);
    },

    show: function () {
        exec(null, null, 'AndroidKeyboard', 'show', []);
    },
    getKeyboardHeight: function (success) {
        exec(success, null, 'AndroidKeyboard', 'getKeyboardHeight', []);
    }
}
module.exports = AndroidKeyboard;
