package se.loveone.zenws;

import java.util.Iterator;

public interface Transactable {
    void commit();
    void rollback();
}
