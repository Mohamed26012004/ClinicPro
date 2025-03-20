package edu.ezip.ing1.pds.medecingrahics;

import java.time.LocalTime;

public class Creneau {

    private LocalTime debut;
    private LocalTime fin;

    public Creneau(LocalTime debut, LocalTime fin) {
        this.debut = debut;
        this.fin = fin;
    }

    public LocalTime getDebut() {
        return debut;
    }

    public void setDebut(LocalTime debut) {
        this.debut = debut;
    }

    public LocalTime getFin() {
        return fin;
    }

    public void setFin(LocalTime fin) {
        this.fin = fin;
    }
}
