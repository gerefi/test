package com.gerefi.nucleo;

import com.devexperts.logging.Logging;
import com.gerefi.RusefiTestBase;
import com.gerefi.f4discovery.DiscoveryPwmHardwareTest;
import org.junit.Test;

import static com.devexperts.logging.Logging.getLogging;

public class NucleoPwmHardwareTest extends RusefiTestBase {
    private static final Logging log = getLogging(DiscoveryPwmHardwareTest.class);

    @Override
    protected boolean needsHardwareTriggerInput() {
        // This test uses hardware trigger input!
        return true;
    }

    @Test
    public void testIdlePin() {
        // todo!
        // PwmHardwareTestLogic.runIdlePwmTest(ecu, "PD2", "PA6");
    }
}
