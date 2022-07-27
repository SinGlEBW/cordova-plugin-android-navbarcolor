/* 
  Этапы создания плагина
  1. 
    Создать проект кордовы для тестирования плагинов. Создать папку CordovaProject и далее в ней открыть терминал и вводить команды:
    cordova create . cordova.tests.plugins TestPlugins
    cordova platforms add android
       
  2. Создаём папку с плагином в корне проекта
    plugman create --name ИмяКлассаJava --plugin_id  ru.cordova.android.keyboard --plugin_version 1.0.0

    Пример:
    plugman create --name AndroidKeyboard --plugin_id  ru.cordova.android.keyboard --plugin_version 1.0.0
      Примечание:
            Почему plugin_id не cordova-plugin-android-keyboard а ru.cordova.android.keyboard т.к. файл создаётся автоматом, и указав 2й вариант (вложенность папок)
            нам меньше всего вносить правок в plugin.xml

      Далее переходим в папку с плагином
    cd ./имя_папки_в_которой_будет_плагин
    plugman platform add --platform_name android

    После в plugin.xml id поменять с id="ru.cordova.android.keyboard" на "cordova-plugin-android-keyboard"
      Для понимания:
        plugin_id - указывается через точку т.к. в дальнейшем будет создана вложенность данных папок 
        в папке platforms/android/app/src/main/java + plugin_id = ...main/java/ru/cordova/android/keyboard

    
    и добавляем package.json для плагина.

    plugman createpackagejson .



    выходим на уровень выше из плагина и вводим: 
      cordova plugin add AndroidKeyboard --link 
    Далее скорей всего получим ошибку 

        Error during processing of action! Attempting to revert...
        Failed to install 'cordova-plugin-android-keyboard': Error: Uh oh!
        EPERM: operation not permitted, symlink '..\..\..\..\..\..\..\..\..\..\..\AndroidKeyboard\src\android\AndroidKeyboard.java

    Связанно это с группировкой папок в самом vscode. Это БАГ. Так что нужно отдельно открыть cmd все vscode перейти в проект cordova где создавали плагин
    предварительно удалив недоустановленный плагин platform/android/app/src/main/java и далее всю папку /ru/...
    и ввести снова команду: cordova plugin add AndroidKeyboard --link
    

    cordova plugin add /путь/до/плагина/ --link
    

*/


/*############------------<{ Структура файла plugins.xml }>-----------############ 

<plugin id="имя пакета (cordova-plugin-android-keyboard)" ...>
</plugin>

<js-module name="ИмяКласса по которому обращаемся(AndroidKeyboard)" src="путь к файлу js  (www/AndroidKeyboard.js)">
    <clobbers target="где будет плагин в window (cordova.plugins.AndroidKeyboard)" />
</js-module>



  <platform name="android">
      <config-file parent="/*" target="res/xml/config.xml">
          <feature name="AndroidKeyboard">
              <param name="android-package" value="cordova-plugin-android-keyboard.AndroidKeyboard" />
          </feature>
      </config-file>
      <config-file parent="/*" target="AndroidManifest.xml"></config-file>

      
      <source-file src="src/android/Файл1.java" target-dir="src/cordova-plugin-android-keyboard/AndroidKeyboard" />
      <source-file src="src/android/Файл2.java" target-dir="src/cordova-plugin-android-keyboard/AndroidKeyboard" />
      // target-dir - начинается с "src/..." который заменяет путь plugin_id, то есть platforms/android/app/src/main/java/...

  </platform>
*/