package com.gerefi.common;

import com.gerefi.Timeouts;
import com.gerefi.config.generated.Integration;
import com.gerefi.autotest.ControllerConnectorState;
import com.gerefi.core.Sensor;
import com.gerefi.core.SensorCentral;
import com.gerefi.enums.engine_type_e;
import com.gerefi.functional_tests.EcuTestHelper;

import static com.gerefi.IoUtil.getEnableCommand;
import static com.gerefi.binaryprotocol.BinaryProtocol.sleep;
import static com.gerefi.config.generated.Integration.*;

public class VssHardwareTestLogic {
    private static final int FREQUENCY = 160;

    public static void runIdleVssTest(EcuTestHelper ecu, String idlePin, String vssPin) {
         ecu.setEngineType(engine_type_e.FRANKENSO_MIATA_NA6_MAP);
        ecu.changeRpm(1000);

        // making output pins available
        ecu.sendCommand(CMD_TRIGGER_SIMULATOR_PIN + " 0 none");
        ecu.sendCommand(CMD_TRIGGER_SIMULATOR_PIN + " 1 none");
        ecu.sendCommand(CMD_TRIGGER_PIN + " 1 none");

        // Hook up 1khz idle on formerly-trigger-stim pin
        ecu.sendCommand(CMD_IDLE_PIN + " " + idlePin);
        ecu.sendCommand("set idle_solenoid_freq 100");

        EcuTestHelper.assertSomewhatClose("VSS no input", 0, SensorCentral.getInstance().getValue(Sensor.vehicleSpeedKph));

        // attaching VSS to idle output since there is a jumper on test discovery
        ecu.sendCommand("set " + CMD_VSS_PIN + " " + vssPin);

        sleep(2 * Timeouts.SECOND);

        // todo: this command does not seem to work for whatever reasons :( cAsE? else?
        ecu.sendCommand("set " + "driveWheelRevPerKm" + " " + "500");
        EcuTestHelper.assertSomewhatClose("VSS with input", 145.58, SensorCentral.getInstance().getValue(Sensor.vehicleSpeedKph));

        // not related to VSS test, just need to validate this somewhere, so this random test is as good as any
        if (ControllerConnectorState.firmwareVersion == null)
            throw new IllegalStateException("firmwareVersion has not arrived");
    }
}
