
export hudId=$(DISPLAY=:0 xinput | grep 'WaveShare WaveShare' | sed  -n "s/^.*id=\([[:digit:]]\+\).*$/\1/p")
export cpId=$(DISPLAY=:0 xinput | grep 'WaveShare WS170120' | sed  -n "s/^.*id=\([[:digit:]]\+\).*$/\1/p")

DISPLAY=:0 xset s off
DISPLAY=:0 xset dpms 1800 1800 1800

DISPLAY=:0 xinput -map-to-output $hudId HDMI-1
DISPLAY=:0 xinput -map-to-output $cpId HDMI-2

DISPLAY=:0 xhost +

sudo DISPLAY=:0 java -jar lbos.jar -m CHAIR -p Chair