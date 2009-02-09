@REM
@REM Copyright (C) 2009 Jörg Werner schreibubi@gmail.com
@REM
@REM This program is free software: you can redistribute it and/or modify
@REM it under the terms of the GNU General Public License as published by
@REM the Free Software Foundation, either version 3 of the License, or
@REM (at your option) any later version.
@REM
@REM This program is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@REM GNU General Public License for more details.
@REM
@REM You should have received a copy of the GNU General Public License
@REM along with this program.  If not, see <http://www.gnu.org/licenses/>.
@REM
@REM This program is free software: you can redistribute it and/or modify
@REM it under the terms of the GNU General Public License as published by
@REM the Free Software Foundation, either version 3 of the License, or
@REM (at your option) any later version.
@REM
@REM This program is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@REM GNU General Public License for more details.
@REM
@REM You should have received a copy of the GNU General Public License
@REM along with this program.  If not, see <http://www.gnu.org/licenses/>.
@REM




%%BEGIN

# Use tesim wrapper (default: "Wrapper", available choices: "Wrapper", "Manual", "Auto", "Dummy")
:~o TesterController "Wrapper"

my $sessiondata = "../SessionData/";
# destination directory for the PCF.dat
my $testdefinitiondata = "../../TestDefinitionData/";
# where to find the different pcf files
my $pcfdata = "../../ATLProgram/ProductBuild_T5571_001G_H70_S01_NORM_FULL_SINGLE/Template/";
my $txtsharefactor = 'SINGLE';
my $testflow = 'A2PN6XBC';
my $programname = 'BMP6';
my @pcf =('pcf_trim_generators',
'pcf_trim_adrlines_measure_reference_voltages',
'pcf_trim_current_measure_voltages_pads',
'pcf_trim_vdd_measure_reference_voltages_and_currents',
'pcf_trim_disable_measure_directly',
'pcf_trim_generators_measure_idd',
'pcf_trim_disable_measure_idd',
'pcf_trim_generators_bi',
'pcf_trim_current_try',
'pcf_trim_misc_measure_direct',
'pcf_trim_vdd_measure_bi',
'pcf_trim_vdd_measure_bi2',
'pcf_trim_vdd_lowvdd',
'pcf_trim_test',
'pcf_cmadly',
'pcf_h1ravln',
'pcf_hactmcheckycyddctl',
'pcf_halfgood',
'pcf_ravalid_falling',
'pcf_tmrasref',
'pcf_trp',
'pcf_hactmsetup',
'pcf_actmspeed',
'pcf_trim_odt',
'pcf_powerup');


:~o Shell "rm -rf ${sessiondata}/BatchMode";
:~o Shell "mkdir ${sessiondata}/BatchMode";

# Disable jam data read-out (because it requires more commands for GPIB)
:~o XML "${sessiondata}/Session.xml" "/OctopusDataFile/Session/Batch/Settings/DataFileOptions/JamData/@switch" "off"

# Prepare share factor attribute in Session.xml
:~o XML "${sessiondata}/Session.xml" "/OctopusDataFile/Session/TestEquipment/\@sharedFactor" "$sharefactor"

# Prepare test flow
:~o XML "${sessiondata}/Session.xml" "/OctopusDataFile/Batch/Settings/TestFlow" "$testflow"

# PCF on
:~o XML "${sessiondata}/Session.xml" "/OctopusDataFile/Session/PCF/\@switch" "on"
# PCF size, never go above 200 because otherwise the pre-defined array-sizes are exhausted.
:~o XML "${sessiondata}/Session.xml" "/OctopusDataFile/Session/PCF/\@blockSize" "190"
# DCLog on
:~o XML "${sessiondata}/Session.xml" "/OctopusDataFile/Batch/Settings/DataFileOptions/DCData/\@switch" "on"

# For each pcf
for ( $s = 0; $s < scalar( @pcf ); $s++ ) {

    # Copy PCF
    :~o Shell "cp ${pcfdata}/$pcf[$s] ${testdefinitiondata}/PCF.dat"

    # Prepare batch name in Session.xml
    :~o XML "${sessiondata}/Session.xml" "/OctopusDataFile/Batch/\@number" "$pcf[$s]"

    # Initialize Octopus Session
    :~o Cmd "Start Octopus"

    # "Ctrl-C" + sleep 1 sec, to make sure the tester controller is available
    :~a ATL "\c"  # ASCII value for "Ctrl-C"
    :~a ATL "\c"  # ASCII value for "Ctrl-C"
    :~a ATL "\c"  # ASCII value for "Ctrl-C"
    :~a ATL "\c"  # ASCII value for "Ctrl-C"
    :~o Shell "sleep 1"

    # CBMONIT
    :~a ATL "/CBMONIT\n\n\n"
    :~a Expect "="

    # Set program name
    :~a ATL "O11$programname\n\n"

    # Clear buffer and set INI key
    :~a ATL "U2"  # Clear cache
    :~a Expect "MODE NO"
    :~a ATL "O18E\n\n"
    :~a Expect "SPECIFY"

    # Start INIT run
    :~a ATL " "
    :~o Sync "EVENT_TEST_SESSION_STARTED"
    :~o Cmd "Load Lot"
    :~o Sync "EVENT_LOT_LOADED"

    :~o Shell "sleep 1"  # Horrible hack, because 'space' is not buffered by the tesim process

    # One of both approaches can be used
    :~a ATL " "
    :~o Sync "EVENT_TOUCHDOWN_ENDED"

    # Finalize session and copy files into the BatchMode directory
    :~o Cmd "Stop Batch"
    :~o Sync "EVENT_TEST_SESSION_ENDED"
    :~o Shell "cp ${sessiondata}/Output/*.asc ${sessiondata}/BatchMode"
    :~o Shell "cp ${sessiondata}/Output/LotResults* ${sessiondata}/BatchMode"
    :~o Cmd "Stop Octopus"

    # Toy
    :~o GUI "Toggle Sidebar"
    :~o Shell "sleep 3"  # Also useful to make sure the Octopus processes are really ended
    :~o GUI "Toggle Sidebar"
}

%%END
