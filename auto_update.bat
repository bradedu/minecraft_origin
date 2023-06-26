set mydate=%date:~0,10%

cd /D G:\game\minecraft_server\original_server

git add world logs world_nether world_the_end res*
git commit -m "auto back up - %mydate%"
git push origin master

pause