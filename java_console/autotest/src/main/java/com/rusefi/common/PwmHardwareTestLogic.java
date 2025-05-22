package com.gerefi.common;

import com.gerefi.Timeouts;
import com.gerefi.config.generated.Integration;
import com.gerefi.core.Sensor;
import com.gerefi.core.SensorCentral;
import com.gerefi.enums.engine_type_e;
import com.gerefi.functional_tests.EcuTestHelper;

import static com.gerefi.IoUtil.getEnableCommand;
import static com.gerefi.binaryprotocol.BinaryProtocol.sleep;
import static com.gerefi.config.generated.VariableRegistryValues.*;

public class PwmHardwareTestLogic {
    private static final int FREQUENCY = 160;

    public static void runIdlePwmTest(EcuTestHelper ecu, String idlePin, String logicCapturePin) {
        ecu.setEngineType(engine_type_e.FRANKENSO_MIATA_NA6_MAP);
        ecu.changeRpm(1000);

        ecu.sendCommand(CMD_TRIGGER_SIMULATOR_PIN + " 0 none");
        ecu.sendCommand(CMD_TRIGGER_SIMULATOR_PIN + " 1 none");
        ecu.sendCommand(CMD_IDLE_PIN + " " + idlePin);

        /* wasted two wire, so we really need 1..3 to be none */
        ecu.sendCommand(CMD_IGNITION_PIN + " 1 none");
        ecu.sendCommand(CMD_IGNITION_PIN + " 2 none");
        ecu.sendCommand(CMD_IGNITION_PIN + " 3 none");

        // changing pin to avoid conflict
        ecu.sendCommand(CMD_TRIGGER_PIN + " 1 PA8");

        /* DBG_LOGIC_ANALYZER */
        ecu.sendCommand("set debug_mode " + com.gerefi.enums.debug_mode_e.DBG_LOGIC_ANALYZER.ordinal());

        /* 160 Hz */
        ecu.sendCommand("set idle_solenoid_freq " + FREQUENCY);

        /* save these for last to ensure logic is started */
        ecu.sendCommand(CMD_LOGIC_PIN + " 0 " + logicCapturePin);
        ecu.sendCommand(CMD_WRITECONFIG);
        sleep(2 * Timeouts.SECOND);
        ecu.sendCommand(getEnableCommand(Integration.CMD_SELF_STIMULATION));

        sleep(2 * Timeouts.SECOND);

        /* +-2% is still acceptable */
        EcuTestHelper.assertSomewhatClose("Idle PWM freq", FREQUENCY, SensorCentral.getInstance().getValue(Sensor.debugIntField1), 0.02);
    }
}
