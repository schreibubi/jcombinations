#!/bin/bash
#
# Copyright (C) 2009 Jörg Werner schreibubi@gmail.com
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
p=`pwd`

target="../../../ATLProgram/ProductBuild_T5571_001G_H70_S01_NORM_FULL_SINGLE_USED_USED_USED/"
seti="../../../ATLProductSource/setichains_001G_H70.xml"
j="../../../../devel_Asdap/Coordinator --conditionprefix pcf  --pretags 001G,H70 --posttags T5571 --patterndir $target --cbmdir $target --pcfdir $target --seti $seti"



## frontend

$j  --cbmOffset 1000 --ignoremissingcbmpos --combinations trim_generators_measure_idd.combinations  
$j  --cbmOffset 2000 --combinations trim_adrlines_measure_reference_voltages.combinations  
$j  --cbmOffset 3000 --combinations trim_current_measure_voltages_pads.combinations  
$j  --cbmOffset 4000 --combinations trim_disable_measure_directly.combinations  
$j  --cbmOffset 5000 --combinations trim_disable_measure_idd.combinations  
$j  --cbmOffset 6000 --combinations trim_disable_measure_network_leakage.combinations  
$j  --cbmOffset 7000 --combinations trim_generators.combinations  
$j  --cbmOffset 8000 --combinations trim_support_measure_idd.combinations  
$j  --cbmOffset 9000 --combinations trim_pasri.combinations  
$j  --cbmOffset 10000 --combinations trim_vdd_measure_reference_voltages_and_currents.combinations  
$j  --cbmOffset 11000 --combinations trim_current_measure_esd.combinations  
$j  --cbmOffset 12000 --combinations trim_srf.combinations
$j  --cbmOffset 13000 --combinations readtemp.combinations
$j  --cbmOffset 14000 --combinations redpadc.combinations
$j  --cbmOffset 15000 --combinations readefuse.combinations
$j  --cbmOffset 16000 --combinations burnefuse.combinations
$j  --cbmOffset 17000 --combinations pasr.combinations
$j  --cbmOffset 18000 --combinations trim_generators_bi.combinations  
$j  --cbmOffset 19000 --combinations trim_misc_measure_direct.combinations  
$j  --cbmOffset 20000 --combinations trim_vdd_measure_bi.combinations  
$j  --cbmOffset 21000 --combinations trim_vdd_measure_bi2.combinations  
$j  --cbmOffset 22000 --combinations trim_vdd_lowvdd.combinations  
$j  --cbmOffset 23000 --combinations cmadly.combinations  
$j  --cbmOffset 24000 --combinations h1ravln.combinations  
$j  --cbmOffset 25000 --combinations halfgood.combinations  
$j  --cbmOffset 26000 --combinations ravalid_falling.combinations   # XXX missing files
$j  --cbmOffset 27000 --combinations tmrasref.combinations  
$j  --cbmOffset 28000 --combinations trp.combinations  
$j  --cbmOffset 29000 --combinations hactmsetup.combinations  
$j  --cbmOffset 30000 --combinations actmspeed.combinations  
$j  --cbmOffset 31000 --combinations tmsaltc.combinations  
$j  --cbmOffset 32000 --combinations trasap.combinations  
$j  --cbmOffset 33000 --nocbmgen --combinations trim_odt.combinations  
$j  --cbmOffset 34000 --nocbmgen --combinations powerup.combinations   

# backend

$j   --cbmOffset 35000 --combinations hactmwritenormalread.combinations  
$j   --cbmOffset 36000 --combinations hactmcheckyaybycyd.combinations  
$j   --cbmOffset 44000 --combinations signal_margin.combinations  
$j   --cbmOffset 45000 --combinations trimmingbe.combinations   
$j   --cbmOffset 46000 --combinations misc1.combinations   

#cd ../../

#find package_tools/Asdap/ -name "*.mpa" -exec gln -sf {} . \;
#find package_tools/Asdap/ -name "b_source*.csv*" -exec gln -sf {} . \;
#find package_tools/Asdap/ -name "*.csv" -exec gln -sf {} . \;
#find package_tools/Asdap/ -name "*.dat" -exec gln -sf {} . \;


