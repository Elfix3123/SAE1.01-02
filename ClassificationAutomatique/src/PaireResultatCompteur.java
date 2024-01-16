public class PaireResultatCompteur <R>{
    private R res;
    private int compteur;

    PaireResultatCompteur(R res, int compteur){
        this.compteur = compteur;
        this.res = res;
    }

    public int getCompteur(){
        return compteur;
    }

    public R getRes() {
        return res;
    }
}