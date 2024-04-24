/**
 * Programme du jeu de Wythoff en joueur Vs Ordinateur.
 * @author P.Vrignaud
 */
class Wythoff2 {
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
		String joueur = SimpleInput.getString ("Quel est le nom du joueur ? > ");
		int idJoueur = 1;
		int idOrdi = 2;
		
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
		int taille;
		do {
			taille = SimpleInput.getInt ("Quel est la taille de votre plateau de jeu (en case)? > ");
		} while (taille <= 2);
		
		char[][] plateau = creerPlateau(taille);
		int[][] positionGagnante = recherchePositionGagnante(plateau);
		rajoutePositionGagnante(plateau, positionGagnante);
		positionDepart(plateau);
		
		
		//Tant que le pion n'a pas atteint le (0,0)
		while (plateau[0][0] != 'o') { 
			
			affichePlateau(plateau);
			System.out.println();
			
			int x = positionX(plateau);
			int y = positionY(plateau);
			String mouvement;
			
			if (joueurActif == 1) {
				System.out.println (joueur + ", C'est à toi de jouer !");
			} else {
				System.out.println ("C'est à l'ordi de jouer !");
			}
			
			///Si c'est à l'ordi de jouer///
			if (joueurActif == 2) {
				char suivant;
				do {
					suivant = SimpleInput.getChar ("écrivez 1 pour que l'ordinateur joue >");
				} while (suivant != '1');
				ordinateur(plateau, x, y);
				
			///Si c'est à l'humain de jouer///
			} else {
				
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
				
				//Tant qu'un mouvement n'à pas été saisie
				while ((mouvement.indexOf("gauche") == -1) && (mouvement.indexOf("bas") == -1) && (mouvement.indexOf("diagonale") == -1)) { 
					System.out.println ("réessaie avec la bonne écriture");
					mouvement = SimpleInput.getString ("Choisi ton mouvement (gauche, bas, diagonale) > ");
				}
				
				
				int n = SimpleInput.getInt ("Combien de case veux-tu parcourir ? > ");
				int deplacementMax;
				
				//On calcule le déplacement de n case maximum en fonction de la direction
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
			System.out.println ("Bien joué " + joueur + ", tu a gagné la partie ! ");
		} else {
			System.out.println ("L'ordi à gagné la partie");
		}
		
		
	}
	
	/**
	 * Programme permettant de faire jouer l'ordinateur au jeu de Wythoff
	 * @param plateau plateau de jeu
	 * @param x position x (ligne) du pion sur le plateau
	 * @param y position y (colonne) du pion sur le plateau
	 */
	void ordinateur(char[][] plateau, int x, int y) {
		int n;
		int choixDirection;
		
		//Si l'ordinateur à l'occasion d'aller sur une position gagnante
		if (gagnanteGauche(plateau,x,y)) {
			n = deplacementGagnant(plateau,x,y,0);
			mouvementGauche(plateau,n,x,y);
			
		} else if (gagnanteBas(plateau,x,y)) {
			n = deplacementGagnant(plateau,x,y,1);
			mouvementBas(plateau,n,x,y);
			
		} else if (gagnanteDiag(plateau,x,y)) {
			n = deplacementGagnant(plateau,x,y,2);
			mouvementDiag(plateau,n,x,y);
			
		} else {
			// On teste tout les différents cas de position pour ne pas créer de boucle infini
			if (maxGauche(plateau,x) == 0) {
				choixDirection = 1;
						
			} else if (maxBas(plateau,y) == 0) {
				choixDirection = 0;
						
			} else {
				choixDirection = (int) (Math.random() * 3); //L'ordi fait ses actions en fonction du choix de direction (aléatoire)
			} 
			
			//L'ordi fait un mouvement à gauche
			if (choixDirection == 0) {
				int deplacementMax = maxGauche(plateau, x);
						
				do {
					n = (int) (Math.random() * (deplacementMax + 1));
				} while (n < 1);
				
				mouvementGauche(plateau, n, x, y);
							
			//L'ordi fait un mouvement en bas
			} else if (choixDirection == 1) {
				int deplacementMax = maxBas(plateau, y);
				
				do {
					n = (int) (Math.random() * (deplacementMax + 1));
				} while (n < 1);
				
				mouvementBas(plateau, n, x, y);
			
			//l'ordi fais un mouvement en diagonale		
			} else {
				int deplacementMax = maxDiagonal(plateau, x, y);
				
				do {
					n = (int) (Math.random() * (deplacementMax + 1));
				} while (n < 1);
				
				mouvementDiag(plateau, n, x, y);
						
			}
		}
	}
	
	/**
	 * Méthode qui permet de savoir si il y a une position gagnante à gauche du pion
	 * @param plateau de jeu
	 * @param x position (ligne) du pion sur le plateau
	 * @param y position (colonne du pion sur le plateau
	 * @return true ssi il y a une position gagnante à sa gauche
	 */
	boolean gagnanteGauche(char[][] plateau, int x, int y) {
		boolean result = false;
		int i = x;
		while (i >= 0 && !result) {
			if(plateau[y][i] == 'x') {
				result = true;
			}
			i--;
		}
		return result;
	}
	
	/**
	 * Méthode qui permet de savoir si il y a une position gagnante dessous le pion
	 * @param plateau plateau de jeu
	 * @param x coordonnee x du pion
	 * @param y coordonnee y du pion
	 * @return retourne true ssi il y a une position gagnante en dessous du pion
	 */
	boolean gagnanteBas(char[][] plateau, int x, int y) {
		boolean result = false;
		int i = y;
		
		while (i >= 0 && !result) {
			if (plateau[i][x] == 'x') {
				result = true;
			}
			i--;
		}
		return result;
	}
	
