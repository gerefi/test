/*
 * @file engine_parts.h
 *
 * @date May 27, 2019
 * @author Andrey Belomutskiy, (c) 2012-2020
 */

#pragma once

#include "static_vector.h"
#include <gerefi/timer.h>

#define MOCK_ADC_SIZE 26

struct Accelerometer {
	float lat = 0; // G value
	float lon = 0;
	float vert = 0;
	float yawRate = 0;
};

struct SensorsState {
	Accelerometer accelerometer;
};

class TransmissionState {
public:
	gear_e gearSelectorPosition;
};

struct warning_t {
	Timer LastTriggered;
	ObdCode Code = ObdCode::None;

	warning_t() { }

	explicit warning_t(ObdCode code)
		: Code(code)
	{
	}

	// Equality just checks the code, timer doesn't matter
	bool operator ==(const warning_t& other) const {
		return other.Code == Code;
	}

	// Compare against a plain OBD code
	bool operator ==(const ObdCode other) const {
		return other == Code;
	}
};

typedef static_vector<warning_t, 24> warningBuffer_t;

class WarningCodeState {
public:
	WarningCodeState();
	void addWarningCode(ObdCode code);
	bool isWarningNow() const;
	bool isWarningNow(ObdCode code) const;
	void clear();
	int warningCounter;
	ObdCode lastErrorCode = ObdCode::None;

	Timer timeSinceLastWarning;

	// todo: we need a way to post multiple recent warnings into TS
	warningBuffer_t recentWarnings;
};

struct multispark_state
{
	efidur_t delay = 0;
	efidur_t dwell = 0;
	uint8_t count = 0;
};
