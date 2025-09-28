public class Main {
    public static void main(String[] args) {
        // RNG
        long M = (long)Math.pow(2, 32);
        long a = 1664525, c = 1013904223, seed = 5;
        long maxAleatorios = 100_000;
        LCG rng = new LCG(a, c, M, seed, maxAleatorios);

        // Filas
        Fila F1 = new Fila("F1", 1, 100, 1, 2);
        Fila F2 = new Fila("F2", 2, 5, 4, 8);
        Fila F3 = new Fila("F3", 2, 10, 5, 15);

        // Roteamento
        F1.destinos.add(new Destino(F2, 0.8));
        F1.destinos.add(new Destino(F3, 0.2));

        F2.destinos.add(new Destino(F1, 0.3));
        F2.destinos.add(new Destino(null, 0.2));
        F2.destinos.add(new Destino(F3, 0.5));

        F3.destinos.add(new Destino(F2, 0.7));
        F3.destinos.add(new Destino(null, 0.3));

        // Simulador
        Simulador sim = new Simulador(rng);
        sim.run(F1, F2, F3);
    }
}
