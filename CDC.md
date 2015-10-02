# DESCRIPTION DU PROJET #

## CONTEXTE ##
Dans le cadre de la formation que dispense l’ITII Alsace, nous sommes amenés à créer et gérer un projet informatique par équipe de quatre.
Notre équipe se compose de :
  * Florence Marchand
  * Benjamin Marzolf
  * Julien Metzmeyer
  * David Sobczak

Après la chute du géant MegaUpload, le monde du partage de vidéo s’est vu amputé de son plus gros représentant. La fin du quasi-monopole de MegaUpload laisse entre ouvert une brèche dans laquelle nombre de sociétés souhaitent s’engouffrer. Ses anciens utilisateurs étant assoiffés de partage, nous  souhaitons récupérer une partie de cette clientèle délaissée et leur permettre d’échanger un grand nombre de vidéos **légales**.
C’est ainsi qu’est né l’idée de **MediArea**.

## PROJET MEDIAREA ##
Plus sérieusement, l’idée de MediArea est en réalité née de la combinaison de trois monstres du net :
  * MegaUpload
  * YouTube
  * Deezer
Au final, MediArea pourrait représenter une alternative à ces trois services.

### QUE PROPOSE MEDIAREA ? ###
Le but de MediArea est de proposer un espace libre où il fait bon vivre et où le partage, l’upload et la création de média n’ont plus de limites.
Deux types de médias peuvent être ajoutés à MediArea : les vidéos et les sons.
MediArea propose une grande variété de fonctionnalités, dont les principales sont :
  * Mise en ligne / téléchargement de média
  * Visionnage de vidéos de haute définition et écoute de sons de haute qualité
  * Gestion d’un réseau social

### A QUI S’ADRESSE MEDIAREA ? ###
MediArea est créé pour être apprécié et utilisé par tous ! Jeune, grand, moins jeune, moins grand, tout le monde a sa place et est libre de partager tous ses médias avec le reste de la communauté !

<br /><br />
# SPÉCIFICATIONS FONCTIONNELLES #

## CRÉATION D’UN COMPTE SUR LA PLATEFORME ##
Il doit être possible de créer un compte sur la plateforme :
| **Nom champ** | **Description** | **Valeur par défaut** |
|:--------------|:----------------|:----------------------|
| **Adresse mail valide** | Adresse mail de la personne qui s’enregistre. Ce champ nous permet d’identifier un membre. Une vérification de l’adresse email sera effectuée. | -                     |
| Pseudo        | Pseudo de l’utilisateur | -                     |
| Nom           | Nom de l’utilisateur | -                     |
| Prénom        | Prénom de l’utilisateur | -                     |
| **Date de naissance** | Date de naissance de l’utilisateur (Ce champ nous permet de contrôler que l’utilisateur est majeur s’il souhaite visualiser une vidéo réservée à un public adulte). | -                     |
| Avatar        | Icône représentant l’utilisateur. L’utilisateur aura le choix dans une liste d’icônes prédéfinis. | -                     |
| Intérêts      | Hobbies, passions... | -                     |
| Portée du profil | La portée du profil spécifie si le profil est de type Public ou Privé. Un profil public est visible de tous, tandis que le profil privé n’est accessible qu’à ses amis. | Privé                 |
| Lien vers les conditions générales d’utilisations | Lien permettant à l’utilisateur de connaître les règles qu’il doit s’engager à respecter en entrant sur le site. | -                     |
| **Accepter les conditions générales d’utilisations** | Checkbox que l’utilisateur doit cocher spécifiant que celui-ci a lu et qu’il accepte les conditions générales d’utilisation du service MediArea | Non coché             |
| **Captcha**   | Le captcha est une image représentant un texte. Le captcha doit être recopié par l’utilisateur dans un champ approprié afin de démontrer que celui-ci n’est pas un robot. | -                     |
| Enregistrer   | Un bouton permettant de valider le formulaire | -                     |

