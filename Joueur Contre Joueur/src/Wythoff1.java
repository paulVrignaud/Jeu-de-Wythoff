/**
 * Programme du jeu de Wythoff en Joueur Vs Joueur.
 * @author P.Vrignaud
 */
class Wythoff1 {
	void principal() {
		
		jouer();
	}
	
	/**
	 * Permet de lancer le jeu de Wythoff
	 */
	void jouer() {
		
		System.out.println();
		
		//On demande si les joueurs connaissent le  jeu
		char choix = SimpleInput.getChar ("Connaissez-vous les règles du jeu de Wythoff ? n/o > ");
		
		if (choix == 'n') {
			System.out.println();
			System.out.println ("Il s’agit d’un jeu à deux joueurs qui peut se jouer sur un plateau type damier avec un unique pion.");
			System.out.println ("Le pion est placé initialement au hasard sur le plateau.");
			System.out.println ("A chaque tour, un joueur a le droit a un seul de ces troi mouvements : la gauche, le bas ou en diagonale vers la gauche et la bas.");
			System.out.println ("Avec un nombre case à parcourir illimité.");
			System.out.println ("Le joueur gagnant est celui qui parvient a mettre le pion sur la case inférieure gauche.");
			System.out.println();
		}
		
		System.out.println ("Bonne partie du jeu Wythoff !");
		
		//On attribue aux joueurs un id
		String joueur1 = SimpleInput.getString ("Quel est le nom du premier joueur ? > ");
		int id1 = 1;
		String joueur2 = SimpleInput.getString ("Quel est le nom du second joueur ? > ");
		int id2 = 2;
		
		//On tire au hasard qui commençera à jouer
		int hasard = (int) (Math.random() * 2);
		int joueurActif;
		int joueurInactif;
		
		if (hasard == 0) {
			joueurActif = 1;
			joueurInactif = 2;
		} else {
			joueurActif = 2;
			joueurInactif = 1;
		}
		
		// On crée le plateau et on positionne le pion
		int taille = SimpleInput.getInt ("Quel est la taille de votre plateau de jeu ? > ");
		char[][] plateau = creerPlateau(taille);
		positionDepart(plateau);
		
		
		//Tant que le pion n'a pas atteint le (0,0)
		while (plateau[0][0] != 'o') { 
			
			affichePlateau(plateau);
			System.out.println();
			
			int x = positionX(plateau);
			int y = positionY(plateau);
			String mouvement;
			
			if (joueurActif == 1) {
				System.out.println (joueur1 + ", C'est à toi de jouer !");
			} else {
				System.out.println (joueur2 + ", C'est à toi de jouer !");
			}
			
			// On teste tout les différents cas de position
			if (maxGauche(plateau,x) == 0) {
				mouvement = "bas";
				System.out.println ("Tu ne peux aller qu'en bas ");
				
			} else if (maxBas(plateau,y) == 0) {
				mouvement = "gauche";
				System.out.println ("Tu ne peux aller qu'à gauche ");
				
			} else {
				mouvement = SimpleInput.getString ("Choisi ton mouvement (gauche, bas, diagonale) > ");
			}
			
			//Tant que l'utilisateur n'a pas bien saisie le mouvement
			while ((mouvement.indexOf("gauche") == -1) && (mouvement.indexOf("bas") == -1) && (mouvement.indexOf("diagonale") == -1)) { 
				System.out.println ("réessaie avec la bonne écriture");
				mouvement = SimpleInput.getString ("Choisi ton mouvement (gauche, bas, diagonale) > ");
			}
			
			
			int n = SimpleInput.getInt ("Combien de case veux-tu parcourir ? > ");
			int deplacementMax;
			
			//On calcule le déplacement de n case maximum
			if (mouvement.indexOf("gauche") != -1) {
				deplacementMax = maxGauche(plateau, x);
				
			} else if (mouvement.indexOf("bas") != -1) {
				deplacementMax = maxBas(plateau, y);
				
			} else {
				deplacementMax = maxDiagonal(plateau, x, y);
			}
			
			//Tant que n ne respecte pas les limites
			while (n < 1 || n > deplacementMax) { 
				System.out.println ("C'est une valeur qui n'est pas bonne, retente.");
				n = SimpleInput.getInt ("Combien de case veux-tu parcourir ? > ");
			}
			
			//On regarde le type d'action demandé et on déplace le pion
			if (mouvement.indexOf("gauche") != -1) { 
				mouvementGauche(plateau, n, x, y);
				
			} else if (mouvement.indexOf("bas") != -1) {
				mouvementBas(plateau, n, x, y);
				
			} else {
				mouvementDiag(plateau, n, x, y);
			}
			
			//On change le joueur actif
			if (joueurActif == 1) {
				joueurActif = 2;
				joueurInactif = 1;
			} else {
				joueurActif = 1;
				joueurInactif = 2;
			}
		}
		
		affichePlateau(plateau);
		System.out.println();
		
		//On attribue la victoire au bon joueur
		if (joueurActif == 2) { 
			System.out.println ("Bien joué " + joueur1 + ", tu a gagné la partie ! ");
		} else {
			System.out.println ("Bien joué " + joueur2 + ", tu a gagné la partie ! ");
		}
		
		
	}
	
