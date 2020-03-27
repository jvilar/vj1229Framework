package es.uji.vj1229.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Generic class to keep a pool of objects.</p>
 * <p>It is used to keep a pool of objects so that they are reused in order to keep
 * object creation and destruction low.</p>
 * <p>Objects can be obtained by a call to {@link Pool#newObject} and released
 * by a call to {@link Pool#free}. The objects released are stored in a list that is used
 * when new object are requested.</p>
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>

 */
public class Pool<T> {
    /**
     * The interface that is used for object creation
     * @param <T> the type of the objects created by the factory
     */
    public interface PoolObjectFactory<T> {
        /**
         * Creates a new object
         * @return The object created
         */
        T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    /**
     * The constructor of the pool
     * @param factory the factory of new objects
     * @param maxSize the maximum number of element kept in the pool
     */
    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<T>(maxSize);
    }

    /**
     * Return an object from the pool or create a new one if the list
     * of returned objects is empty
     * @return The requested object
     */
    public T newObject() {
        T object;

        if (freeObjects.isEmpty())
            object = factory.createObject();
        else
            object = freeObjects.remove(freeObjects.size() - 1);
        return object;
    }

    /**
     * Free an object for subsequent uses
     * @param object the object to be freed
     */
    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }
}
