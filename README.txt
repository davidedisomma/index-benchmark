Passi da eseguire. Documento di riferimento si trova all'url http://it.wikipedia.org/wiki/Aiuto:Analisi_del_database

1- Installare versione di Mysql

2- Creare utente secondo le istruzioni di questo link http://dev.mysql.com/doc/refman/5.1/en/adding-users.html

3- Creare database come spiegato nel documento di riferimento 

4- Scaricare mwdumper.jar per importare velocemente grandi quantità di dati. Ocio che la versione buona è di questo link 
http://downloads.dbpedia.org/mwdumper.jar 
come segnalato in questo link 
https://bugzilla.wikimedia.org/show_bug.cgi?id=18328
A questo url ci sono anche le istruzioni
http://www.mediawiki.org/wiki/Manual:MWDumper

5- Scaricare da 
http://svn.wikimedia.org/viewvc/mediawiki/trunk/phase3/maintenance/tables.sql?view=co

le tabelle del DB e crearle su MySql seguendo i consigli del manuale di riferimento

6-Scaricare il dump xml dall'url
http://download.wikimedia.org/enwiki/20101011/pages-meta-current.xml.bz2

7-Eseguire 
java -jar mwdumper.jar --format=sql:1.5 itwiki-latest-pages-meta-current.xml.bz2 | mysql -u<utente> -p itwiki