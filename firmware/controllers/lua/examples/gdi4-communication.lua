-- scriptname gdi4-communication.lua
--
-- '-- scriptname FILENAME' is a preprocessor directive allowing console to reload script from disk
--
-- use console, Lua tab, 'More...' to point at folder with Lua files
-- use 'Reload' for includes to be processed
-- this is all about code reuse and console instability
--
-- '-- include XXX' and '-- endinclude' are the directive for code reuse
--

-- communication with https://github.com/gerefi/gerefi-hardware/tree/main/GDI-4ch/firmware
-- hpfpbench

busIndex = 1

-- see can_common.h
GDI4_BASE_ADDRESS = 0xBB20
GDI_CHANGE_ADDRESS = GDI4_BASE_ADDRESS + 0x10

-- include utils-print-array.lua
-- endinclude

function printPacket(bus, id, dlc, data)
--	print('Received status packet ' ..arrayToString(data))
end

function getTwoBytesLSB(data, offset, factor)
	return (data[offset + 2] * 256 + data[offset + 1]) * factor
end

function setTwoBytesLsb(data, offset, value)
  if value > 1000000000 then
    print(value .. " looks like NA EFI_LUA_LOOKUP missing?")
    return
  end
	value = math.floor(value)
	data[offset + 2] = value >> 8
	data[offset + 1] = value & 0xff
end

function onCanConfiguration1(bus, id, dlc, data)
  if bus ~= busIndex then
    print("******************* WRONG GDI BUS ************************** " .. bus)
    return
  end

	print("Received configuration1 "..arrayToString(data))
	print("GDI4 says BoostVoltage  "..getTwoBytesLSB(data, 0, 1) )
	print("GDI4 says BoostCurrent  "..getTwoBytesLSB(data, 2, 1 / 128) )
	print("GDI4 says TBoostMin     "..getTwoBytesLSB(data, 4, 1) )
	print("GDI4 says TBoostMax     "..getTwoBytesLSB(data, 6, 1) )
end

function onCanConfiguration2(bus, id, dlc, data)
	print("Received configuration2 "..arrayToString(data))
	print("GDI4 says PeakCurrent   "..getTwoBytesLSB(data, 0, 1 / 128) )
	print("GDI4 says TpeakDuration "..getTwoBytesLSB(data, 2, 1) )
	print("GDI4 says TpeakOff      "..getTwoBytesLSB(data, 4, 1) )
	print("GDI4 says Tbypass       "..getTwoBytesLSB(data, 6, 1) )
end

function onCanConfiguration3(bus, id, dlc, data)
	print("Received configuration3 "..arrayToString(data))
	print("GDI4 says HoldCurrent   "..getTwoBytesLSB(data, 0, 1 / 128) )
	print("GDI4 says TholdOff      "..getTwoBytesLSB(data, 2, 1) )
	print("GDI4 says THoldDuration "..getTwoBytesLSB(data, 4, 1) )
	pumpPeak = getTwoBytesLSB(data, 6, 1 / 128)
	print("GDI4 says PumpPeakCurrent ".. pumpPeak)
	setLuaGauge(1, pumpPeak)
end

function onCanConfiguration4(bus, id, dlc, data)
	print("Received configuration4 "..arrayToString(data))
	print("GDI4 says PumpHoldCurrent "..getTwoBytesLSB(data, 0, 1 / 128) )
end

function onCanVersion(bus, id, dlc, data)
    year = data[1] * 100 + data[2]
    month = data[3]
    day = data[4]
    print ("GDI4 firmware " .. year .. '/' .. month .. '/' .. day)
end

canRxAdd(GDI4_BASE_ADDRESS, printPacket)
canRxAdd(GDI4_BASE_ADDRESS + 1, onCanConfiguration1)
canRxAdd(GDI4_BASE_ADDRESS + 2, onCanConfiguration2)
canRxAdd(GDI4_BASE_ADDRESS + 3, onCanConfiguration3)
canRxAdd(GDI4_BASE_ADDRESS + 4, onCanConfiguration4)
canRxAdd(GDI4_BASE_ADDRESS + 5, onCanVersion)

GDI4_CAN_SET_TAG = 0x78
local data_set_settings = { GDI4_CAN_SET_TAG, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }

FIXED_POINT = 128

setTickRate(10)

counter = 0
packet_kinds = 5

