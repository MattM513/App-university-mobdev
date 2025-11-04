# Mini-Système Universitaire

**Projet : Labwork 1 - Application de gestion universitaire**
**Auteur :** [Matis Bader]
**Date :** 05/11/2025

---

## Aperçu du Projet

Cette application Android permet de gérer un mini-système universitaire basé sur les rôles : **Administrateur**, **Enseignant** et **Étudiant**.
Elle implémente une architecture **MVVM moderne** avec **Jetpack Compose**, **Room**, **Hilt** et **Navigation Compose**.

---

## Comment Naviguer dans l’Application

La navigation est gérée par `NavGraphs.kt` et l’état d’authentification par `AuthViewModel`.
L’application est divisée en quatre flux selon le rôle utilisateur.

### 1. Flux d’Authentification (Tous)

* **Écran d’accueil :** `LoginScreen`
* **Inscription :**

  * Cliquez sur "Don't have an account? Register"
  * Choisissez un rôle : `Student` ou `Teacher`
  * Remplissez le formulaire selon le rôle choisi
* **Connexion :**

  * **Admin :** `admin@admin.com` / `admin`
  * **Teacher / Student :** identifiants personnels
* **Redirection :**

  * `Admin → AdminHomeScreen`
  * `Teacher → TeacherHomeScreen`
  * `Student → StudentHomeScreen`

---

### 2. Flux Administrateur (Admin)

Connexion via : `admin@admin.com`

**Tableau de bord :** `AdminHomeScreen`
Modules disponibles :

* **Gérer les Étudiants**

  * `StudentListScreen` → + → `StudentFormScreen`
  * Supprimer via l’icône poubelle
* **Gérer les Enseignants**

  * `TeacherListScreen` → + → `TeacherFormScreen`
* **Gérer les Cours**

  * `CourseListScreen` → + → `CourseFormScreen`
  * Sélectionnez un enseignant avant d’enregistrer.
* **Gérer les Inscriptions / Notes**

  * `SubscribeListScreen` → + → `SubscribeFormScreen`
  * Associez un étudiant à un cours et attribuez une note.
* **Déconnexion :** Retour à `LoginScreen`

---

### 3. Flux Enseignant (Teacher)

**Tableau de bord :** `TeacherHomeScreen`
Fonctionnalités :

* **Gérer ses cours :**

  * `CourseListScreen` (filtré pour l’enseignant connecté)
  * * → `CourseFormScreen` (teacherId auto-assigné)
* **Saisir les notes :**

  * `SubscribeFormScreen`
* **Déconnexion :** Retour à `LoginScreen`

---

### 4. Flux Étudiant (Student)

**Tableau de bord :** `StudentHomeScreen`
Fonctionnalités :

* **Voir les cours :** `CourseListScreen` (filtré par niveau)
* **S’inscrire à un cours :**

  * `SubscribeFormScreen` (studentId verrouillé)
  * Score initial = 0
* **Voir ses notes :** `SubscribeListScreen`
* **Voir sa moyenne :** `StudentGradesScreen` (calcul pondéré selon les ECTS)
* **Déconnexion :** Retour à `LoginScreen`

---

## Architecture du Projet

L’application suit une architecture **MVVM (Model - View - ViewModel)**.

### Composants Principaux

#### Vue (View)

* Interface construite avec **Jetpack Compose**
* Composables sans logique métier
* Observation des `StateFlow` exposés par les `ViewModels`

#### ViewModel

* Classes : `AuthViewModel`, `StudentViewModel`, `TeacherViewModel`, `CourseListViewModel`, etc.
* Gèrent la logique UI et interagissent avec le `Repository`

#### Modèle (Model)

* **Repository :** `SCRUDRepository` (source de vérité unique)
* **Base de données :** `Room`
* **DAO :** `StudentDao`, `TeacherDao`, `CourseDao`, `SubscribeDao`
* **Entities :** `StudentEntity`, `TeacherEntity`, `CourseEntity`, `SubscribeEntity`

#### Autres Composants

* **Hilt (DI)** : Injection de dépendances
* **Navigation Compose** : Graphes imbriqués (auth, admin, teacher, student)

---

## Technologies et Bibliothèques

| Bibliothèque       | Rôle                     | Justification                                           |
| ------------------ | ------------------------ | ------------------------------------------------------- |
| Jetpack Compose    | UI déclarative           | Simplifie la création d’interfaces et la gestion d’état |
| Room               | Persistance locale       | Abstraction SQLite, vérification SQL à la compilation   |
| Hilt               | Injection de dépendances | Intégration simplifiée avec Dagger                      |
| ViewModel          | Gestion de l’état        | Résiste aux changements de configuration                |
| Coroutines & Flow  | Asynchronisme            | Gestion fluide des données réactives                    |
| Navigation Compose | Navigation               | Gestion fluide des graphes imbriqués et du RBAC         |

---

## Difficultés Rencontrées

1. **Navigation imbriquée complexe :**
   Configuration de `AppNavHost` avec 4 graphes distincts (auth, admin, teacher, student).

2. **Contrôle d’accès basé sur les rôles (RBAC) :**
   Adaptation des écrans et comportements selon le rôle (admin, enseignant, étudiant).

3. **Calcul de la moyenne finale :**
   Requête `JOIN` personnalisée dans `SubscribeDao` pour récupérer les ECTS et calculer la moyenne pondérée.

4. **Partage d’état entre écrans :**
   Propagation du `userId` depuis `AuthViewModel` vers les graphes `teacher` et `student`.


---

## Conclusion

Ce projet illustre une implémentation complète d’un système universitaire Android, basé sur une **architecture moderne**, une **navigation multi-rôles** et des **principes de réactivité et modularité** robustes.
Il constitue une base solide pour des extensions futures : ajout d’un backend distant, authentification réelle via API, ou synchronisation en ligne.

---
