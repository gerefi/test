#pragma once

#include "ac_control_generated.h"
#include <gerefi/timer.h>

class AcController : public ac_control_s, public EngineModule {
public:
    constexpr static int PRESSURE_DEADBAND_WIDTH = 5;

	using interface_t = AcController;

	void onSlowCallback() override;

	virtual bool isAcEnabled() const;

	Timer timeSinceStateChange;

private:
	bool getAcState();

	Timer m_timeSinceNoAc;
};