function onTick()
  counter = counter + 1

  if (counter % packet_kinds) == 0 then
    -- set mc33_hvolt 60
	  boostVoltage     = getCalibration("mc33_hvolt")
	  boostCurrent     = getCalibration("mc33_i_boost") / 1000.0
	  TBoostMin        = getCalibration("mc33_t_min_boost")
	  print("To send 0: boostVoltage " .. boostVoltage .." boostCurrent " ..boostCurrent .." TBoostMin " ..TBoostMin)
	  setTwoBytesLsb(data_set_settings, 1, boostVoltage)
	  setTwoBytesLsb(data_set_settings, 3, boostCurrent * FIXED_POINT)
	  setTwoBytesLsb(data_set_settings, 5, TBoostMin)
--	print('Will be sending ' ..arrayToString(data_set_settings))
	  txCan(busIndex, GDI_CHANGE_ADDRESS, 1, data_set_settings)
	end

  if (counter % packet_kinds) == 1 then
  	-- set mc33_t_max_boost 380
	  TBoostMax        = getCalibration("mc33_t_max_boost")
    -- set mc33_i_peak 14500
	  peakCurrent      = getCalibration("mc33_i_peak") / 1000.0
	  TpeakDuration    = getCalibration("mc33_t_peak_tot")
	  print("To send 1: TBoostMax " .. TBoostMax .." peakCurrent " ..peakCurrent .." TpeakDuration " ..TpeakDuration)
	  setTwoBytesLsb(data_set_settings, 1, TBoostMax)
	  setTwoBytesLsb(data_set_settings, 3, peakCurrent * FIXED_POINT)
	  setTwoBytesLsb(data_set_settings, 5, TpeakDuration)
--	print('Will be sending ' ..arrayToString(data_set_settings))
  	txCan(busIndex, GDI_CHANGE_ADDRESS + 1, 1, data_set_settings)
  end

  if (counter % packet_kinds) == 2 then
    -- set mc33_t_peak_off 14
	  TpeakOff         = getCalibration("mc33_t_peak_off")
    -- set mc33_t_bypass 13
  	Tbypass          = getCalibration("mc33_t_bypass")
	  holdCurrent = getCalibration("mc33_i_hold") / 1000.0
	  print("To send 2: TpeakOff " .. TpeakOff .. " Tbypass " .. Tbypass .." holdCurrent " ..holdCurrent)
	  setTwoBytesLsb(data_set_settings, 1, TpeakOff)
	  setTwoBytesLsb(data_set_settings, 3, Tbypass)
	  setTwoBytesLsb(data_set_settings, 5, holdCurrent * FIXED_POINT)
--	print('Will be sending ' ..arrayToString(data_set_settings))
  	txCan(busIndex, GDI_CHANGE_ADDRESS + 2, 1, data_set_settings)
  end

  if (counter % packet_kinds) == 3 then
	  TholdOff = getCalibration("mc33_t_hold_off")
	  THoldDuration = getCalibration("mc33_t_hold_tot")
	  pumpPeakCurrent      = getCalibration("mc33_hpfp_i_peak")
    print("To send 3: TholdOff " .. TholdOff .. " THoldDuration " .. THoldDuration .. " pumpPeakCurrent " .. pumpPeakCurrent)
	  setTwoBytesLsb(data_set_settings, 1, TholdOff)
	  setTwoBytesLsb(data_set_settings, 3, THoldDuration)
-- set mc33_hpfp_i_peak 6
	  setTwoBytesLsb(data_set_settings, 5, pumpPeakCurrent * FIXED_POINT)
--	print('Will be sending ' ..arrayToString(data_set_settings))
	  txCan(busIndex, GDI_CHANGE_ADDRESS + 3, 1, data_set_settings)
	end

  if (counter % packet_kinds) == 4 then
  	pumpHoldCurrent      = getCalibration("mc33_hpfp_i_hold")
    print("To send 4: pumpHoldCurrent " .. pumpHoldCurrent)
	  setTwoBytesLsb(data_set_settings, 1, pumpHoldCurrent * FIXED_POINT)
	  outputCanID = GDI4_BASE_ADDRESS
	  setTwoBytesLsb(data_set_settings, 3, outputCanID)
--	print('Will be sending ' ..arrayToString(data_set_settings))
	  txCan(busIndex, GDI_CHANGE_ADDRESS + 4, 1, data_set_settings)
	end

	-- selfStimulateRPM(3000)
end


