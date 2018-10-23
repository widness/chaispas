# IT_Infrastructure
Ce projet est réalisé à l'occasion d'un module de la HEVS en informatique de gestion.

## Description:
Cette application permet au personne concerné de gérer le parc informatique d'une école (ou d'une entreprise).
En y attribuant des ordinateurs et des personnes (des élèves ou des employés) à une salle (ou un bureau).

## Fonctionnalités de l'application:
1. Gestion compléte des personnes (ajout, modification et suppresion)
2. Gestion complète des salles (ajout, modification et suppresion)
3. Gestion complète des des ordinateurs (ajout, modification et suppresion)
4. Lier une/des personne à une salle
5. Lier un/des ordinateurs à une salle
6. Changement des paramêtres
    1. Choix des salles favorites (salles affichées sur le premier écran)

## Tables: 

### Persons

| Type   | Fields    |
|--------|-----------|
| int    | id        |
| string | firstname |
| string | lastname  |
| date   | birthday  |
| int    | room_id   |

### Computers

| Type   | Fields        |
|--------|---------------|
| int    | id            |
| string | label         |
| int    | type          |
| int    | os            |
| string | description   |
| string | CPU           |
| int    | RAM           |
| int    | diskSpace     |
| date   | purchase_date |
| int    | room_id       |

### Rooms

| Type   | Fields      |
|--------|-------------|
| int    | id          |
| string | label       |
| int    | NbOfPlaces  |

# Démarrer l'app sur votre smartphone 

Si les données ou les models ont été changé, désinstaller l'application du smatphone puis relancer la compilation