#!/bin/bash

#while :
#do
	date=`date +"%Y%m%d"`
	git add world logs world_nether world_the_end res
	git commit -m "auto back up - $date"
	git push origin master
#	sleep 604800
#done

