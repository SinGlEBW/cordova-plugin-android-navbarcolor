var exec = require('cordova/exec');

var namedColors = {
    "black": "#000000",
    "darkGray": "#A9A9A9",
    "lightGray": "#D3D3D3",
    "white": "#FFFFFF",
    "gray": "#808080",
    "red": "#FF0000",
    "green": "#00FF00",
    "blue": "#0000FF",
    "cyan": "#00FFFF",
    "yellow": "#FFFF00",
    "magenta": "#FF00FF",
    "orange": "#FFA500",
    "purple": "#800080",
    "brown": "#A52A2A"
};


let classInJava = 'AndroidNavBar';

var AndroidNavBar = {

    backgroundColorByName: function (colorname, lightNavigationBar) {
        return AndroidNavBar.backgroundColorByHexString(namedColors[colorname], lightNavigationBar);
    },
    
    backgroundColorByHexString: function (hexString, lightNavigationBar) {
        if (hexString.charAt(0) !== "#") {
            hexString = "#" + hexString;
        }

        if (hexString.length === 4) {
            var split = hexString.split("");
            hexString = "#" + split[1] + split[1] + split[2] + split[2] + split[3] + split[3];
        }

        lightNavigationBar = !!lightNavigationBar;

        exec(null, null, classInJava, "backgroundColorByHexString", [hexString, lightNavigationBar]);
    },

    hide: function () {
        exec(null, null, classInJava, "hide", []);
        AndroidNavBar.isVisible = false;
    },

    show: function () {
        exec(null, null, classInJava, "show", []);
        AndroidNavBar.isVisible = true;
    }

};




module.exports = AndroidNavBar;