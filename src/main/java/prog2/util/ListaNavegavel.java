package prog2.util;

import java.util.List;

public class ListaNavegavel<T> {
    private int indexAtual;
    List<T> elementos;

    T getAtual() {
        return elementos.get(indexAtual);
    }

    T getProximo() {
        if (indexAtual + 1 < elementos.size()) {
            return elementos.get(indexAtual + 1);
        }
        return elementos.getFirst();
    }

    T getAnterior() {
        if (indexAtual > 0) {
            return elementos.get(indexAtual - 1);
        }
        return elementos.getLast();
    }

    T irParaProximo() {
        return null;
    }

    T irParaAnterior() {
        return null;
    }

    List<T> getTodos() {
        return null;
    }
}
