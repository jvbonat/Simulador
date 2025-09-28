
public class Evento implements Comparable<Evento> {
    TipoEvento tipo;
    Fila fila;
    double tempo;

    public Evento(TipoEvento tipo, Fila fila, double tempo) {
        this.tipo = tipo;
        this.fila = fila;
        this.tempo = tempo;
    }

    @Override
    public int compareTo(Evento o) {
        return Double.compare(this.tempo, o.tempo);
    }
}

