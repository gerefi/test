# Trigger Decoders

This folder is and should not be aware of engine.h or engine_configuration.h

# Hints on adding new trigger

Step 1: add into gerefi_config.txt

Step 2: add into gerefi_enums.h, update TT_UNUSED, invoke gen_enum script

Step 3: get it working.

It's useful to un-comments *AllTriggersFixture* line in unit_tests/main.cpp

It's useful to add setVerboseTrigger(true) into unit tests while troubleshooting fresh trigger code

## Sometimes you better add a unit test

See unit_tests/tests/trigger/resources for logic analyzer files we test against