Certains champs du formulaire seront vérifiés à la volée afin que l’utilisateur soit directement informé d’une quelconque erreur.
L’utilisateur sera obligé de remplir le champ pseudo.
Les champs en fond gris représentent les champs obligatoires.
La création d’un utilisateur engendrera la création automatique d’une Playlist personnelle donc non visible par tous et de type Favoris.

## LOGIN ##

### CONNEXION ###
Le formulaire de connexion comprend les champs suivant :
| **Nom** | **Description** |
|:--------|:----------------|
| Login   | Adresse Mail de l'utilisateur |
| Mot de passe | Mot de passe de l’utilisateur |
| Valider | Validation du formulaire |

En outre, l’utilisateur, lors de la connexion doit pouvoir avoir accès à des solutions de récupération de mot de passe.
La méthode de la « question secrète » n’a pas été retenue, car source de faille de sécurité. Nous allons donc opter pour une récupération du mot de passe par adresse email :
Un lien dans la page de connexion nommé « Vous avez oublié votre mot de passe ? » pointe vers une nouvelle page qui contient le formulaire suivant :
| **Nom** | **Description** |
|:--------|:----------------|
| Adresse mail | Adresse email du compte à récupérer |
| Valider | Validation du formulaire |


Une fois le formulaire validé, nous allons vérifier l’adresse email et renvoyer un mot de passe généré aléatoirement.

L’utilisateur connecté peut accéder à TOUTES les fonctionnalités du système. Pour l’utilisateur non identifié, en revanche, seul l’accès et le visionnage des médias est possible.

### DECONNEXION ###
Un bouton « Déconnexion » permettra à l’utilisateur enregistré de fermer sa session.

## GESTION DU PROFIL ##
Cette page affiche les informations personnelles que l’utilisateur a bien voulu renseigner.<br />
L’utilisateur aura la possibilité de modifier toutes ses informations personnelles via un lien « modifier » qui le renverra vers un formulaire de modification de champs.


## GESTION DES MESSAGES ##
Notre service propose la gestion d’une messagerie pour les utilisateurs. Cette messagerie doit permettre l’envoi de messages privés.

### MESSAGES PRIVE ###
Les messages privés sont des messages envoyés d’utilisateur en utilisateur. Seul deux amis peuvent s’échanger des messages privés.
Pour envoyer un message privé, il faut se rendre sur la page dédié qui contient le formulaire suivant :
| **Nom champ** | **Description** | **Valeur par défaut** |
|:--------------|:----------------|:----------------------|
| Adresse amis  | Adresse email du destinataire du message | -                     |
| Message       | Message qu’on souhaite envoyer | -                     |
| Valider       | Envoi du message | -                     |


## GESTION DES PLAYLISTS ##
La gestion des playlists est une fonctionnalité permettant à l’utilisateur de :
  * Créer sa playlist :
    1. Une playlist à un nom
    1. Portée (visible ou non visible; par défaut, elle est non visible)
  * Supprimer sa playlist
  * Supprimer un média de sa playlist


## GESTION DES AMIS ##
La gestion des amis inclus :
  * La recherche d’utilisateurs
  * La demande d’amis
  * Accepter un ami
  * Refuser un ami
  * Supprimer un ami
La gestion d’amis se fera sur une page dédiée.

## NOTIFICATIONS ##
Pour tenir les utilisateurs en alerte et au courant des activités sur le site, un système de notification doit être mis en place. Les notifications serviront à spécifier :
  * Les  demandes d’amis
  * Les messages des administrateurs/modérateurs
  * Les messages privés
  * Les messages sur son mur

## GESTION DU PANIER ##
Le panier est une page web contenant l’ensemble des articles que l’utilisateur souhaite télécharger.
Pour chaque média, un lien « Ajouter au panier » est présent. Ce lien permet d’ajouter le média à la liste de média à télécharger.
Quand l’utilisateur a fini de faire ses « courses », il va pouvoir télécharger l’ensemble des médias en une seule fois (archive .rar).