	/**
	 * Permet d'afficher le plateau de jeu, 'o' à l'endroit où le pion se situe sinon ' '
	 * (au-delà d'un plateau de taille 10x10, bug graphique)
	 * @param plateau plateau à afficher
	 */
	void affichePlateau(char[][] plateau) {
		
		for (int i = plateau.length-1; i >= 0 ; i--) {
			System.out.print((i) + "  | ");
			
			for (int j = 0; j < plateau.length; j++) {
				System.out.print (plateau[i][j] + " | ");
			}
			System.out.println();
		}
		System.out.print ("     ");
		for (int i = 0; i < plateau.length; i++) {
			System.out.print (i + "   ");
		}
	}
	
	/**
	 * Affiche un tableau à 2 dimensions
	 * @param tab tableau à 2 dimensions
	 */
	void displayTab (char[][] tab) {
		System.out.print ("{");
		for (int i = 0; i < tab.length; i++) {
			System.out.print ("{");
			for (int j = 0; j < tab[i].length; j++) {
				System.out.print(tab[i][j]);
				if (j < tab[i].length - 1) {
					System.out.print(",");
				}
			}
			System.out.print("}");
			if (i < tab.length - 1) {
				System.out.print(",");
			}
		}
		System.out.print ("}");
	}
	
	/**
	 * Méthode qui crée un plateau de taille (n x n)
	 * @param n taille du plateau (en case)
	 * @return plateau à 2 dimensions de char
	 */
	char[][] creerPlateau(int n) {
		char[][] plateau = new char[n][n];
		
		for (int i = 0; i < plateau.length; i++) {
			for (int j = 0; j < plateau[i].length; j++) {
				plateau[i][j] = ' ';
			}
		}
		
		return plateau;
	}
	
	/**
	 * Compare 2 tableaux à 2 dimensions pour vérifier si ce sont les mêmes
	 * @param tab1 premier tableau
	 * @param tab2 deuxième tableau
	 * @return renvoie true ssi ils sont identiques
	 */
	boolean identique(char[][] tab1, char[][] tab2) {
		
		boolean identique = true;
		int i = 0;
		
		if (tab1.length != tab2.length) {
			identique = false;
		}
		
		while (i < tab1.length && identique) { //On parcours les tableaux
			int j = 0;
			
			while (j < tab1[i].length && identique) {
				if (tab1[i][j] != tab2[i][j]) {
					identique = false;
				}
				j++;
			}
			i++;
		}
		
		return identique;
	}
	
	
	/**
	 * Positionne aléatoirement le pion en début de partie sur la surface autorisé
	 * @param plateau pour jouer
	 */
	void positionDepart(char[][] plateau) {
		int choix = (int) (Math.random() * 2);
		int x;
		int y;
		
		/// On commence par les x
		if (choix == 0) { 
			//Tant que x est dans une position interdite, on re-tire aléatoirement
			do {
				x = (int) (Math.random() * (plateau.length));
			} while (x == 0);
			
			//Si x est le plus à droite sur le plateau
			if (x == plateau.length-1) { 
				//Tant que y est au plus bas on re tire aléatoirement
				do {
					y = (int) (Math.random() * (plateau.length));
				} while (y == 0 || y == plateau.length-1);

			//y ne peux être qu'à la plus haute ordonnée
			} else { 
				y = plateau.length-1;
			}
		/// On commence pas les y
		} else { 
			//Tant que y est dans une position interdite on re-tire aléatoirement
			do {
				y = (int) (Math.random() * (plateau.length));
			} while(y == 0);
		
			if (y == plateau.length-1) {
				//Tant que x est le plus à gauche on re tire aléatoirement
				do {
					x = (int) (Math.random() * (plateau.length));
				} while (x == 0 || x == plateau.length-1);
			
			//x ne peux être qu'à la plus haute abscisse
			} else {
				x = plateau.length-1;
			}
		}
		//On place le pion aux coordonnées
		plateau[y][x] = 'o';
	}
	
