package com.gerefi.tools.tune;

import com.devexperts.logging.Logging;
import com.opensr5.ConfigurationImage;
import com.opensr5.ini.*;
import com.gerefi.binaryprotocol.BinaryProtocol;
import com.gerefi.binaryprotocol.MsqFactory;
import com.gerefi.config.generated.Integration;
import com.gerefi.config.generated.VariableRegistryValues;
import com.gerefi.enums.engine_type_e;
import com.gerefi.tune.xml.Constant;
import com.gerefi.tune.xml.Msq;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import static com.devexperts.logging.Logging.getLogging;
import static com.gerefi.tools.tune.TuneCanTool.ENGINE_TUNE_OUTPUT_FOLDER;

/**
 * see <a href="https://github.com/gerefi/gerefi/wiki/Canned-Tune-Process">...</a>
 */
public class WriteSimulatorConfiguration {
    private static final Logging log = getLogging(WriteSimulatorConfiguration.class);

    private static final String ROOT_FOLDER = System.getProperty("ROOT_FOLDER", "../simulator/");

    public static void main(String[] args) {
        if (args.length != 1)
            throw new IllegalArgumentException("One argument expected: .ini file name");
        String iniFileName = args[0];
        IniFileModelImpl ini = IniFileModelImpl.readIniFile(iniFileName);
        BinaryProtocol.iniFileProvider = signature -> ini;

        log.info("ROOT_FOLDER=" + ROOT_FOLDER);
        try {
            try {
                readBinaryWriteXmlTune(iniFileName, Integration.SIMULATOR_TUNE_BIN_FILE_NAME, ENGINE_TUNE_OUTPUT_FOLDER + TuneCanTool.DEFAULT_TUNE, ini);
            } catch (Throwable e) {
                throw new IllegalStateException("White default tune", e);
            }
            for (engine_type_e type : WriteSimulatorEngines.ENGINE_TYPE_ES) {
                writeSpecificEngineType(iniFileName, type, ini);
            }
            //TuneCanToolRunner.runPopular();
        } catch (Throwable e) {
            log.error("Unfortunately", e);
            System.exit(-1);
        } finally {
            // No way to set Process.exec to be a daemon, we need explicit exit
            System.exit(0);
        }
    }

    private static void writeSpecificEngineType(String iniFileName, engine_type_e engineType, IniFileModelImpl ini) {
        try {
            String in = Integration.SIMULATOR_TUNE_BIN_FILE_NAME_PREFIX + "_" + engineType.ordinal() + Integration.SIMULATOR_TUNE_BIN_FILE_NAME_SUFFIX;
            readBinaryWriteXmlTune(iniFileName, in,
                TuneCanTool.getDefaultTuneOutputFileName(engineType), ini);
        } catch (Throwable e) {
            throw new IllegalStateException("With " + engineType, e);
        }
    }

    private static void readBinaryWriteXmlTune(String iniFileName, String inputBinaryTuneFileName, String outputXmlFileName, IniFileModel ini) throws JAXBException, IOException {
        // we have to use board-specific .ini to account for all the board-specific offsets
        // INI_FILE_FOR_SIMULATOR is just not universal enough
        if (ini == null)
            throw new IllegalStateException("Not found " + iniFileName);
        byte[] fileContent = Files.readAllBytes(new File(ROOT_FOLDER + inputBinaryTuneFileName).toPath());
        int pageSize = ini.getMetaInfo().getTotalSize();
        log.info("Got " + fileContent.length + " from " + inputBinaryTuneFileName + " while expecting " + pageSize);
        if (fileContent.length != pageSize)
            throw new IllegalStateException("Unexpected image size " + fileContent.length + " while expecting " + pageSize);
        ConfigurationImage configuration = new ConfigurationImage(fileContent);
        log.info("Got " + Objects.requireNonNull(configuration, "configuration"));
        Msq m = MsqFactory.valueOf(configuration, ini);
        String name = "KNOCKNOISERPMBINS";
        Constant noiseRpmBins = m.page.get(1).getConstantsAsMap().get(name);
        if (!noiseRpmBins.getValue().contains(VariableRegistryValues.DEFAULT_RPM_AXIS_HIGH_VALUE + ".0"))
            throw new IllegalStateException(name + " canary wonders if everything is fine?");
        m.writeXmlFile(outputXmlFileName);

        Msq newTuneJustToValidate = Msq.readTune(ROOT_FOLDER + outputXmlFileName);
        log.info("Looks valid " + newTuneJustToValidate);
    }
}