## PAGE D’ACCUEIL ##
La page d’accueil va contenir un grand nombre d’informations utiles pour l’utilisateur :
  * Les dernières vidéos uploadées
  * Ses notifications
  * Les recommandations :
    1. Top vues
    1. Top notations
    1. Top nouveautés
    1. Dernières activités


## UPLOAD DE MEDIA ##
L’upload des médias est une fonctionnalité critique de l’application. Il faut que celle-ci soit la plus ergonomique et fonctionnelle possible.
Pour chacun des médias à ajouter, nous aurons un formulaire contenant les informations suivantes :
| **Nom champ** | **Description** | **Valeur par défaut** |
|:--------------|:----------------|:----------------------|
| Type          | Type du média à ajouter (vidéo ou sons) Le type est normalement automatiquement reconnu en fonction du média ajouté. Cependant, il pourra arriver qu’un format ne soit pas pris en compte et qu’il faille spécifier le type.| -                     |
| Nom           | Nom du média    | Nom du fichier uploadé |
| Fichier       | Fichier à uploader (il faudra vérifier que celui-ci ait un format compatible) | -                     |
| Catégorie     | Catégorie du fichier (« Vidéo ; humour », « Sons ; Classique » etc …) | « Type » + « Autre »  |
| Photo         | Photo (ou thumbnail) de couverture du média | Thumbnail par défaut  |
| Tags          | Tags permettant de trier les médias en fonction des recherches effectuées par les utilisateurs | -                     |
| Portée        | Visibilité de la vidéo pour les autres membres du site : Public (à tout le monde), privé (à ses amis) | Privé                 |
| Mot de passe  | Si la portée est privée, il est possible d’ajouter un mot de passe afin que la vidéo soit accessible par d’autres personnes (non amis) | -                     |
| Description   | Description du média | -                     |
| Supprimer     | Reset de tous les champs du formulaire | -                     |

Enfin, il sera possible d’effectuer les actions suivantes :
| **Nom champ** | **Description** |
|:--------------|:----------------|
| Ajouter un formulaire | Ajoute un formulaire de média |
| Supprimer tout | Supprimer tous les formulaires |
| Envoyer       | Envoi de tous les formulaires de média |


La page Upload de média est très fonctionnelle et permet donc :
  * Un multi upload : Possibilité d’ajouter autant de médias que l’on souhaite en une fois (taille totale limitée à 5 Go par upload).
  * Un drag & drop des fichiers directement depuis le bureau de l’utilisateur.

Une fois le/les formulaire(s) soumit, une barre de progression doit pouvoir afficher l’avancement de l’upload.

## LA RECHERCHE/LE TRI ##

### RECHERCHE ###
Un bandeau/menu doit être accessible à l’utilisateur afin de rechercher les médias qu’il désire.
| **Nom** | **Description** |
|:--------|:----------------|
| Type    | Type de média à afficher : Tout, Vidéos, Sons |
| Tri par note | Tri en fonction de la notation des médias |
| Tri par vus | Tri en fonction du nombre de vus des médias |
| Tri par téléchargement | Tri en fonction du nombre de fois que les médias ont été téléchargés |
| Tri par média du moment | Tri en fonction des dernières nouveautés bien notées |
| Tri par date | Tri en fonction des nouveautés sur le site |
| Recommandation | Affiche les médias recommandés pour l’utilisateur (en fonction de ces précédentes recherches ou visionnages) |
| Recherche | Recherche d’un média dont le titre ou les tags correspondent à ce qu’a entré l’utilisateur |
| Quantité | Nombre de médias à afficher par page (maximum 100) |
| Pagination | Navigation entre les différentes pages |

Pour tous les champs de tri, nous alternons la méthode de tri à chaque clic (premier clic ordre croissant, deuxième clic ordre décroissant, troisième clic ordre croissant etc.).
(Option) Les méthodes de tri ne rechargent pas la page. (/Option)
(Option) Recherche de médias sur d’autres sites. (/Option)
Dans tous les cas, les vidéos proposées sont seulement celles dont l’utilisateur a accès :
  * Le média est public.
  * Le média est privé mais il appartient à un ami.
  * La vidéo n’est pas de nature choquante.
  * La vidéo est de nature choquante mais l’utilisateur est majeur.

