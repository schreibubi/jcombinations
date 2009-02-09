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

x=/home/butkus/ASDAP01H46/GASDAPFE71/ATLProgram/ProductBuild_T5571_002G_H46_SINGLE_USED_USED_USED_USED_1.3/

bsub -I /home/butkus/ASDAP01H46/DEVEL/devel_Asdap/Coordinator  --conditionprefix pcf -h pcf.header_T5571 --pretags 002G,H46 --posttags T5571  --combinations trim_test.combinations --patterndir $x --cbmdir $x --pcfdir . --seti /home/butkus/ASDAP01H46/GASDAPFE71/ATLProductSource/setichains_002G_H46.xml  