	/** Permet de récuperer la position en x (ligne) du pion sur le plateau
	 * @param plateau plateau de jeu
	 * @return position x du pion sur le plateau si il est présent sinon -1
	 */
	int positionX(char[][] plateau) {
		int x = -1;
		boolean trouver = false;
		int i = 0;
		
		while (i < plateau.length && !trouver) {
			int j = 0;
			
			while (j < plateau[i].length && !trouver) {
				if (plateau[i][j] == 'o') {
					x = j;
					trouver = true;
				}
				j++;
			}
			i++;
		}
		return x;
	}
	
	/** Permet de récuperer la position en y (colonne) du pion sur le plateau
	 * @param plateau plateau de jeu
	 * @return position y du pion sur le plateau si il est présent sinon -1
	 */
	int positionY(char[][] plateau) {
		int y = - 1;
		boolean trouver = false;
		int i = plateau.length-1;
		
		while (i >= 0 && !trouver) {
			int j = 0;
			while (j < plateau[i].length && !trouver) {
				if (plateau[i][j] == 'o') {
					y = i;
					trouver = true;
				}
				j++;
			}
			i--;
		}
		return y;
	}
	
	
	/**
	 * Méthode qui permet de faire déplacer le pion vers la gauche de n case
	 * depuis la position (x,y), (on vérifie si cela est possible dans jouer() !)
	 * @param plateau plateau sur lequel est joué le jeu
	 * @param n nombre de case de déplacement
	 * @param x position x (ligne) du pion
	 * @param y position y (colonne) du pion
	 */
	void mouvementGauche(char[][] plateau, int n, int x, int y) {
		
		plateau[y][x] = ' '; 
		plateau[y][x-n] = 'o'; 
	}
	
	/**
	 * permet de faire déplacer le pion vers le bas de n case
	 * depuis la position (x,y), (on vérifie si cela est possible dans jouer() !)
	 * @param plateau plateau sur lequel est joué le jeu
	 * @param n nombre de case de déplacement
	 * @param x position x (ligne) du pion
	 * @param y position y (colonne) du pion
	 */
	void mouvementBas(char[][] plateau, int n, int x, int y) {
		
		plateau[y][x] = ' '; 
		plateau[y-n][x] = 'o';
	}
	
	/**
	 * permet de faire déplacer le pion en diagonale vers la gauche de n case
	 * depuis la position (x,y), (on vérifie si cela est possible dans jouer() !)
	 * @param plateau plateau sur lequel est joué le jeu
	 * @param n nombre de case de déplacement
	 * @param x position x (ligne) du pion
	 * @param y position y (colonne) du pion
	 */
	void mouvementDiag(char[][] plateau, int n, int x, int y) {
		
		plateau[y][x] = ' '; 	
		plateau[y-n][x-n] = 'o';//On déplace le pion de n case vers la gauche et vers le bas
	}
	
	/**
	 * Renvoie le maximum de case qu'on peut parcourir vers la gauche
	 * en partant de la position x
	 * @param plateau plateau de jeu
	 * @param x position x (ligne) du pion
	 * @return le maximum de case qu'on peut parcourir vers la gauche
	 */
	int maxGauche(char[][] plateau, int x) {
		int compteur = 0;
		int i = x;
		
		//On rajoute 1 au compteur tant qu'on peut avancer
		while (i > 0) {
			compteur++;
			i--;
		}
		return compteur;
	}
	
	/**
	 * Renvoie le maximum de case qu'on peut parcourir vers le bas
	 * en partant de la position y
	 * @param plateau plateau de jeu
	 * @param y position y (colonne) du pion
	 * @return le maximum de case qu'on peut parcourir vers le bas
	 */
	int maxBas(char[][] plateau, int y) {
		int compteur = 0;
		int i = y;
		
		//On ajoute 1 au compteur tant qu'on peut avancer
		while (i > 0) {
			compteur++;
			i--;
		}
		return compteur;
	}
	
	/**
	 * Renvoie le maximum de case qu'on peut parcourir possible en diagonale
	 * à partir de la position (x,y)
	 * @param plateau plateau de jeu
	 * @param x coordonnée position x (ligne) du pion 
	 * @param y coordonnée position y (colonne) du pion 
	 * @return Renvoie le maximum de case possible à parcourir en diagonale
	 */
	int maxDiagonal(char[][] plateau, int x, int y) {
		int compteur = 0;
		int i = y;
		int j = x;
		
		//On ajoute 1 au compteur tant qu'on peut avancer
		while (i > 0 && j > 0) { 
			compteur++;
			i--;
			j--;	
		}
		return compteur;
	}
}