### L’AFFICHAGE DES MEDIAS TROUVES ###
Les médias seront affichés différemment selon les préférences de l’utilisateur :
  * Affichage miniature
  * Affichage en liste
  * Affichage détaillé

## DETAIL D’UN MEDIA ##
Lorsque l’utilisateur a trouvé un média qu’il l’intéresse, l’utilisateur peut décider d’y accéder.
Chaque média dispose d’une page dédiée où il est possible de visionner/écouter le média, de voir les informations correspondantes (nombre de vues, nombre de téléchargements, statistiques), ainsi que les commentaires associés.
Pour chaque média, il doit être possible :
  * d’ajouter une note (de 1 à 5 étoiles)
  * d’ajouter un commentaire
  * de supprimer un commentaire (jusqu’à deux minutes après sa publication)
  * de l’ajouter au panier
  * de l’ajouter aux favoris
  * de l’ajouter à une playlist (indirectement, l’ajoute aux favoris)
  * (Option) de l’ajouter à la liste de lecture en cours (/Option)
  * de le partager sur un réseau social (Google +, Facebook, Twitter, LinkedIn)
  * d’afficher les statistiques du média (graphiques)
  * de choisir la résolution/qualité sons du Player
  * de signaler le média ou un commentaire
Lorsque le propriétaire du média affiche la page, tous les champs doivent pouvoir être modifiés et enregistrés.
Si une personne, non ami du propriétaire, arrive sur ce média (en entrant directement l’url, par exemple), il sera soit refoulé, soit une demande de mot de passe sera affichée.

## ADMINISTRATION ##
L’administration du site se fera à partir d’un panneau de configuration. Seuls les membres ayant les droits suffisants pourront y accéder.
Dans ce panneau de configuration, l’utilisateur va pouvoir gérer le système.

### GESTION DES UTILISATEURS ###
Le panneau de configuration doit permettre l’affichage et la recherche de l’ensemble des utilisateurs du système.
Pour chaque utilisateur, il doit être possible d’accéder à sa fiche (que le profil soit public ou non) et de le supprimer.

### GESTION DES MEDIAS ###
Le panneau de configuration doit permettre l’affichage et la recherche de médias.
L’utilisateur pourra également accéder à toutes les données de chaque média et le supprimer si besoin.

### GESTION DES SIGNALEMENTS ###
Une page du panneau de configuration va permettre d’afficher le contenu signalé (commentaire, message, média) par les utilisateurs et de le supprimer si besoin.
Plus un objet est signalé, plus haut il apparaît dans la liste des signalements.


## GESTION DES ERREURS ##
Pour toutes les pages du site, une gestion fine des erreurs est attendue. Le but étant de ne jamais tomber sur une erreur incompréhensible par l’utilisateur lambda.
Si une erreur n’a pas été gérée, l’interface doit s’adapter et afficher la StackTrace de l’erreur ainsi qu’un bouton permettant d’alerter les administrateurs.


## PAGES ##
Recensement des pages contenant les spécifications précédentes :
  * Page d’accueil
  * Page de profil
  * Page de messagerie
  * Page de panier
  * Page d’affichage / recherche de médias
  * Page de détail d’un média
  * Page d’upload d’un média
  * Page de gestion des amis
  * Page d'inscription
  * Page de récupération de mot de passe
  * Page d’administration


## PAGES ANNEXES ##
  * Page « Découvrir MediArea »
  * Page « A propos »
  * Page des conditions générales d’utilisation
  * Page des mentions légales


## MENU ##
Le menu de l’application doit permettre l’accès à l’ensemble du contenu présent sur le site.
Ce menu doit être clair, lisible et surtout fixe.
Dans le menu sont présents des liens vers la :
  * Page d’accueil
  * Page de profil / inscription
  * Page de connexion / déconnexion
  * Page de messagerie personnelle
  * Page de playlist
  * Page du panier + upload
  * Page des amis

