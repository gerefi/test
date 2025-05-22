package com.gerefi.tools.tune;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static com.gerefi.LocalIniFileProvider.INI_FILE_FOR_SIMULATOR;

public class TuneCanToolRunner extends TuneCanTool {
    static {
        TuneCanToolHelper.initialize(INI_FILE_FOR_SIMULATOR);
    }

    //    public static void main(String[] args) throws JAXBException, IOException {
//        runPopular();
//    }

    public static void runPopular() throws JAXBException, IOException {
        // while adding a line here make sure corresponding line is at gerEfiFunctionalTest.cpp
        // https://github.com/gerefi/gerefi/issues/4038
//        processREOtune(1621, engine_type_e.HONDA_OBD1, "Honda-OBD1", "");
//        processREOtune(985, engine_type_e.MAZDA_MIATA_NB2, "MazdaMiataNB2", "nb2");
//        processREOtune(1508, engine_type_e.HELLEN_154_HYUNDAI_COUPE_BK1, "COUPE-BK1", "coupleBK1");
//        processREOtune(1507, engine_type_e.HELLEN_154_HYUNDAI_COUPE_BK2, "COUPE-BK2", "coupleBK2");
//        processREOtune(1626, engine_type_e.HYUNDAI_PB, "PB", "pb");
//        processREOtune(1591, engine_type_e.BMW_M52, "M52", "");
//        processREOtune(1641, engine_type_e.HELLEN_121_NISSAN_6_CYL, "VQ", "");
//        processREOtune(1622, engine_type_e.MERCEDES_M111, "m111-alex", "");
    }
}
