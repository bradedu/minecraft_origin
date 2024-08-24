@echo off
::java -Xms4G -Xmx8G  -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:MaxGCPauseMillis=100 -XX:+DisableExplicitGC -XX:TargetSurvivorRatio=90 -XX:G1NewSizePercent=50 -XX:G1MaxNewSizePercent=80 -XX:InitiatingHeapOccupancyPercent=10 -XX:G1MixedGCLiveThresholdPercent=50 -XX:+AlwaysPreTouch -Dusing.aikars.flags=mcflags.emc.gs -jar spigot-1.19
G:
cd G:\game\minecraft_server\original_server\
java -Xms4G -Xmx8G  -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:MaxGCPauseMillis=100 -XX:+DisableExplicitGC -XX:TargetSurvivorRatio=90 -XX:G1NewSizePercent=50 -XX:G1MaxNewSizePercent=80 -XX:InitiatingHeapOccupancyPercent=10 -XX:G1MixedGCLiveThresholdPercent=50 -XX:+AlwaysPreTouch -Dusing.aikars.flags=mcflags.emc.gs -jar spigot-1.21.1.jar

pause

:: java -Xms2G -Xmx4G -XX:MaxPermSize=128M -jar spigot-1.18.jar