<br /><br />
# SPÉCIFICATIONS NON-FONCTIONNELLES #

## SÉCURITÉ ##
Pour assurer une totale sécurité des données et des utilisateurs de MediArea, un certain nombre de points devront être gérés avec attention !

### MOT DE PASSE ###
Pour accéder au site, l’utilisateur doit être enregistré par un mot de passe fiable.
Le système doit être capable de renseigner l’utilisateur sur la fiabilité du mot de passe inséré. Pour cela le programme devra être capable d’émettre des avertissements si le mot de passe :
  * Ne contient que des lettres
  * Ne contient que des chiffres
  * Ne contient pas de caractères majuscules
  * A une taille inférieure à 3 caractères et supérieur à 12 caractères
Mais dans ces cas-là, la création du compte est quand même possible sauf pour le nombre de caractères que contient le mot de passe. Il ne faut pas forcer les utilisateurs à créer des mots de passe trop compliqués qu’ils ne pourront pas retenir.


### SESSION ###
La session ne devra pas être fermée automatiquement par le site. Seul le bouton « Déconnexion » le permettra.
Ce système peut représenter une faille de sécurité pour l’utilisateur, mais notre conception du service l’oblige : L’utilisateur peut regarder autant de médias qu’il désire, avec des playlists pouvant être très importantes. Il est donc possible que l’utilisateur reste inactif (ne change pas de page) pendant un très long moment.


### INJECTIONS SQL ###
Pour contrer toutes les attaques par injections SQL, tous les champs du site devront être protégés.

### PROTECTION DES DONNÉES ###
Les données seront stockées sur des disques durs branchés en RAID (1 ou 5)

### SAUVEGARDE REGULIERE DE LA BASE DE DONNÉES ###
Pour des raisons de sécurité et de préservation « totale » des données, une politique de sauvegarde de la base de données doit être mise en place.
  * Tous les soirs, un backup total de la base de données est effectué.
  * Ces backups sont gardés pendant 1 mois.
  * À la fin du mois, on ne préserve qu’un backup par semaine (celui du dimanche soir et du dernier jour du mois).
  * À la fin de l’année, on ne préserve qu’un backup par mois (le dernier de chaque mois).

Cette politique de sauvegarde nous permet d’économiser la mémoire disques tout en assurant la préservation des données et le retour en arrière.

## PERFORMANCE ##
L’attente des utilisateurs est de pouvoir disposer d’un service fournit et puissant. Nous allons donc mettre l’accent sur les performances du site.
| **Nom** | **Temps de chargement attendu** | **Temps de chargement maximum** |
|:--------|:--------------------------------|:--------------------------------|
| Pages générales (accueil, profil, favoris etc.) | < 1 seconde                     | 2 secondes                      |
| Page de recherche de medias | < 3 secondes                    | 5 secondes                      |
| Page de consultation d’un media | Page : < 1 seconde - Media : < 3 secondes avant lancement | Page : 2 secondes - Media : 5 secondes avant lancement |
_Temps pour une connexion haut débit standard (1024 Ko/s)_

## CAPACITÉ ##
### BANDE PASSANTE ###
YouTube a environ 123 511 utilisateurs uniques par heure. En une heure, un utilisateur effectue en moyenne 100 actions.
On a donc environ 12.351.100 transactions par heures.
Notre système étant très largement moindre, on peut estimer qu’une capacité de 10.000**transactions par heure est très largement suffisante.**

### MÉMOIRE ###
Le système doit être capable de stocker 100 Go**de données.**

### BESOIN DE CROISSANCE ###
MediArea étant conçu pour évoluer dans un futur plus ou moins proche, il devra être possible de gérer :
  * 100 000 transactions par heure
  * Stocker 10 To de données
_Chiffres estimés pour un futur proche (1-2 ans)_

