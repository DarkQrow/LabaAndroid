В листинге 10.6 приведена расширенная заготовка сервиса. Наш сервис при запуске и останове выводит
соответствующее уведомление.

<center>Листинг 10.6. Расширенная заrотовка сервиса</center>

  ```Java
package com.example.den.myfirstservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    @Override
    public IBinder onВind(Intent arg0) {
// TODO Auto-generated method stuЬ
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Cлyжбa запущена ... ",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена ... ",
                Toast.LENGTH_LONG).show();
    }
}
```

Мы только что создали службу, которая ничего не делает. Пусть она послужит основой для ваших
собственных сервисов. А сейчас давайте рассмотрим создание сервиса, который бы воспроизводил
МРЗ-файл. Создайте новое приложение, поместите в каталог `res\raw` какой-либо МРЗ-файл на ваше
усмотрение. Пусть он называется `music.mpЗ`. Добавьте новый класс и назовите его `MediaService`. Код
этого класса показан в листинге 10.7.
<center>Листинг 10.7. Код класса MediaService</center>

```Java
package com.example.den.soundserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.media.MediaPlayer;

public class MediaService extends Service {
    MediaPlayer ambientMediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("He реализовано");
    }

    @Override
    public void onCreate() {
        ambientMediaPlayer = MediaPlayer.create(this, R.raw.zymotyx);
        ambientMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        ambientMediaPlayer.start();
        return START_STICКY;
    }

    @Override
    public void onDestroy() {
        ambientMediaPlayer.stop();
    }
}
   ```

Добавьте сервис `MediaService` в файл манифеста:

```xml

<service android:name=".MediaService" android:enaЬled="true" android:exported="true"></service>
```

Регистрация сервиса производится в узле `application` с помощью добавления элемента `<service>`. В
нем определяется атрибут `android:name`, который хранит название класса сервиса. Кроме того, он
может принимать еще ряд атрибутов:

+ `android:enabled` - если имеет значение __true__, то сервис может создаваться системой. Значение
  по умолчанию - __true__;
+ `android:exported` - указывает, могут ли другие компоненты приложения обращаться к сервису. Если
  имеет значение __true__, то могут, если имеет значение
  __false__, то нет;
+ `android:icon` - значок сервиса, представляет собой ссылку на ресурс drawable;
+ `android:isolatedProcess` - если имеет значение __true__, то сервис может быть запущен как
  специальный процесс, изолированный от остальной системы;
+ `android:laЬel` - название сервиса, которое отображается пользователю;
+ `android:perrmission`- набор разрешений, которые должно применять приложение для запуска сервиса;
+ `android:process` - название процесса, в котором запущен сервис. Как правило, имеет то же
  название, что и пакет приложения.

Вернемся к коду службы. Как мы договорились ранее, для воспроизведения музыкального файла сервис
будет использовать компонент `MediaPlayer`. В сервисе переопределяются все четыре метода жизненного
цикла (разве что лишь метод `onВind()` по сути не имеет никакой реализации):

- в методе `onCreate()` инициализируется медиапроиrрыватель с помощью музыкального ресурса, который
  добавлен в папку `res\raw`;
- в методе `onStartCommand()` начинается воспроизведение. Метод `onStartCommand()`
  может возвращать одно из значений, которое предполагает различное поведение в случае, если процесс
  сервиса был неожиданно завершен системой:
    + __START_STICKY__ - в этом случае сервис снова возвращается в запущенное состояние, как если бы
      снова был вызван метод `onStartCommand()` без передачи в этот метод объекта `Intent`;
    + __START_REDELIVER_INTENT__ - в этом случае сервис снова возвращается в запущенное состояние,
      как если бы снова был вызван метод `onStartCommand()` с передачей в этот метод
      объекта `Intent`;
    + __START_NOT_STICKY__ - сервис остается в остановленном положении;
- метод `onDestroy()` завершает воспроизведение. Для управления сервисом нужно изменить нашу
  активность. Сначала добавим в разметку две кнопки управления сервисом: первая кнопка будет
  запускать сервис, вторая - останавливать (листинг 10.8).

<center>Листинг 10.8 Разметка приложения</center>

```xml
<?xml version="l.O" encoding="utf-8"?>
<LinearLayout xrnlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="match_parent"
    android:orientation="horizontal">
    <Button android:id="@+id/start" android:layout_width="match_parent"
        android:layout_height="wrap_content" android:layout_weight="l" android:text="Start"
        android:onClick="click" />
    <Button android:id="@+id/stop" android:layout_width="match_parent"
        android:layout_height="wrap_content" android:layout_weight="l" android:text="Stop"
        android:onClick="click" />
</LinearLayout>
```

Код активности приведен в листинге 10.9.

<center>Листинг 10.9. Код активности</center>

```Java
package com.exarmple.den.soundserviceapp;

import android.support.v7.app.ActionВarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menultern;
import android.content.Intent;
import android.view.View;

public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_rnain);
    }

    public void click(View v) {
        Intent i = new Intent(this, MediaService.class);
        if (v.getld() == R.id.start)
            startService(i);
        else {
            stopService(i);
        }
    }
}
 ```

Для запуска сервиса используется объект `Intent`:

```Java
Intent i=new Intent(this,MediaService.class); 
```

