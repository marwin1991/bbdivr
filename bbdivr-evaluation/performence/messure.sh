while true; 
do
	date +"%H:%M:%S" | cat >> ./`date -u +"%Y%m%d.csv"`; 
	docker stats --no-stream | cat >> ./`date -u +"%Y%m%d.csv"`; 
	sleep 1; 
done

