package net.avh4.data.datum.peer.java;

import net.avh4.data.datum.prim.Id;
import net.avh4.data.datum.prim.KnownId;
import net.avh4.data.datum.store.DatumStore;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class DocumentInvocationHandler implements InvocationHandler {
    private final Id entityId;
    private final DatumStore store;

    public static <T> T getDocument(DatumStore store, Class<T> documentClass, final Id entityId) {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(documentClass.getClassLoader(), new Class[]{documentClass},
                new DocumentInvocationHandler(store, entityId));
    }

    public DocumentInvocationHandler(DatumStore store, Id entityId) {
        this.entityId = entityId;
        this.store = store;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Class<?> returnType = method.getReturnType();
        final String attribute_name = method.getName();

        if (attribute_name.equals("_id")) return entityId;
        if (attribute_name.equals("toString")) return "Document:" + entityId.toString();

        if (returnType.isArray()) {
            final String[] storeValues = store.getArray(entityId, attribute_name);
            return stringsToObjects(returnType.getComponentType(), storeValues);
        } else {
            final String storedValue = store.get(entityId, attribute_name);
            return toObject(returnType, storedValue);
        }
    }

    private Object toObject(Class<?> clazz, String storedValue) {
        if (clazz.equals(String.class)) {
            return storedValue;
        } else {
            return getDocument(store, clazz, new KnownId(storedValue));
        }
    }

    private Object stringsToObjects(Class<?> itemClass, String[] values) {
        final Object a = Array.newInstance(itemClass, values.length);
        for (int i = 0; i < values.length; i++) {
            Array.set(a, i, toObject(itemClass, values[i]));
        }
        return a;
    }
}