Для запуска сервиса в классе `Activity` определен метод `startService()`, в который передается
объект
`Intent`. Этот метод будет посылать команду сервису и вызывать его метод `onStartCommand()`, а также
указывать системе, что сервис должен продолжать работать до тех пор, пока не будет вызван метод
`stopService()`. Метод `stopService()` также определен в классе `Activity` и принимает
объект `Intent`. Он останавливает работу сервиса, вызывая его метод `onDestroy()`.

# 10.3. Широковещательные приемники

Широковещательные приемники прослушивают широковещательные сообщения системы. Примеры таких
сообщений:

- низкий заряд батареи;
- нажата кнопка камеры;
- установлено новое приложение.

Помимо системных событий пользователь может создавать свои события - например, когда поток завершил
вычисления или когда поток начал работу. Стандартные широковещательные сообщения следующие:

- __ACТION_ТIМЕ_TICK__ - изменение времени, генерируется каждую минуту;
- __ACTION_ТIМЕ_CНANGED__ - установлено новое время;
- __ACTION_TIМEZONE_CНANGED__ - установлен новый часовой пояс;
- __ACTION_BOOT_COМPLETED__ - загрузка выполнена;
- __ACTION_РАСКАGЕ_ADDED__ - установлено новое приложение;
- __ACTION_PACКAGE_CНANGED__ - приложение (пакет) было изменено, например, включен/выключен какой-то
  компонент;
- __ACТION_РАСКАGЕ_REМOVED__ - пакет (приложение) был удален;
- __ACTION_РАСКАGЕ_RESTARTED__ - приложение было перезапущено;
- __ACTION_PACКAGE_DATA_CLEARED__ - очищены данные приложения;
- __ACTION_BATTERY_CНANGED__ - информация об изменении заряда батареи;
- __ACTION_POWER_CONNECTED__ - подключено внешнее питание;
- __ACTION_POWER_DISCONNECTED__ - отключено внешнее питание;
- __ACTION_SHUTDOWN__ - началось завершение работы.

Дополнительную информацию о событиях можно получить по адресу:
http://developer.android.com/reference/android/content/lntent.html.

Помимо системных событий пользователь может создавать свои события - например, когда поток завершил
вычисления или когда поток начал работу. Широковещательный приемник- это объект
класса `BroadcastReceiver` или одного из его подклассов. Самый главный метод этого класса

- `onReceive()`, который вызывается, когда приемник получает сообщение. Рассмотрим пример запуска
  службы на основании получения широковещательного сообщения - нажатия кнопки камеры. Приемник
  станет прослушивать сообщения, а фильтр намерения будет установлен на
  действие `Intent.ACТION_CAМERA_BUTTON`, которое соответствует нажатию кнопки камеры.
  Зарегистрировать приемник можно функцией `registerReceiver()`, а удалить приемник -
  функцией `unregisterReceiver().`

Итак, начнем создавать наше приложение. Код основной деятельности МainActivity представлен в файле
MainActivity.java (листинг 10.10).
<center>Листинг 10.10</center>

```Java
package com.samples.my_receiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends Activity { // Создаем объект iReceiver класса MyReceiver 
    MyReceiver iReceiver = new MyReceiver();

    /** Вызывается, когда деятельность запускается впервые. */
    @Override
    public void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.main); // Создаем фильтр намерения
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_CAМERA_ВUTTОN);
        // Добавляем действие в фильтр iFilter.
        addAction(Intent.ACГION_PACI < AGE_ADDED);
        // Регистрируем приемник 
        registerReceiver(iReceiver, iFilter);
    }

    @Override
    protected void onDestroy() {
        // Уничтожаем приемник по завершении приложения 
        unregisterReceiver(iReceiver);
        super.onDestroy();
    }
}
 ```

Разберемся, что здесь есть что. Первым делом мы объявили объект `iReceiver` класса `MyReceiver` (
этот класс будет описан позже). Затем создали фильтр намерения:

```Java
IntentFilter iFilter=new IntentFilter(Intent.ACГION_CAМERA_BUТТON);
        iFilter.addAction(Intent.ACTION_PACI<AGE_ADDED); 
```

После этого нужно зарегистрировать приемник с помощью `registerReceiver()`:

```Java
registerReceiver(iReceiver,iFilter); 
```

Первый параметр - это сам приемник, второй - фильтр намерений. Вот теперь надо создать
класс `MyReceiver`. Как это сделать с помощью Android Studio, вы уже знаете, поэтому привожу сразу
код класса (листинг 10.11).
<center>Листинг 10.11</center>

```Java
package com.samples.my_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// Класс MyReceiver является расширением класса BroadcastReceiver 
public class MyReceiver extends BroadcastReceiver {
    // Переопределяем метод onReceive()
    @Override
    public void onReceive(Context rcvContext, Intent rcvIntent) {
        String action = rcvIntent.getAction();
        // Если действие = Intent.ACTION_CAМERA_BUTTON 
        if (action.equals(Intent.ACTION_CAМERA_BUTTON)) {
            // то запускаем сервис MyService
            rcvContext.startService(new Intent(rcvContext, MyService.class));
        }
    }
}
```

Приемник анализирует полученное действие. Если нажата кнопка камеры, то будет запущен сервис (служба) `MyService`,
описанный в файле `MyService.java`. Код этого файла (расширенная заготовка сервиса)
был представлен в листинге 10.6. Вам нужно изменить только первую строчку - она должна выглядеть
так:


```Java
package com.samples.my_receiver;
``` 

<center>* * *</center>

В следующей главе мы подробно рассмотрим взаимодействие с
аппаратными средст.вами смартфона/планшета. 