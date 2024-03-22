# Fast Config API

A lightweight API to manage your Configs easily.

[Click here to see the wiki](https://github.com/Infinituum17/FastConfigAPI/wiki).

## Features

Load, manage and delete your config directly at runtime.

## Loading a Config

*Fast Config API* uses classes with default field values to write default configs and to read changed ones.

Create a Java class and declare your config's fields as class fields:
```java
class MyConfig {
    public int ticks = 10;
    public String blockId = "minecraft:chest";
    
    /* ... */
}
```

> **NOTE**: Each Config class needs at least two constructors:
> - An empty constructor (with 0 parameters, that builds the class with the default values).
> - A constructor that accepts all fields as parameters.
>
> Example: 
> ```java
> public MyConfig() {}
> 
> public MyConfig(int ticks, String blockId) {
>     this.ticks = ticks;
>     this.blockId = blockId;
> }
> ```

## Wiki
To see the list of all features and how to set up everything properly, visit the [wiki](https://github.com/Infinituum17/FastConfigAPI/wiki).