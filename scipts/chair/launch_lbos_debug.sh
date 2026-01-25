echo Launching LaissezOS..
set -x
sudo systemctl restart systemd-networkd
cd ~
export DISPLAY=:0
sudo -E /home/pwisneskey/.sdkman/candidates/java/current/bin/java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 -jar lbos.jar -m CHAIR -p Chair
#read -n 1 -s -r -p "Press any key to continue..."