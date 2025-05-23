#pragma once

#include "gppwm.h"

#include "gerefi_types.h"

struct gppwm_channel;
class OutputPin;
struct IPwm;
class ValueProvider3D;

struct GppwmResult {
	percent_t Result;
	float X;
	float Y;
};

class GppwmChannel {
public:
	void init(bool usePwm, IPwm* pwm, OutputPin* outputPin, const ValueProvider3D* table, const gppwm_channel* config);
	GppwmResult update(size_t index);
	GppwmResult getOutput(size_t index) const;

	// Returns actual output duty, with hysteresis applied
	float setOutput(float result);

private:
	// Store the current state so we can apply hysteresis
	bool m_state = false;

	// Configuration fields
	const gppwm_channel* m_config = nullptr;
	bool m_usePwm = false;
	IPwm* m_pwm = nullptr;
	OutputPin* m_output = nullptr;
	const ValueProvider3D* m_table = nullptr;
};
