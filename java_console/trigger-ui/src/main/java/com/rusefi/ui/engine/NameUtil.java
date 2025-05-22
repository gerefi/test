package com.gerefi.ui.engine;

import com.gerefi.config.generated.Integration;
import com.gerefi.core.Sensor;

import java.util.Map;
import java.util.TreeMap;


// todo: merge with EngineChart
public class NameUtil {
    protected static final Map<String, Sensor> name2sensor = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static String getUiName(String name) {
        if (name.isEmpty())
            return name;
        if (name.charAt(0) == Integration.PROTOCOL_CRANK1.charAt(0))
            return "Trigger #" + name.substring(1);
        if (name.charAt(0) == Integration.PROTOCOL_COIL_SHORT_PREFIX.charAt(0))
            return "Coil #" + name.substring(1);
        if (name.charAt(0) == Integration.PROTOCOL_INJ_SHORT_PREFIX.charAt(0))
            return "Injector #" + name.substring(1);
        if (name.charAt(0) == Integration.PROTOCOL_INJ_STAGE2_SHORT_PREFIX.charAt(0))
            return "Injector Second Stage #" + name.substring(1);
        return name;
    }
}
