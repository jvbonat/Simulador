import java.util.ArrayList;
import java.util.List;

public class Fila {
    String nome;
    int servidores;
    int capacidade;
    int clientes = 0;
    int atendendo = 0;
    int perdas = 0;
    double[] tempoEstado;
    double tempoMinServico;
    double tempoMaxServico;
    List<Destino> destinos = new ArrayList<>();

    public Fila(String nome, int servidores, int capacidade, double tempoMinServico, double tempoMaxServico) {
        this.nome = nome;
        this.servidores = servidores;
        this.capacidade = capacidade;
        this.tempoMinServico = tempoMinServico;
        this.tempoMaxServico = tempoMaxServico;
        this.tempoEstado = new double[capacidade + 1];
    }

    public boolean temEspaco() {
        return clientes < capacidade;
    }

    public boolean podeAtenderMais() {
        return atendendo < servidores && atendendo < clientes;
    }

    public double amostraServico(LCG rng) {
        return tempoMinServico + (tempoMaxServico - tempoMinServico) * rng.nextRandom();
    }
}
