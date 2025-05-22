package com.gerefi;

import com.devexperts.logging.Logging;
import com.gerefi.newparse.DefinitionsState;
import com.gerefi.newparse.parsing.Definition;

import java.io.IOException;

public class ExtraUtil {
    private final static Logging log = Logging.getLogging(ExtraUtil.class);
    static void handleFiringOrder(String firingEnumFileName, VariableRegistry variableRegistry, DefinitionsState parseState) throws IOException {
        if (firingEnumFileName != null) {
            log.info("Reading firing from " + firingEnumFileName);
            String result = FiringOrderTSLogic.invoke(firingEnumFileName);
            parseState.addDefinition(variableRegistry, "FIRINGORDER", result, Definition.OverwritePolicy.NotAllowed);
        }
    }
}
