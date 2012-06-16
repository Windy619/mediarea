#SAUVEGARDE REGULIERE DE LA BASE DE DONNEES
#Pour des raisons de sécurité et de préservation « totale » des données, une politique de sauvegarde de la base de données doit être mise en place.
#->	Tous les soirs, un backup total de la base de données est effectué. 
#->	Ces backups sont gardés pendant 1 mois.
#->	À la fin du mois, on ne préserve qu’un backup par semaine (celui du dimanche soir et du dernier jour du mois).
#->	À la fin de l’année, on ne préserve qu’un backup par mois (le dernier de chaque mois).




# Pour une sauvegarde quotidienne
# /home/MediArea/backup/backup.sh >> /home/MediArea/backup/save-mysql.log

#!/bin/sh
# sauvegarde des bases de données hebdommadaire
USER=root
PASS=root
DBHOST=88.191.133.150
DESTINATION=/home/MediArea/backup

DATE=`date +%Y-%m-%d`
TIME=`date +%H:%M`
DAY=`date +%d`
DEL=`TZ=CST+24 date +%Y-%m-%d`

  
echo "[START] $DATE $TIME" >> $DESTINATION/save-mysql.log

rm -rf $DESTINATION/*.sql.gz

# backup de la semaine
mysqldump -u $USER -p$PASS --host=$DBHOST MediArea | gzip > $DESTINATION/MediArea-$TIME.$DATE.sql.gz
  
#répéter les 2 lignes précédentes autant de fois que de bases à sauvegarder


RESULT=$?
[ "$RESULT" -eq 0 ] && echo "[INFO] Sauvegarde réussie" >> $DESTINATION/save-mysql.log || echo "[INFO] Echec de la sauvegarde" >> $DESTINATION/save-mysql.log

echo -e "[FIN] `date +%H:%M` \n" >> $DESTINATION/save-mysql.log

exit 0

# efface le backup de la veille
#rm -rf $DESTINATION/*.$DEL.sql.gz