## DISPONIBILITÉ ##
Le site web MediArea est accessible 24/24h 7/7j hormis périodes de maintenance.
Les maintenances « Normales » ou « Évolutives » seront effectuées pendant des périodes de faibles activités :
  * En vacance : de 4h à 8h du matin
  * En période travaillée : de 2h à 6h du matin

## FIABILITÉ ##
### TEMPS DE BON FONCTIONNEMENT ###
Le temps d’indisponibilité attendu est de moins de 2 h par semaine (maintenance, et pannes compris).
Le temps d’indisponibilité maximum est de 300 h par an.
### TEMPS DE RÉTABLISSEMENT ###
À la suite d’une panne, le système doit être capable de repartir en moins de 2 h.

## INTÉGRITÉ ##
### COMPRESSION D’IMAGES ###
Les images stockées par le système peuvent être de plusieurs origines :
  * Photo/Avatar d’un utilisateur : Toutes les photos sont redimensionnées en 50\*50 pour alléger le poids de la page et la vitesse de téléchargement.
  * Thumbnail d’un média : Le thumbnail d’un média est redimensionné en 200\*200 (ou reste comme elle est si l’image d’origine est plus petite)

## COMPATIBILITÉ ##
### COMPATIBILITÉ AUX APPLICATIONS PARTAGÉES ###
Le système sera en mesure de communiquer avec des systèmes tiers comme :
  * Google
  * Facebook
  * Twitter
  * LinkedIn

### COMPATIBILITÉ AUX SYSTEMES D’EXPLOITATION ###
Notre système étant une application Web, celle-ci ne dépendra pas du système d’exécution mais uniquement du navigateur.

### COMPATIBILITÉ AUX NAVIGATEURS ###
Dans le cadre d’un service destiné à un public jeune et/ou non néophyte, nous allons concentrer notre effort pour rendre compatible MediArea avec les navigateurs récents :
  * Mozilla Firefox > 8
  * Google Chrome > 15
  * Internet Explorer > 7
  * Opera > 10
  * Safari > 4

## APTITUDE A LA MAINTENANCE ##
Un des points les plus importants lorsqu’on développe un système informatique est de s’assurer de la maintenabilité du projet.
Pour favoriser cette maintenabilité, nous prendrons en compte les différents aspects suivants.

### DOCUMENTATION DU CODE ###
Le code produit devra être très bien commenté.
Ainsi, pour chaque élément important/compliqué du code, est attendu une ligne de commentaire expliquant (en français compréhensible par tous) le fonctionnement du code.
Toutes les méthodes doivent être commentées afin de connaître :
  * Le but de la méthode
  * Les arguments
  * Le type de retour
  * Les dépendances
De plus, on pensera à commenter toutes les classes afin de pouvoir comprendre rapidement l’utilité de chacune d’elles.

La technologie à utiliser est « JavaDoc ». JavaDoc offre diverses possibilités concernant la documentation du code source :
  * Formatage des commentaires (en HTML)
  * Accès rapide à la documentation de chaque membre de chaque classe (l’IDE s’en charge automatiquement)
  * Création de pages HTML contenant l’ensemble de la documentation des membres. Ces pages permettent de visualiser et de comprendre rapidement le fonctionnement d’une classe.

L’inconvénient de la JavaDoc ? Cela prend un peu de temps au développeur pour l’écrire soigneusement (commentaires complets et formatés). Cependant, une fois écrite, elle facilite grandement la maintenabilité et la réutilisabilité du code !

### UTILISATION DE PATRONS D’ARCHITECTURE ###
Pour favoriser la maintenabilité du code, il existe des patrons de conception qui ont été créé pour résoudre des problèmes spécifiques.
Un des patrons les plus connus est le patron MVC (Modèle Vue Contrôleur). Il est conseillé de l’employer lors de la création du projet.
Il existe également d’autres patrons comme Singleton, Multiton, Factory, Strategy, Facade …
L’utilisation de chacun d’entre eux se fera à l’appréciation du programmeur.