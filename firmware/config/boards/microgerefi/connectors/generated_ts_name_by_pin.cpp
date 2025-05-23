//DO NOT EDIT MANUALLY, let automation work hard.

// auto-generated by PinoutLogic.java based on config/boards/microgerefi/connectors/J4.yaml
// auto-generated by PinoutLogic.java based on config/boards/microgerefi/connectors/iobox.yaml
// auto-generated by PinoutLogic.java based on config/boards/microgerefi/connectors/main.yaml
// auto-generated by PinoutLogic.java based on config/boards/microgerefi/connectors/pads.yaml
#include "pch.h"

// see comments at declaration in pin_repository.h
const char * getBoardSpecificPinName(brain_pin_e brainPin) {
	switch(brainPin) {
		case Gpio::A0: return "18 - AN temp 1";
		case Gpio::A1: return "23 - AN temp 2";
		case Gpio::A15: return "AUX J2 PA15";
		case Gpio::A2: return "24 - AN temp 3 or Hall Input";
		case Gpio::A3: return "22 - AN temp 4 or Hall Input";
		case Gpio::A4: return "28 - AN volt 10, Aux Reuse";
		case Gpio::A5: return "25 - Hall Cam";
		case Gpio::A6: return "26 - AN volt 2";
		case Gpio::A7: return "31 - AN volt 3";
		case Gpio::B0: return "36 - AN volt 8, Aux Reuse";
		case Gpio::B1: return "40 - AN volt 9, Aux Reuse";
		case Gpio::B10: return "AUX J13";
		case Gpio::B11: return "AUX J12";
		case Gpio::B7: return "AUX J18 PB7";
		case Gpio::B8: return "AUX J2 PB8";
		case Gpio::B9: return "AUX J2 PB9";
		case Gpio::C0: return "27 - AN volt 1";
		case Gpio::C12: return "AUX J2 PC12";
		case Gpio::C2: return "19 - AN volt 4";
		case Gpio::C3: return "20 - AN volt 5";
		case Gpio::C4: return "32 - AN volt 6, Aux Reuse";
		case Gpio::C5: return "30 - AN volt 7";
		case Gpio::C6: return "45 - VR/Hall Crank";
		case Gpio::D1: return "12 - Ignition 4";
		case Gpio::D2: return "11 - Ignition 3";
		case Gpio::D3: return "10 - Ignition 2";
		case Gpio::D4: return "9 - Ignition 1";
		case Gpio::D6: return "13 - GP Out 6";
		case Gpio::D7: return "14 - GP Out 5";
		case Gpio::E0: return "AUX J6";
		case Gpio::E5: return "AUX J11 PE5";
		case Gpio::E6: return "AUX J10 PE6";
		case Gpio::MSIOBOX_0_OUT_1: return "MS IO-Box 0 OUT 1";
		case Gpio::MSIOBOX_0_OUT_2: return "MS IO-Box 0 OUT 2";
		case Gpio::MSIOBOX_0_OUT_3: return "MS IO-Box 0 OUT 3";
		case Gpio::MSIOBOX_0_OUT_4: return "MS IO-Box 0 OUT 4";
		case Gpio::MSIOBOX_0_OUT_5: return "MS IO-Box 0 OUT 5";
		case Gpio::MSIOBOX_0_OUT_6: return "MS IO-Box 0 OUT 6";
		case Gpio::MSIOBOX_0_OUT_7: return "MS IO-Box 0 OUT 7";
		case Gpio::MSIOBOX_0_SW_1: return "MS IO-Box 0 SW1";
		case Gpio::MSIOBOX_0_VSS_1: return "MS IO-Box 0 VSS1 (VR)";
		case Gpio::MSIOBOX_0_VSS_2: return "MS IO-Box 0 VSS2 (VR)";
		case Gpio::MSIOBOX_0_VSS_3: return "MS IO-Box 0 VSS2 (Hall)";
		case Gpio::MSIOBOX_0_VSS_4: return "MS IO-Box 0 VSS3 (Hall)";
		case Gpio::TLE8888_PIN_1: return "37 - Injector 1";
		case Gpio::TLE8888_PIN_2: return "38 - Injector 2";
		case Gpio::TLE8888_PIN_21: return "35 - GP Out 1";
		case Gpio::TLE8888_PIN_22: return "34 - GP Out 2";
		case Gpio::TLE8888_PIN_23: return "33 - GP Out 3";
		case Gpio::TLE8888_PIN_24: return "43 - GP Out 4";
		case Gpio::TLE8888_PIN_3: return "41 - Injector 3";
		case Gpio::TLE8888_PIN_4: return "42 - Injector 4";
		case Gpio::TLE8888_PIN_5: return "3 - Lowside 2";
		case Gpio::TLE8888_PIN_6: return "7 - Lowside 1";
		default: return nullptr;
	}
	return nullptr;
}
