package infinituum.fastconfigapi.fabric.tests;

import infinituum.fastconfigapi.api.annotations.Describe;
import infinituum.fastconfigapi.api.annotations.FastConfig;

import static infinituum.fastconfigapi.FastConfigAPI.MOD_ID;

@FastConfig(modId = MOD_ID)
@Describe(name = "Config Test 1", description = "This is a Test", tooltip = "§6This is a Tooltip")
public class ConfigTest1 {
    private Short shortValue = 1;
    private Long longValue = 2L;
    private Double doubleValue = 3d;
    private Float floatValue = 4f;
    private Boolean booleanValue = true;
    private Byte byteValue = 6;
    private Character charValue = '7';
    private String str = "8";
    private E eEnum = E.NINE;
    private Integer[] arr = new Integer[]{1, 2, 3};
    private Person objPerson = new Person();
    private Integer intValue = 0;

    public enum E {
        NINE,
        EIGHT,
        SEVEN,
        CYCLE
    }

    public static class Person {
        private String name = "Rick";
        private int age = 23;
    }
}