	/**
	 * Méthode qui permet de vérifier si il y a une position gagnante dans la diagonale du pion
	 * @param plateau plateau de jeu
	 * @param x coordoonee x du pion
	 * @param y coordonnee y du pion
	 * @return renvoie true ssi il y a une position gagnante dans la diagonale
	 */
	boolean gagnanteDiag(char[][] plateau, int x, int y) {
		boolean result = false;
		int i = x;
		int j = y;
		
		while (i >= 0 && j >= 0 && !result) {
			if (plateau[j][i] == 'x') {
				result = true;
			}
			i--;
			j--;
		}
		
		return result;
	}
	
	/**
	 * Méthode permettant de rajouter les positions gagnantes au plateau de jeu
	 * @param plateau plateau de jeu
	 * @param position tableau à 2 dimensions des positions gagnantes
	 */
	void rajoutePositionGagnante(char[][] plateau, int[][] position) {
		int x = 0;
		int y = 0;
		
		//On parcours le tableau position
		for (int i = 0; i < position.length; i++) {
			
			for (int j = 0; j < 2; j++) {
				x = position[i][0];
				y = position[i][1];
			}
			//On met les valeurs trouvés dans le tableau
			plateau[y][x] = 'x';
			plateau[x][y] = 'x';
		}
	}
	
	/**
	 * Permet d'afficher le plateau de jeu, 'o' à l'endroit où le pion se situe et 'x', sinon ' '
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
	
	/**
	 * Affiche un tableau d'entiers à 2 dimensions 
	 * @param tab tableau d'entiers à 2 dimensions
	 */
	void displayTabInt (int[][] tab) {
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
	 * Méthode qui permet de rechercher les positions gagnantes sur le plateau
	 * @param plateau plateau de jeu
	 * @return un tableau à 2 dimensions avec les coordonnées des positions gagnantes selon leur rang
	 */
	int[][] recherchePositionGagnante(char[][] plateau) {	
		int[][] tabPosition = new int[plateau.length/2][2];
		int rang = 1;
		tabPosition[0][0] = 0;
		tabPosition[0][1] = 0;
		
		while (rang < tabPosition.length) {
			int entier = 1;
			boolean estDans = estDans(entier,tabPosition);
			
			//On cherche le plus petit entier non utilisé
			while (estDans) {
				entier += 1;
				estDans = estDans(entier,tabPosition);
			}
			
			int ordonnee = rang + entier;
			
			tabPosition[rang][0] = entier;
			tabPosition[rang][1] = ordonnee;
			
			rang++;
		}
		
		// On vérifie qu'il n'y à pas de position en trop
		int taille = tabPosition.length;
		
		for (int i = 0; i < tabPosition.length; i++) {
			if (plateau.length <= tabPosition[i][0] || plateau.length <= tabPosition[i][1]) {
				taille -= 1;
			}
		}
		
		int[][] tab = new int[taille][2];
		
		for (int i = 0; i < taille; i++) {
			for (int j = 0; j < 2 ; j++) {
				tab[i][j] = tabPosition[i][j];
			}
		}
		tabPosition = tab;
		
		return tabPosition;
	}
	
	/**
	 * Compare 2 tableaux à 2 dimensions pour vérifier si ce sont les mêmes
	 * @param tab1 premier tableau
	 * @param tab2 deuxième tableau
	 * @return renvoie true ssi ils sont identiques
	 */
	boolean identiqueInt(int[][] tab1, int[][] tab2) {
		
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
	 * Méthode qui permet de savoir si un entier est déjà utilisé pour les positions gagnantes
	 * @param entier entier à tester
	 * @param tab tableau à 2 dimensions d'entiers à tester
	 * @return false ssi l'entier n'est pas présent, sinon true
	 */
	boolean estDans(int entier, int[][] tab) {
		boolean estDans = false;
		int i = 0;
		
		while (i < tab.length && !estDans) {
			int j = 0;
			
			while (j < tab[i].length && !estDans) {
				
				if (tab[i][j] == entier) {
					estDans = true;
				}
				j++;
			}
			i++;
		}
		
		return estDans;
	}
	
	/**
	 * Méthode qui permet de savoir de combien de case le pion doit se déplacer pour atteindre une position gagnante
	 * @param plateau plateau de jeu
	 * @param x coordonnee x du pion
	 * @param y coordonnee y du pion
	 * @param direction direction dans laquelle se trouve la position gagnante (0 : gauche, 1 : bas, 2 : diagonale)
	 * @return retourne le nombre de case à parcourir pour atteindre la position gagnante la plus proche
	 */
	int deplacementGagnant(char[][] plateau, int x, int y, int direction) {
		int compteur = 0;
		boolean trouve = false;
		int i = x;
		int j = y;
		
		if (direction == 0) {
			//On parcours sur la gauche
			while (i >= 0 && !trouve) {
				if (plateau[y][i] == 'x') {
					trouve = true;
				}
				i--;
				if (!trouve) {
					compteur += 1;
				}
			}
			
		} else if (direction == 1) {
			//On parcours vers le bas
			while (j >= 0 && !trouve) {
				if (plateau[j][x] == 'x') {
					trouve = true;
				}
				j--;
				if (!trouve) {
					compteur += 1;
				}
			}
			
		} else {
			//On parcours en diagonale
			while (j >= 0 && i >= 0 && !trouve) {
				if (plateau[j][i] == 'x') {
					trouve = true;
				}
				i--;
				j--;
				if (!trouve) {
					compteur += 1;
				}
			}
		}
		return compteur;
	}
}
