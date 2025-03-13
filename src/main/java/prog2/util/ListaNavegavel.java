package prog2.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class ListaNavegavel<T> extends ArrayList<T> {
    private int indexAtual;

    public T getAtual() {
        return get(indexAtual);
    }

    public boolean temProximo() {
        return indexAtual + 1 < size();
    }

    public boolean temAnterior() {
        return indexAtual > 0;
    }

    public T irParaProximo() {
        if (!temProximo()) {
            throw new NoSuchElementException("Não há próximo elemento");
        }
        return get(++indexAtual);
    }

    public T irParaAnterior() {
        if (!temAnterior()) {
            throw new NoSuchElementException("Não há elemento anterior");
        }
        return get(--indexAtual);
    }

    public T getProximo() {
        if (!temProximo()) {
            throw new NoSuchElementException("Não há próximo elemento");
        }
        return get(indexAtual + 1);
    }

    public T getAnterior() {
        if (!temAnterior()) {
            throw new NoSuchElementException("Não há elemento anterior");
        }
        return get(indexAtual - 1);
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        if (index <= indexAtual) {
            indexAtual++;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean added = addAll(index, c);
        if (added && index <= indexAtual) {
            indexAtual += c.size();
        }
        return added;
    }

    @Override
    public T remove(int index) {
        final T removedElement = super.remove(index);
        if (index < indexAtual) {
            indexAtual--;
        } else if (index == indexAtual) {
            if (indexAtual == size()) {
                indexAtual = Math.max(0, indexAtual - 1);
            }
        }
        return removedElement;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            modified |= remove(o);
        }
        return modified;
    }

    @Override
    public void clear() {
        super.clear();
        indexAtual = 0;
    }

}
