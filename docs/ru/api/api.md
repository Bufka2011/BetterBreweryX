
# API Аддонов

BreweryX поддерживает аддоны, которые могут добавить новые фишки в плагин.
Это могут быть новые рецепты варки, кастомные предметы, или другие вещи.
Эта секция рассказывает, как сделать аддон для BreweryX.

## Создание аддона

Посмотри наш репозиторий [template addons](https://github.com/BreweryTeam/ExampleBreweryAddon)
для настройки проекта и какие зависимости включить.

После настройки своего проекта, тебе нужно сделать main class, который расширяет `BreweryAddon`
и реализует `onAddonEnable` метод.

=== "Java"

    ``` java
    import com.dre.brewery.api.addons.BreweryAddon;
    import com.dre.brewery.api.addons.AddonInfo;    
    
    @AddonInfo(name = "MyAddon", version = "1.0", author = "Jonah")
    public class MyAddon extends BreweryAddon {
        @Override
        public void onAddonEnable() {
            // Код, который выполняется когда MyAddon включён
        }

        @Override
        public void onAddonDisable() {
            // Код, который выполняется когда BreweryX выключен
        }

        @Override
        public void onBreweryReload() {
            // Код, который выполняется когда команда `/breweryx reload` выполняется
        }
    }
    ```
=== "Kotlin"

    ``` kotlin
    import com.dre.brewery.api.addons.BreweryAddon
    import com.dre.brewery.api.addons.AddonInfo

    @AddonInfo(name = "MyAddon", version = "1.0", author = "Jonah")
    class MyAddon : BreweryAddon() {
        override fun onAddonEnable() {
            // Код, который выполняется когда MyAddon включён
        }

        override fun onAddonDisable() {
            // Код, который выполняется когда BreweryX выключен
        }

        override fun onBreweryReload() {
            // Код, который выполняется когда команда `/breweryx reload` выполняется
        }
    }
    ```

## Команды Аддона

Команды аддона следует регистрировать с помощью метода `registerCommand` в методе `onAddonEnable`.
Команды аддона будут отображаться в игре как подкоманды для команды `/breweryx`.

=== "Java"

    ``` java
    public class MyCommand implements AddonCommand {
        @Override
        public void execute(BreweryPlugin breweryPlugin, Lang lang, CommandSender sender, String label, String[] args) {
            sender.sendMessage("Привет от MyCommand!");
        }

        @Override
        public List<String> tabComplete(BreweryPlugin breweryPlugin, CommandSender sender, String label, String[] args) {
            return null;
        }

        @Override
        public String getPermission() {
            return "brewery.myaddon.command";
        }

        @Override
        public boolean playerOnly() {
            return false;
        }
    }

    @Override
    public void onAddonEnable() {
        registerCommand("mycommand", new MyCommand());
    }
    ```

=== "Kotlin"

    ``` kotlin
    class MyCommand : AddonCommand {
        override fun execute(breweryPlugin: BreweryPlugin, lang: Lang, sender: CommandSender, label: String, args: Array<String>) {
            sender.sendMessage("Привет от MyCommand!")
        }

        override fun tabComplete(breweryPlugin: BreweryPlugin, sender: CommandSender, label: String, args: Array<String>): List<String>? {
            return null
        }

        override fun getPermission(): String {
            return "brewery.myaddon.command"
        }

        override fun playerOnly(): Boolean {
            return false
        }
    }

    override fun onAddonEnable() {
        registerCommand("mycommand", MyCommand())
    }
    ```

## События в аддонах

События в аддонах должны объявляться так же, как и в обычных плагинах Bukkit.
Единственное отличие заключается в том, что вы должны зарегистрировать их через метод `registerListener`
вашего дополнения в методе `onAddonEnable`.

=== "Java"

    ``` java
    public class MyListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            event.getPlayer().sendMessage("Привет от MyListener!");
        }
    }

    @Override
    public void onAddonEnable() {
        registerListener(new MyListener());
    }
    ```

=== "Kotlin"

    ``` kotlin
    class MyListener : Listener {
        @EventHandler
        fun onPlayerJoin(event: PlayerJoinEvent) {
            event.player.sendMessage("Привет от MyListener!")
        }
    }

    override fun onAddonEnable() {
        registerListener(MyListener())
    }
    ```

## Конфигурационные файлы в адлдонах

Аддоны поддерживают конфигурационные файлы, используя Okaeri config,
которая представляет собой мощную и простую в использовании библиотеку конфигурации.

Lombok рекомендован для Java разработчиков для сокращения шаблонного кода.

=== "Java"

    ``` java
    @OkaeriConfigFile(fileName = "addon-config.yml")
    @Getter @Setter
    public class MyConfig extends AddonConfigFile {
        public String message = "Привет от MyConfig!";
    }

    @Override
    public void onAddonEnable() {
        MyConfig config = getAddonConfigManager().getConfig(MyConfig.class);
        getLogger().info(config.getMessage());
    }
    ```

=== "Kotlin"

    ``` kotlin
    @OkaeriConfigFile(fileName = "addon-config.yml")
    class MyConfig : AddonConfigFile() {
        @JvmField
        var message: String = "Привет от MyConfig!"
    }

    override fun onAddonEnable() {
        val config = addonConfigManager.getConfig(MyConfig::class.java)
        logger.info(config.message)
    }
    ```


# Внешний API плагина

Для Java-разработчиков: используйте любой выпущенный JAR-файл локально или, что предпочтительнее, используйте JitPack:

## Maven

```XML
<repository>
   <id>jitpack.io</id>
   <url>https://jitpack.io</url>
</repository>

<dependency>
   <groupId>com.github.BreweryTeam</groupId>
   <artifactId>BreweryX</artifactId>
   <version>VERSION</version>
   <scope>provided</scope>
</dependency>
```

## Gradle

=== "Gradle (Groovy)"

    ``` groovy
    repositories {
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        implementation 'com.github.BreweryTeam:BreweryX:VERSION'
    }
    ```

=== "Gradle (KTS)"

    ``` kotlin
    repositories {
        maven("https://jitpack.io")
    }

    dependencies {
        implementation("com.github.BreweryTeam:BreweryX:VERSION")
    }
    ```

!!! важно

    Не забудьте заменить VERSION на версию [доступную на JitPack](https://jitpack.io/#BreweryTeam/BreweryX#releasesLink).

Вам также нужно добавить зависимость в файл `plugin.yml` или `paper-plugin.yml`. Это гарантирует, что BreweryX загрузится перед вашим плагином.

=== "plugin.yml"

    ``` yaml
    depend: [BreweryX]
    ```

=== "paper-plugin.yml"

    ``` yaml
    dependencies:
        server:
            BreweryX:
                load: BEFORE
                required: true
                join-classpath: true
    ```
