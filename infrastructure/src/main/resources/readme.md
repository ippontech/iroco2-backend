# Documentation Postman

Cette documentation a pour but de faciliter la prise en main de Postman pour tester l'API du projet IroC0².

---

## Qu'est-ce que Postman ?

Postman est un outil de développement permettant de tester, documenter et partager des APIs.  
Il offre une interface graphique intuitive pour envoyer des requêtes HTTP, visualiser les réponses et organiser les collections de requêtes pour différents environnements.

> **Remarque :** Vous pouvez télécharger l'application Postman ou utiliser sa version web directement depuis votre navigateur.

---

## Importer les Collections et Environnements

### Importer la Collection
1. Ouvrez l'onglet **"Collections"** dans Postman.
2. Cliquez sur le bouton **"Import"**.
3. Sélectionnez le fichier d'export **iroco2.postman_collection.json**.

### Importer les Environnements
1. Rendez-vous dans l'onglet **"Environments"**.
2. Cliquez sur le bouton **"Import"**.
3. Importez les fichiers **Local.postman_environment.json** et **Dev.postman_environment.json**.

---

## Structure de la Collection IcoC0²

La collection **IcoC0²** est organisée en packages par use case, reflétant la structure du projet. Voici la répartition :

- **Actuator**  
  Routes basiques permettant de connaître l'état du backend.

- **Infrastructure**  
  Routes pour afficher, créer et supprimer des infrastructures.

- **Analyse CUR**  
  Routes pour générer de nouvelles analyses, les afficher et les supprimer.

- **Scanner**
  Routes pour déclencher, consulter ou supprimer un scan.

- **Catalog**
  Routes pour lister les services cloud providers.

- **Component**
  Routes pour afficher, créer et supprier des composants

- **Service Config Setting**
  Route pour afficher les options de configuration 

- **Jwt Token**
  Routes pour générer le token JWT et vérifier l'API KEY AWS

---

## Environnements : Local vs Dev

Deux environnements sont actuellement disponibles : **Local** et **Dev**.

- **Environnement Local** : Configuré pour utiliser les routes exposées par votre machine locale. Assurez-vous que le projet **IroCo²** est bien démarré en local pour que les requêtes fonctionnent.
- **Environnement Dev** : Pointe vers l’instance déployée de **IroCo²**, dédiée aux développeurs. Cet environnement est accessible sans avoir besoin de démarrer le projet en local.
---

## Comment utiliser Postman

Prenons l’exemple d’une route qui ne nécessite pas de jeton d’autorisation.

### Vérifier l’état de l’application

1. Ouvrez le dossier **Actuator** dans la collection **Iroco2** de votre workspace Postman.
2. Sélectionnez la requête **GET Health**.
3. Dans la liste déroulante en haut à droite, choisissez l’environnement **Dev**.
4. Cliquez sur **Send**.

Si l’application est fonctionnelle, vous devriez recevoir une réponse sous forme de JSON :

```json
{
  "status": "UP"
}
```

Cette réponse indique que l’application IroCo² est bien déployée en environnement Dev et prête à l’emploi.

### Que faire si l’application n’est pas disponible ?

Si l’application rencontre un problème, vous pourriez obtenir une réponse différente, par exemple :

```json
{
    "status": "DOWN"
}
```

Ou encore une erreur HTTP :

```json
{
    "error": "Internal Server Error",
    "message": "Service unavailable"
}
```

Dans ce cas, voici quelques vérifications à effectuer :

- Assurez-vous que l’environnement Dev est bien sélectionné.
- Vérifiez que le backend est bien déployé en Dev.
- Consultez les logs pour identifier d’éventuelles erreurs.
- Contactez l’équipe en charge du projet si le problème persiste.

## Authorization

Toutes les routes, à l'exception de celles relatives aux sections **public**, **health** et **info**, nécessitent un token **Bearer** fourni par Clerk.
Voici une version révisée et reformulée de la procédure pour obtenir le token Bearer, en tenant compte de la nouvelle méthode d'obtention via Postman :

---

### Procédure pour obtenir le token Bearer via Clerk

1. **Accédez à l'application IroC0²** en environnement **local** ou **dev**.
2. **Ouvrez les DevTools** de votre navigateur et allez dans l'onglet **Network**.
3. **Déconnectez-vous** de votre session actuelle si vous êtes déjà connecté.
4. **Reconnectez-vous** en entrant votre **username** et **mot de passe**.
5. Dans l'onglet **Network**, repérez une requête qui commence par **"sign_in"**.
6. Dans l'en-tête de cette requête, copiez la valeur du header **"clerk-db-jwt"**.
7. **Ouvrez Postman** et sélectionnez l'environnement souhaité.
8. Collez la valeur du token dans la **Current Value** de la variable **CLERK_DB_JWT** dans votre environnement Postman.
9. Renseignez votre **username** dans la **Current Value** de la variable **CLERK_USER**.
10. Renseignez votre **mot de passe** dans la **Current Value** de la variable **CLERK_PWD**.
11. **Lancez la requête "Post Clerk Authentication"** dans Postman. Ce script va automatiquement récupérer le JWT et le stocker dans la variable **BEARER_TOKEN**.

> **Attention :** Le token Bearer a une durée de vie limitée (quelques minutes seulement). Lorsque le token expire, il suffit de relancer la requête "Post Clerk Authentication" pour obtenir un nouveau token.

---

### Détails supplémentaires :

- **Note sur les variables :**
    - `CLERK_DB_JWT` : Token nécessaire pour s'authentifier.
    - `CLERK_USER` et `CLERK_PWD` : Identifiants pour la connexion.

Le pré-script Postman s'assurera que le JWT est récupéré automatiquement après chaque exécution de la requête **Post Clerk Authentication**, et le token sera stocké dans la variable **BEARER_TOKEN** pour les appels ultérieurs.
