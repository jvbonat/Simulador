import java.util.PriorityQueue;

public class Simulador {
    double clock = 0.0;
    PriorityQueue<Evento> filaEventos = new PriorityQueue<>();
    LCG rng;

    public Simulador(LCG rng) {
        this.rng = rng;
    }

    public void agendaEvento(Evento e) {
        filaEventos.add(e);
    }

    public void run(Fila F1, Fila F2, Fila F3) {
        // agenda primeira chegada externa em F1 no tempo 2.0
        agendaEvento(new Evento(TipoEvento.CHEGADA, F1, 2.0));

        while (!filaEventos.isEmpty()) {
            Evento e = filaEventos.poll();

            // calcula tempo decorrido
            double dt = e.tempo - clock;
            if (dt < 0) dt = 0;

            // acumula tempo no estado atual da fila
            if (e.fila.clientes < e.fila.tempoEstado.length)
                e.fila.tempoEstado[e.fila.clientes] += dt;

            // atualiza clock
            clock = e.tempo;

            // processa o evento
            switch (e.tipo) {
                case CHEGADA -> processaChegada(e.fila);
                case SAIDA -> processaSaida(e.fila);
            }

            // se atingiu limite de aleatórios, não agenda mais eventos
            if (rng.atingiuLimite()) {
                filaEventos.clear(); // garante saída do loop
            }
        }

        // imprime tempo global da simulação
        System.out.println("===================================================");
        System.out.printf("Tempo global da simulação: %.2f minutos%n", clock);
        System.out.println("===================================================");

        // imprime resultados detalhados por fila
        imprimeResultados(F1, F2, F3);
    }

    private void processaChegada(Fila f) {
        // próximas chegadas externas só em F1
        if (f.nome.equals("F1") && !rng.atingiuLimite()) {
            double prox = clock + (2 + 2 * rng.nextRandom()); // intervalo 2..4
            if (!rng.atingiuLimite()) {
                agendaEvento(new Evento(TipoEvento.CHEGADA, f, prox));
            }
        }

        // adiciona cliente se houver espaço
        if (f.temEspaco()) {
            f.clientes++;
            // coloca em atendimento enquanto houver servidores livres
            while (f.podeAtenderMais() && !rng.atingiuLimite()) {
                f.atendendo++;
                double ts = f.amostraServico(rng);
                agendaEvento(new Evento(TipoEvento.SAIDA, f, clock + ts));
            }
        } else {
            f.perdas++;
        }
    }

    private void processaSaida(Fila f) {
        f.clientes--;
        f.atendendo--;

        // atende próximos clientes, se houver
        while (f.podeAtenderMais() && !rng.atingiuLimite()) {
            f.atendendo++;
            double ts = f.amostraServico(rng);
            agendaEvento(new Evento(TipoEvento.SAIDA, f, clock + ts));
        }

        // roteamento probabilístico
        if (!f.destinos.isEmpty() && !rng.atingiuLimite()) {
            double r = rng.nextRandom();
            double cumul = 0;
            for (Destino d : f.destinos) {
                cumul += d.probabilidade;
                if (r < cumul) {
                    if (d.fila != null)
                        agendaEvento(new Evento(TipoEvento.CHEGADA, d.fila, clock));
                    break;
                }
            }
        }
    }

    private void imprimeResultados(Fila... filas) {
        for (Fila f : filas) {
            double total = 0.0;
            for (double t : f.tempoEstado)
                total += t;

            System.out.println("---------------------------------------------------");
            System.out.println("Fila " + f.nome);
            System.out.println("Clientes perdidos: " + f.perdas);
            System.out.println("Distribuição de probabilidade por estado:");
            for (int i = 0; i < f.tempoEstado.length; i++) {
                if (f.tempoEstado[i] > 0) {
                    double p = (total > 0) ? (f.tempoEstado[i] / total) * 100 : 0;
                    System.out.printf("Estado %d: tempo = %.2f, prob = %.4f%%%n",
                            i, f.tempoEstado[i], p);
                }
            }
            System.out.println("---------------------------------------------------");
        }
    }
}
