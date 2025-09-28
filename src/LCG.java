public class LCG {
    long a, c, m, seed, usados = 0, limite;

    public LCG(long a, long c, long m, long seed, long limite) {
        this.a = a; this.c = c; this.m = m; this.seed = seed; this.limite = limite;
    }

    public double nextRandom() {
        seed = ((a * seed) + c) % m;
        usados++;
        return (double) seed / m;
    }

    public boolean atingiuLimite() {
        return usados >= limite;
    }
}
