# TunerStudio

For TS Protocol details see https://www.tunerstudio.com/index.php/support/manuals/tsdevmanuals/137-ecu-definition-specification-aka-the-ini-document

This directory contains the initialization and configuration files for the
gerEFI interface to TunerStudio.

The primary contents are a set ```gerefi*.ini``` initialization files, used to
configure TunerStudio to setup and monitor a specific ECU board.  These are
the only files a typical end user needs.

The ```translations``` directory contains non-English-language translations
for TunerStudio.

The initialization files are automatically generated from a combination
of input files located both in this directory and in the board-specific
directories .

```gerefi*.ini``` files are generated based on the following four inputs:
1) ```gerefi_config.txt``` contains configuration region definition in proprietary text format.
2) ```tunerstudio.template.ini``` contains the UI - all the menus and dialogs. UI definition starts at ```menuDialog = main```
line - here you will see all top level menus defined with internal IDs and visible text labels.
3) ```mapping.yaml``` is a minor detail related to how pins are named in drop downs
4) ```prepend.txt``` is a minor detail which allows you to hide elements of the UI using ```@@if_XXX``` syntax.


The combined file is generated by ConfigDefinition.jar tool.
On Windows this may be run with ```gen_config.bat```.


Q: how do I offer my changes to TS project?

A: please PR only tunerstudio.template.ini. Once merged, gen_config.bat would be executed automatically and results would be pushed by automation.
