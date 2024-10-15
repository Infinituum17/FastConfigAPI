package infinituum.fastconfigapi.api.annotations;

import com.google.common.base.CaseFormat;
import infinituum.fastconfigapi.FastConfigs;
import infinituum.fastconfigapi.PlatformHelper;
import infinituum.fastconfigapi.api.annotations.Loader.Type;
import infinituum.fastconfigapi.api.serializers.SerializerWrapper;
import infinituum.fastconfigapi.impl.FastConfigFileImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.UUID;

/**
 * <i>Auto-Registered</i> Class Annotation.
 * <p>
 * Indicates that a Class should be used as a FastConfig.
 * This Annotation is <i>automatically registered</i> on the correct {@link Side} and you can access its {@link FastConfigFileImpl} (Config data) using the {@link FastConfigs}'s class.
 * <p>
 * A FastConfig class is instantiated through the default (parameterless) constructor, for this reason it mustn't
 * define any other constructor except one without parameters (otherwise the default one will be used).
 * <p>
 * Class' fields special keywords: <ul>
 * <li>{@code transient}: Used to denote that this field will not be serialized.
 * </ul>
 * <p><br/>
 * <b>Example</b>: Let's define a class that will store the position of all the waypoints of the current player.
 * <pre>
 * {@code
 * // File: MyConfig.java
 * @FastConfig(fileName = "waypoints", serializer = JSONSerializer.class, side = Side.CLIENT)
 * public class MyConfig {
 *     List<BlockPos> waypoints = new ArrayList<>();
 *     String playerName;
 *     transient UUID playerUUID;
 * }
 * }
 * </pre>
 * Our brand-new Config will have its file name set to "waypoints" (instead of "my-config"),
 * it will be serialized with the JSON serializer, and it will be stored <b>only</b> on the client side.
 * <br/><br/>
 * Fields:<ul>
 * <li>{@code waypoints}: A list of waypoints that will be initialized with a new {@link ArrayList}.
 * <li>{@code playerName}: A {@link String} that will contain the player's name (not initialized).
 * <li>{@code playerUUID}: A {@link UUID} that identifies the player. This field is transient and so it will not be serialized.
 * </ul>
 * <p>As a result we'll get a file called "waypoints-client.json":
 * <pre>
 * {@code {
 *     "waypoints": [],
 *     "playerName": ""
 * }}
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FastConfig {
    /**
     * The mod-id of the mod that manages this Config file.
     *
     * @return {@link String} mod-id
     */
    String modId();

    /**
     * The file name used to create the Config file.
     *
     * @return {@link String} - default: The Class' file name (formatted in {@link CaseFormat#LOWER_HYPHEN} case)
     */
    String fileName() default "";
    
    /**
     * The serializer used to Serialize.
     *
     * @return {@link Class Class<? extends SerializeWrapper>} - default: {@link PlatformHelper#getDefaultSerializer()} (default platform serializer)
     */
    Class<? extends SerializerWrapper> serializer() default SerializerWrapper.class;

    /**
     * The physical side on which the Config will be stored.
     *
     * @return {@link Side} - default: {@link Side#COMMON}
     */
    Side side() default Side.COMMON;

    /**
     * The subdirectory where the config will be stored.
     *
     * @return {@link String} - default: None
     */
    String subdirectory() default "";

    /**
     * The default loader of the config.
     * <p>
     * This loader is checked when the config needs to be created.
     *
     * @return {@link Loader} - default: {@link Loader @Loader}
     */
    Loader loader() default @Loader(type = Type.DEFAULT);

    /**
     * Side Enum.
     * <p>
     * Stores all available Physical Sides of the Game.
     */
    enum Side {
        /**
         * Represents the Client side.
         */
        CLIENT("client"),
        /**
         * Represents the Server side.
         */
        SERVER("server"),
        /**
         * Represents both the client and the server side.
         */
        COMMON("common");

        /**
         * String that will be appended to the end of the file name to denote on which side the Config is loaded.
         */
        private final String fileModifier;

        Side(String fileModifier) {
            this.fileModifier = fileModifier;
        }

        /**
         * This method adds a post-fix to the file name passed in to denote the side on which the config was created.
         *
         * @param fileName The file name
         * @return A new file name with a post-fix (ex. "filename-common")
         */
        public String appendTo(String fileName) {
            return fileName + "-" + fileModifier;
        }
    }
}
