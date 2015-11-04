package mainPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

public class Main {

	public static void main(String[] args) {

		//on déclare le solveur
		Solver solver = new Solver("solveur MatrixColor");


		//nombre de couleur maximal
		//int nbCouleurMax = 4;

		//nombre de couleur
		//IntVar ivCouleur = VF.bounded("ivCouleur", 0, nbCouleurMax, solver);
		IntVar ivCouleur = VF.fixed(3, solver);

		//nombre de ligne
		int nbLigne = 2;

		//nombre de colonne
		int nbColone = 2;




		//On construit une matrice de IntVar matrix, de dimension nbLigne*nbColonne
		//les couleurs possibles vont de 0 à nbCouleur-1


		IntVar matrix[][] = VF.boundedMatrix("matrix", nbLigne, nbColone, 0, ivCouleur.getValue()-1, solver);



		for(int ligne = 0; ligne < nbLigne; ligne++){

			//pour chaque case de la matrice...
			for(int colone = 0; colone < nbColone; colone ++){

				IntVar caseActu = matrix[ligne][colone];

				//si le voisin de droite existe
				if(colone+1 <= nbColone-1){
					IntVar voisinDroite = matrix[ligne][colone+1];

					//il est different de la case
					solver.post(IntConstraintFactory.arithm(caseActu, "!=", voisinDroite));
				}
				//si le voisin du bas existe
				if(ligne+1 <= nbLigne-1){
					IntVar voisinbas = matrix[ligne+1][colone];

					//il est différent de la case
					solver.post(IntConstraintFactory.arithm(caseActu, "!=", voisinbas));

				}




			}


		}

		//on va recupérer tous les intvars de la matrice sous forme de liste 
		//ceci afin d'appliquer une contrainte dessus par la suite

		IntVar[] intVars = new IntVar[nbLigne*nbColone];

		//indice indiquant la variable actuelle renseigné
		int idx = 0;

		//pour chaque case de la matrice
		for(int l = 0; l < nbLigne; l++){
			for(int c = 0; c < nbColone; c++){

				intVars[idx] = matrix[l][c];
				//on incrémente l'indice
				idx++;
			}

		}





		//solver.post(IntConstraintFactory.atmost_nvalues(intVars, ivCouleur, true));
		solver.post(IntConstraintFactory.nvalues(intVars, ivCouleur));

		solver.set(IntStrategyFactory.domOverWDeg(intVars, 1));





		System.out.println("Hello");

		//on minimise le nombre de couleur
		//solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, ivCouleur);


		Chatterbox.showSolutions(solver);

		//solver.findAllOptimalSolutions(ResolutionPolicy.MINIMIZE, ivCouleur,true);
		//solver.findAllSolution();

		solver.findSolution();

		affichageMatrix(matrix);
		
		while(solver.nextSolution()){
			System.out.println("next solution :\n");
			affichageMatrix(matrix);
		}

		//Chatterbox.printStatistics(solver);

		//Chatterbox.showSolutions(solver);
		//Chatterbox.printSolutions(solver);
		//Chatterbox.showDecisions(solver);


		System.out.println(ivCouleur);

		for(int l = 0; l< nbLigne; l++){
			for(int c=0; c<nbColone; c++){

				System.out.println(matrix[l][c]);
			}


		}



	}

	public static void affichageMatrix(IntVar[][] matrix){
		int nbLigne = matrix.length;
		int nbColone = matrix[0].length;

		//affichage de la matrice

		for(int l=0; l < nbLigne; l++){
			String s="";

			for(int c=0; c < nbColone; c++){
				if(matrix[l][c].getDomainSize() != 1){
					//System.out.println("ERREUR VARIABLE " + matrix[l][c].getName() + " NON INSTANCIEE");

					System.err.println("ERREUR VARIABLE " + matrix[l][c].getName() + " NON INSTANCIEE");
					System.exit(0);
				}
				s = s + matrix[l][c].getValue() +" ";

			}
			System.out.println(s);


		}




	}

}
